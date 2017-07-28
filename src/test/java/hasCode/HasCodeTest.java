package hasCode;

import Utils.RestUtil;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Map;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.testng.Assert.assertEquals;

/**
 * Created by Sadasiva.Kuppaswamy on 17-04-2017.
 */
public class HasCodeTest {
   @BeforeSuite
   public void defaultParamInitialization(){

       RestUtil.setBaseURI("http://localhost:8080/rest-assured-example/");
   }
   @Test
   public void testGetSingleUser() {
      expect().
              statusCode(200).
              body(
                      "email", equalTo("test@hascode.com"),
                      "firstName", equalTo("Tim"),
                      "lastName", equalTo("Testerman"),
                      "id", equalTo("1")).
              when().
              get("/service/single-user");
   }
    @Test
    public void testGetSingleUserProgrammatic() {
        Response res = get("/service/single-user");
        assertEquals(200, res.getStatusCode());
        String json = res.asString();
        JsonPath jp = new JsonPath(json);
        assertEquals("test@hascode.com", jp.get("email"));
        assertEquals("Tim", jp.get("firstName"));
        assertEquals("Testerman", jp.get("lastName"));
        assertEquals("1", jp.get("id"));
    }
    @Test
    public void testFindUsingGroovyClosure() {
        String json = get("/service/persons/json").asString();
        JsonPath jp = new JsonPath(json);
        jp.setRoot("person");
        Map person = jp.get("find {e -> e.email =~ /test@/}");
        assertEquals("test@hascode.com", person.get("email"));
        assertEquals("Tim", person.get("firstName"));
        assertEquals("Testerman", person.get("lastName"));
    }
    @Test
    public void testGetSingleUserAsXml() {
        expect().
                statusCode(200).
                body(
                        "user.email", equalTo("test@hascode.com"),
                        "user.firstName", equalTo("Tim"),
                        "user.lastName", equalTo("Testerman"),
                        "user.id", equalTo("1")).
                when().
                get("/service/single-user/xml");
    }
    @Test
    public void testCreateuser() {
        final String email = "sadasiva@hascode.com";
        final String firstName = "sadasiva";
        final String lastName = "kuppaswamy";

        given().
                parameters(
                        "email", email,
                        "firstName", firstName,
                        "lastName", lastName).
                expect().
                body("email", equalTo(email)).
                body("firstName", equalTo(firstName)).
                body("lastName", equalTo(lastName)).
                when().
                get("/service/user/create");
    }
    @Test
    public void authenticationTest(){
        expect().statusCode(401).when().get("/service/secure/person");
        expect().statusCode(200).when().with().authentication().basic("admin","admin").
                get("/service/secure/person");
    }


}
