package glueCode;


import com.aventstack.extentreports.Status;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;
import io.restassured.response.Response;
import com.aventstack.extentreports.Status;
import com.cucumber.listener.Reporter;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import static org.junit.Assert.assertEquals;

import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.junit.Assert;
import utilities.Utils;
import utilities.HelperTestMethods;
import org.json.JSONObject;
import javax.naming.spi.ResolveResult;
import java.util.*;
import java.lang.Integer;

public class StepDef extends Utils {
    public static String apiHostName = "http://api.openweathermap.org/data/2.5/forecast";
    public static String apiEndPointUri;
    public static String endPointUri;
    public static String testName;
    public static String CONTENT_TYPE;
    public static String STATUS_CODE;
    public static String REQUESTBODY;
    public static JSONObject RESPONSEBODYJson;
    public static String RESPONSEBODY;
    private Response response;



    @Given("^I want to forecast the Weather for \"([^\"]*)\" for test case \"([^\"]*)\"$")
    public void iWantToForecastTheWeatherForForTestCase(String cityName, String testCaseName) throws Throwable {
        endPointUri = createSearchQueryPath(ForecastParams(cityName));
        Reporter.addStepLog(Status.PASS + " :: Cucumber Hostname Request URL is :: " + String.format("%s%s", apiHostName, endPointUri));
    }

    @When("^I set header content type as \"([^\"]*)\"$")
    public void iSetHeaderContentTypeAs(String contentType) throws Throwable {
        if (contentType != null && !contentType.isEmpty()) {
            CONTENT_TYPE = contentType;
            Reporter.addStepLog(Status.PASS + " :: content type is :: " + CONTENT_TYPE);
        } else {
            Reporter.addStepLog(Status.FAIL + " :: content type cannot be null or empty!");
        }
    }

    @And("^I hit the API with requestbody \"([^\"]*)\" and request method is \"([^\"]*)\"$")
    public void iHitTheAPIWithRequestbodyAndRequestMethodIs(String requestBodyPath, String requestType) throws Throwable {
        setBaseURI(apiHostName);
        setContentType(CONTENT_TYPE);
        //createSearchQueryPath(ForecastParams());
        if (requestType.equalsIgnoreCase("GET")) {
            response = getResponse();
        }
        STATUS_CODE = String.valueOf(response.getStatusCode());
        RESPONSEBODY = response.getBody().asString();
        RESPONSEBODYJson = new JSONObject(RESPONSEBODY);
        Reporter.addStepLog(Status.PASS + " :: Request successfully processed");
        Reporter.addStepLog("Response is :: " + response.prettyPrint());
    }

    @Then("^I try to verify the status code is \"([^\"]*)\"$")
    public void iTryToVerifyTheStatusCodeIs(@org.jetbrains.annotations.NotNull String statusCode) throws Throwable {
        if (statusCode.equals(String.valueOf(STATUS_CODE))) {
            switch (statusCode){
                case "200": {
                    HelperTestMethods.checkStatusIs200(response);
                    Reporter.addStepLog(Status.PASS + " :: Status Code is :: " + STATUS_CODE);
                    break;
                }
                case "400": {
                    HelperTestMethods.checkStatusIs400(response);
                    Reporter.addStepLog(Status.PASS + " :: Status Code is :: " + STATUS_CODE);
                    break;
                }
                case "404": {
                    HelperTestMethods.checkStatusIs404(response);
                    Reporter.addStepLog(Status.PASS + " :: Status Code is :: " + STATUS_CODE);
                    break;
                }
                case "500": {
                    HelperTestMethods.checkStatusIs500(response);
                    Reporter.addStepLog(Status.PASS + " :: Status Code is :: " + STATUS_CODE);
                    break;
                }
            }

        } else {
            assertEquals("Status Check Failed!", STATUS_CODE, response.getStatusCode());
            Reporter.addStepLog(Status.FAIL + " :: Status Code is :: " + STATUS_CODE);
        }
    }

    @And("^I try to verify the response schema as \"([^\"]*)\"$")
    public void iTryToVerifyTheResponseSchemaAs(String responseSchemaPath) throws Throwable {
        response.then().assertThat().body(matchesJsonSchemaInClasspath(responseSchemaPath));
    }

    public HashMap<String, String> ForecastParams(String cityName){
        HashMap<String, String> params = new HashMap<String, String>() ;
        params.put("q",cityName );
        params.put("APPID", Utils.getApiKey());
        return params;
    }

    public int  numberofDaysHigherthanTwentyDegrees(Response response, int temperatureinDegreeCelsius){
        List<Float> ls = getJsonPath(response).getList("list.main.temp");
        int count_higher_twenty=0;
        HashMap hashmap = new HashMap();
        for (int i =0; i<ls.size();i++)
        {
            Double temp =getJsonPath(response).getDouble("list["+i+"]"+".main.temp")-273.15;
            String ts = getJsonPath(response).get("list["+i+"]"+".dt_txt");
            System.out.println("\nThe substrings is :"+ts.substring(8, 10));
            if(temp > Double.valueOf(temperatureinDegreeCelsius))
            {
                hashmap.put(ts.substring(8, 10),temp);
            }
        }
        for (Object name: hashmap.keySet()){
            String key = name.toString();
            String value = hashmap.get(key).toString();
            System.out.println(key + " " + value);

        }
        count_higher_twenty = hashmap.size();
        return  count_higher_twenty;
    }

    public int numberofWeatherTypeDays(Response response, String weatherType){
        List<Float> ls = getJsonPath(response).getList("list.weather");
        int counter=0;
        for (int i =0; i<ls.size();i++)
        {
            String weather =getJsonPath(response).getString("list["+i+"]"+".weather[0].main");
            if(weather.startsWith(weatherType))
            {
                counter++;
            }
        }
        return counter;
    }

    @And("^I find out the number of days where the temperature is predicted to be above \"([^\"]*)\" degrees$")
    public void iFindOutTheNumberOfDaysWhereTheTemperatureIsPredictedToBeAboveDegrees(int temperatureinDegreeCelsius) throws Throwable {
        int numberOFDays = numberofDaysHigherthanTwentyDegrees(response,temperatureinDegreeCelsius);
        if(temperatureinDegreeCelsius>=0)
        {
            Assert.assertTrue("There are no days higher than "+temperatureinDegreeCelsius, temperatureinDegreeCelsius>=0);
            Reporter.addStepLog(Status.PASS + " :: Number of Days having Temperature higher than " + temperatureinDegreeCelsius+" is :: " + numberOFDays);
        }
        else
            Reporter.addStepLog(Status.FAIL + " :: Number of Days having Temperature higher than " + temperatureinDegreeCelsius+" is :: " + numberOFDays);
    }

    @And("^I find out the number of \"([^\"]*)\" Days from the Forecast$")
    public void iFindOutTheNumberOfDaysFromTheForecast(String weatherType) throws Throwable {
        int numberofSunnyDays = numberofWeatherTypeDays(response,weatherType);
        if(numberofSunnyDays>=0)
        {
            Assert.assertTrue("There are no "+ weatherType +" days.", numberofSunnyDays>=0);
            Reporter.addStepLog(Status.PASS + " :: Number of Days having "+weatherType+" days is :: " + numberofSunnyDays);
        }
        else
            Reporter.addStepLog(Status.FAIL + " :: Number of Days having "+weatherType+" days is :: " + numberofSunnyDays);
    }
}