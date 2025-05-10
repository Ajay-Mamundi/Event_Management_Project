package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cts.exceptions.InvalidDataException;
import com.cts.exceptions.UserNotFoundException;
import com.cts.model.UserRegistration;
import com.cts.repository.UserRegistrationRepository;
import com.cts.service.UserRegistrationServiceImpl;

class UserRegistrationServiceImplTest {

    @Mock
    private UserRegistrationRepository repository;

    @InjectMocks
    private UserRegistrationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<UserRegistration> users = Arrays.asList(new UserRegistration(), new UserRegistration());
        when(repository.findAll()).thenReturn(users);

        List<UserRegistration> result = service.getAllUsers();
        assertEquals(users, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetUserById() throws UserNotFoundException {
        UserRegistration user = new UserRegistration();
        when(repository.findById(1)).thenReturn(Optional.of(user));

        UserRegistration result = service.getUserById(1);
        assertEquals(user, result);
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getUserById(1));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testSaveUser() throws InvalidDataException {
        UserRegistration user = new UserRegistration();
        when(repository.save(user)).thenReturn(user);

        UserRegistration result = service.saveUser(user);
        assertEquals(user, result);
        verify(repository, times(1)).save(user);
    }

    @Test
    void testSaveUserInvalidData() {
        UserRegistration user = new UserRegistration();
        when(repository.save(user)).thenReturn(null);

        assertThrows(InvalidDataException.class, () -> service.saveUser(user));
        verify(repository, times(1)).save(user);
    }

    @Test
    void testUpdateUser() throws UserNotFoundException {
        UserRegistration existingUser = new UserRegistration();
        UserRegistration updatedUser = new UserRegistration();
        when(repository.findById(1)).thenReturn(Optional.of(existingUser));
        when(repository.save(existingUser)).thenReturn(existingUser);

        String result = service.updateUser(1, updatedUser);
        assertEquals("User updated successfully!", result);
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUserNotFound() {
        UserRegistration updatedUser = new UserRegistration();
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.updateUser(1, updatedUser));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testDeleteUserById() throws UserNotFoundException {
        when(repository.existsById(1)).thenReturn(true);

        service.deleteUserById(1);
        verify(repository, times(1)).existsById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteUserByIdNotFound() {
        when(repository.existsById(1)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> service.deleteUserById(1));
        verify(repository, times(1)).existsById(1);
    }

    @Test
    void testValidateUser() {
        UserRegistration user = new UserRegistration();
        user.setUserEmail("test@example.com");
        user.setUserPassword("password");
        when(repository.findByUserEmail("test@example.com")).thenReturn(user);

        boolean result = service.validateUser("test@example.com", "password");
        assertTrue(result);
        verify(repository, times(1)).findByUserEmail("test@example.com");
    }

    @Test
    void testValidateUserInvalid() {
        when(repository.findByUserEmail("test@example.com")).thenReturn(null);

        boolean result = service.validateUser("test@example.com", "password");
        assertFalse(result);
        verify(repository, times(1)).findByUserEmail("test@example.com");
    }
}

