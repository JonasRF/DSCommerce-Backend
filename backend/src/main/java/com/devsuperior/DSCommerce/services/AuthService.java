package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.DTO.EmailDTO;
import com.devsuperior.DSCommerce.entities.PasswordEncoder;
import com.devsuperior.DSCommerce.entities.User;
import com.devsuperior.DSCommerce.repositories.PasswordRecoverRepository;
import com.devsuperior.DSCommerce.repositories.UserRepository;
import com.devsuperior.DSCommerce.services.exceptions.ForbiddenException;
import com.devsuperior.DSCommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverURI;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private EmailService emailService;

    public void validateSelfOrAdmin(long userId){
        User user = userService.authenticated();
        if(!user.hasRole("ROLE_ADMIN") && !user.getId().equals(userId)){
            throw new ForbiddenException("Access denied");
        }
    }

    @Transactional
    public void createRecoverToken(EmailDTO body) {

        Optional<User> user = userRepository.findByEmail(body.email());
        if(user.isEmpty()){
            throw  new ResourceNotFoundException("Email não encontrado!");
        }

        String token = UUID.randomUUID().toString();

        PasswordEncoder entity = new PasswordEncoder();
        entity.setEmail(body.email());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        entity = passwordRecoverRepository.save(entity);

        String text = "Acesse o link para definir uma nova senha\n\n"
                + recoverURI + token + ". Validade de "+ tokenMinutes + " minutos.";

        emailService.sendEmail(body.email(), "Recuperação de senha", text);
    }
}
