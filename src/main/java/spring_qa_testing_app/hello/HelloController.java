package spring_qa_testing_app.hello;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class HelloController {

    private static final String TEMPLATE = "Hello, %s!";

    @RequestMapping("/hello")
    public HttpEntity<Hello> hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        Hello hello = new Hello(String.format(TEMPLATE, name));
        hello.add(linkTo(methodOn(HelloController.class).hello(name)).withSelfRel());
        return new ResponseEntity<>(hello, HttpStatus.OK);
    }
}

