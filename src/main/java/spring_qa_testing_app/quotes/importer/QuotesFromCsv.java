package spring_qa_testing_app.quotes.importer;

import com.opencsv.bean.CsvBindByName;
import lombok.*;


@Setter
@Getter
@ToString
public class QuotesFromCsv {

    @CsvBindByName(column = "quote")
    private String quote;

    @CsvBindByName(column = "quote_note")
    private String quoteNote;

    @CsvBindByName(column = "person_id")
    private String personId;

    public QuotesFromCsv() {
    }
}
