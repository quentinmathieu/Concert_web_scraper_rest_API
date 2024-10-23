package fr.afpa.concertwebscraper;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import fr.afpa.concertwebscraper.services.ScraperService;

@EntityScan
@SpringBootApplication
public class ConcertWebScraperApplication {
	public static void main(String[] args) throws IOException {
		ScraperService.getInstance("http://www.tyzicos.com");
		// SpringApplication.run(ConcertWebScraperApplication.class, args);
	}

}
