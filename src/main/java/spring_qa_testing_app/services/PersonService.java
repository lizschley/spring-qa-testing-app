package spring_qa_testing_app.services;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import spring_qa_testing_app.entities.Person;
import spring_qa_testing_app.quotes.PersonRepository;
import spring_qa_testing_app.web.Name;
import spring_qa_testing_app.web.PersonData;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Log4j2
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public ArrayList<Person> retrieveAllPeople() {
        return personRepository.people();
    }

    public Person findPersonWithId(Long id) {
        String possibleErrMsg = "Person with " + id + " is not found";
        return personRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, possibleErrMsg));

    }

    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    public ArrayList<Person> findPersonWithName(String firstName, String lastName) {
        return personRepository.findPersonByName(firstName, lastName);
    }

    public Long peopleCount() {
        return personRepository.count();
    }

    @Transactional
    public Person findOrCreatePerson(PersonData personData, PersonRepository personRepository) {
        Person person = null;
        Optional<Person> optPerson = personRepository.findById(personData.getId());
        if (optPerson.isPresent()) {
            person = optPerson.get();
        }
        if (person == null) {
            return createPerson(personData, personRepository);
        }
        namesDatesOk(personData, person);
        log.info("person to be returned is " + person);
        return person;
    }

    @Transactional(readOnly = true)
    public ArrayList<Name> namesInOrder(PersonRepository personRepository) {
        return personRepository.namesInOrder();
    }

    public void namesDatesOk (PersonData personData, Person person) throws IllegalStateException {
        if ((personData.getFirstName() == null || personData.getDates().trim().isEmpty()) &&
                (personData.getLastName() == null || personData.getDates().trim().isEmpty()) &&
                (personData.getDates() == null || personData.getDates().trim().isEmpty())) {
            return;
        }
        if (personData.getId() == person.getId()) {
            equalityCheck(personData, person);
            return;
        }
        inequalityCheck(personData, person);
    }

    private void equalityCheck(PersonData personData, Person person) {
        if (!personData.getFirstName().equals(person.getFirstName()) ||
                !personData.getLastName().equals(person.getLastName()) ||
                !personData.getDates().equals(person.getDates())) {
            throw new IllegalStateException(String.format("Matching Person ids: " + person.getId() + ", but non-matching data"));
        }
    }
    private void inequalityCheck(PersonData personData, Person person) {
        if (personData.getFirstName().equals(person.getFirstName()) &&
                personData.getLastName().equals(person.getLastName()) &&
                personData.getDates().equals(person.getDates())) {
            throw new IllegalStateException(String.format("Non-matching Person id error. Existing id: " + person.getId() +
                    " & passed in id: " + personData.getId() +  ", but unique constraint violation on names and date fields"));
        }
    }
    public Person createPerson(PersonData personData, PersonRepository personRepository) {
        Person person = new Person();
        person.setId(personData.getId());
        person.setPersonNote(personData.getPersonNote());
        person.setDates(personData.getDates());
        person.setFirstName(personData.getFirstName());
        person.setLastName(personData.getLastName());
        return personRepository.save(person);
    }
}
