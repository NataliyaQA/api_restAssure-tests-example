package ra;


import Helper.Utils;
import api.request.Bookingdates;
import api.request.RequestUser;
import api.response.ResponseUser;
import apiMethods.ApiMethod;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

//import static Helper.Utils.generateStringFromResource;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class TryRestAssured {
    ApiMethod apiMethod = new ApiMethod();
    Utils utils = new Utils();

    @BeforeMethod
    public void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        RestAssured.port = 443;
//      RestAssured.authentication = preemptive().basic("username", "password");
    }

    @Test
    public void whenRequestGet_thenOK() {
        when().request("GET", "/booking")
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void whenValidateResponseTime_thenSuccess() {
        when().get("/users/booking")
                .then().time(lessThan(5000L));
    }

    @Test
    public void givenUrl_whenSuccessOnGetsResponseAndJsonHasRequiredKV_thenCorrect() {
        when().get("/booking/7")
                .then().statusCode(200)
                .assertThat()
                .body("totalprice", equalTo(439));
    }

    @Test
    public void given_post_createUser() {
        String firstname = "Lola";
        String lastname = "Lee";
        int totalPrice = 777;

        //RequestUser user = RequestUser.builder()
        RequestUser user = new RequestUser.Builder() //I use this statement at least several times. My I move it to the class?
                .firstname(firstname)
                .lastname(lastname)
                .additionalneeds("Breakfast")
                //.bookingdates(Bookingdates.builder().checkin("2021-01-01").checkout("2021-12-01").build())
                .bookingdates(new Bookingdates.Builder().checkin("2021-01-01").checkout("2021-12-01").build())
                .totalprice(totalPrice)
                .depositpaid(true)
                .build();

        Response response = apiMethod.postMethod(user, "/booking");

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        ResponseUser responseUser = response.as(ResponseUser.class);

        Assert.assertNotNull(responseUser.getBookingid(), "Booking ID validation");
        Assert.assertEquals(firstname, responseUser.getBooking().getFirstname(), "firstname validation");
        Assert.assertEquals(lastname, responseUser.getBooking().getLastname(), "lastname validation");
        Assert.assertEquals(totalPrice, responseUser.getBooking().getTotalprice(), "totalPrice validation");
    }

    @Test
    public void putchResultForbidden() {
        String firstname = "Name";
        String lastname = "Forbidden";
        String checkin = "01.01.2021";
        String checkout = "01.02.2021";

        //RequestUser user = RequestUser.builder()
        RequestUser user = new RequestUser.Builder()
                .firstname(firstname)
                .lastname(lastname)
                //.bookingdates(Bookingdates.builder().checkin(checkin).checkout(checkout).build())
                .bookingdates(new Bookingdates.Builder().checkin(checkin).checkout(checkout).build())
                .build();

        Response response = apiMethod.patchMethod(user, "/booking/8");

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_FORBIDDEN);
    }


    @Test
    public void postResultBadRequest() throws IOException {
        String jsonBody = utils.generateStringFromJSON(System.getProperty("user.dir")+"\\src\\test\\java\\ra\\badRequest.json");
        Response response = apiMethod.postMethod(jsonBody, "/booking");

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void putchResultWithCreds() {

        String firstname = "NewName";
        String lastname = "NewLastname";
        String checkin = "01.01.2021";
        String checkout = "01.02.2021";
        //write fields to be changed
        //RequestUser user = RequestUser.builder()
        RequestUser user = new RequestUser.Builder()
                .firstname(firstname)
                .lastname(lastname)
                //.bookingdates(Bookingdates.builder().checkin(checkin).checkout(checkout).build())
                .bookingdates(new Bookingdates.Builder().checkin(checkin).checkout(checkout).build())
                .build();

        Response response = apiMethod.patchMethod( user,"/booking/8", "admin", "password123");

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        RequestUser responseUser = response.as(RequestUser.class);

        Assert.assertEquals(firstname, responseUser.getFirstname(), "firstname validation");
        Assert.assertEquals(lastname, responseUser.getLastname(), "lastname validation");
    }

    @Test
    public void putchResultWithCookie() {

        String firstname = randomAlphanumeric(20).toLowerCase();
        String lastname = randomAlphanumeric(20).toLowerCase();
        String checkin = "01.01.2021";
        String checkout = "01.02.2021";

        //RequestUser user = RequestUser.builder()
        RequestUser user = new RequestUser.Builder()  //write fields to be changed
                .firstname(firstname)
                .lastname(lastname)
                //.bookingdates(Bookingdates.builder().checkin(checkin).checkout(checkout).build())
                .bookingdates(new Bookingdates.Builder().checkin(checkin).checkout(checkout).build())
                .build();
        Response response = apiMethod.patchMethod(user, "/booking/8", apiMethod.userToken());

        Assert.assertEquals(response.statusCode(), HttpStatus.SC_OK);
        RequestUser responseUser = response.as(RequestUser.class);
        Assert.assertEquals(firstname, responseUser.getFirstname(), "firstname validation");
        Assert.assertEquals(lastname, responseUser.getLastname(), "lastname validation");
    }

    @Test //passed but there are questions
    public void putchResultNew() {

        String firstname = "NewName";
        String lastname = "NewLastname";
        String checkin = "01.01.2021";
        String checkout = "01.02.2021";
        int totalPrice = 123;

        //RequestUser user = RequestUser.builder()
        RequestUser user = new RequestUser.Builder()
                .firstname(firstname)
                .lastname(lastname)
                .additionalneeds("Breakfast")
                //.bookingdates(Bookingdates.builder().checkin(checkin).checkout(checkout).build())
                .bookingdates(new Bookingdates.Builder().checkin(checkin).checkout(checkout).build())
                .totalprice(totalPrice)
                .depositpaid(true)
                .build();

        //GIVEN
        given()
                .baseUri("https://restful-booker.herokuapp.com/booking/8") // how to do it shorter?
                .cookie("token", apiMethod.userToken())
                .contentType(ContentType.JSON)
                .body(user)
                // WHEN
                .when()
                .patch() // how system knows which fields should be edited?
                // THEN
                .then()
                .assertThat()
                .statusCode(200)
                .body("firstname", Matchers.equalTo("NewName"))
                .body("lastname", Matchers.equalTo("NewLastname"));

    }
}
