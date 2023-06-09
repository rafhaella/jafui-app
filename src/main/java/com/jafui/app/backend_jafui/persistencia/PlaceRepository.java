package com.jafui.app.backend_jafui.persistencia;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.jafui.app.backend_jafui.entidades.Place;

import java.util.Optional;


@EnableScan()
public interface PlaceRepository extends CrudRepository<Place, String> {

    public Optional<Place> findById(String id);

}
