package fr.afpa.concertwebscraper.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import fr.afpa.concertwebscraper.entities.Concert;
import fr.afpa.concertwebscraper.entities.Place;
import fr.afpa.concertwebscraper.repositories.ConcertRepository;
import fr.afpa.concertwebscraper.repositories.PlaceRepository;

@Service
public class ScraperService{
    private PlaceRepository placeRepository;
	private static ScraperService instance;
	private String baseUrl;

	//--------------construct--------------\\
	public ScraperService (PlaceRepository placeRepository) throws IOException{
        super();
        this.placeRepository = placeRepository;
		this.baseUrl = "http://www.tyzicos.com";
        this.analyzeSite();
	}

	public static void setInstance (ScraperService instance){
		ScraperService.instance = instance;
	}

	public String getBaseUrl(){
		 return this.baseUrl;
	}

	public void setBaseUrl (String url){
		this.baseUrl = url;
	}


	public String scrapElement(String selector) throws IOException{
        return "";
	}

	public List<Place> analyzePlaces(String urlEnd) throws IOException{

        Document doc = Jsoup.connect(this.baseUrl+urlEnd).get();
		doc.select("ul li a").forEach(place -> {
            try {
                if (place != null && place.attr("href") != null){
                    this.analyzePlace(place.attr("href"));
                }
            } catch (IOException e) {
                System.err.println("meh");
                e.printStackTrace();
            }
        });
        return new ArrayList<>();
	}

    public Place analyzePlace(String urlEnd) throws IOException{
        Document doc = Jsoup.connect(this.baseUrl+urlEnd).get();
        // get title if exist
        if (doc.select(".title h1") != null && doc.select(".title h1").first() != null) {
            Place place = new Place();
            place.setName(doc.select(".title h1").first().text());
            // get adress
            if (doc.select(".adress span") != null && doc.select(".adress span").first() != null) {
                place.setAddress(doc.select(".adress span").first().text());
                // get phone if exist
                if (doc.select(".adress span").get(1) != null){
                    place.setPhone(doc.select(".adress span").get(1).text());
                }
            }

            // get img if exist
            if (doc.select(".visuelLieu") != null && doc.select(".visuelLieu").first() != null){
                String style = doc.select(".visuelLieu").first().attr("style");
                place.setImage( style.substring( style.indexOf("http://"), style.indexOf("')") ) );
            }
            // get coordinates if exist
            if (doc.select("#mapId") != null && doc.select("#mapId").attr("data-lat") != null && doc.select("#mapId").attr("data-lon")!= null){
                place.setCoordinates(doc.select("#mapId").attr("data-lat")+";"+doc.select("#mapId").attr("data-lon"));
            }
            return this.placeRepository.save(place);
        }
        return null;
    }

	// public List<Place> analyzeFestivals(){
	// }

	// public List<Concert> analyzeDate(){
	// }

	// public Concert analyzeConcert(){
	// }

	// public List<Genre> addGenres(){
	// }

	// public List<Concert> analyzeGenre(){
	// }

	public void analyzeSite() throws IOException{
        this.analyzePlaces("/concerts-salles-bars/bretagne");
        this.analyzePlaces("/concerts-par-festivals/bretagne");
        // this.analyzePlace("/lieu-concerts/le-vauban");
	}


    // public static final ScraperService getInstance(String url) throws IOException {
    //     //Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
    //     //d'éviter un appel coûteux à synchronized, 
    //     //une fois que l'instanciation est faite.
    //     if (ScraperService.instance == null) {
    //        // Le mot-clé synchronized sur ce bloc empêche toute instanciation
    //        // multiple même par différents "threads".
    //        // Il est TRES important.
    //        synchronized(ScraperService.class) {
    //          if (ScraperService.instance == null) {
    //            ScraperService.instance = new ScraperService(url, ScraperService.placeRepository);
    //          }
    //        }
    //     }
    //     return ScraperService.instance;
    // }
}
