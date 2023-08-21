package spring_qa_testing_app.quotes;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring_qa_testing_app.entities.Person;
import spring_qa_testing_app.web.Name;

import java.util.ArrayList;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(value = "SELECT p.id, p.dates, p.last_name, p.first_name, p.person_note " +
         "FROM person p " +
         "ORDER BY p.last_name, p.first_name",
         nativeQuery = true)
    ArrayList<Person> people();

    @Query(value="SELECT * FROM person WHERE first_name=?1 AND last_name=?2",
            nativeQuery = true)
    ArrayList<Person> findPersonByName(String firstName, String lastName);

    @Query("""
         SELECT new spring_qa_testing_app.web.Name(p.lastName, p.firstName)
         FROM Person p
         ORDER BY p.lastName, p.firstName
         """)
    ArrayList<Name> namesInOrder();
}
