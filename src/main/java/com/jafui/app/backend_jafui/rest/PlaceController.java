package com.jafui.app.backend_jafui.rest;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jafui.app.backend_jafui.entidades.Place;
import com.jafui.app.backend_jafui.negocio.PlaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/places")
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;

    }

    @GetMapping
    public List<Place> getAllPlaces() {

        return placeService.getAllPlaces();
    }

    @GetMapping(value = "{id}")
    public Place getPlaceById(@PathVariable String id) throws Exception {
        if (!ObjectUtils.isEmpty(id)) {
            return placeService.getPlaceById(id);
        }
        throw new Exception("Lugar com codigo " + id + " nao encontrado");
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<List<Place>> deletePlace(@PathVariable String id) {
        try {
            placeService.deletePlace(id);
            List<Place> places = placeService.getAllPlaces();
            return ResponseEntity.ok(places);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para criar um novo lugar
    @PostMapping("")
    public ResponseEntity<Place> createPlace(@RequestParam("photo") ArrayList<MultipartFile> photo,
                                             @RequestParam("name") String name,
                                             @RequestParam("address") String address,
                                             @RequestParam("description") String description,
                                             @RequestParam("rating") int rating) {
        try {
            // Crie um objeto Place com os dados fornecidos
            Place placeRequest = new Place();
            placeRequest.setName(name);
            placeRequest.setAddress(address);
            placeRequest.setDescription(description);
            placeRequest.setRating(rating);

            // Chama o método createPlace da classe PlaceService, que já realiza todas as operações necessárias para criar um lugar no banco de dados e salvar a imagem no S3
            Place savedPlace = placeService.createPlace((ArrayList<MultipartFile>) photo, placeRequest);

            // Retorna a resposta com o lugar criado e o status HTTP 201 (CREATED)
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPlace);
        } catch (Exception e) {
            // Retorna o status HTTP 500 (INTERNAL SERVER ERROR) em caso de erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable String id, @RequestBody Place updatedPlace) {
        try {
            // Busca o lugar a ser atualizado pelo id
            Place placeToUpdate = placeService.getPlaceById(id);

            // Chama o método updatePlace da classe PlaceService, que atualiza o lugar no banco de dados
            Place updatedPlaceInDB = placeService.updatePlace(placeToUpdate.getId(), updatedPlace);

            // Retorna a resposta com o lugar atualizado e o status HTTP 200 (OK)
            return ResponseEntity.ok(updatedPlaceInDB);
        } catch (NoSuchElementException e) {
            // Retorna o status HTTP 404 (NOT FOUND) caso o lugar não seja encontrado pelo id
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Retorna o status HTTP 500 (INTERNAL SERVER ERROR) em caso de erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




}





