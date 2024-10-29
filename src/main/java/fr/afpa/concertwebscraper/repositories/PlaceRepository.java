package fr.afpa.concertwebscraper.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.afpa.concertwebscraper.entities.Place;

import java.util.UUID;
import java.util.Optional;


public interface PlaceRepository extends CrudRepository<Place, UUID> {
        Optional<Place> findFirstByName(String string);

}
