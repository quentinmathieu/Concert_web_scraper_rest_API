package fr.afpa.concertwebscraper.services;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public String fetchCoordinates(String address){
        address = address.replace("[^A-Za-z0-9\\s]", " ");
        address = address.replace("'", " ");
        address = address.replace(" ", "%20");
        address = Normalizer.normalize(address, Normalizer.Form.NFD);
        address = address.replaceAll("[^\\p{ASCII}]", "");
        address = address.replaceAll("[^A-Za-z0-9%]", "");
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL("https://api-adresse.data.gouv.fr/search/?q="+address+"&autocomplete=1");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            var result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // Jackson main object
            ObjectMapper mapper = new ObjectMapper();

            // read the json strings and convert it into JsonNode
            JsonNode node = mapper.readTree(result.toString());

            String lat = node.get("features").get(0).get("geometry").get("coordinates").get(1).toString();
            String lon = node.get("features").get(0).get("geometry").get("coordinates").get(0).toString();
            return lat+";"+lon;
        } 
        catch (IOException e) {
            e.printStackTrace();
            return null;
        } 
        
    }
    

    public Place analyzePlace(String urlEnd, Boolean isFestival) throws IOException{
        Document doc = Jsoup.connect(this.baseUrl+urlEnd).get();
        // get title if exist
        Elements titleContainer = doc.select(".title h1");
        if (titleContainer != null && titleContainer.first() != null) {
            Place place = new Place();
            // get place from db if already exist
            if (placeRepository.findFirstByName(getFirstText(doc, ".title h1")).isPresent()){
                place = placeRepository.findFirstByName(getFirstText(doc, ".title h1")).get();
            }
            else{
                place.setName(removeTags(getFirstText(doc, ".title h1")));
                this.placeRepository.save(place);
            }
            String address = null;
            // get address
            if (doc.select(".adress span") != null && doc.select(".adress span").first() != null && getFirstText(doc, ".adress span") != null) {
                address = getFirstText(doc, ".adress span");
                place.setAddress(address);
                // get phone if exist
                if (doc.select(".adress .tel") != null && doc.select(".adress .tel").first() != null){
                    place.setPhone(getFirstText(doc, ".adress .tel"));
                }
            }

            // get img if exist
            if (doc.select(".visuelLieu") != null && doc.select(".visuelLieu").first() != null && getFirstText(doc, ".visuelLieu") != null){
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
                else if (address != null){
                    place.setCoordinates(fetchCoordinates(address));
                }
            }
            else if (address != null){
                place.setCoordinates(fetchCoordinates(address));
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

    public static String getFirstText(Element parent, String selector){
        return removeTags(parent.select(selector).first().text());
    }

    public static String removeTags(String string){
        return HtmlUtils.htmlUnescape(HtmlUtils.htmlEscape(string));
    }
   
	public Concert createConcert(Element oneConcert, Place place, LocalDateTime schedule){
        String title = getFirstText(oneConcert, ".titre");
        //  Do nothing if event is already in db
        if (concertRepository.findFirstByNameAndScheduleAndPlace(title, schedule, place).isPresent()){
            return null;
        }
        else{
            Concert concert = new Concert();
            concert.setSchedule(schedule);
            concert.setPlace(place);
            concert.setName(removeTags(title));
            concert.setPrice(oneConcert.select(".prix span").text());
            ScraperService.managePrice(concert);
            return concertRepository.save(concert);
        }
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
        Elements titleContainer = doc.select(".columns h1");
        if (titleContainer != null && titleContainer.first() != null) {
            if (genreRepository.findFirstByName(getFirstText(doc, ".columns h1")).isPresent()){
                genre = genreRepository.findFirstByName(getFirstText(doc, ".columns h1")).get();
            }
            else{
                genre.setName(getFirstText(doc, ".columns h1"));
                genreRepository.save(genre);
            }
            // for each date
            for (Element dateConcerts : doc.select("#block-tyzicos-block-concerts-par-date .date-row")){
                // add genre to each concert
                for (Element oneConcert : dateConcerts.select(".one-concert")) {
                    try {
                        Concert concert = concertRepository.findFirstByNameAndSchedule(getFirstText(oneConcert, ".titre"), ScraperService.parseDateTime(dateConcerts.select(".day-num").text(), dateConcerts.select(".month").text(), dateConcerts.select(".year").text(), oneConcert.select(".heure span").text())).get();
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
        this.analyzePlaces("/concerts-par-festivals/bretagne", true);
        this.analyzePlaces("/concerts-salles-bars/bretagne", false);
        // this.analyzeGenres();


        // this.analyzePlace("/lieu-concerts/baleine-deshydratee-0", true);
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
