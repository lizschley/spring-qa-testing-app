package spring_qa_testing_app.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import spring_qa_testing_app.web.PersonData;
import spring_qa_testing_app.web.QuoteData;

import java.io.IOException;
import java.util.ArrayList;

@Service
@Log4j2
public class PersonQuoteJsonInput {

    public PersonData processPersonAndQuotes(String jsonInput)
            throws IOException {
        //create JsonParser object
        JsonParser jsonParser = new JsonFactory().createParser(jsonInput);
        // new PersonData record
        PersonData personData = new PersonData();
        // new QuoteData Array
        ArrayList<QuoteData> quoteList = new ArrayList<>();
        // loop data through using parser
        parseJSON(jsonParser, personData, quoteList);
        // add QuoteData list to PersonData
        personData.setQuotes(quoteList);
        // clean up
        jsonParser.close();
        //print employee object
        log.info("Person Data\n" + personData);
        return personData;
    }

    private PersonData parseJSON(JsonParser jsonParser,
                                 PersonData personData,
                                 ArrayList<QuoteData> quoteList)
            throws IOException {

        //loop through the JsonTokens
        while(jsonParser.nextToken() != JsonToken.END_OBJECT &&
                jsonParser.nextToken() != JsonToken.END_ARRAY ) {
            String key = jsonParser.getCurrentName();
            if (key == null) {
                break;
            }
            System.out.println("key==" + key);
            if("id".equals(key)){
                jsonParser.nextToken();
                personData.setId(jsonParser.getLongValue());
            }else if(key.equals("firstName")){
                personData.setFirstName(jsonParser.getText());
            }else if(key.equals("lastName")){
                personData.setLastName(jsonParser.getText());
            }else if(key.equals("dates")){
                personData.setDates(jsonParser.getText());
            }else if(key.equals("personNote")){
                String personNote = jsonParser.getText();
                if (personNote != null) {
                    personData.setPersonNote(personNote);
                }
            }else if("quotes".equals(key)){
                //nested object, recursive call
                parseJSON(jsonParser, personData, quoteList);
            }else if("quote".equals(key)){
                // add empty quoteData record
                quoteList.add(new QuoteData());
                jsonParser.nextToken();
                int idx = quoteList.size() - 1;
                String quote = jsonParser.getText();
                QuoteData quoteItem = quoteList.get(idx);
                quoteItem.setQuote(quote);
                quoteList.set(idx, quoteItem);
            }else if("quoteNote".equals(key)){
                String quoteNote = jsonParser.getText();
                if (quoteNote != null) {
                    int idx = quoteList.size() - 1;
                    QuoteData quoteItem = quoteList.get(idx);
                    quoteItem.setQuoteNote(quoteNote);
                    quoteList.set(idx, quoteItem);
                }
            }
        }
        return personData;
    }
}
