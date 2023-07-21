
Feature: WEB - Calculating Tex
  Background: Landing Page
    Given the user is on the landing page
    And the tax year is set to the previous year
    And the salary before deduction is "20000"
    And the salary period is set to monthly

  Scenario: As a user, I want to calculate tax when the age is 22
    Given the user is under the age "22"
    When the user calculates their tax
    Then the take home must be "17639"




