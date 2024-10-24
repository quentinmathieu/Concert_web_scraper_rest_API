package fr.afpa.concertwebscraper.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.afpa.concertwebscraper.entities.Concert;
import fr.afpa.concertwebscraper.entities.Genre;

import java.time.LocalDateTime;
import java.util.UUID;

public interface GenreRepository extends CrudRepository<Genre, UUID> {



}
