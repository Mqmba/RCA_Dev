package be.api.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorResponse {
    private Date timestamp;
    private int status;
    private String path;
    private String error;
    public String message;
}
