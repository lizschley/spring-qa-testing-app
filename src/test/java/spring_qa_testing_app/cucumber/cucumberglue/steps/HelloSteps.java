package spring_qa_testing_app.cucumber.cucumberglue.steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloSteps {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private ResponseEntity<String> responseEntity;

    @When("the client named {string} calls hello")
    public void callHello(String name) {
        responseEntity = testRestTemplate.getForEntity("/hello?name=" + name, String.class);
    }

    @Then("the client receives status code of 200")
    public void checkStatus200(){
        assertEquals(responseEntity.getStatusCode() , HttpStatus.OK);
    }

    @And("the client with name {string} receives server hello message")
    public void checkHelloResponseValue(String name) {
        assertTrue(responseEntity.getBody().contains(name));
    }
}
