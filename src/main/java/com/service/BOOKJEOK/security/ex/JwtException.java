package com.service.BOOKJEOK.security.ex;

import com.service.BOOKJEOK.security.jwt.JwtError;
import org.springframework.security.core.AuthenticationException;

public class JwtException extends AuthenticationException {
    public JwtException(JwtError message) {
        super(message.getMessage());
    }

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
