package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.DTO.EmailDTO;
import com.devsuperior.DSCommerce.entities.PasswordRecover;
import com.devsuperior.DSCommerce.entities.User;
import com.devsuperior.DSCommerce.repositories.PasswordRecoverRepository;
import com.devsuperior.DSCommerce.repositories.UserRepository;
import com.devsuperior.DSCommerce.services.exceptions.ForbiddenException;
import com.devsuperior.DSCommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.DSCommerce.tests.AuthFactory;
import com.devsuperior.DSCommerce.tests.EmailFactory;
import com.devsuperior.DSCommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;
    @Value("${spring.mail.username}")
    private String emailFrom;
    @Mock
    private PasswordRecoverRepository passwordRecoverRepository;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;
    @Mock
    private JavaMailSender javaMailSender;
    private PasswordRecover passwordRecover;
    private User user, admin, selfClient, otherClient;
    private String entity, toEmail, subject, body, existingUserName, nonExistingUserName;
    private EmailDTO emailDTO;

    @BeforeEach
    void setUp() throws Exception{
        existingUserName = "alex@gmail.com";
        toEmail = "jonasflajo@gmail.com";
        subject = "email teste";
        body = "Teste de envio de token";

        admin = UserFactory.createAdminUser();
        selfClient = UserFactory.createCustomClientUser(1L, "Bob");
        otherClient = UserFactory.createCustomClientUser(2L, "Ana");
        user = UserFactory.createCustomClientUser01(3L, "Jonas");
        passwordRecover = AuthFactory.createdPasswordRecover(toEmail);
        emailDTO = new EmailDTO(toEmail);

        when(userRepository.findByEmail(any())).thenReturn(user);
        when(userRepository.findByEmail(any())).thenThrow(ResourceNotFoundException.class);
        when(passwordRecoverRepository.save(any())).thenReturn(passwordRecover);
    }
    @Test
    public void validateValidateSelfOrAdminShouldDoNothingWhenAdminLogged() {

        when(userService.authenticated()).thenReturn(admin);

        Long userId = admin.getId();

        Assertions.assertDoesNotThrow(() -> {
            authService.validateSelfOrAdmin(userId);
        });
    }

    @Test
    public void ValidateSelfOrAdminShouldDoNothingWhenSelfLogged() {

        when(userService.authenticated()).thenReturn(selfClient);

        Long userId = selfClient.getId();

        Assertions.assertDoesNotThrow(() -> {
            authService.validateSelfOrAdmin(userId);
        });
    }

    @Test
    public void validateSelfOrAdminForbiddenExceptionWhenClientOtherLogged() {

        when(userService.authenticated()).thenReturn(selfClient);

        Long userId = otherClient.getId();

        Assertions.assertThrows(ForbiddenException.class, () -> {
            authService.validateSelfOrAdmin(userId);
        });
    }


}
