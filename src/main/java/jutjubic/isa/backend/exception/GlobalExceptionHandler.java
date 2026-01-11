package jutjubic.isa.backend.exception;

import jutjubic.isa.backend.dto.ResponseMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessageDTO> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest() // HTTP 400
                .body(new ResponseMessageDTO(ex.getMessage()));
    }
}
