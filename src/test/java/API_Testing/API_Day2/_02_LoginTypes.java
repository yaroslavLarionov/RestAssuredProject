package API_Testing.API_Day2;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class _02_LoginTypes {

    //TODO Log in with full URL with query params and verify status code and Content-type is equal to JSON

    @Test
    public void testsUsingQueryParams() {
        RestAssured.given().when().post("https://api.octoperf.com/public/users/login?password=Helpdesk2012!&username=yarillo@yahoo.com").then()
                .assertThat().statusCode(200).and().assertThat().contentType(ContentType.JSON);
    }

    //TODO Log in using Map to verify Content-type
    //Map => Stores Key and Value, Hashmap implements Map, we can store different data types of object

    @Test
    public void logInWithMap() {
        RestAssured.baseURI="https://api.octoperf.com";
        String path = "/public/users/login";

        Map<String, Object> map = new HashMap<>();
        map.put("username", "yarillo@yahoo.com");
        map.put("password", "Helpdesk2012!");

        RestAssured.given().queryParams(map).when().post(path).then().assertThat().statusCode(200).and().assertThat().contentType(ContentType.JSON);
    }

    //TODO Log in using query param
    @Test
    public void logInWithQueryParam() {
        RestAssured.baseURI = "https://api.octoperf.com";
        String path = "/public/users/login";
        RestAssured.given().queryParam("username", "yarillo@yahoo.com").queryParam("password", "Helpdesk2012!").when()
                .post(path).then().assertThat().statusCode(200).and().assertThat().contentType(ContentType.JSON);
    }

}
