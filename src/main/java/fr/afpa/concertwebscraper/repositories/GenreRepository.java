package fr.afpa.concertwebscraper.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.afpa.concertwebscraper.entities.Genre;

import java.util.UUID;
import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, UUID> {
        Optional<Genre> findFirstByName(String string);
}
