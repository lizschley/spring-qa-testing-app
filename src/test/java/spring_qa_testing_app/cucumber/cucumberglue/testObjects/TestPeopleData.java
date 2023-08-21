package spring_qa_testing_app.cucumber.cucumberglue.testObjects;

import io.cucumber.spring.ScenarioScope;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import spring_qa_testing_app.entities.Person;
import spring_qa_testing_app.entities.Quote;

import java.util.ArrayList;
import java.util.HashMap;

@Log4j2
@Component("mockPeople")
@ScenarioScope
public class TestPeopleData {

    private ArrayList<Person> personList;

    public TestPeopleData(){
        this.personList = initiatePeopleData();
    }

    public ArrayList<Person> getPersonList() {
        this.personList = personList;
        return personList;
    }

    private ArrayList<Person> initiatePeopleData () {
        ArrayList<Person> people = new ArrayList<Person>();
        HashMap<String, Person> peopleHash = peopleToUse();
        HashMap<String, Quote> quoteHash = quotesToUse(peopleHash);

        ArrayList<Quote> janeList = new ArrayList<Quote>();
        janeList.add(quoteHash.get("janeFirst"));
        janeList.add(quoteHash.get("janeSecond"));
        peopleHash.get("jane").setQuotes(janeList);

        ArrayList<Quote> peterList = new ArrayList<Quote>();
        peterList.add(quoteHash.get("spiderMan"));
        peopleHash.get("peter").setQuotes(peterList);

        ArrayList<Quote> harrietList = new ArrayList<Quote>();
        harrietList.add(quoteHash.get("harrietM"));
        peopleHash.get("harriet").setQuotes(harrietList);

        people.add(peopleHash.get("jane"));
        people.add(peopleHash.get("peter"));
        people.add(peopleHash.get("harriet"));
        return people;
    }

    public HashMap<String, Person> peopleToUse () {
        Person jane = new Person(1L, "Jane", "Austen", "1775-12-16 to 1817-7-18", null, null);
        Person peter = new Person(2L, "Peter", "Parker", "1962-08 to present", "SpiderMan original creators: Stan Lee and Steve Ditko", null);
        Person harriet = new Person(3L, "Harriet M.", "Welsch", "1964-10 (Harriet the Spy original publication)", "Louise Fitzhugh", null);

        HashMap<String, Person> peopleHash = new HashMap<>();
        peopleHash.put("jane", jane);
        peopleHash.put("peter", peter);
        peopleHash.put("harriet", harriet);
        return peopleHash;
    }

    public HashMap<String, Quote> quotesToUse (HashMap<String, Person> people) {
        Quote janeFirst = new Quote(1L, "One half of the world cannot understand the pleasures of the other.", "Emma in Emma", people.get("jane"));
        Quote janeSecond = new Quote(2L, "The person, be it gentleman or lady, who has not pleasure in a good novel, must be intolerably stupid.", "Catherine in Northanger Abbey", people.get("jane"));
        Quote spiderMan = new Quote(3L, "What a tangled web we weave when first we practice to deceive", "Sir Walter Scott, 1808, really (but for me, Spiderman)", people.get("peter"));
        Quote harrietM = new Quote(4L, "It won’t do you a bit of good to know everything if you don’t do anything with it.", "Ole Golly in Harriet the Spy", people.get("harriet"));

        HashMap<String, Quote> quoteHash = new HashMap<>();
        quoteHash.put("janeFirst", janeFirst);
        quoteHash.put("janeSecond", janeSecond);
        quoteHash.put("spiderMan", spiderMan);
        quoteHash.put("harrietM", harrietM);

        return quoteHash;
    }
}
