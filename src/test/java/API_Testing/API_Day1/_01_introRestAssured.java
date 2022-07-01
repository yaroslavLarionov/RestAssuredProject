package API_Testing.API_Day1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class _01_introRestAssured {

    //Rest Assured is a Java library that is used to perform API tests
    //It uses BDD style such as given, when, then, etc.
    //Rest Assured has methods to fetch data from the response body when making a request
    //Rest Assured works with http requests: CRUD operations (Create, Read, Update, Delete) also known as (POST, GET, PUT, DELETE)
    //As testers we mostly use 2 requests - GET and POST among DELETE and PUT

    //NOTE: As testers we mostly work with JSON payloads (BODY), we also experience XML files, text, pdf, etc.

    /*Some of the most used methods in Rest Assured are:
     -  When making a request:
           given()        ---->   used to prepare the request
           when()         ---->   used to send the request
           then()         ---->   used to verify the request
     -  When we verify the response:
           prettyPeek()   ---->   used to print the response headers, URLs, etc. in pretty format in the Console
           prettyPrint()  ---->   used to print the response in pretty format
           log()          ---->   logs(prints) all the responses
           asString()     ---->   allows us to print in String format
           contentType()  ---->   used to verify the content type from the response body when making a POST request
           accept()       ---->   used to verify the body response from the headers when making a GET request

           baseURI        ---->   used to save the base URL for all the resources
     */

    public static String baseURI = "https://api.octoperf.com";
    //When we make a request we only provide the path(endpoint) to a specific baseURI
    private String path = "public/users/login";

    //What's the endpoint??
    // Endpoint is a unique URL that represents and object or collection of Objects
    //Example: https://www.google.com,/search?source=JavaBook
    //                   baseURI      Endpoint   ?  parameter

    //Base url = https://api.octoperf.com
    //FUL URL = https://api.octoperf.com/public/users/login
    //Full url with query parameters = https:/api.octoperf.com/public/users/login?password=Helpdesk2012!&username=yarillo@yahoo.com

    //TODO Make a HTTP: POST request with given full URL with query params and print the response
    @Test
    public void printResponse() {
        RestAssured.given().when().post("https://api.octoperf.com/public/users/login?password=Helpdesk2012!&username=yarillo@yahoo.com").prettyPeek();
    }

    @Test
    public void printResponsePrettyPrint() {
        RestAssured.given().when().post("https://api.octoperf.com/public/users/login?password=Helpdesk2012!&username=yarillo@yahoo.com").prettyPrint();
    }

    /*
    When we verify the status code we must pay attention to the following errors in the response body
      1**  -->  Information
      2**  -->  Success (200 -> OK, 201 -> Created, 202 -> Accepted, 204 -> No content)
      3**  -->  Redirection
      4**  -->  Client error (400 -> Bad Request, 401 -> Unauthorized, 403 -> Forbidden, 404 -> Not Found)
      5**  -->  Server error (500 -> Internal Server Error, 501 - Not Implemented, 503 - Service Unavailable)
     */

    //TODO perform a POST request to log in API endpoint, verify status code is 200 OK
    @Test
    public void verifyStatusCode() {
        RestAssured.given().when().post("https://api.octoperf.com/public/users/login?password=Helpdesk2012!&username=yarillo@yahoo.com")
                   .then().assertThat().statusCode(200);
    }

    //TODO perform a POST request to log in API endpoint, verify content type is JSON
    @Test
    public void verifyContentType() {
        RestAssured.given().when().post("https://api.octoperf.com/public/users/login?password=Helpdesk2012!&username=yarillo@yahoo.com")
                .then().assertThat().contentType(ContentType.JSON);
    }



}
