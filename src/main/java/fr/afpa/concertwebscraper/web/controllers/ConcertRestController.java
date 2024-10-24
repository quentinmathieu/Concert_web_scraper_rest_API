package fr.afpa.concertwebscraper.web.controllers;

import java.util.List;

import fr.afpa.concertwebscraper.repositories.ConcertRepository;
import fr.afpa.concertwebscraper.repositories.PlaceRepository;
import fr.afpa.concertwebscraper.entities.Concert;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/concerts")
public class ConcertRestController {
 
    private ConcertRepository concertRepository;
    private PlaceRepository placeRepository;

    public ConcertRestController(ConcertRepository concertRepository, PlaceRepository placeRepository) {
        this.concertRepository = concertRepository;
        this.placeRepository = placeRepository;
    }

    // @GetMapping
    // public List<Account> getAll() {
    //     return (List<Account>) accountRepository.findAll(); 
    // }
    @GetMapping
    public List<Concert> getAll() {
        return (List<Concert>) concertRepository.findAll(); 
    }

   
    // @GetMapping("/{id}")
    // public Optional<Account> getOne(@PathVariable long id) {
    //     return accountRepository.findById(id);
    // }

}
