package com.devsuperior.DSCommerce.tests;

import com.devsuperior.DSCommerce.entities.PasswordRecover;

import java.time.Instant;
import java.util.UUID;

public class AuthFactory {

    private static final Long tokenMinutes = 30L;

    public static PasswordRecover createdPasswordRecover(String email) {

        String token = UUID.randomUUID().toString();
        PasswordRecover passwordRecover = new PasswordRecover();
        passwordRecover.setEmail(email);
        passwordRecover.setToken(token);
        passwordRecover.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));

        return passwordRecover;
    }
}
