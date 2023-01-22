package com.google.pages;

import base.DriversManager;
import io.appium.java_client.pagefactory.WithTimeout;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.time.temporal.ChronoUnit;

public class GoogleLandingPage {

    public GoogleLandingPage(WebDriver driver) {
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,
                DriversManager.defaultTimeout), this);
    }

    public static final String googleLandingPageURL = "https://www.google.com/";

    @WithTimeout(time = DriversManager.defaultTimeout, chronoUnit = ChronoUnit.SECONDS)
    @FindBy(css= "img[alt='Google']")
    public WebElement img_googleLogo;

    @WithTimeout(time = DriversManager.defaultTimeout, chronoUnit = ChronoUnit.SECONDS)
    @FindBy(name= "btnK")
    public WebElement btn_googleSearch;
}
