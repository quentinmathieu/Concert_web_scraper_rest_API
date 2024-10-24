package fr.afpa.concertwebscraper.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.afpa.concertwebscraper.entities.Concert;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ConcertRepository extends CrudRepository<Concert, UUID> {

    Concert findByNameAndSchedule(String string, LocalDateTime localDateTime);
    Concert findByName(String string);
    Concert findBySchedule(LocalDateTime localDateTime);


}
