package ApiTests;

import Utils.*;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.assertTrue;


@FixMethodOrder(MethodSorters.NAME_ASCENDING) //For Ascending order test execution
public class Example1Test {

    //First, I declared Response and JsonPath objects.
    private Response res = null; //Response object
    private JsonPath jp = null; //JsonPath object

    /*
    Second, we should do setup operations, get JSON response from the API and put it into JsonPath object
    Then we will do query and manipulations with JsonPath class’s methods.
    We can do all of the preparation operations after @Before Junit annotation.
    */
    @Before
    public void setup (){
        //Test Setup
        RestUtil.setBaseURI("http://api.vidible.tv/index.html"); //Setup Base URI
        RestUtil.setBasePath("search"); //Setup Base Path
        RestUtil.setContentType(ContentType.JSON); //Setup Content Type
        RestUtil.createSearchQueryPath("barack obama", "videos.json", "num_of_videos", "4"); //Construct the path
        res = RestUtil.getResponse(); //Get response
        jp = RestUtil.getJsonPath(res); //Get JsonPath
    }

    @Test
    public void T01_StatusCodeTest() {
        //Verify the http response status returned. Check Status Code is 200?
        HelperMethods.checkStatusIs200(res);
    }

    @Test
    public void T02_SearchTermTest() {
        //Verify the response contained the relevant search term (barack obama)
        Assert.assertEquals("Title is wrong!", ("Search results for barack obama"), jp.get("api-info.title"));
        //assertThat(jp.get("api-info.title"), containsString("barrack obama"));
    }

    @Test
    public void T03_verifyOnlyFourVideosReturned() {
        //Verify that only 4 video entries were returned
        Assert.assertEquals("Video Size is not equal to 4", 4, HelperMethods.getVideoIdList(jp).size());
    }

    @Test
    public void T04_duplicateVideoVerification() {
        //Verify that there is no duplicate video
        assertTrue("Duplicate videos exist!", HelperMethods.findDuplicateVideos(HelperMethods.getVideoIdList(jp)));
    }

    @Test
    public void T05_printAttributes() {
        //Print video title, pubDate & duration
        printTitlePubDateDuration(jp);
    }

    @After
    public void afterTest (){
        //Reset Values
        RestUtil.resetBaseURI();
        RestUtil.resetBasePath();
    }

    //*******************
    //***Local Methods***
    //*******************
    //Prints Attributes
    private void printTitlePubDateDuration (JsonPath jp) {
        for(int i=0; i < HelperMethods.getVideoIdList(jp).size(); i++ ) {
            System.out.println("Title: " + jp.get("items.title[" + i + "]"));
            System.out.println("pubDate: " + jp.get("items.pubDate[" + i + "]"));
            System.out.println("duration: " + jp.get("items.duration[" + i + "]"));
            System.out.print("\n");
        }
    }
}