package com.devsuperior.DSCommerce.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO(@Email(message = "Email inválido") @NotBlank(message="Campo obrigatório") String email) {

}
