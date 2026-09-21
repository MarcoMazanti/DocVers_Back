package tg.DocVers.Controller.Advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tg.DocVers.DTO.External.ErrorResponse;
import tg.DocVers.Exception.DadosInvalidosException;
import tg.DocVers.Exception.RegistroInexistenteException;
import tg.DocVers.Exception.SolicitacaoNegadaException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Erro 400 - Bad Request
    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<Object> handleRequisicaoIncompletaException(DadosInvalidosException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now()));
    }

    // Erro 401 - Unauthorized
    @ExceptionHandler(SolicitacaoNegadaException.class)
    public ResponseEntity<Object> handleSolicitacaoNegadaException(SolicitacaoNegadaException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), LocalDateTime.now()));
    }

    // Erro 404 - Not Found
    @ExceptionHandler(RegistroInexistenteException.class)
    public ResponseEntity<Object> handleRegistroInexistenteException(RegistroInexistenteException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now()));
    }

    // Erro 500 - Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), LocalDateTime.now()));
    }
}
