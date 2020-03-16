package utilities;

import io.restassured.response.Response;
import static org.junit.Assert.*;

public class HelperTestMethods {

    //Verify the http response status returned. Check Status Code is 200?
    public static void checkStatusIs200 (Response res) {
        assertEquals("Status Check Failed!", 200, res.getStatusCode());
    }

    public static void checkStatusIs400 (Response res) {
        assertEquals("Status Check Failed!", 400, res.getStatusCode());
    }

    public static void checkStatusIs404 (Response res) {
        assertEquals("Status Check Failed!", 404, res.getStatusCode());
    }

    public static void checkStatusIs500 (Response res) {
        assertEquals("Status Check Failed!", 500, res.getStatusCode());
    }

    public static void checkContentTypeIsJson (Response res) {
        assertEquals("Response Content Type isn't Json", "application/json", res.getContentType());
    }

    public static void checkContentTypeIsXML (Response res) {
        assertEquals("Response Content Type isn't XML", "application/xml", res.getContentType());
    }
}
