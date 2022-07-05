package API_Testing.workspaceRestAPI;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class E2E_Project {
    public String path;
    String id;
    String userId;
    String projectID;
    Map<String, String> variables;
    Response response;


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
    public void memberOf() {
        String memberOf = "/workspaces/member-of";
        response = RestAssured.given().header("authorization", setupLogInAndToken()).when().get(memberOf).then().log().all().extract().response();
        //Verify status code
        Assert.assertEquals(SC_OK, response.statusCode());
        Assert.assertEquals("Default", response.jsonPath().getString("name[0]"));

        //Save the id so it could be used in other requests
        id = response.jsonPath().get("id[0]");
        //Save the userId so it can be used in other requests
        userId = response.jsonPath().get("userId[0]");
        variables = new HashMap<>();
        variables.put("id", id);
        variables.put("userId", userId);
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

    @Test(dependsOnMethods = {"memberOf"})
    public void createProject() {
        String requestBody = "{\"id\":\"\",\"created\":\"2021-03-11T06:15:20.845Z\",\"lastModified\":\"2021-03-11T06:15:20.845Z\",\"userId\":\"" +
                            variables.get("userId") + "\",\"workspaceId\":\"" + variables.get("id") +
                             "\",\"name\":\"testing223\",\"description\":\"testing\",\"type\":\"DESIGN\",\"tags\":[]}";

        response = RestAssured.given().contentType(ContentType.JSON).header("authorization", setupLogInAndToken()).and().body(requestBody).when().post("/design/projects").then()
                .extract().response();
        System.out.println(response.prettyPrint());

        Assert.assertEquals("testing22", response.jsonPath().getString("name"));
        // Using hamcrest matchers validation
        assertThat(response.jsonPath().getString("name"), is("testing22"));
        Assert.assertEquals(id, response.jsonPath().getString("workspaceId"));
        Assert.assertEquals(userId, response.jsonPath().getString("userId"));

        //Store id on a variable for future use
        projectID = response.jsonPath().get("id");

    }

    @Test(dependsOnMethods = {"memberOf", "createProject"}, description = "we need those 2 methods to be able to update the project")
    public void updateProject() {
        //Create JSON body
        JSONObject body = new JSONObject();
        body.put("created", 1615443320845L);
        body.put("description", "TLAupdate");
        body.put("id", projectID);
        body.put("lastModified", 1656902579223L);
        body.put("name", "TLA accounting");
        body.put("type", "DESIGN");
        body.put("userId", variables.get("userId"));
        body.put("workspaceId", variables.get("id"));

        //Get response
        response = RestAssured.given().header("authorization", setupLogInAndToken()).contentType(ContentType.JSON).body(body.toString())
                .put("design/projects/" + projectID);

        //Another way of doing it using String request body
//        String requestBody1 = "{\"created\":1615443320845,\"description\":\"TLAupdate\",\"id\":\"" + projectID +
//                               "\",\"lastModified\":1629860121757,\"name\":\"TLA accounting\",\"tags\":[],\"type\":\"DESIGN\",\"userId\":\"" +
//                               variables.get("userID") + "\",\"workspaceId\":\"" + variables.get("id") + "\"}";
//
//
//        response = RestAssured.given().headers("Content-type", ContentType.JSON).header("authorization", setupLogInAndToken()).and().body(requestBody1)
//                   .when().put("/design/projects/" + projectID).then().extract().response();
        System.out.println(response.prettyPrint());

        //TODO do assertions for id, name, type, userId, workspaceId, status code, Content-type
        Assert.assertEquals(projectID, response.jsonPath().getString("id"));
        Assert.assertEquals(body.get("name"), response.jsonPath().getString("name"));
        Assert.assertEquals(body.get("type"), response.jsonPath().getString("type"));
        Assert.assertEquals(userId, response.jsonPath().getString("userId"));
        Assert.assertEquals(id, response.jsonPath().getString("workspaceId"));
        Assert.assertEquals(SC_OK, response.statusCode());
        Assert.assertEquals(ContentType.JSON.toString(), response.contentType());
    }


    @Test(dependsOnMethods = {"memberOf", "createProject", "updateProject"})
    public void deleteProject() {
        response = RestAssured.given().header("authorization", setupLogInAndToken()).when().delete("design/projects/" + projectID).then()
                .extract().response();
        Assert.assertEquals(SC_NO_CONTENT, response.statusCode());
    }

    @Test(description = "My own method for updating the already existing project using hardcoded data of id, userId and workspaceId")
    public void updateProjectSeparately() {
        //Create JSON body using hardcoded data
        JSONObject body = new JSONObject();
        body.put("created", 1615443320845L);
        body.put("description", "testing YESSS");
        body.put("id", "Giizy4EBrZGjujUF9aYc");
        body.put("lastModified", 1656979977499L);
        body.put("name", "testing223");
        body.put("type", "DESIGN");
        body.put("userId", "15kNvH0B9ik-roTyxShe");
        body.put("workspaceId", "eqEOvH0Bp7hMViDsyIob");

        //Get response
        response = RestAssured.given().header("authorization", setupLogInAndToken()).contentType(ContentType.JSON).body(body.toString())
                .put("design/projects/" + "Giizy4EBrZGjujUF9aYc");
        System.out.println(response.prettyPrint());

    }



}
