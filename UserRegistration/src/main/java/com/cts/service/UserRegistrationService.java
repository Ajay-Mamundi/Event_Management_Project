package com.cts.service;

import java.util.*;

import com.cts.exceptions.UserNotFoundException;
import com.cts.model.UserRegistration;

public interface UserRegistrationService {

	public abstract List<UserRegistration> getAllUsers();

	public abstract UserRegistration getUserById(int id) throws UserNotFoundException;

	public abstract UserRegistration saveUser(UserRegistration user);

	public abstract String updateUser(int id, UserRegistration updatedUser);

	public abstract void deleteUserById(int id);

	public abstract boolean validateUser(String email, String password);



}
