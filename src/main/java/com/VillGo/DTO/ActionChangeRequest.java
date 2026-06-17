package com.VillGo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionChangeRequest {

    private Integer id;
    private Boolean action; // true = active, false = inactive
}
