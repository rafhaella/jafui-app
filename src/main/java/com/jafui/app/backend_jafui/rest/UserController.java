package com.jafui.app.backend_jafui.rest;

import com.amazonaws.services.kms.model.NotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
//    @GetMapping(value = "{id}")
//    public User getUserById(@PathVariable String id) throws Exception {
//        if (!ObjectUtils.isEmpty(id)) {
//            return userService.getUserById(id);
//        }
//        throw new Exception("Usuário com codigo " + id + " nao encontrado");
//    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody @NotNull User user) throws Exception {
        return userService.createAccount(user);
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

//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/{id}")
//    public Object deleteUser(@PathVariable String id) {
//        try {
//            userService.deleteUser(id);
//            List<User> users = userService.getUsers();
//            return ResponseEntity.noContent();
//        } catch (NotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

}
