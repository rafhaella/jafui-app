package com.jafui.app.negocio;

import java.lang.invoke.MethodHandles;

import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jafui.app.entidades.User;
import com.jafui.app.persistencia.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepo;

    public UserService(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    public List<User> getUsers(){
        if(logger.isInfoEnabled()){
            logger.info("Buscando todos os usuarios..");
        }

        Iterable<User> lista = this.userRepo.findAll();

        if(lista == null){
            return new ArrayList<User>();
        }
        return IteratorUtils.toList(lista.iterator());

    }
}
