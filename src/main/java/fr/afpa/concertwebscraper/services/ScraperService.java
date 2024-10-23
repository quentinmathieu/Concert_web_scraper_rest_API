package fr.afpa.concertwebscraper.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import fr.afpa.concertwebscraper.entities.Concert;

public class ScraperService{
	private static ScraperService instance;
	private String url;

	//--------------construct--------------\\
	public ScraperService (String url) throws IOException{
        super();
		this.url = url;
        this.analyzeSite();
	}

	public static void setInstance (ScraperService instance){
		ScraperService.instance = instance;
	}

	public String getUrl(){
		 return this.url;
	}

	public void setUrl (String url){
		this.url = url;
	}


	public String scrapElement(String selector) throws IOException{
        return "";
	}

	// public List<Place> analyzePlaces(){
    //     return new ArrayList<>();
	// }

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
        Document doc = Jsoup.connect(this.url+"/concerts-salles-bars/bretagne").get();
		doc.select(".nom b").stream().map(place -> place.text()).forEach(System.out::println);
	}


    public static final ScraperService getInstance(String url) throws IOException {
        //Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
        //d'éviter un appel coûteux à synchronized, 
        //une fois que l'instanciation est faite.
        if (ScraperService.instance == null) {
           // Le mot-clé synchronized sur ce bloc empêche toute instanciation
           // multiple même par différents "threads".
           // Il est TRES important.
           synchronized(ScraperService.class) {
             if (ScraperService.instance == null) {
               ScraperService.instance = new ScraperService(url);
             }
           }
        }
        return ScraperService.instance;
    }
}
