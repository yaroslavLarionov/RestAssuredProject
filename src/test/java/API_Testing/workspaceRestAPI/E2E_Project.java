package API_Testing.workspaceRestAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;

public class E2E_Project {
    public String path;

    //What is a TestNG annotation that allows us to run a Test Before each method
    @BeforeTest
    public String setupLogInAndToken() {
        RestAssured.baseURI = "https://api.octoperf.com";
        path = "/public/users/login";
        Map<String, Object> map = new HashMap<>();
        map.put("username", "yarillo@yahoo.com");
        map.put("password", "Helpdesk2012!");

        return RestAssured.given().
                           queryParams(map).
                           contentType(ContentType.JSON).
                           accept(ContentType.JSON).
                           post(path).
                           then()
                           .statusCode(SC_OK)  //verify status code = 200 or OK
                           .extract()          //method that extracts response JSON data
                           .body()             //body extracted as JSON format
                           .jsonPath()         // navigate using jsonPath
                           .get("token");      //get value foe key token
    }

    //TODO write a test for API endpoint member-of
    @Test
    public void verifyToken() {
        String memberOf = "/workspaces/member-of";
        Response response = RestAssured.given().header("authorization", setupLogInAndToken()).when().get(memberOf).then().log().all().extract().response();
        //Verify status code
        Assert.assertEquals(SC_OK, response.statusCode());
        Assert.assertEquals("Default", response.jsonPath().getString("name[0]"));
    }

    //TODO add tests for ID, userID, Description
    @Test
    public void verifyID() {
        String memberOf = "/workspaces/member-of";
        Response response = RestAssured.given().header("authorization", setupLogInAndToken()).when().get(memberOf).then().log().all().extract().response();
        //Verify ID
        Assert.assertEquals("eqEOvH0Bp7hMViDsyIob", response.jsonPath().getString("id[0]"));
    }

    @Test
    public void verifyUserID() {
        String memberOf = "/workspaces/member-of";
        Response response = RestAssured.given().header("authorization", setupLogInAndToken()).when().get(memberOf).then().log().all().extract().response();
        //Verify userId
        Assert.assertEquals("15kNvH0B9ik-roTyxShe", response.jsonPath().getString("userId[0]"));
    }

    @Test
    public void verifyDescription() {
        String memberOf = "/workspaces/member-of";
        Response response = RestAssured.given().header("authorization", setupLogInAndToken()).when().get(memberOf).then().log().all().extract().response();
        //Verify description
        Assert.assertEquals("", response.jsonPath().getString("description[0]"));
    }

}
