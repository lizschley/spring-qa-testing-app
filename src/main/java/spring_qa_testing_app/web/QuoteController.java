package spring_qa_testing_app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_qa_testing_app.services.QuoteService;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class QuoteController {
    private final QuoteService quoteService;


    @Autowired
    QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/show")
    ArrayList<PersonQuote> show() {
        return quoteService.showCuratedQuotes();
    }

    @PostMapping(value = "/quotes")
    public ResponseEntity<ArrayList> addQuotesAndPeople(@RequestBody String quoteInput) throws IOException {
        ArrayList<PersonQuote> personQuotes = quoteService.addPersonAndQuotes(quoteInput);
        return ResponseEntity.ok(personQuotes);
    }

    @RequestMapping(value = "/quote_count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String num_quotes() {
        return quoteService.quoteCount().toString();
    }

    @DeleteMapping(value="/quote/{id}")
    public ResponseEntity<Long> deletePost(@PathVariable Long id) {
        quoteService.deleteById(id);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
