package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.DTO.EmailDTO;
import com.devsuperior.DSCommerce.entities.PasswordRecover;
import com.devsuperior.DSCommerce.entities.User;
import com.devsuperior.DSCommerce.repositories.UserRepository;
import com.devsuperior.DSCommerce.services.exceptions.ForbiddenException;
import com.devsuperior.DSCommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.DSCommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    private User user, admin, selfClient, otherClient;

    private String email, existingUserName, nonExistingUserName;

    @BeforeEach
    void setUp() throws Exception{
        existingUserName = "alex@gmail.com";

        admin = UserFactory.createAdminUser();
        selfClient = UserFactory.createCustomClientUser(1L, "Bob");
        otherClient = UserFactory.createCustomClientUser(2L, "Ana");

        user = UserFactory.createCustomAdminUser(1L, existingUserName);

        when(userRepository.findByEmail(existingUserName)).thenReturn(user);
        when(userRepository.findByEmail(any())).thenThrow(ResourceNotFoundException.class);

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
