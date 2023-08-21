package spring_qa_testing_app.cucumber.cucumberglue.testObjects;

import io.cucumber.spring.ScenarioScope;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import spring_qa_testing_app.entities.Person;
import spring_qa_testing_app.entities.Quote;

import java.util.ArrayList;

@Log4j2
@Component("mockPerson")
@ScenarioScope
@Getter
public class TestPerson {

    private Person testPerson;
    public TestPerson (){
        this.testPerson = initiatePerson();
    }

    private Person initiatePerson () {
        Person basePerson = new Person(2L,
                "Peter",
                "Parker",
                "1962-08 to present",
                "SpiderMan original creators: Stan Lee and Steve Ditko",
                null);
        ArrayList<Quote> quotes = new ArrayList<>();
        Quote spiderMan = new Quote(3L,
                "What a tangled web we weave when first we practice to deceive",
                "Sir Walter Scott, 1808, really (but for me, Spiderman)",
                 basePerson);
        quotes.add(spiderMan);
        basePerson.setQuotes(quotes);
        return basePerson;
    }
}

