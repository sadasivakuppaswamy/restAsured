package ApiTests;

/**
 * Created by Sadasiva.Kuppaswamy on 29-03-2017.
 */

import Utils.RestUtil;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

public class RestTest {

    @Before
    public  void getRequestFindCapital() throws JSONException {

       /*RestAssured.baseURI = "http://qaweb5.dev.zoomsystems.com/ecenter/smart/api/companies/";
        given().contentType("application/json").header("Authorization", "Bearer o0dq0dingf81bp2u9isbenikvt").
        when().get("1230/planograms").
        then().statusCode(200).assertThat().
                body("items.data[1].name",equalTo("Best Buy Canada 1.0.1"));
       */
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
    public void testCreateuser() {
        final String email = "test@hascode.com";
        final String firstName = "Tim";
        final String lastName = "Tester";

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
    public void testAuthenticationWorking() {
        // we're not authenticated, service returns "401 Unauthorized"
        expect().
                statusCode(401).
                when().
                get("/service/secure/person");

        // with authentication it is working
        expect().
                statusCode(200).
                when().
                with().
                authentication().basic("admin", "admin").
                get("/service/secure/person");
    }
    @Test
    public void testFileUpload() {
        final File file = new File(getClass().getClassLoader()
                .getResource("F:\\cplatest\\untitled\\src\\test\\java\\ApiTests\\test").getFile());
        assertNotNull(file);
        assertTrue(file.canRead());
        given().
                multiPart(file).
                expect().
                body(equalTo("This is an uploaded test file.")).
                when().
                post("/service/file/upload");
    }


}
