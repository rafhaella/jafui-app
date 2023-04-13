package com.jafui.app.backend_jafui.persistencia;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.jafui.app.backend_jafui.entidades.User;

@EnableScan()
public interface UserRepository extends CrudRepository<User, String> {

    public User findByEmail(String email);

}