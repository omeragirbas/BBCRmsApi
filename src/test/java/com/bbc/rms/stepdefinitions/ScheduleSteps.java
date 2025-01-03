package com.bbc.rms.stepdefinitions;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.cucumber.java.en.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.bbc.rms.baseurl.RMS_BaseUrl.spec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ScheduleSteps {

    private Response response;

    @Given("a GET request is sent to the endpoint")
    public void aGETRequestIsSentToTheEndpoint() {
        response = given(spec).when().get();
    }

    @Then("the HTTP status code should be {int}")
    public void theHTTPStatusCodeShouldBe(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("the response time should be less than {int} milliseconds")
    public void theResponseTimeShouldBeLessThanMilliseconds(int maxResponseTime) {
        assertTrue("Response time exceeded!", response.time() < maxResponseTime);
    }

    @Then("the id under the elements block in the data field should never be null or empty")
    public void theIdUnderTheElementsBlockInTheDataFieldShouldNeverBeNullOrEmpty() {
        List<Map<String, Object>> data = response.jsonPath().getList("schedule.elements");
        data.forEach(item -> {
            assertNotNull("ID field is null!", item.get("id"));
            assertFalse("ID field is empty!", item.get("id").toString().isEmpty());
        });
    }

    @And("the type field within episode should always be {string} for every item")
    public void theTypeFieldWithinShouldAlwaysBeForEveryItem(String episode) {
        List<Map<String, Object>> data = response.jsonPath().getList("schedule.elements.episode");
        data.forEach(item -> {
            assertEquals("Unexpected type value!", episode, item.get("type"));
        });
    }

    @Then("the title field in episode should never be null or empty")
    public void theTitleFieldInEpisodeShouldNeverBeNullOrEmpty() {
        List<Map<String, Object>> data = response.jsonPath().getList("schedule.elements.episode");
        data.forEach(item -> {
            assertNotNull("ID field is null!", item.get("title"));
            assertFalse("ID field is empty!", item.get("title").toString().isEmpty());
        });
    }

    @Then("only one episode in the schedule should have the live field set to true")
    public void onlyOneEpisodeInTheScheduleShouldHaveTheLiveFieldSetToTrue() {
        List<Map<String, Object>> data = response.jsonPath().getList("schedule.elements.episode");
        int liveCount = 0;
        for (Map<String, Object> item : data) {
            if ((boolean) item.get("live")) {
                liveCount++;
            }
        }
        assertEquals("More than one episode has the live field set to true!", 1, liveCount);
    }

    @Then("the transmission_start date should be before the transmission_end date")
    public void theDateShouldBeBeforeTheDate() {
        List<Map<String, Object>> data = response.jsonPath().getList("schedule.elements");
        data.forEach(item -> {
            assertTrue(
                    "Transmission start date is not before transmission end date!",
                    ZonedDateTime.parse((String) item.get("transmission_start"))
                            .isBefore(ZonedDateTime.parse((String) item.get("transmission_end")))
            );
        });
    }

    @Then("the response header should contain the current Date value")
    public void theResponseHeaderShouldContainTheCurrentDateValue() {
        String apiHeaderDate = response.header("Date");
        String currentDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        assertThat("The API Date header does not contain the expected date!", apiHeaderDate, containsString(currentDate));
    }

    @Given("a GET request is sent to the endpoint {string}")
    public void aGETRequestIsSentToTheEndpoint(String endpoint) {
        spec.pathParam("p1", endpoint);
        response = given(spec).when().get("/{p1}");
    }

    @And("the error object should contain the property details and http_response_code")
    public void theErrorObjectShouldContainThePropertyAnd() {
        JsonPath jsonPath = response.jsonPath();
        Map<String, Object> errorObject = jsonPath.getMap("error");
        assertTrue("The 'details' property is missing or empty!",
                errorObject.containsKey("details") && errorObject.get("details") != null && !errorObject.get("details").toString().isEmpty());
        assertTrue("The 'http_response_code' property is missing or does not match!",
                errorObject.containsKey("http_response_code") && errorObject.get("http_response_code").equals(404));
    }
}