package utilities;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static io.restassured.RestAssured.*;

public class Utils {
    //Global Setup Variables
    public static String path;
    public static String jsonPathTerm;
    private static String APIKey;

    //Sets Base URI
    public static void setBaseURI(String baseURI) {
        RestAssured.baseURI = baseURI;
    }

    //Sets base path
    public static void setBasePath(String basePathTerm) {
        RestAssured.basePath = basePathTerm;
    }

    //Reset Base URI (after test)
    public static void resetBaseURI() {
        RestAssured.baseURI = null;
    }

    //Reset base path
    public static void resetBasePath() {
        RestAssured.basePath = null;
    }

    //Set API Key
    public static void setAPIKey() {
        APIKey = "692ba5507f53a9aa4ce5d8ce89f119a0";
    }

    //Get API Key
    public static  String getApiKey(){
        setAPIKey();
        return APIKey;
    }

    //Sets ContentType
    public static void setContentType1(ContentType Type) {
        given().contentType(Type);
    }

    //Sets ContentType
    public static void setContentType(String contentType) {
        ContentType Type = null;
        if (contentType.toLowerCase().contains("json"))
        {
            Type = ContentType.JSON;
        }
        else if (contentType.toLowerCase().contains("xml"))
        {
            Type = ContentType.XML;
        }
        else if (contentType.toLowerCase().contains("html"))
        {
            Type = ContentType.HTML;
        }
        given().contentType(Type);
    }

    //Sets Json path term
    public static void setJsonPathTerm(String jsonPath) {
        jsonPathTerm = jsonPath;
    }

    //Returns response
    public static Response getResponse() {
        System.out.print("path: " + path +"\n");
        return get(path);
    }

    //Returns JsonPath object
    public static JsonPath getJsonPath(Response res) {
        String json = res.asString();
        //System.out.print("returned json: " + json +"\n");
        return new JsonPath(json);
    }

    public static String createSearchQueryPath(HashMap params){
        path = "?q="+params.get("q")+"&APPID="+params.get("APPID");
        return path;
    }
}