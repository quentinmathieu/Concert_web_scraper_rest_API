package fr.afpa.concertwebscraper.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import fr.afpa.concertwebscraper.entities.Concert;
import fr.afpa.concertwebscraper.entities.Genre;
import fr.afpa.concertwebscraper.entities.Place;
import fr.afpa.concertwebscraper.repositories.ConcertRepository;
import fr.afpa.concertwebscraper.repositories.GenreRepository;
import fr.afpa.concertwebscraper.repositories.PlaceRepository;


@Service
public class ScraperService{
    private PlaceRepository placeRepository;
    private ConcertRepository concertRepository;
    private GenreRepository genreRepository;
	private String baseUrl;

	//--------------construct--------------\\
	public ScraperService (PlaceRepository placeRepository, ConcertRepository concertRepository, GenreRepository genreRepository) throws IOException{
        super();
        this.placeRepository = placeRepository;
		this.baseUrl = "http://www.tyzicos.com";
        this.concertRepository = concertRepository;
        this.genreRepository = genreRepository;
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
        List<Place> places = new ArrayList<>();
        Document doc = Jsoup.connect(this.baseUrl+urlEnd).get();
		doc.select("#block-system-main ul li a").forEach(place -> {
            try {
                if (place != null && place.attr("href") != null){
                    Place newPlace = this.analyzePlace(place.attr("href"), isFestival);
                    places.add(newPlace);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return places;
	}

    public Place analyzePlace(String urlEnd, Boolean isFestival) throws IOException{
        Document doc = Jsoup.connect(this.baseUrl+urlEnd).get();
        // get title if exist
        if (doc.select(".title h1") != null && doc.select(".title h1").first() != null) {
            Place place = new Place();
            place.setName(doc.select(".title h1").first().text());
            // get address
            this.placeRepository.save(place);
            if (doc.select(".adress span") != null && doc.select(".adress span").first() != null) {
                place.setAddress(doc.select(".adress span").first().text());
                // get phone if exist
                if (doc.select(".adress span").size() > 1 && doc.select(".adress span").get(1) != null){
                    place.setPhone(doc.select(".adress span").get(1).text());
                }
            }

            // get img if exist
            if (doc.select(".visuelLieu") != null && doc.select(".visuelLieu").first() != null){
                // extract img from the css class
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
            // for each date
            for (Element dateConcerts : doc.select("#block-tyzicos-block-concerts-par-date-et-lieu .date-row")){
                // create each concert
                for (Element oneConcert : dateConcerts.select(".one-concert")) {
                    this.createConcert(oneConcert, place, ScraperService.parseDateTime(dateConcerts.select(".day-num").text(), dateConcerts.select(".month").text(), dateConcerts.select(".year").text(), oneConcert.select(".heure span").text()));
                }
            }
            place.setIsFestival(isFestival);
            return this.placeRepository.save(place);
        }
        return null;
    }

    

   
	public Concert createConcert(Element oneConcert, Place place, LocalDateTime schedule){
        Concert concert = new Concert();

        concert.setSchedule(schedule);
        concert.setPlace(place);
        concert.setName(HtmlUtils.htmlEscape(oneConcert.select(".titre").first().text()));
        concert.setPrice(oneConcert.select(".prix span").text());
        ScraperService.managePrice(concert);
        return concertRepository.save(concert);
	}

    public static void managePrice(Concert concert){
        if (concert.getPrice().contains("-")){
            String[]priceArray = concert.getPrice().split("-");
            int minPrice = Integer.parseInt(priceArray[0].replaceAll("\\D", ""));
            int maxPrice = Integer.parseInt(priceArray[1].replaceAll("\\D", ""));
            concert.setMinPrice(minPrice);
            concert.setMaxPrice(maxPrice);
        }
        else if(concert.getPrice().contains("€")){
            int price = Integer.parseInt(concert.getPrice().replaceAll("\\D", ""));
            concert.setMinPrice(price);
            concert.setMaxPrice(price);
        }
    }

    public List<Genre> analyzeGenres() throws IOException {
        String urlEnd = "/concerts-par-style";
        List<Genre> genres = new ArrayList<>();
        Document doc = Jsoup.connect(this.baseUrl+urlEnd).get();
		doc.select("#block-system-main ul li a").forEach(genre -> {
            if (genre != null && genre.attr("href") != null){
                Genre newGenre;
                try {
                    newGenre = this.analyzeGenre(genre.attr("href"));
                    genres.add(newGenre);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return genres;
	}

	public Genre analyzeGenre(String urlEnd) throws IOException{
        Genre genre = new Genre();
        Document doc = Jsoup.connect(this.baseUrl+urlEnd).get();
        // get title if exist
        if (doc.select(".columns h1") != null && doc.select(".columns h1").first() != null) {
            genre.setName(HtmlUtils.htmlEscape(doc.select(".columns h1").first().text()));
            genreRepository.save(genre);
            // for each date
            for (Element dateConcerts : doc.select("#block-tyzicos-block-concerts-par-date .date-row")){
                // add genre to each concert
                for (Element oneConcert : dateConcerts.select(".one-concert")) {
                    try {
                        Concert concert = concertRepository.findByNameAndSchedule(HtmlUtils.htmlEscape(oneConcert.select(".titre").first().text()), ScraperService.parseDateTime(dateConcerts.select(".day-num").text(), dateConcerts.select(".month").text(), dateConcerts.select(".year").text(), oneConcert.select(".heure span").text()));
                        this.addGenreToConcert(genre, concert);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                   
                }
            }
        }
        genreRepository.save(genre);
        return genre;
	}

    public List<Concert> addGenreToConcert(Genre genre, Concert concert){
        List<Concert> concerts = new ArrayList<>();
        concert.setGenre(genre);
        this.concertRepository.save(concert);
        concerts.add(concert);
        return concerts;
	}

	public void analyzeSite() throws IOException{
        // analyze pages only if the db is empty
        if (((List<Concert>) concertRepository.findAll()).isEmpty()){
            this.analyzePlaces("/concerts-salles-bars/bretagne", false);
            this.analyzePlaces("/concerts-par-festivals/bretagne", true);
            this.analyzeGenres();
        }
	}

    public static LocalDateTime parseDateTime(String dayNum, String month, String year, String time){
        int hour = Integer.parseInt(time.split(" ")[0].split("h")[0]);
        int min = Integer.parseInt(time.split(" ")[0].split("h")[1]);
        int monthNum =  1;
        switch (month.toLowerCase()) {
            case "février":
                monthNum = 2;
                break;
            case "mars":
                monthNum = 3;
                break;
            case "avril":
                monthNum = 4;
                break;
            case "mai":
                monthNum = 5;
                break;
            case "juin":
                monthNum = 6;
                break;
            case "juillet":
                monthNum = 7;
                break;
            case "aout":
                monthNum = 8;
                break;
            case "septembre":
                monthNum = 9;
                break;
            case "octobre":
                monthNum = 10;
                break;
            case "novembre":
                monthNum = 11;
                break;
            case "décembre":
                monthNum = 12;
                break;
            default:
                break;
        }

        return LocalDateTime.of(Integer.parseInt(year), monthNum, Integer.parseInt(dayNum), hour, min);
    }
}
