package be.api.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


public class ResponseSuccess extends ResponseEntity<ResponseSuccess.Payload> {


    // Mô tả response trả về khi thành công cho PUT, PATCH, DELETE
    public ResponseSuccess(HttpStatusCode status, String message) {
        super(new Payload(status.value(), message), HttpStatus.OK);
    }

    // Mô tả response trả về khi thành công cho GET, POST có dữ liệu trả về
    public ResponseSuccess(HttpStatusCode status, String message, Object data) {
        super(new Payload(status.value(), message, data), HttpStatus.OK);
    }

    @Data
    public static class Payload {
        private final int status;
        private final String message;
        private Object data;

        public Payload(int status, String message) {
            this.status = status;
            this.message = message;
        }
        public Payload(int status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }
    }
}
