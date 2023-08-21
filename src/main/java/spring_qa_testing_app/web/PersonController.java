package spring_qa_testing_app.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_qa_testing_app.entities.Person;
import spring_qa_testing_app.services.PersonService;


@CrossOrigin(origins = "http://localhost:8083")
@RestController
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService){
        this.personService = personService;
    }

    @GetMapping("/people")
    public ResponseEntity<List<Person>> getAllPeople() {
        List<Person> people = personService.retrieveAllPeople();

        if (people.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") long id) {
        Person person = personService.findPersonWithId(id);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @GetMapping("/quotesByName")
    public ResponseEntity<ArrayList<Person>> getQuotesByName(@RequestParam("firstName") String firstName,
                                                             @RequestParam("lastName") String lastName) {
        ArrayList<Person> people = personService.findPersonWithName(firstName, lastName);
        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    @DeleteMapping(value = "/person/{id}")
    public ResponseEntity<Long> deletePost(@PathVariable Long id) {
        personService.deleteById(id);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @RequestMapping(value = "/people_count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String num_quotes() {
        return personService.peopleCount().toString();
    }
}
