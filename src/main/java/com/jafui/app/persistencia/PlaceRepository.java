package com.jafui.app.persistencia;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import com.jafui.app.entidades.Place;

@EnableScan()
public interface PlaceRepository extends CrudRepository<Place, String> {

    public Place findByName(String name);

}
