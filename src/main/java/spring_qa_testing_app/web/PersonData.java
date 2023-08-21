package spring_qa_testing_app.web;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonData {
    private long id;
    private String firstName;
    private String lastName;
    private String dates;
    private String personNote;
    private List <QuoteData> quotes;
}
