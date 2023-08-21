package spring_qa_testing_app.cucumber.cucumberglue.steps;

import io.cucumber.java.en.Then;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.test.context.SpringBootTest;
import spring_qa_testing_app.quotes.importer.CsvDataLoader;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Log4j2
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CsvLoaderErrorTest {

    @Then("the app or tests will throw an error if the file does not exist")
    public void tryUnsuccessfullyToFindFile() {
        CsvDataLoader dataLoader = new CsvDataLoader();
        String badFileName = "people.csv";
        Throwable exception = assertThrows(
        NullPointerException.class, () -> {
            dataLoader.csvToList(badFileName);
        });
    }
}
