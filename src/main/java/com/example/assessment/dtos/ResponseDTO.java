package com.example.assessment.dtos;

import com.example.assessment.exceptions.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> implements Serializable {

    private ResponseMetaDTO responseMeta;

    private T data;

    public ResponseDTO(int status, T data, String message) {
        this.responseMeta = new ResponseMetaDTO(status, message);
        this.data = data;
    }

    public ResponseDTO(int status, T data, String message, boolean success) {
        this.responseMeta = new ResponseMetaDTO(status, message, success);
        this.data = data;
    }

    public ResponseDTO(int status, ErrorDTO errorDTO) {
        this.responseMeta = new ResponseMetaDTO(status, errorDTO);
    }

    public String getErrorDisplay() {
        if (responseMeta == null) {
            return "";
        }

        final ErrorDTO error = responseMeta.getError();
        if (error == null || error.getMessage() == null) {
            return "";
        }

        return error.getCode() + ": " + error.getMessage();
    }

}