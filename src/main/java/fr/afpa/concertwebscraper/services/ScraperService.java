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
	private String baseUrl;

	//--------------construct--------------\\
	public ScraperService (PlaceRepository placeRepository) throws IOException{
        super();
        this.placeRepository = placeRepository;
		this.baseUrl = "http://www.tyzicos.com";
        this.analyzeSite();
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

	public List<Place> analyzePlaces(String urlEnd, Boolean isFestival) throws IOException{

        Document doc = Jsoup.connect(this.baseUrl+urlEnd).get();
		doc.select("#block-system-main ul li a").forEach(place -> {
            try {
                if (place != null && place.attr("href") != null){
                    this.analyzePlace(place.attr("href"), isFestival);
                }
            } catch (IOException e) {
            }
        });
        return new ArrayList<>();
	}

    public Place analyzePlace(String urlEnd, Boolean isFestival) throws IOException{
        Document doc = Jsoup.connect(this.baseUrl+urlEnd).get();
        // get title if exist
        if (doc.select(".title h1") != null && doc.select(".title h1").first() != null) {
            Place place = new Place();
            place.setName(doc.select(".title h1").first().text());
            // get address
            if (doc.select(".adress span") != null && doc.select(".adress span").first() != null) {
                place.setAddress(doc.select(".adress span").first().text());
                // get phone if exist
                if (doc.select(".adress span").size() > 1 && doc.select(".adress span").get(1) != null){
                    place.setPhone(doc.select(".adress span").get(1).text());
                }
            }

            // get img if exist
            if (doc.select(".visuelLieu") != null && doc.select(".visuelLieu").first() != null){
                String style = doc.select(".visuelLieu").first().attr("style");
                place.setImage( style.substring( style.indexOf("http://"), style.indexOf("')") ) );
            }
            // get coordinates if exist
            if (doc.select("#mapId") != null && doc.select("#mapId").attr("data-lat") != null && doc.select("#mapId").attr("data-lon") != null){
                String coordString = doc.select("#mapId").attr("data-lat")+";"+doc.select("#mapId").attr("data-lon");
                if (coordString.length() > 20){
                    place.setCoordinates(coordString);
                }
            }
            // place.setIsFestival(isFestival);
            return this.placeRepository.save(place);
        }
        return null;
    }


	// public List<Concert> analyzeDate(){
	// }

	// public Concert analyzeConcert(){
	// }

	// public List<Genre> addGenres(){
	// }

	// public List<Concert> analyzeGenre(){
	// }

	public void analyzeSite() throws IOException{
        // this.analyzePlaces("/concerts-salles-bars/bretagne", false);
        // this.analyzePlaces("/concerts-par-festivals/bretagne", true);
        this.analyzePlace("/lieu-concerts/le-vauban", false);
	}


}
