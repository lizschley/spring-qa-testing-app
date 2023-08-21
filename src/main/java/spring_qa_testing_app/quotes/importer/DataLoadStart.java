package spring_qa_testing_app.quotes.importer;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import spring_qa_testing_app.entities.Person;
import spring_qa_testing_app.entities.Quote;
import spring_qa_testing_app.quotes.PersonRepository;
import spring_qa_testing_app.quotes.QuoteRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoadStart implements CommandLineRunner {
    private final PersonRepository personRepository;
    private final QuoteRepository quoteRepository;

    public void run(String... args) throws Exception{
        CsvDataLoader dataLoader = new CsvDataLoader();
        HashMap<String, Person> personHash = dataLoader.loadPerson();
        List<QuotesFromCsv> csvQuoteList = dataLoader.loadQuote();
        QuoteProcessor quoteProcessor = new QuoteProcessor(personHash, csvQuoteList);
        ArrayList<Quote> quotes = quoteProcessor.processQuotes();
        Collection<Person> values = personHash.values();
        ArrayList<Person> people = new ArrayList<Person>(values);
        personRepository.saveAll(people);
        quoteRepository.saveAll(quotes);
    }
}
