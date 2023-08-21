package spring_qa_testing_app.cucumber.cucumberglue.testObjects;


import io.cucumber.spring.ScenarioScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring_qa_testing_app.quotes.PersonRepository;
import spring_qa_testing_app.services.PersonService;
import spring_qa_testing_app.web.Name;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

@Component
@ScenarioScope
public class IntegrationTestNames {
    private ArrayList<String> first;
    private ArrayList<String> last;

    private ArrayList<String> delimited_names;

    @Autowired
    private PersonService personService = new PersonService();

    public void setInitialNames(PersonRepository personRepository) {
        ArrayList<Name> names = personService.namesInOrder(personRepository);
        delimited_names = new ArrayList<String>();
        first = new ArrayList<String>();
        last = new ArrayList<String>();
        Iterator<Name> namesIterator = names.listIterator();
        while (namesIterator.hasNext()) {
            Name name = namesIterator.next();
            delimited_names.add(name.lastName() + ";" + name.firstName());
            first.add(name.firstName());
            last.add(name.lastName());
        }
    }

    public void addToNameArray(String firstName, String lastName){
        String name = lastName + ";" + firstName;
        delimited_names.add(name);
        Collections.sort(delimited_names);
        String[] temp;
        first.clear();
        last.clear();
        for(int i = 0; i < delimited_names.size(); i++) {
            name = delimited_names.get(i);
            temp = name.split(";");
            last.add(temp[0]);
            first.add(temp[1]);
        }
    }

    public ArrayList<String> getLastNameArray(){
        return last;
    }
}
