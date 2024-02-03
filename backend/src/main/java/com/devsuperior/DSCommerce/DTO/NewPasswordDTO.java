package com.devsuperior.DSCommerce.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record NewPasswordDTO(@NotBlank(message = "Campo obrigatorio") String token, @NotBlank(message = "Campo obrigatorio")
                             @Size(message = "Deve ter no minimo 8 caracteres") String password) {
}
