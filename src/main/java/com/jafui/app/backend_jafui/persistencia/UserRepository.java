package com.jafui.app.backend_jafui.persistencia;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.jafui.app.backend_jafui.entidades.User;

import java.util.Optional;

@EnableScan()
public interface UserRepository extends CrudRepository<User, String> {

      public Optional<User> findById(String id);

}