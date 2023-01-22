package com.google.steps.googlesearch;

import base.DriversManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import com.google.pages.GoogleLandingPage;
import com.google.pages.GoogleSearchResultsPage;
import util.Log;
import util.TestUtils;

import java.util.Date;

public class GoogleSearchSteps {

    WebDriver driver ;
    GoogleLandingPage googleLandingPage;
    GoogleSearchResultsPage googleSearchResultsPage;
    TestUtils testUtils = new TestUtils();

    public GoogleSearchSteps() throws Exception {
        driver = DriversManager.getWebDriver();
        googleLandingPage = new GoogleLandingPage(driver);
        googleSearchResultsPage = new GoogleSearchResultsPage(driver);
    }

    @Given("User visits the landing page of google website")
    public void verifyGoogleWelcomeScreen() {
        Assert.assertTrue(driver.getCurrentUrl().contains(GoogleLandingPage.googleLandingPageURL),"User is not navigated to google website.");
        Assert.assertTrue(googleLandingPage.img_googleLogo.isDisplayed(),"Missing Google Logo in Landing Page.");
    }

    @When("User searches for today's date")
    public void search_today_date_google() {
        testUtils.sendKeys(googleSearchResultsPage.txtbx_search,"What is today's date?");
        testUtils.clickElement(googleLandingPage.btn_googleSearch);
    }
    @Then("User should see correct date in the search results")
    public void verify_current_date_in_google_search_results() {
        String expectedCurrentDate = testUtils.getFormattedDate(new Date(), "dd MMMM yyyy");

        Assert.assertTrue(googleLandingPage.img_googleLogo.isDisplayed()
                , "Google logo is missing in search results screen.");
        Assert.assertEquals(googleSearchResultsPage.txt_searchedResults_todayDate.getText()
                , expectedCurrentDate);
    }

    @Then("Verify User should see incorrect search text in the search box - Failure Assertion")
    public void verify_incorrect_search_text_failure_assertion() {
        Assert.assertEquals(googleSearchResultsPage.txtbx_search.getAttribute("value")
                , "What is current time?");
    }
}

