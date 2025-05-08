package com.cts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cts.exceptions.UserNotFoundException;
import com.cts.model.UserRegistration;
import com.cts.repository.UserRegistrationRepository;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {
    @Autowired
    private UserRegistrationRepository repository;

    @Override
    public List<UserRegistration> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public UserRegistration getUserById(int id) throws UserNotFoundException {
        Optional<UserRegistration> optional = repository.findById(id);
        if (optional.isPresent())
            return optional.get();
        else
            throw new UserNotFoundException("Invalid ID");
    }

    @Override
    public UserRegistration saveUser(UserRegistration user) {
        return repository.save(user);
    }

    @Override
    public String updateUser(int id, UserRegistration updatedUser) throws UserNotFoundException {
        Optional<UserRegistration> optionalUser = repository.findById(id);
        if (optionalUser.isPresent()) {
            UserRegistration existingUser = optionalUser.get();
            existingUser.setUserName(updatedUser.getUserName());
            existingUser.setUserEmail(updatedUser.getUserEmail());
            existingUser.setUserPassword(updatedUser.getUserPassword());
            existingUser.setUserContactNumber(updatedUser.getUserContactNumber());
            repository.save(existingUser);
            return "User updated successfully!";
        }
        throw new UserNotFoundException("User not found with id: " + id);
    }

    @Override
    public void deleteUserById(int id) throws UserNotFoundException {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

//    @Override
//    public void validateUserDetails(UserRegistration user) {
//        if (!StringUtils.hasText(user.getUserName())) {
//            throw new IllegalArgumentException("Name is required");
//        }
//        if (!StringUtils.hasText(user.getUserEmail()) || !user.getUserEmail().contains("@")) {
//            throw new IllegalArgumentException("Valid email is required");
//        }
//        if (!StringUtils.hasText(user.getUserPassword()) || user.getUserPassword().length() < 6) {
//            throw new IllegalArgumentException("Password must be at least 6 characters int");
//        }
//        if (!StringUtils.hasText(user.getUserContactNumber()) || user.getUserContactNumber().length() != 10) {
//            throw new IllegalArgumentException("Valid contact number is required");
//        }
//    }

    @Override
    public boolean validateUser(String email, String password) {
        UserRegistration user = repository.findByUserEmail(email);
        if(user != null && user.getUserPassword().equals(password)) {
        	return true;
        }
        return false;
        		
    }
}
