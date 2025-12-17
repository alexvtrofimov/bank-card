package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUser(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User save(UserDto userDto) {
        Role role = userDto.getRole() != null ? Role.valueOf(userDto.getRole()) : Role.ROLE_USER;
        User user = new User(
                null,
                userDto.getUsername(),
                userDto.getPassword(),
                role,
                new ArrayList<>()
        );
        return save(user);
    }

    public boolean existsUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void removeUser(User user) {
        userRepository.delete(user);
    }

    public boolean isValidUserPassword(User user, String rawPassword) {
        String encodedPassword = user.getPassword();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return findUser(principal.getUsername()).get();
    }
}
