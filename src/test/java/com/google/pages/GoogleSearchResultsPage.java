package com.google.pages;

import base.DriversManager;
import io.appium.java_client.pagefactory.WithTimeout;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.time.temporal.ChronoUnit;

public class GoogleSearchResultsPage {

    public GoogleSearchResultsPage(WebDriver driver) {
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,
                DriversManager.defaultTimeout), this);
    }

    @WithTimeout(time = DriversManager.defaultTimeout, chronoUnit = ChronoUnit.SECONDS)
    @FindBy(xpath= "//input[@aria-label='Search']")
    public WebElement txtbx_search;


    @WithTimeout(time = DriversManager.defaultTimeout, chronoUnit = ChronoUnit.SECONDS)
    @FindBy(xpath= "//div[contains(@data-hveid, 'QAQ')]/div/div/span")
    public WebElement txt_searchedResults_todayDate;


}
