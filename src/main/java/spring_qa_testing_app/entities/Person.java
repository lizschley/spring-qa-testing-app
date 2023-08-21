package spring_qa_testing_app.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Person")
@Table(
        name = "person",
        uniqueConstraints = {
            @UniqueConstraint(name = "person_id_unique", columnNames = "id"),
            @UniqueConstraint(name = "unique_data_constraint", columnNames={"first_name", "last_name", "dates"})
        }
)
public class Person {

    @Id
    @SequenceGenerator(
            name = "person_sequence",
            sequenceName = "person_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "person_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @NotNull(message = "First name cannot be null")
    @Column(
            name = "first_name",
            columnDefinition = "TEXT"
    )
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Column(
            name = "last_name",
            columnDefinition = "TEXT"
    )
    private String lastName;

    @NotNull(message = "Dates must not be null")
    @Column(
            name = "dates",
            columnDefinition = "TEXT"
    )
    private String dates;

    @Column(
            name = "person_note",
            columnDefinition = "TEXT"
    )
    private String personNote;

    @OneToMany(
            mappedBy = "person",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Quote> quotes;

    @Override
    public String toString() {
        return "\nPerson: " + this.id + "\n name: " + this.firstName + " " + this.lastName +
                "\n dates: " + this.dates + "\n note: " + this.personNote;
    }
}
