package com.ra.controller;

import com.ra.model.dto.request.UserLogin;
import com.ra.model.dto.request.UserRegister;
import com.ra.model.dto.response.JwtResponse;
import com.ra.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign_in")
    public ResponseEntity<JwtResponse> handleLogin(@RequestBody @Valid UserLogin userLogin) {
        return new ResponseEntity<>(userService.handleLogin(userLogin), HttpStatus.OK);
    }

    @PostMapping("/sign_up")
    public ResponseEntity<String> handleRegister(@RequestBody @Valid UserRegister userRegister) {
        return new ResponseEntity<>(userService.handleRegister(userRegister),HttpStatus.CREATED);
    }
}
