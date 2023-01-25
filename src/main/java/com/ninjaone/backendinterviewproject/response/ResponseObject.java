package com.ninjaone.backendinterviewproject.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseObject<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public ResponseObject() {}

    public ResponseObject(HttpStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
