package me.whiteship.commons;

import java.util.List;

import lombok.Data;

/**
 * @author LeeSooHoon
 */
@Data
public class ErrorResponse {

    private String message;

    private String code;

    private List<FieldError> errors;

    // TODO
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }

}
