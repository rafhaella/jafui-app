package com.jafui.app.backend_jafui.rest;

import com.amazonaws.services.kms.model.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.jafui.app.backend_jafui.entidades.User;
import com.jafui.app.backend_jafui.negocio.UserService;



import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {

        return userService.getUsers();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) throws Exception{
        Optional<User> user = Optional.ofNullable(userService.getUserById(id));

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            throw new Exception("Usuário com codigo " + id + " nao encontrado");
        }
    }

    @GetMapping("/by_email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) throws Exception{
        Optional<User> user = Optional.ofNullable(userService.getUserByEmail(email));

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            throw new Exception("Usuário com email " + email + " nao encontrado");
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody @NotNull User user) throws Exception {
        return userService.createAccount(user);
    }

    @PostMapping(value="login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (user == null) {
            throw new ValidationException("Invalid login request");
        }

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            if (user.getName() != null && !user.getName().isEmpty()) {
                userService.updateUserName(id, user.getName());
            }
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                userService.updateUserEmail(id, user.getEmail());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                userService.updateUserPassword(id, user.getPassword());
            }
            User updatedUser = userService.getUserById(id);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "{id}")
    public boolean deleteUser(@PathVariable String id) throws Exception {
        userService.deleteUser(id);
        return true;
    }

}
