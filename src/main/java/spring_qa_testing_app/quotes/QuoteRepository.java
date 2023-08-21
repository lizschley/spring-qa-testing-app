package spring_qa_testing_app.quotes;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring_qa_testing_app.entities.Quote;
import spring_qa_testing_app.web.PersonQuote;

import java.util.ArrayList;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query("""
         SELECT new spring_qa_testing_app.web.PersonQuote(p.dates, p.lastName, p.firstName,
         p.personNote, q.quote, q.quoteNote)
         FROM Person p
         JOIN Quote q ON p=q.person
         ORDER BY p.lastName, p.firstName
         """)
    ArrayList<PersonQuote> showQuotes();

    ArrayList<Quote> findByPersonId(long personId);
}

