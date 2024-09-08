package com.belajar.belajartodolist.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.belajar.belajartodolist.models.Users;
import com.belajar.belajartodolist.repositories.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/registrasi")
    public ResponseEntity<Map<String, String>> registrasi(@RequestBody Users user) {
        Map<String, String> response = new HashMap<>();
        try {
            if (userRepository.findByUsername(user.getUsername()) != null) {
                throw new Exception("Username already exists");
            }
            Users newUser = new Users();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(user.getPassword());
            Users savedUser = userRepository.save(newUser);

            response.put("message", "User registered successfully");
            response.put("username", savedUser.getUsername());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Error registering user: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Users user) {
        Map<String, String> response = new HashMap<>();

        try {
            Users existingUser = userRepository.findByUsername(user.getUsername());

            if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
                response.put("message", "Login success");
                response.put("username", existingUser.getUsername());
                return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            } else {
                response.put("message", "Invalid username or password");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response.put("message", "Error during login: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Internal server error: " + e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
