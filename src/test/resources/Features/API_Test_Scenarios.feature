#This feature file contains few test scenarios to verify Rest API services
#Author: Mah Niggah Bawali :-D
Feature: To verify Weather API automation with Rest Assured

   @API_Test
   Scenario:  GET the Number of Days where the forecast predicts temperature above 20 Degrees
    Given I want to forecast the Weather for "Sydney" for test case "Demo test 1"
    When I set header content type as "application/json"
    And I hit the API with requestbody "" and request method is "GET"
    Then I try to verify the status code is "200"
    And I try to verify the response schema as "TemplateSchemas/Weather200.json"
     And I find out the number of days where the temperature is predicted to be above "20" degrees
     And I find out the number of "Sunny" Days from the Forecast
