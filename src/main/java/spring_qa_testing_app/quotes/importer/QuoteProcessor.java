package spring_qa_testing_app.quotes.importer;

import spring_qa_testing_app.entities.Person;
import spring_qa_testing_app.entities.Quote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class QuoteProcessor extends ArrayList<Quote> {

    private final HashMap<String, Person> personHash;
    private final List<QuotesFromCsv> csvQuoteList;

    public QuoteProcessor(HashMap<String, Person> personHash, List<QuotesFromCsv> csvQuoteList) {
        this.personHash = personHash;
        this.csvQuoteList = csvQuoteList;
    }

    public ArrayList<Quote> processQuotes() {
        Iterator<QuotesFromCsv> quotesFromCsvIterator = csvQuoteList.listIterator();
        ArrayList<Quote> quotes = new ArrayList<>();
        while (quotesFromCsvIterator.hasNext()) {
            QuotesFromCsv csvQuote = quotesFromCsvIterator.next();
            String key = csvQuote.getPersonId();
            Person person = personHash.get(key);
            Quote quote = new Quote();
            quote.setQuote(csvQuote.getQuote());
            quote.setQuoteNote(csvQuote.getQuoteNote());
            quote.setPerson(person);
            quotes.add(quote);
        }
        return quotes;
    }
}
