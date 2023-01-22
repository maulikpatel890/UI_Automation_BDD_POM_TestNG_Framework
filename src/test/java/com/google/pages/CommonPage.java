package com.google.pages;

/*************************************** PURPOSE **********************************
 - This class will hold all the the commonly used data members & methods of different pages across the app.
 */

import base.DriversManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.TestUtils;


public class CommonPage {

    public CommonPage(WebDriver driver) {
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,
                DriversManager.defaultTimeout), this);
    }



}
