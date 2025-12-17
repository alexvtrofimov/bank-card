package com.example.bankcards.controller;

import com.example.bankcards.dto.SignUserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtUtil;
import com.example.bankcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signin")
    public String signIn(@RequestBody SignUserDto userRequest) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userRequest.username(),
                userRequest.password()
        );

        Authentication authenticate = authenticationManager.authenticate(
                authentication
        );

        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody SignUserDto userRequest) {
        if (userService.existsUsername(userRequest.username())) {
            return "User already exist";
        }

        final User newUser = new User(
                null,
                userRequest.username(),
                userRequest.password(),
                Role.ROLE_USER,
                new ArrayList<>()
        );
        userService.save(newUser);
        return String.format("User %s successfully created", newUser.getUsername());
    }
}
