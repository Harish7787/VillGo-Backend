package com.VillGo.DTO.Responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@AllArgsConstructor
public class ListResponse<T> {

    private boolean success;
    private HttpStatus status;
    private String message;
    private List<T> data;
}
