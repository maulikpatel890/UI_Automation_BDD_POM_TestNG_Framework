package com.google.steps;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import base.DriversManager;
import org.testng.annotations.AfterTest;
import util.Log;

import static base.DriversManager.appiumDriver;
import static base.DriversManager.platform;

public class CommonHooks {
    public static int failedScenariosCount, passedScenariosCount;

    @After()
    public void afterScenario(Scenario scenario) throws Exception {
        Log.info("####After Test scenario");
        if (scenario.isFailed()) {
            failedScenariosCount++;
            if(platform.equalsIgnoreCase("Mobile")){
                final byte[] screenshot = ((TakesScreenshot) appiumDriver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png",scenario.getId());
                DriversManager.restartApp();
            }
            else{
                final byte[] screenshot = ((TakesScreenshot) DriversManager.getWebDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png",scenario.getId());
            }
        }
        else{
            passedScenariosCount++;
        }
        if(DriversManager.getWebDriver()!=null){
            DriversManager.closeWebDriver();
        }
    }
    @AfterTest()
    public void tearDown() throws Exception {
        Log.info("##Test end!!!!");
        if(DriversManager.getWebDriver()!=null){
            DriversManager.closeWebDriver();
        }
    }
}
