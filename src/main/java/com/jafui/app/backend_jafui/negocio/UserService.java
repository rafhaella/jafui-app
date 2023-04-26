package com.jafui.app.backend_jafui.negocio;

import java.lang.invoke.MethodHandles;
import com.amazonaws.services.kms.model.NotFoundException;
import com.jafui.app.backend_jafui.persistencia.UserRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jafui.app.backend_jafui.entidades.User;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public String notFoundExceptionText = "User not found";
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private  final UserRepository userRepo;

    public UserService(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    public List<User> getUsers() {
        if (logger.isInfoEnabled()) {
            logger.info("Buscando todos os usuarios..");
        }
        Iterable<User> lista = this.userRepo.findAll();

        if (lista == null) {
            return new ArrayList<User>();
        }
        return IteratorUtils.toList(lista.iterator());
    }

    public User getUserById(String id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (logger.isInfoEnabled()) {
            logger.info("Buscando usuario com o codigo {}", id);
        }
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new RuntimeException("Usuário com o id " + id + " nao encontrado");
        }
    }

    public User createAccount(User user) {

        if (logger.isInfoEnabled()) {
            logger.info("Salvando usuário com os detalhes {}", user.toString());
        }
        return this.userRepo.save(user);
    }

    public void updateUserName(String id, String name) {
        User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException(notFoundExceptionText));
        user.setName(name);
        userRepo.save(user);
    }

    public void updateUserEmail(String id, String email) {
        User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException(notFoundExceptionText));
        user.setEmail(email);
        userRepo.save(user);
    }

    public void updateUserPassword(String id, String password) {
        User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException(notFoundExceptionText));
        user.setPassword(password);
        userRepo.save(user);
    }

    public void deleteUser(String id) {
        if (logger.isInfoEnabled()) {
            logger.info("Excluindo usuario com id {}", id);
        }
        this.userRepo.deleteById(id);
    }

}
