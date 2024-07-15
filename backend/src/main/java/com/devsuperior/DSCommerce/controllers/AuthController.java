package com.devsuperior.DSCommerce.controllers;

import com.devsuperior.DSCommerce.DTO.NewPasswordDTO;
import com.devsuperior.DSCommerce.DTO.PasswordEncoderDTO;
import com.devsuperior.DSCommerce.DTO.UserDTO;
import com.devsuperior.DSCommerce.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/recover-token")
    public ResponseEntity<PasswordEncoderDTO> createRecoverToken(@Valid @RequestBody PasswordEncoderDTO body) {
         PasswordEncoderDTO dto = authService.createRecoverToken(body);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping(value = "/new-password")
    public ResponseEntity<UserDTO> saveNewPassword(@Valid @RequestBody NewPasswordDTO body) {
        authService.saveNewPassword(body);
        return ResponseEntity.noContent().build();
    }
}
