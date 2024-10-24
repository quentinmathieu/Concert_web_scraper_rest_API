package fr.afpa.concertwebscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import fr.afpa.concertwebscraper.services.ScraperService;

@EntityScan
@SpringBootApplication
public class ConcertWebScraperApplication {
	
	//  scrap all datas and persist it
	@autowired
	private ScraperService scraperService;
	public static void main(String[] args) {
		SpringApplication.run(ConcertWebScraperApplication.class, args);
	}

}
