package com.VillGo.DTO.Responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class DataResponse<T> {

    private boolean success;
    private HttpStatus status;
    private String message;
    private T data;
}