package spring_qa_testing_app.quotes;

public class CustomConstraintException extends RuntimeException {
    public CustomConstraintException(String message) {
        super(message);
    }
}
