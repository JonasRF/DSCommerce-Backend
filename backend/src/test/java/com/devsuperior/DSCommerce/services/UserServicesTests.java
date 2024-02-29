package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.entities.User;
import com.devsuperior.DSCommerce.projections.UserDetailsProjection;
import com.devsuperior.DSCommerce.repositories.UserRepository;
import com.devsuperior.DSCommerce.tests.UserDetailsFactory;
import com.devsuperior.DSCommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServicesTests {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private String existingUserName, nonExistingUserName;
    private User user;
    private List<UserDetailsProjection> userDetails;
    @BeforeEach
    void setUp() {
        existingUserName = "maria@gmail.com";
        nonExistingUserName = "user@gmail.com";

        user = UserFactory.createCustomAdminUser(1L, existingUserName);
        userDetails = UserDetailsFactory.createCustomAdminUser(existingUserName);

        when(userRepository.searchUserAndRolesByEmail(existingUserName)).thenReturn(userDetails);
        when(userRepository.searchUserAndRolesByEmail(nonExistingUserName)).thenReturn(new ArrayList<>());
    }

    @Test
    public void loadUserByUserNameShouldReturnUserDetailsWhenUserExists() {

        UserDetails result = userService.loadUserByUsername(existingUserName);

        Assertions.assertEquals(result.getUsername(), existingUserName);
        Assertions.assertNotNull(result);
    }

    @Test
    public void loadUserByUserNameShouldThrowUsernameNotFoundExceptionWhenUserNameDoesNotExist() {

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(nonExistingUserName);
        });
    }
}
