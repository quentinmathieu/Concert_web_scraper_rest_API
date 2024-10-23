package fr.afpa.concertwebscraper.repositories;

import org.springframework.data.repository.CrudRepository;
import fr.afpa.concertwebscraper.entities.Place;

import java.util.UUID;


public interface PlaceRepository extends CrudRepository<Place, UUID> {
}
