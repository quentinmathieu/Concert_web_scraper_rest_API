package fr.afpa.concertwebscraper.web.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.afpa.concertwebscraper.repositories.ConcertRepository;
import fr.afpa.concertwebscraper.entities.Concert;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/concerts")
public class ConcertRestController {
 
    private ConcertRepository concertRepository;

    public ConcertRestController(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @GetMapping
    public List<Concert> getAll() {
        return (List<Concert>) concertRepository.findAll(); 
    }

   
    @GetMapping("/{id}")
    public Optional<Concert> getOne(@PathVariable UUID id) {
        return concertRepository.findById(id);
    }

}
