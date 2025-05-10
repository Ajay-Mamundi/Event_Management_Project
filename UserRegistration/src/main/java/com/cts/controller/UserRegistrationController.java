package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cts.exceptions.InvalidDataException;
import com.cts.exceptions.UserNotFoundException;
import com.cts.model.UserRegistration;
import com.cts.service.UserRegistrationService;

@RestController
@RequestMapping("/users")
public class UserRegistrationController {

    @Autowired
    private UserRegistrationService userService;

    @PostMapping("/signup")
    public UserRegistration signup(@RequestBody UserRegistration user) throws InvalidDataException {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserRegistration user) {
        boolean isValidUser = userService.validateUser(user.getUserEmail(), user.getUserPassword());
        return isValidUser ? "Login successful" : "Invalid credentials";
    }

    @GetMapping("/all")
    public List<UserRegistration> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserRegistration getUserById(@PathVariable int id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @PostMapping("/save")
    public UserRegistration createUser(@RequestBody UserRegistration user) throws InvalidDataException {
        return userService.saveUser(user);
    }

    @PutMapping("/update/{id}")
    public String updateUser(@PathVariable int id, @RequestBody UserRegistration updatedUser) throws UserNotFoundException {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUserById(@PathVariable int id) throws UserNotFoundException {
        userService.deleteUserById(id);
        return "User deleted successfully";
    }
}
