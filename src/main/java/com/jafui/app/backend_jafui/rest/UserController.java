package com.jafui.app.backend_jafui.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jafui.app.backend_jafui.entidades.User;
import com.jafui.app.backend_jafui.negocio.UserService;

import java.util.List;

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
}
