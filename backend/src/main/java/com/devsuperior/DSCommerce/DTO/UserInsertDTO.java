package com.devsuperior.DSCommerce.DTO;

import com.devsuperior.DSCommerce.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class
UserInsertDTO extends UserDTO {
    @NotBlank(message = "Campo obrigatório")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#])(?:([0-9a-zA-Z$*&@#])(?!\\1)){8,}$")
    @Size(min = 8, message = "Deve ter no mínimo 8 caracteres")
    private String password;

    UserInsertDTO(){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
