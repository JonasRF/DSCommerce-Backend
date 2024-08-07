package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.DTO.UserDTO;
import com.devsuperior.DSCommerce.entities.User;
import com.devsuperior.DSCommerce.projections.UserDetailsProjection;
import com.devsuperior.DSCommerce.repositories.UserRepository;
import com.devsuperior.DSCommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.DSCommerce.tests.UserDetailsFactory;
import com.devsuperior.DSCommerce.tests.UserFactory;
import com.devsuperior.DSCommerce.util.CustomUserUtil;
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
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServicesTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomUserUtil customUserUtil;
    private String existingUserName, nonExistingUserName;
    private long existingId, nonExistingId;
    private User user, username;
    private UserDTO userDTO;
    private List<UserDetailsProjection> userDetails;
    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        existingUserName = "alex@gmail.com";
        nonExistingUserName = "user@gmail.com";

        user = UserFactory.createCustomAdminUser(1L, existingUserName);
        username = UserFactory.createClientUser();
        userDetails = UserDetailsFactory.createCustomAdminUser(existingUserName);

        userDTO = new UserDTO(user);

        when(userRepository.findById(existingId)).thenReturn(Optional.ofNullable(username));
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());


        when(userRepository.searchUserAndRolesByEmail(existingUserName)).thenReturn(userDetails);
        when(userRepository.searchUserAndRolesByEmail(nonExistingUserName)).thenReturn(new ArrayList<>());

        when(userRepository.findByEmail(existingUserName)).thenReturn(user);
        when(userRepository.findByEmail(nonExistingUserName)).thenThrow(UsernameNotFoundException.class);
    }

    @Test
    public void findByIdShouldReturnUserDTOWhenIdExists() {
        UserDTO result = userService.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getName(), username.getName());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdNonExistingId() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(nonExistingId);
        });
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

    @Test
    public void authenticatedShouldReturnUserWhenUserExists() {

        when(customUserUtil.getLoggedUsername()).thenReturn(existingUserName);

        User result = userService.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), existingUserName);
    }

    @Test
    public void authenticatedShouldThrowUserNameNotFoundExceptionWhenUserDoesNotExist() {

        doThrow(ClassCastException.class).when(customUserUtil).getLoggedUsername();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.authenticated();
        });
    }
    @Test
    public void getMeShouldReturnUserDTOWhenAuthenticated() {

          UserService spyUserService = spy(userService);

          doReturn(user).when(spyUserService).authenticated();
          UserDTO result = spyUserService.getMe();

          Assertions.assertNotNull(result);
          Assertions.assertEquals(result.getEmail(), existingUserName);
    }

    @Test
    public void getMeShouldThrowUserNameNotFoundExceptionWhenUserNotAuthenticated() {

        UserService spyUserService = spy(userService);
        doThrow(UsernameNotFoundException.class).when(spyUserService).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDTO result = spyUserService.getMe();
        });
    }
}

