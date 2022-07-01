package API_Testing.StudentPractice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

public class API_Activities {

    // Testing URI = https://fakerestapi.azurewebsites.net/api/v1/

    // Task 1: Using Rest Assured validate the status code for endpoint /Activities
    @Test
    public void verifyStatusCode() {
        RestAssured.given().when().get("https://fakerestapi.azurewebsites.net/api/v1/Activities").then().assertThat().statusCode(200);
    }

    // Task 2: Using Rest Assured Validate Content-Type  is application/json; charset=utf-8; v=1.0
    // for endpoint /Activities
    @Test
    public void verifyContentType() {
        RestAssured.given().when().get("https://fakerestapi.azurewebsites.net/api/v1/Activities").then().assertThat().contentType(ContentType.JSON);
    }


    // Task 1: Using Rest Assured validate the status code for endpoint /Activities/12
    @Test
    public void verifyStatusCode02() {
        RestAssured.given().when().get("https://fakerestapi.azurewebsites.net/api/v1/Activities/12").then().assertThat().statusCode(200);
    }

    // Task 2: Using Rest Assured Validate Content-Type  is application/json; charset=utf-8; v=1.0
    // for endpoint /Activities/12
    @Test
    public void verifyContentType02() {
        //another way of verifying content type
        RestAssured.given().when().get("https://fakerestapi.azurewebsites.net/api/v1/Activities/12")
                   .then().assertThat().header("Content-Type", "application/json; charset=utf-8; v=1.0");
    }
}
