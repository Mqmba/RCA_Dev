package be.api.dto.response;

import lombok.Data;



@Data
public class ResponseData<T> {
    private final int status;
    public  final String message;
    private final T data;

    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}


