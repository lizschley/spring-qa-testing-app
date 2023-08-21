package spring_qa_testing_app.services;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_qa_testing_app.entities.Person;
import spring_qa_testing_app.entities.Quote;
import spring_qa_testing_app.quotes.CustomConstraintException;
import spring_qa_testing_app.quotes.PersonRepository;
import spring_qa_testing_app.quotes.QuoteRepository;
import spring_qa_testing_app.web.PersonData;
import spring_qa_testing_app.web.PersonQuote;
import spring_qa_testing_app.web.QuoteData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

@Service
@Log4j2
public class QuoteService {
    @Autowired
    private QuoteRepository quoteRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;

    @Transactional(readOnly = true)
    public ArrayList<PersonQuote> showCuratedQuotes() {
        return quoteRepository.showQuotes();
    }

    @Transactional
    public ArrayList<PersonQuote> addPersonAndQuotes(String jsonInput) throws IOException,
            IllegalStateException, DataIntegrityViolationException, ConstraintViolationException {
        PersonQuoteJsonInput processor = new PersonQuoteJsonInput();
        PersonData personData = processor.processPersonAndQuotes(jsonInput);
        Person person = personService.findOrCreatePerson(personData, personRepository);
        addQuotes(personData, quoteRepository, person);
        return quoteRepository.showQuotes();
    }

    public Long quoteCount() {
        return quoteRepository.count();
    }
     private void addQuotes(PersonData personData, QuoteRepository quoteRepository, Person person) {
        ArrayList<Quote> quotes = new ArrayList<>();
        ArrayList<QuoteData> quoteDataList = new ArrayList<>();
        quoteDataList.addAll(personData.getQuotes());
        Iterator<QuoteData> quoteDataIterator = quoteDataList.listIterator();
        while (quoteDataIterator.hasNext()) {
            QuoteData quoteData = quoteDataIterator.next();
            Quote quote = new Quote();
            String quoteField = quoteData.getQuote();
            if (quoteField == null || quoteField.trim().isEmpty() || quoteField == "null") {
                throw new CustomConstraintException("Quote must not be null or empty");
            }
            quote.setQuote(quoteField);
            quote.setQuoteNote(quoteData.getQuoteNote());
            quote.setPerson(person);
            log.info("quote (including person found by id == " + quote);
            quotes.add(quote);
        }
        quoteRepository.saveAll(quotes);
    }

    public void deleteById(Long id) {
        quoteRepository.deleteById(id);
    }
}
