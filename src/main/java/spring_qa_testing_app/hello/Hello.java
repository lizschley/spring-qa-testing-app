package spring_qa_testing_app.hello;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

/*
https://spring.io/guides/gs/rest-hateoas/

^^^ walks you through the process of creating a “Hello, World” Hypermedia-driven REST web service with Spring.

Hypermedia is an important aspect of REST. It lets you build services that decouple client and server to a large
extent and let them evolve independently. The representations returned for REST resources contain not only data
but also links to related resources.
 */

public class Hello extends RepresentationModel<Hello> {

    private final String content;

    @JsonCreator
    public Hello(@JsonProperty("content") String content) {
        this.content = content;
    }

    public String getContent() {
            return content;
        }
}
