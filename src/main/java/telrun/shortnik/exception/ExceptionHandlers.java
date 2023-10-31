package telrun.shortnik.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ExceptionHandlers {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionHandlers.class);

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BadRequestBodyJson> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            LOGGER.info("incorrect input fieldName -" + fieldName);
        });

        return ResponseEntity.status(400).body(new BadRequestBodyJson(errors));
    }

    @ExceptionHandler(NoPremiumRoleException.class)
    public ResponseEntity<Response> handleException(NoPremiumRoleException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
