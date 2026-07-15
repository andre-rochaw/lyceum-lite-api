package br.techne.api.infra.exceptions;

import java.time.LocalDateTime;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String message
) {
}
