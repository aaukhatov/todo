package io.artur.todo.security;

public class JwtGenerateException extends RuntimeException {

    public JwtGenerateException(String message) {
        super(message);
    }

    public JwtGenerateException(String message, Throwable cause) {
        super(message, cause);
    }
}
