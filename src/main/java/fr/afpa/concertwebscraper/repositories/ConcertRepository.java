package fr.afpa.concertwebscraper.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.afpa.concertwebscraper.entities.Concert;
import java.util.UUID;

public interface ConcertRepository extends CrudRepository<Concert, UUID> {
}
