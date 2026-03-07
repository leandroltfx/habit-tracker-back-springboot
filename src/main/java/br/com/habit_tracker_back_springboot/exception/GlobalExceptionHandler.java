package br.com.habit_tracker_back_springboot.exception;

import br.com.habit_tracker_back_springboot.dto.ApiResponseDTO;
import br.com.habit_tracker_back_springboot.dto.FieldErrorDTO;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException methodArgumentNotValidException
    ) {
        List<FieldErrorDTO> fieldErrorDTOS = new ArrayList<>();

        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(err -> {
            FieldErrorDTO fieldErrorDTO = new FieldErrorDTO(err.getDefaultMessage(), err.getField());
            fieldErrorDTOS.add(fieldErrorDTO);
        });

        ApiResponseDTO apiResponseDTO = ApiResponseDTO
                .builder()
                .message("Campos inválidos.")
                .data(fieldErrorDTOS)
                .build();

        return new ResponseEntity<>(apiResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiResponseDTO> handleEntityExistsException(
            EntityExistsException entityExistsException
    ) {
        ApiResponseDTO apiResponseDTO = ApiResponseDTO
                .builder()
                .message(entityExistsException.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(apiResponseDTO, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
