package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.DTO.NewPasswordDTO;
import com.devsuperior.DSCommerce.DTO.PasswordEncoderDTO;
import com.devsuperior.DSCommerce.entities.PasswordRecover;
import com.devsuperior.DSCommerce.entities.User;
import com.devsuperior.DSCommerce.repositories.PasswordRecoverRepository;
import com.devsuperior.DSCommerce.repositories.UserRepository;
import com.devsuperior.DSCommerce.services.exceptions.ForbiddenException;
import com.devsuperior.DSCommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverURI;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private EmailService emailService;

    public void validateSelfOrAdmin(Long userId) {
        User user = userService.authenticated();

        if (user.hasRole("ROLE_ADMIN", "ROLE_EMPLOYEE")) {
                return;
            }
            if (!user.getId().equals(userId)) {
                throw new ForbiddenException("Access denied");
            }
        }

    @Transactional
    public PasswordEncoderDTO createRecoverToken(PasswordEncoderDTO body) {

        User user = userRepository.findByEmail(body.getEmail());
        if(user == null){
            throw  new ResourceNotFoundException("Email não encontrado!");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail());
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        entity = passwordRecoverRepository.save(entity);

        String text = "Acesse o link para definir uma nova senha\n\n"
                + recoverURI + token + ". Validade de "+ tokenMinutes + " minutos.";

        emailService.sendEmail(body.getEmail(), "Recuperação de senha", text);
        return new PasswordEncoderDTO(entity);
    }

    @Transactional
    public void saveNewPassword(NewPasswordDTO body) {
        List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(body.token(), Instant.now());
        if(result.isEmpty()){
            throw new ResourceNotFoundException("token invalido");
        }
        User user = userRepository.findByEmail(result.get(0).getEmail());
        user.setPassword(passwordEncoder.encode(body.password()));
        user = userRepository.save(user);
    }
}
