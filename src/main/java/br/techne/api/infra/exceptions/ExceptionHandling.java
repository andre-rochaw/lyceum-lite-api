package br.techne.api.infra.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, messageOrDefault(ex, "Recurso não encontrado."));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, messageOrDefault(ex, "Recurso não encontrado."));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex) {
        return buildResponse(HttpStatus.CONFLICT, messageOrDefault(ex, "Conflito de regra de negocio."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, messageOrDefault(ex, "Requisição inválida."));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, messageOrDefault(ex, "Dados inválidos."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Dados inválidos.");
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException invalidFormatException
                && invalidFormatException.getTargetType().equals(java.time.LocalDate.class)) {
            return buildResponse(HttpStatus.BAD_REQUEST, "Formato de data inválido. Use YYYY-MM-DD.");
        }
        return buildResponse(HttpStatus.BAD_REQUEST, messageOrDefault(ex, "Corpo da requisição inválido."));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, messageOrDefault(ex, "Credenciais inválidas."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, messageOrDefault(ex, "Acesso negado."));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiError> handleLocked(LockedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, messageOrDefault(ex, "Conta bloqueada."));
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ApiError> handleInternalAuthentication(InternalAuthenticationServiceException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, messageOrDefault(ex, "Login ou senha incorretos."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof SQLException sqlEx) {
            String errorMessage = sqlEx.getMessage() != null ? sqlEx.getMessage().toLowerCase() : "";
            if (errorMessage.contains("login")) {
                return buildResponse(HttpStatus.BAD_REQUEST, "Login já existe.");
            }
            if (errorMessage.contains("nome_usuario")) {
                return buildResponse(HttpStatus.BAD_REQUEST, "Nome de usuário já existe.");
            }
            if (errorMessage.contains("cpf")) {
                return buildResponse(HttpStatus.BAD_REQUEST, "CPF já cadastrado.");
            }
            if (errorMessage.contains("email")) {
                return buildResponse(HttpStatus.BAD_REQUEST, "E-mail já cadastrado.");
            }
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de integridade dos dados.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, messageOrDefault(ex, "Erro interno do servidor."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor.");
    }

    private String messageOrDefault(Throwable ex, String defaultMessage) {
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            return defaultMessage;
        }
        return message;
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message) {
        ApiError body = new ApiError(
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                status.value(),
                message
        );
        return ResponseEntity.status(status).body(body);
    }
}
