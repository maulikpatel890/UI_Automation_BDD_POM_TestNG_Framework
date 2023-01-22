@GoogleSearch
Feature: As a user, I want to search current UTC time in google search engine.

  Scenario: 1 Verify user is able to search current UTC time in google search.
    Given User visits the landing page of google website
    When User searches for today's date
    Then User should see correct date in the search results

  Scenario: 2 - Failure Scenario - Verify user is able to navigate to sign in page.
    Given User visits the landing page of google website
    When User searches for today's date
    Then Verify User should see incorrect search text in the search box - Failure Assertion

  Scenario: 3 Verify user is able to search current UTC time in google search.
    Given User visits the landing page of google website
    Given User visits the landing page of google website
    When User searches for today's date

  Scenario: 4 Verify user is able to search current UTC time in google search.
    Given User visits the landing page of google website
    Given User visits the landing page of google website
    When User searches for today's date