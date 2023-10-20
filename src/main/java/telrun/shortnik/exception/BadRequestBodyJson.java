package telrun.shortnik.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class BadRequestBodyJson {
    @JsonProperty
    private final Map<String, String> errors;

    @JsonCreator
    public BadRequestBodyJson(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}

