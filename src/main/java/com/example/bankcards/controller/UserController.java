package com.example.bankcards.controller;

import com.example.bankcards.dto.PageParams;
import com.example.bankcards.dto.PasswordDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.PasswordException;
import com.example.bankcards.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity getAllUsers(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "direction", required = false) String direction,
            @RequestParam(value = "field", required = false) String field
    ) {
        if (page != null || size != null) {
            PageParams pageParams = new PageParams(page, size, direction, field);
            Page<User> pageUsers = userService.getAllUsers(pageParams.getPageable());
            return ResponseEntity.ofNullable(pageUsers);
        }
        return ResponseEntity.ofNullable(userService.getAllUsers());
    }

    @PostMapping("/add")
    @Secured("ROLE_ADMIN")
    public ResponseEntity addUser(@RequestBody @Validated UserDto userDto) {
        User savedUser = this.userService.save(userDto);
        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/remove")
    @Secured("ROLE_ADMIN")
    public ResponseEntity updateUser(
            @RequestParam(value = "username" , required = false) String username,
            @RequestParam(name = "id", required = false) Long id
    ) {
        if (username != null) {
            User user = userService
                    .findUser(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
            userService.removeUser(user);
            return ResponseEntity.ok(username + " successfully removed");
        }
        if (id != null) {
            User user = userService
                    .findUser(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found!"));
            userService.removeUser(user);
            return ResponseEntity.ok("User with id " + id + " successfully removed");
        }
        return ResponseEntity.badRequest().body("Parameter 'id' or 'username' not found");
    }

    @PatchMapping("password")
    @Secured("ROLE_USER")
    public ResponseEntity changePassword(@RequestBody @Validated PasswordDto passwordDto) throws PasswordException {
        User currentUser = userService.getCurrentUser();
        if (userService.isValidUserPassword(currentUser, passwordDto.getOldPassword())) {
            currentUser.setPassword(passwordDto.getNewPassword());
            userService.save(currentUser);
            return ResponseEntity.ok("Password successfully updated");
        }
        throw new PasswordException("Incorrect old password");
    }

    // todo "Доделать обновление пользователя"
    //    @PutMapping("update")
    //    @Secured("ROLE_ADMIN")
    //    public ResponseEntity updateUser(
    //            @RequestParam(name = "id") Long id,
    //            @RequestBody @Validated UpdateUserDto userDto
    //    ) {
    //        User user = userService
    //                .findUser(id)
    //                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found!"));
    //    }
}
