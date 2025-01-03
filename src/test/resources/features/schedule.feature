@api
Feature: RMS Test Schedule API

  Background:
    Given a GET request is sent to the endpoint

  Scenario: TC01_Verify API status code and response time
    Then the HTTP status code should be 200
    And the response time should be less than 1000 milliseconds

  Scenario: TC02_Verify the "id" and "type" fields in the API response
    Then the id under the elements block in the data field should never be null or empty
    And the type field within episode should always be "episode" for every item

  Scenario: TC03_Verify the "title" field in "episode" is never null or empty
    Then the title field in episode should never be null or empty

  Scenario: TC04_Verify only one episode in the list has "live" field in "episode" as true
    Then only one episode in the schedule should have the live field set to true

  Scenario: TC05_Verify "transmission_start" is before "transmission_end"
    Then the transmission_start date should be before the transmission_end date

  Scenario: TC06_Verify the "Date" value in response headers
    Then the response header should contain the current Date value

  Scenario: TC07_Verify the API response for a non-existent date
    Given a GET request is sent to the endpoint "2023-09-11"
    Then the HTTP status code should be 404
    And the error object should contain the property details and http_response_code


