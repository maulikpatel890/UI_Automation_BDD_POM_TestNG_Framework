package util;

import base.MessageEnum;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static base.DriversManager.*;


public class TestUtils {

    /*
     * This will type text in the textbox
     * @element will accept WebElement
     * @value accept String value which needs to be typed
     */
    public void sendKeys(WebElement element, String value){
        element.clear();
        element.sendKeys(value);
    }
    /*
     * This will type text in the textbox - specially designed for mobile app
     * @element will accept WebElement
     * @value accept String value which needs to be typed
     */
    public void appSendKeys(WebElement element, String value) {
        element.click();
        element.clear();

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        element.sendKeys(value);
    }
    public void clickElement(WebElement element){
        element.click();
    }
    /*
     * This method will check whether element is present or not on a page
     * @locator it will accept String value and this will be value of xpath, id or other locator type
     * @locationType will accept String value this will be either xpath, id or css selector
     */
    public boolean isElementPresent(String locatorType, String locator) throws Exception {
        if(platform.equalsIgnoreCase("Mobile")){
            List<WebElement> allElements = null;
            if (locatorType.equals("xpath")) {
                allElements = appiumDriver.findElements(By.xpath(locator));
            } else if (locatorType.equalsIgnoreCase("cssSelector")) {
                allElements = appiumDriver.findElements(By.cssSelector(locator));
            } else if (locatorType.equalsIgnoreCase("id")) {
                allElements = appiumDriver.findElements(By.id(locator));
            }
            else if (locatorType.equalsIgnoreCase("tagName")) {
                allElements = appiumDriver.findElements(By.tagName(locator));
            }
            else if (locatorType.equalsIgnoreCase("name")) {
                allElements = appiumDriver.findElements(By.name(locator));
            }
            else if (locatorType.equalsIgnoreCase("className")) {
                allElements = appiumDriver.findElements(By.className(locator));
            }
            else if (locatorType.equalsIgnoreCase("linkText")) {
                allElements = appiumDriver.findElements(By.linkText(locator));
            }
            else if (locatorType.equalsIgnoreCase("partialLinkText")) {
                allElements = appiumDriver.findElements(By.partialLinkText(locator));
            }
            if (allElements.size() == 0)
                return false;
            else
                return true;
        }
        else {
            List<WebElement> allElements = null;
            if (locatorType.equals("xpath")) {
                allElements = getWebDriver().findElements(By.xpath(locator));
            } else if (locatorType.equalsIgnoreCase("cssSelector")) {
                allElements = getWebDriver().findElements(By.cssSelector(locator));
            } else if (locatorType.equalsIgnoreCase("id")) {
                allElements = getWebDriver().findElements(By.id(locator));
            }
            else if (locatorType.equalsIgnoreCase("tagName")) {
                allElements = getWebDriver().findElements(By.tagName(locator));
            }
            else if (locatorType.equalsIgnoreCase("name")) {
                allElements = getWebDriver().findElements(By.name(locator));
            }
            else if (locatorType.equalsIgnoreCase("className")) {
                allElements = getWebDriver().findElements(By.className(locator));
            }
            else if (locatorType.equalsIgnoreCase("linkText")) {
                allElements = getWebDriver().findElements(By.linkText(locator));
            }
            else if (locatorType.equalsIgnoreCase("partialLinkText")) {
                allElements = getWebDriver().findElements(By.partialLinkText(locator));
            }

            if (allElements.size() == 0)
                return false;
            else
                return true;
       }
    }
    /*
     * This method will check whether web element is present or not on a page
     * @element it will accept Web Element(s)
     */
    public boolean isWebElementPresent(List<WebElement> element){
        if (element.size() == 0)
            return false;
        else
            return true;
    }
    /*
     * This method will check whether element is present or not on a page
     * @element it will accept Mobile Element(s)
     */
    public boolean isAppElementPresent(List<WebElement> element){
        if (element.size() == 0)
            return false;
        else
            return true;
    }


    public String getFormattedDate(Date date, String datePattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        return simpleDateFormat.format(date);
    }

    public String getDayOfCurrentMonth(int offset){
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar cal = Calendar.getInstance(timeZone);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)+offset;
        if(dayOfMonth>28){
            dayOfMonth = 1;
        }
        return  String.valueOf(dayOfMonth);
    }


    public void allowMediaAccess()  {
        WebDriverWait wait = new WebDriverWait(appiumDriver, Duration.ofSeconds(2));
        final String allowMediaAccess = "com.android.permissioncontroller:id/permission_allow_button";

        //Allow location access in Android
        if(mobileOS.equalsIgnoreCase("Android")){
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(allowMediaAccess)));
                if(appiumDriver.findElement(By.id(allowMediaAccess)).isDisplayed());
                {
                    appiumDriver.findElement(By.id(allowMediaAccess)).click();
                }
            } catch (Exception e) {
            }

        }
    }
    public String getExpectedMessage(String expectedValue){
        MessageEnum messageEnum = MessageEnum.valueOf(expectedValue);
        String message = messageEnum.getMessage();
        return message;
    }
    public List<String> getRoundedCurrentDateTime(){
        String roundedCurrentDateTime = "";
        String roundedCurrentDateTimePlus30Mins = "";
        SimpleDateFormat mobileDateFormatter = new SimpleDateFormat("MMM dd, YYYY h:mm aa");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int round = calendar.get(Calendar.MINUTE) % 30;
        calendar.add(Calendar.MINUTE, round < 8 ? -round : (30-round));
        calendar.set( Calendar.SECOND, 0 );
        roundedCurrentDateTime = mobileDateFormatter.format(calendar.getTime());
        calendar.add(Calendar.MINUTE, 30);
        roundedCurrentDateTimePlus30Mins = mobileDateFormatter.format(calendar.getTime());
        return Arrays.asList(roundedCurrentDateTime,roundedCurrentDateTimePlus30Mins);
    }
    public List<Integer> getCurrentHHMM(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int MM = calendar.get(Calendar.MINUTE);
        int HH = calendar.get(Calendar.HOUR_OF_DAY);
        return Arrays.asList(HH,MM);
    }
    public HashMap<Object, Object> getCurrentUTCHHMM(){
        HashMap<Object, Object> currentUTCTimeHHMM = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(Instant.now()));
        currentUTCTimeHHMM.put("MM",calendar.get(Calendar.MINUTE));
        currentUTCTimeHHMM.put("HH",calendar.get(Calendar.HOUR_OF_DAY));

        return currentUTCTimeHHMM;
    }
    public String getCurrentUTChhmmampm() {

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }
    /*
     * This will return random integer number between 100 - 999999
     */
    public static int getRandomNumber(){
        Random r = new Random();
        int low = 100;
        int high = 999999;
        return r.nextInt(high-low) + low;
    }
}
