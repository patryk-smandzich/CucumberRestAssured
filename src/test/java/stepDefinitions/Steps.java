package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.junit.Assert;
import testData.NewUser;

public class Steps {

    private static final String BASE_URL = "https://reqres.in/";

    private static NewUser responseBody;

    public RequestSpecification base() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification httpRequest = RestAssured.given();
        httpRequest.header("Content-Type", "application/json");
        return httpRequest;
    }

    @Given("There are some users")
    public void there_are_some_users() {

        Response response = base().post("/api/users");
        Assert.assertEquals(response.getStatusCode(), 201);

    }

    @When("Add a new user")
    public void add_a_new_user() {

        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "lindsay.ferguson@reqres.in");
        requestParams.put("password", "testPass");

        RequestSpecification request = base();
        request.body(requestParams.toJSONString());
        Response response = request.post("/api/register");
        Assert.assertEquals(response.getStatusCode(), 200);
        ResponseBody body = response.getBody();

        responseBody = body.as(NewUser.class);
        System.out.println(responseBody.id);
    }

    @Then("The new user is there")
    public void the_new_user_is_there() {

        Response response = base().get("/api/users/" + responseBody.id);
        Assert.assertEquals(response.getStatusCode(), 200);

        JsonPath json = response.jsonPath();
        Assert.assertEquals(json.get("data.first_name"), "Lindsay");
    }

}
