package com.jafui.app.backend_jafui.negocio;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jafui.app.backend_jafui.entidades.Place;
import com.jafui.app.backend_jafui.persistencia.PlaceRepository;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    public String notFoundExceptionText = "Place not found";
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PlaceRepository placeRepo;
    private final AmazonS3 amazonS3;
    private final AmazonDynamoDB amazonDynamoDB;

    public PlaceService(AmazonS3 amazonS3, AmazonDynamoDB amazonDynamoDB, PlaceRepository placeRepository) {
        this.amazonS3 = amazonS3;
        this.amazonDynamoDB = amazonDynamoDB;
        this.placeRepo = placeRepository;
    }

    public List<Place> getAllPlaces() {
        if (logger.isInfoEnabled()) {
            logger.info("Buscando todos os lugares..");
        }
        Iterable<Place> lista = this.placeRepo.findAll();

        if (lista == null) {
            return new ArrayList<Place>();
        }
        return IteratorUtils.toList(lista.iterator());
    }

    public Place getPlaceById(String id) {
        if (logger.isInfoEnabled()) {
            logger.info("Buscando lugar com o codigo {}", id);
        }
        Optional<Place> retorno = placeRepo.findById(id);
        if (!retorno.isPresent()) {
            throw new RuntimeException("Lugar com o id " + id + " nao encontrado");
        }
        return retorno.get();
    }

    public Place savePlace(Place place) {
        if (logger.isInfoEnabled()) {
            logger.info("Salvando lugar com os detalhes {}", place.toString());
        }
        return this.placeRepo.save(place);
    }

    public void deletePlace(String id) {
        if (logger.isInfoEnabled()) {
            logger.info("Ecluindo lugar com id {}", id);
        }
        this.placeRepo.deleteById(id);
    }

    public boolean isPlaceExists(Place place) {
        Optional<Place> retorno = placeRepo.findById(place.getId());
        return retorno.isPresent() ? true : false;
    }

    public void updatePlaceName(String id, String name) {
        Place place = placeRepo.findById(id).orElseThrow(() -> new NotFoundException(notFoundExceptionText));
        place.setName(name);
        placeRepo.save(place);
    }

    public void updatePlaceAddress(String id, String address) {
        Place place = placeRepo.findById(id).orElseThrow(() -> new NotFoundException(notFoundExceptionText));
        place.setAddress(address);
        placeRepo.save(place);
    }

    public void updatePlaceDescription(String id, String description) {
        Place place = placeRepo.findById(id).orElseThrow(() -> new NotFoundException(notFoundExceptionText));
        place.setDescription(description);
        placeRepo.save(place);
    }

    public void updatePlaceRating(String id, Float rating) {
        Place place = placeRepo.findById(id).orElseThrow(() -> new NotFoundException(notFoundExceptionText));
        place.setRating(rating);
        placeRepo.save(place);
    }

    public void updatePlacePhotos(String id, List<String> photos) {
        Place place = placeRepo.findById(id).orElseThrow(() -> new NotFoundException("Place not found"));
        place.setPhotos(photos);
        placeRepo.save(place);
    }


    public Place createPlace(MultipartFile photo, Place placeRequest) throws Exception {
        // Salva a imagem no Amazon S3
        String fileName = photo.getOriginalFilename();
        PutObjectRequest putObjectRequest = new PutObjectRequest("jafui-bucket", fileName, photo.getInputStream(), null);
        amazonS3.putObject(putObjectRequest);

        // Salva o URL da imagem no DynamoDB
        String urlPhoto = amazonS3.getUrl("jafui-bucket", fileName).toString();

        // Cria um objeto Place com as informações do request
        Place place = new Place();
        place.setName(placeRequest.getName());
        place.setAddress(placeRequest.getAddress());
        place.setDescription(placeRequest.getDescription());
        place.setRating(placeRequest.getRating());

        // Cria uma lista de fotos e adiciona a foto salva no Amazon S3
        List<String> fotos = new ArrayList<>();
        fotos.add(urlPhoto);
        place.setPhotos(fotos);

        // Salva o Place no DynamoDB
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.PUT)
                .build();
        DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB, mapperConfig);
        mapper.save(place);

        // Cria a resposta com o ID do Place salvo
        Place responsePlace = new Place();
        responsePlace.setId(place.getId());
        return responsePlace;
    }


}
