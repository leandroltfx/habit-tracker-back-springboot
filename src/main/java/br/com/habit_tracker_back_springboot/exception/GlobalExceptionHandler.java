package br.com.habit_tracker_back_springboot.exception;

import br.com.habit_tracker_back_springboot.dto.ApiResponseDTO;
import br.com.habit_tracker_back_springboot.dto.FieldErrorDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.CredentialException;
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

    @ExceptionHandler(CredentialException.class)
    public ResponseEntity<ApiResponseDTO> handleCredentialException(
            CredentialException credentialException
    ) {
        ApiResponseDTO apiResponseDTO = ApiResponseDTO
                .builder()
                .message(credentialException.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponseDTO);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO> handleRuntimeException() {
        ApiResponseDTO apiResponseDTO = ApiResponseDTO
                .builder()
                .message("Ocorreu um erro interno, tente novamente.")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponseDTO);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO> handleAccessDeniedException(
            AccessDeniedException accessDeniedException
    ) {
        ApiResponseDTO apiResponseDTO = ApiResponseDTO
                .builder()
                .message(accessDeniedException.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponseDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponseDTO> handleEntityNotFoundException(
            EntityNotFoundException entityNotFoundException
    ) {
        ApiResponseDTO apiResponseDTO = ApiResponseDTO
                .builder()
                .message(entityNotFoundException.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponseDTO);
    }

}
