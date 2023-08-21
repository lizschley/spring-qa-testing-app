package spring_qa_testing_app.quotes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomConstraintExceptionHandler {
    
    @ExceptionHandler(CustomConstraintException.class)
    public ResponseEntity<String> handleCustomException(CustomConstraintException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
