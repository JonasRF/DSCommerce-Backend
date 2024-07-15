package com.devsuperior.DSCommerce.DTO;

import com.devsuperior.DSCommerce.entities.PasswordRecover;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class PasswordEncoderDTO implements Serializable {

    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private Instant expiration;

    public PasswordEncoderDTO(){
    }

    public PasswordEncoderDTO(Long id, String token, String email, Instant expiration) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.expiration = expiration;
    }

    public PasswordEncoderDTO(PasswordRecover entity) {
        id = entity.getId();
        token = entity.getToken();
        email = entity.getEmail();
        expiration = entity.getExpiration();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordEncoderDTO that = (PasswordEncoderDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
