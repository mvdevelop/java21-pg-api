package com.exemplo.api.exception;

/**
 * Exceção customizada para recurso não encontrado.
 *
 * Substitui RuntimeExceptions genéricas por exceções tipadas,
 * permitindo melhor tratamento de erros e response codes HTTP apropriados.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
