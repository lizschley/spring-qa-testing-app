package spring_qa_testing_app.quotes.importer;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import spring_qa_testing_app.entities.Person;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;


@Service
@Log4j2
public class CsvDataLoader {

    public HashMap<String, Person> loadPerson() throws Exception {
        log.info("Loading data using --> person.csv");
        HashMap<String, Person> personHash = new HashMap<String, Person>();
        List<Record> records = csvToList("person.csv");
        for (Record record : records) {
            String person_id = record.getString("person_id");
            Person person = new Person();
            person.setId(Long.parseLong(person_id));
            person.setPersonNote(record.getString("person_note"));
            person.setDates(record.getString("dates"));
            person.setFirstName(record.getString("first_name"));
            person.setLastName(record.getString("last_name"));
            personHash.put(person_id, person);
        }
        log.info(personHash.toString());
        return personHash;
    }

    public List<QuotesFromCsv> loadQuote() throws IOException {
        File file = getFile("quote.csv");
        log.info("Loading data using --> quote.csv");

        List<QuotesFromCsv> beans;
        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            beans = new CsvToBeanBuilder(new CSVReader(reader))
                    .withType(QuotesFromCsv.class)
                    .build()
                    .parse();
        }
        log.info(beans.toString());
        return beans;
    }

    public List<Record> csvToList (String filename) throws Exception {
        File file = getFile(filename);
        InputStream input = new FileInputStream(file);
        CsvParserSettings setting = new CsvParserSettings();
        setting.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(setting);
        return parser.parseAllRecords(input);
    }

    @NotNull
    @Contract("_ -> new")
    private File getFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getFile());
    }
}
