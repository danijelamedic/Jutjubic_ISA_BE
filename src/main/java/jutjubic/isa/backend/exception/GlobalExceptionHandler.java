package jutjubic.isa.backend.exception;

import jutjubic.isa.backend.dto.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessage> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest() // HTTP 400
                .body(new ResponseMessage(ex.getMessage()));
    }
}
