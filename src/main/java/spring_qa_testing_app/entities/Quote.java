package spring_qa_testing_app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Quote")
@Table(
        name = "quote",
        uniqueConstraints = {
                @UniqueConstraint(name = "quote_unique", columnNames = "quote")
        })
public class Quote {
    @Id
    @SequenceGenerator(
            name = "quote_sequence",
            sequenceName = "quote_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "quote_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @NotNull(message = "Quote cannot be null")
    @Column(
            name = "quote"
    )
    private String quote;

    @Column(
            name = "quote_note"
    )
    private String quoteNote;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(
            name = "person_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "person_quote_fk"
            )
    )
    @JsonIgnore
    private Person person;

    @Override
    public String toString() {
        return "\nQuote: " + this.id + "\n quote: " + this.quote + "\n note: " + this.quoteNote ;
    }
}
