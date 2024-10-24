package fr.afpa.concertwebscraper.web.controllers;

import java.io.IOException;
import java.util.List;

import fr.afpa.concertwebscraper.repositories.PlaceRepository;
import fr.afpa.concertwebscraper.entities.Place;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/places")
public class PlaceRestController {
 
    private PlaceRepository placeRepository;

    public PlaceRestController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    // @GetMapping
    // public List<Account> getAll() {
    //     return (List<Account>) accountRepository.findAll(); 
    // }
    @GetMapping
    public List<Place> getAll() throws IOException {
        return (List<Place>) placeRepository.findAll(); 
    }

   
    // @GetMapping("/{id}")
    // public Optional<Account> getOne(@PathVariable long id) {
    //     return accountRepository.findById(id);
    // }

}
