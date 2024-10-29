package fr.afpa.concertwebscraper.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.afpa.concertwebscraper.entities.Concert;
import fr.afpa.concertwebscraper.entities.Place;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Optional;


public interface ConcertRepository extends CrudRepository<Concert, UUID> {

    Optional<Concert> findFirstByNameAndScheduleAndPlace(String string, LocalDateTime localDateTime, Place place);
    Optional<Concert> findFirstByNameAndSchedule(String string, LocalDateTime localDateTime);
    Optional<List<Concert>> findByName(String string);
    Optional<List<Concert>> findBySchedule(LocalDateTime localDateTime);


}
