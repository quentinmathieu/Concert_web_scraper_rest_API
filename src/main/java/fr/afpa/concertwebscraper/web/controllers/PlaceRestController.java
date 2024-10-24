package fr.afpa.concertwebscraper.web.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.afpa.concertwebscraper.repositories.PlaceRepository;
import fr.afpa.concertwebscraper.entities.Place;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/places")
public class PlaceRestController {
 
    private PlaceRepository placeRepository;

    public PlaceRestController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @GetMapping
    public List<Place> getAll() {
        return (List<Place>) placeRepository.findAll(); 
    }

   
    @GetMapping("/{id}")
    public Optional<Place> getOne(@PathVariable UUID id) {
        return placeRepository.findById(id);
    }

}
