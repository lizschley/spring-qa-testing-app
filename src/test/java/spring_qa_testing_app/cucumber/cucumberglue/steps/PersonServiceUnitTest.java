package spring_qa_testing_app.cucumber.cucumberglue.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import spring_qa_testing_app.cucumber.cucumberglue.testObjects.ScenarioContext;
import spring_qa_testing_app.cucumber.cucumberglue.testObjects.TestPeopleData;
import spring_qa_testing_app.cucumber.cucumberglue.testObjects.TestPerson;
import spring_qa_testing_app.entities.Person;
import spring_qa_testing_app.entities.Quote;
import spring_qa_testing_app.quotes.PersonRepository;
import spring_qa_testing_app.services.PersonService;
import spring_qa_testing_app.web.PersonData;
import spring_qa_testing_app.web.QuoteData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringContains.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceUnitTest {
    private final String MOCK_PERSON = "mockPerson";
    private final String MOCK_PERSON_DATA = "mockPersonData";
    @Mock
    private PersonRepository mockRepository;
    @InjectMocks
    private PersonService underTest;
    @Spy
    private PersonService spiedUpon;

    private ScenarioContext currentScenario;

    @Before("@personServiceUnit")
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.currentScenario = new ScenarioContext();
    }

    @When("user makes a call to get all people")
    public void userMakesCallToGetAllPeople() {
        TestPeopleData peopleData = new TestPeopleData();
        ArrayList<Person> mockPeople = peopleData.getPersonList();
        when(mockRepository.people()).thenReturn(mockPeople);
        assertEquals(3, mockRepository.people().size());
    }
    @Then("service will return a list of three people")
    public void serviceWillReturnListOfThreePeople() {
        ArrayList<Person> peopleList = underTest.retrieveAllPeople();
        assertEquals(3, peopleList.size());
    }

    @And("service will return quotes along with the specified people")
    public void serviceWillReturnExpectedPeopleAndQuotes() {
        ArrayList<Person> peopleList = underTest.retrieveAllPeople();
        String name = peopleList.get(0).getFirstName() + " " + peopleList.get(0).getLastName();
        assertEquals("Jane Austen", name);
        ArrayList<Quote> janeList = new ArrayList<>(peopleList.get(0).getQuotes());
        assertEquals(2, janeList.size());
        ArrayList<Quote> spiderManList = new ArrayList<>(peopleList.get(1).getQuotes());
        String quote = spiderManList.get(0).getQuote();
        String quoteNote = spiderManList.get(0).getQuoteNote();
        assertThat(quote, containsString("tangled web"));
        assertThat(quoteNote, containsStringIgnoringCase("sir walter scott"));
        assertThat(quoteNote, containsStringIgnoringCase("spiderman"));
    }

    @When("user makes a call to get all people when the person table is empty")
    public void userMakesCallToGetPeopleFromEmptyTable() {
        ArrayList<Person> mockPeople = new ArrayList<>();
        when(mockRepository.people()).thenReturn(mockPeople);
        assertEquals(0, mockRepository.people().size());
    }
    @Then("service will return an empty list")
    public void serviceWillReturnEmptyArray() {
        ArrayList<Person> peopleList = underTest.retrieveAllPeople();
        assertEquals(0, peopleList.size());
    }

    //  Scenario: code will prevent creating a person with duplicate data
    @When("a person is not found by id, but the name and date find a match")
    public void personNotFoundByIdWithNameDateMatch(){
        TestPerson testPerson = new TestPerson();
        Person mockPerson = testPerson.getTestPerson();
        PersonData mockPersonData = correspondingPersonData(mockPerson);
        mockPersonData.setId(99L);
        setPersonContext(mockPerson, mockPersonData);
    }
    @Then("an exception will be thrown even without a call to the database")
    public void ExceptionThrownEvenWithoutCallToDatabase(){
        Person mockPerson = (Person) currentScenario.getContext(MOCK_PERSON);
        PersonData mockPersonData = (PersonData) currentScenario.getContext(MOCK_PERSON_DATA);
        assertThatThrownBy(() -> underTest.namesDatesOk(mockPersonData, mockPerson))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Non-matching Person id error. Existing id: " + mockPerson.getId() +
                        " & passed in id: " + mockPersonData.getId() +
                        ", but unique constraint violation on names and date fields");
    }

    // Scenario call to check a person found by id but with different name or dates will fail
    @When("a person is found by id but either the name fields or dates field does not match the existing record")
    public void personFoundByIdBtInputNotPerfectMatch() {
        TestPerson testPerson = new TestPerson();
        Person mockPerson = testPerson.getTestPerson();
        PersonData mockPersonData = correspondingPersonData(mockPerson);
        mockPersonData.setFirstName("Alexandria");
        setPersonContext(mockPerson, mockPersonData);
    }
    @Then("an illegal state exception will be thrown")
    public void illegalStateExceptionThrown(){
        Person mockPerson = (Person) currentScenario.getContext(MOCK_PERSON);
        PersonData mockPersonData = (PersonData) currentScenario.getContext(MOCK_PERSON_DATA);
        assertThatThrownBy(() -> underTest.namesDatesOk(mockPersonData, mockPerson))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Matching Person ids: " + mockPerson.getId() + ", but non-matching data");
    }

    // Scenario: person data checker will not throw exception if only person id and quotes are passed in
    @When("a Person was found or created and there is no data conflict")
    public void personFoundOrCreatedNoDataConflict(){
        TestPerson testPerson = new TestPerson();
        Person mockPerson = testPerson.getTestPerson();
        PersonData mockPersonData = correspondingPersonData(mockPerson);
        setPersonContext(mockPerson, mockPersonData);
    }
    @Then("no exception will be thrown")
    public void NoExceptionWillBeThrown(){
        Person mockPerson = (Person) currentScenario.getContext(MOCK_PERSON);
        PersonData mockPersonData = (PersonData) currentScenario.getContext(MOCK_PERSON_DATA);
        assertDoesNotThrow(() -> underTest.namesDatesOk(mockPersonData, mockPerson));
    }

    // Scenario: person data checker will not throw exception if only person id and quotes are passed in
    @When("no person specific data other that id is passed in")
    public void onlyPersonSpecificDataPassedInIsID(){
        TestPerson testPerson = new TestPerson();
        Person mockPerson = testPerson.getTestPerson();
        PersonData mockPersonData = idAndQuotesAreOnlyPersonData(mockPerson);
        setPersonContext(mockPerson, mockPersonData);
    }
    @Then("exception will never be thrown by the person data checker")
    public void exceptionNotThrownByPersonDataChecker(){
        Person mockPerson = (Person) currentScenario.getContext(MOCK_PERSON);
        PersonData mockPersonData = (PersonData) currentScenario.getContext(MOCK_PERSON_DATA);
        assertDoesNotThrow(() -> underTest.namesDatesOk(mockPersonData, mockPerson));
    }

    @When("find or create person is called with mocked data that does not find a person")
    public void mockPersonDataAndPersonAndPersonRepository(){
        TestPerson testPerson = new TestPerson();
        Person mockPerson = testPerson.getTestPerson();
        PersonData mockPersonData = correspondingPersonData(mockPerson);
        when(mockRepository.findById(mockPersonData.getId())).thenReturn(Optional.empty());
        setPersonContext(mockPerson, mockPersonData);
    }
    @Then("the create person method will be called")
    public void ensurePersonIsReturned(){
        PersonData mockPersonData = (PersonData) currentScenario.getContext(MOCK_PERSON_DATA);
        spiedUpon.findOrCreatePerson(mockPersonData, mockRepository);
        Mockito.verify(mockRepository, Mockito.times(1)).findById(mockPersonData.getId());
        Mockito.verify(spiedUpon, Mockito.times(1)).createPerson(mockPersonData, mockRepository);
    }
    private void setPersonContext(Person mockPerson, PersonData mockPersonData) {
        currentScenario.setContext(MOCK_PERSON, mockPerson);
        currentScenario.setContext(MOCK_PERSON_DATA, mockPersonData);
    }
    private PersonData correspondingPersonData(Person testPerson) {
        ArrayList<QuoteData> quoteList = new ArrayList<>();
        PersonData personData = new PersonData();
        personData.setId(testPerson.getId());
        personData.setFirstName(testPerson.getFirstName());
        personData.setLastName(testPerson.getLastName());
        personData.setDates(testPerson.getDates());
        personData.setPersonNote(testPerson.getPersonNote());
        List<Quote> originalList = testPerson.getQuotes();
        for(Quote origQuote : originalList){
            QuoteData newQuote = new QuoteData();
            newQuote.setQuote(origQuote.getQuote());
            newQuote.setQuoteNote(origQuote.getQuoteNote());
            quoteList.add(newQuote);
        }
        personData.setQuotes(quoteList);
        return personData;
    }

    private PersonData idAndQuotesAreOnlyPersonData(Person testPerson) {
        ArrayList<QuoteData> quoteList = new ArrayList<>();
        PersonData personData = new PersonData();
        personData.setId(testPerson.getId());
        List<Quote> originalList = testPerson.getQuotes();
        for(Quote origQuote : originalList){
            QuoteData newQuote = new QuoteData();
            newQuote.setQuote(origQuote.getQuote());
            newQuote.setQuoteNote(origQuote.getQuoteNote());
            quoteList.add(newQuote);
        }
        personData.setQuotes(quoteList);
        return personData;
    }
}
