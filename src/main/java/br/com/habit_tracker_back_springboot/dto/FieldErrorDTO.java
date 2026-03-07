package br.com.habit_tracker_back_springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorDTO {

    private String message;
    private String field;
}
