package com.VillGo.DTO.Responce;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String message;
    private int status;
    private LocalDateTime time;
}