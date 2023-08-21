package spring_qa_testing_app.cucumber.cucumberglue.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.json.JSONArray;
import spring_qa_testing_app.cucumber.cucumberglue.testObjects.ScenarioContext;
import spring_qa_testing_app.cucumber.cucumberglue.testObjects.IntegrationTestNames;
import spring_qa_testing_app.cucumber.cucumberglue.testObjects.TestStringInput;
import spring_qa_testing_app.entities.Person;
import spring_qa_testing_app.quotes.PersonRepository;
import spring_qa_testing_app.quotes.QuoteRepository;
import spring_qa_testing_app.services.PersonQuoteJsonInput;
import spring_qa_testing_app.services.PersonService;
import spring_qa_testing_app.web.PersonData;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonQuoteIntegrationTest {

    // keys
    private final String BEFORE_COUNT = "beforeCount";
    private final String RESPONSE_ENTITY = "responseEntity";
    private final String PERSON_DATA = "personData";
    @Autowired
    private QuoteRepository quoteRepository;

    private final IntegrationTestNames testNames = new IntegrationTestNames();
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    private final PersonService personService = new PersonService();
    private ScenarioContext currentScenario;

    @Before("@integration")
    public void setup() {
        this.currentScenario = new ScenarioContext();
        this.currentScenario.setContext(BEFORE_COUNT, quoteRepository.count());
        testNames.setInitialNames(personRepository);
    }

    @When("the client calls GET existing quotes")
    public void callQuote() {
        this.currentScenario.setContext(RESPONSE_ENTITY, testRestTemplate.getForEntity("/show", String.class));
    }
    @Then("the quote client receives status code of 200")
    public void checkStatus200(){
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) this.currentScenario.getContext(RESPONSE_ENTITY);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @And("the client gets a sorted array of four people and four quotes")
    public void checkQuoteResponseValue() throws JSONException {
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) this.currentScenario.getContext(RESPONSE_ENTITY);
        String stringQuotes = responseEntity.getBody();
        //Converting jsonData string into JSON object
        JSONArray jsonArray = new JSONArray(stringQuotes);
        assertEquals((Long) this.currentScenario.getContext(BEFORE_COUNT), jsonArray.length());
        assertOrderByName(jsonArray);
        JSONObject row = jsonArray.getJSONObject(0);
        String quote = row.getString("quote");
        String dates = row.getString("dates");
        assertTrue(quote.contains("understand the pleasures"));
        assertTrue(dates.contains("1775"));
    }
    // Scenario: system does a uniqueness check before a new user is saved
    @When("json string input having a new id with existing name and dates")
    public void createOrUpdatePersonCalledExistingPersonDataAndWrongID() throws IOException {
        TestStringInput data = new TestStringInput();
        String input = data.getInvalidIdUniquenessTestInput();
        PersonQuoteJsonInput converter = new PersonQuoteJsonInput();
        PersonData personData = converter.processPersonAndQuotes(input);

        assertEquals(99, personData.getId());
        assertEquals("Jane Austen", personData.getFirstName() + " " + personData.getLastName());
        assertEquals("Timid men prefer the calm of despotism to the tempestuous sea of liberty.",
                      personData.getQuotes().get(0).getQuote());
        assertEquals("really Thomas Jefferson",
                personData.getQuotes().get(0).getQuoteNote());
        this.currentScenario.setContext(PERSON_DATA, personData);
    }
    @Then("the database will throw data integrity violation exception")
    public void personConstraintViolationOnUniqueNameAndDates() {
        PersonData personData = (PersonData) this.currentScenario.getContext(PERSON_DATA);
        assertThatThrownBy(() -> personService.findOrCreatePerson(personData, personRepository))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @And("the transaction will roll back and the new record will not be created")
    public void ensureNewRecordDoesNotExistUsingFindById() {
        PersonData personData = (PersonData) this.currentScenario.getContext(PERSON_DATA);
        Optional<Person> optPerson = personRepository.findById(personData.getId());
        assertFalse(optPerson.isPresent());
    }

    // Scenario: the add new person with quotes endpoint allows the system to create a person and their quotes
    @When("the api input is turned into a java class")
    public void setUpApiInputForAddingPersonAndQuotes(){
        // delaying work on this for now
        TestStringInput data = new TestStringInput();
        String input = data.getHappyPathValidCreatePersonTestInput();
        this.currentScenario.setContext(RESPONSE_ENTITY, testRestTemplate.postForEntity("/quotes", input, String.class));
    }
    @Then("the api call will show a successful return code")
    public void ensureNewlyCreatedPersonAndQuotesAddedToExistingApi(){
        // can not implement until the above when works
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) this.currentScenario.getContext(RESPONSE_ENTITY);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @And("api return body will show the correct number of quotes and the correct data")
    public void ensureValidityOfAddedPersonAndQuote()  throws JSONException{
        testNames.addToNameArray("Tamora", "Pierce");
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) this.currentScenario.getContext(RESPONSE_ENTITY);
        String stringQuotes = responseEntity.getBody();
        //Converting jsonData string into JSON object
        JSONArray jsonArray = new JSONArray(stringQuotes);
        Long beforeCount = (Long) this.currentScenario.getContext(BEFORE_COUNT);
        assertEquals(beforeCount + 2, jsonArray.length());
        assertOrderByName(jsonArray);
    }

    // Scenario: the quotes endpoint allows the system to find an existing person and add new quotes
    @When("the input is turned into a java class")
    public void setUpApiInputForAddingQuotesOnly(){
        // delaying work on this for now
        TestStringInput data = new TestStringInput();
        String input = data.getHappyPathValidCreateQuotesOnlyTestInput();
        this.currentScenario.setContext(RESPONSE_ENTITY, testRestTemplate.postForEntity("/quotes", input, String.class));
    }

    @Then("api will be successful and return body will show the correct number of quotes")
    public void ensureReturnCodeAndValidityOfAddedQuote() throws JSONException{
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) this.currentScenario.getContext(RESPONSE_ENTITY);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String stringQuotes = responseEntity.getBody();
        //Converting jsonData string into JSON object
        JSONArray jsonArray = new JSONArray(stringQuotes);
        Long beforeCount = (Long) this.currentScenario.getContext(BEFORE_COUNT);
        assertEquals(beforeCount + 1, jsonArray.length());
    }

    // Scenario: find a person by id and person is found
    @When("a person is found by id and the return code is 200")
    public void successfulPersonFoundById(){
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/person/2", String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        this.currentScenario.setContext(RESPONSE_ENTITY, responseEntity);
    }
    @Then("a person and their quotes are returned")
    public void returnPersonWithIdPlusQuotes() throws JSONException{
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) this.currentScenario.getContext(RESPONSE_ENTITY);
        String stringQuotes = responseEntity.getBody();
        //Converting jsonData string into JSON object
        JSONObject record = new JSONObject(stringQuotes);
        System.out.println(record);
        assertEquals(2, record.getInt("id"));
        assertEquals("Rosalind", record.getString("firstName"));
        assertEquals("Franklin", record.getString("lastName"));
        JSONArray jsonArray = new JSONArray(record.getString("quotes"));
        JSONObject row = jsonArray.getJSONObject(0);
        assertTrue(row.getString("quote").contains("Science, for me, gives a partial explanation of life"));
    }
    // Scenario: find a person by id and person is not found
    @Then("a person is not found by id and the return code is 404")
    public void personNotFoundById(){
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/person/159", String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    //Scenario: find a person by name returns an array with the correct person
    @When("a person is found by name")
    public void successfulPersonFoundByName(){
        Map<String, String> map = new HashMap<>();
        map.put("firstName", "Rosalind");
        map.put("lastName", "Franklin");
        String url = "http://localhost:8083/quotesByName?firstName={firstName}&lastName={lastName}";
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(url, String.class, map);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        this.currentScenario.setContext(RESPONSE_ENTITY, responseEntity);
    }
    @Then("the person is returned in an array with a successful return code")
    public void personIsReturnedInArray() throws JSONException{
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) this.currentScenario.getContext(RESPONSE_ENTITY);
        String stringQuotes = responseEntity.getBody();
        //Converting jsonData string into JSON object
        JSONArray jsonArray = new JSONArray(stringQuotes);
        JSONObject row = jsonArray.getJSONObject(0);
        String dates = row.getString("dates");
        assertEquals("Rosalind", row.getString("firstName"));
        assertEquals("Franklin", row.getString("lastName"));
        assertTrue(row.getString("dates").contains("1958"));
        JSONArray quoteArray = new JSONArray(row.getString("quotes"));
        JSONObject firstQuote = quoteArray.getJSONObject(0);
        assertTrue(firstQuote.getString("quote").contains("Science, for me, gives a partial explanation of life"));
    }
    //Scenario: find a person by name with wrong name returns an empty array
    @When("a person is found by name when the name is not in the database")
    public void noPersonIsReturnedIfNoMatchInDatabaseForThenName(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("firstName", "Eliza");
        map.put("lastName", "Dolittle");
        String url = "http://localhost:8083/quotesByName?firstName={firstName}&lastName={lastName}";
        this.currentScenario.setContext(RESPONSE_ENTITY, testRestTemplate.getForEntity(url, String.class, map));
     }
    @Then("the result is an empty array along with a successful return code")
    public void anEmptyArrayIsStillSuccessful() throws JSONException{
        ResponseEntity<String> responseEntity = (ResponseEntity<String>) this.currentScenario.getContext(RESPONSE_ENTITY);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String stringQuotes = responseEntity.getBody();
        //Converting jsonData string into JSON object
        JSONArray jsonArray = new JSONArray(stringQuotes);
        assertEquals(0, jsonArray.length());
    }
    private void assertOrderByName(JSONArray jsonArray) throws JSONException{
        // expected order of first and last names
        ArrayList<String> last = testNames.getLastNameArray();
        ArrayList<String> newLast = new ArrayList<>();
        for(int i = 0; i<jsonArray.length(); i++) {
            log.info(jsonArray.getJSONObject(i));
            // test actual against expected
            JSONObject row = jsonArray.getJSONObject(i);
            String firstName = row.getString("firstName");
            String lastName = row.getString("lastName");
            System.out.println(firstName + " " + lastName);
            if (!newLast.contains(lastName)){
                newLast.add(lastName);
            }
        }
        assertEquals(newLast.size(), last.size());
        assertEquals(newLast, last);
    }
}
