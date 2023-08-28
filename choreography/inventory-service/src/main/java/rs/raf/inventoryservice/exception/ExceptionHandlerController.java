package rs.raf.inventoryservice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> notFoundException(NotFoundException exception) {
        return prepareResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    private ResponseEntity<ExceptionResponse> prepareResponse(HttpStatus status, String message) {
        ExceptionResponse response = new ExceptionResponse(status, LocalDateTime.now(), message);
        log.info("Exception: {}", response);
        return new ResponseEntity<>(response, status);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ExceptionResponse {
        private HttpStatus httpStatus;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        private LocalDateTime timestamp;
        private String message;
    }
}
