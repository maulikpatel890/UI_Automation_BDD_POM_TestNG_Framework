package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.DeviceSettings;
import util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class DriversManager {

    private static final String WINDOW_WIDTH = System.getenv("window-width");
    private static final String WINDOW_HEIGHT = System.getenv("window-height");
    private static final String HEADLESS = "--headless";
    private static final String HEADLESS_CHROME = "headless-chrome";
    private static final String FIREFOX = "firefox";
    private static final String HEADLESS_FIREFOX = "headless-firefox";
    private static final String WINDOW_SIZE = "--window-size=" + WINDOW_WIDTH + "x" + WINDOW_HEIGHT;
    public static WebDriverWait wait;
    public static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>() ;
    private static final String fileSeparator = File.separator;
    public static FileInputStream fis;
    public static Logger log = Logger.getLogger("devpinoyLogger");
    private static final String rootDirectory = System.getProperty("user.dir");
    public static Properties config = new Properties();
    private static AppiumDriverLocalService service;
    private static AppiumServiceBuilder builder;
    public static DesiredCapabilities capabilities;
    public final static int defaultTimeout = 10;
    public static String testUrl;
    public static String platform;
    public static String mobileOS;
    public static String packageName;
    public static AppiumDriver appiumDriver;
    public static String iOSAppURL;
    public static void setUp() throws Exception {
        fis = new FileInputStream(rootDirectory +fileSeparator + "src" + fileSeparator + "test" + fileSeparator + "resources" + fileSeparator
                + "properties" + fileSeparator + "Config.properties");
        config.load(fis);
        BasicConfigurator.configure();
        testUrl = config.getProperty("testUrl");
        log.info("Config file loaded !!!");
    }

    public static WebDriver getWebDriver() throws Exception {
        if(webDriver.get()==null) {
            Log.info("########WebDriver is null so creating new driver");
            createWebDriver();
        }
        return webDriver.get();
    }
    public static void closeWebDriver(){
        webDriver.get().quit();
        webDriver.remove();
    }

    public static void restartWebDriver() throws Exception {
        webDriver.get().manage().deleteAllCookies();         // Clear Cookies on the browser
        webDriver.get().quit();
        createWebDriver();
    }

    public static AppiumDriver getAppDriver() throws Exception {
        if(appiumDriver==null) {
            DriversManager.createAppDriver();
        }
        return appiumDriver;
    }

//    @Before
    public static void createWebDriver() throws Exception {
        setUp();
        platform = config.getProperty("platform");
        String browserName = config.getProperty("browserName");
        capabilities = new DesiredCapabilities();

        switch (browserName.toLowerCase()) {
            case HEADLESS_CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(HEADLESS);
                chromeOptions.addArguments(WINDOW_SIZE);
                WebDriverManager.chromedriver().setup();
                webDriver.set(new ChromeDriver(chromeOptions));
            case FIREFOX:
                WebDriverManager.chromedriver().setup();
                webDriver.set(new FirefoxDriver());
            case HEADLESS_FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments(HEADLESS);
                WebDriverManager.firefoxdriver().setup();
                webDriver.set(new FirefoxDriver(firefoxOptions));
            default:
                WebDriverManager.chromedriver().setup();
                capabilities.setBrowserName("Chrome");
                webDriver.set(new ChromeDriver());
        }
        webDriver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriver.get().get(testUrl);
        webDriver.get().manage().window().maximize();
        Log.info("#######URL opened: " + webDriver.get());
    }
    public static void createAppDriver() throws Exception {
        setUp();
        platform = config.getProperty("platform");
        mobileOS = config.getProperty("mobileOS");
        packageName = config.getProperty("packageName");
        iOSAppURL = "https://appstoreconnect.apple.com/apps/123123123/testflight/ios/0f643c1d-4316-4385-8574-ebbc7e669a4d";

        int port = 4723;

        if(!checkIfServerIsRunnning(port)) {
            setupAppiumServer();
        } else {
            Log.info("Appium Server already running on Port - " + port);
        }
        appiumDriver = openApp();
        wait = DriversManager.getAppWebDriverWait(appiumDriver);

    }

    public static void restartApp(){
//        appiumDriver.terminateApp(packageName);
//        appiumDriver.activateApp(packageName);
        Log.info("App is restarted....");
    }

    public static boolean checkIfServerIsRunnning(int port) {

        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            //If control comes here, then it means that the port is in use
            isServerRunning = true;
        } finally {
            serverSocket = null;
        }
        return isServerRunning;
    }
    public static void setupAppiumServer(){
        //Set Capabilities
        capabilities = new DesiredCapabilities();
        capabilities.setCapability("noReset", "false");

        //Build the Appium service
        builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1");
        builder.usingPort(4723);
        builder.withCapabilities(capabilities);
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
        builder.withArgument(GeneralServerFlag.RELAXED_SECURITY);

        //Start the server with the builder
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        Log.info("Appium Server started....");
    }
    public static WebDriverWait getWebDriverWait(WebDriver driver){
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        return wait;
    }

    public static AppiumDriver openApp() throws Exception {
        if (appiumDriver == null) {
            String serverUrl = "http://127.0.0.1:4723/wd/hub";
            DeviceSettings capabilities  = new DeviceSettings();
            if (mobileOS.equalsIgnoreCase("iOS")) {
                appiumDriver = new IOSDriver(new URL(serverUrl), capabilities.iOSCaps());
                acceptLocationAccess();
            }
            if (mobileOS.equalsIgnoreCase("Android")) {
                appiumDriver = new AndroidDriver(new URL(serverUrl), capabilities.androidCaps());
                acceptLocationAccess();
                AndroidDriver androidDriver = (AndroidDriver) appiumDriver;
            }
            else {
                Log.info("The mobileOS <" + mobileOS + "> seems invalid. Please check CLI mobileOS argument.");
            }
        }
        return appiumDriver;
    }

    public static void acceptLocationAccess()  {
        WebDriverWait wait = new WebDriverWait(appiumDriver, Duration.ofSeconds(10));
        final String locationAccessAlert = "com.android.permissioncontroller:id/permission_allow_foreground_only_button";

        //Allow location access in Android
        if(mobileOS.equalsIgnoreCase("Android")){
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locationAccessAlert)));
                if(appiumDriver.findElement(By.id(locationAccessAlert)).isDisplayed());
                {
                    appiumDriver.findElement(By.id(locationAccessAlert)).click();
                }
            } catch (Exception e) {
            }

        }
    }

    public static WebDriverWait getAppWebDriverWait(AppiumDriver appiumDriver){
        wait = new WebDriverWait(appiumDriver, Duration.ofSeconds(20));
        return wait;
    }



}
