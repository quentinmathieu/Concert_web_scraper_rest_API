package fr.afpa.concertwebscraper.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="place")
public class Place{
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

    @Column(name = "image")
	private String image;

    @Column(name = "address")
	private String address;

    @Column(name = "phone")
	private String phone;

    @Column(name = "name")
	private String name;

    @Column(name = "coordinates")
	private String coordinates;

	@OneToMany(mappedBy = "place")
	@JsonIgnoreProperties({"place"})
	private List<Concert> concerts;

	@Column(name = "isFestival")
	private Boolean isFestival = false;

	@Column
	private LocalDateTime nextConcert = null;

	@Column
	private LocalDateTime farthestConcert = null;

	@Column
	private int highestPrice = -1;

	@Column
	private int lowestPrice = -1;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "genre_place", 
	joinColumns = @JoinColumn(name = "place_id"), 
	inverseJoinColumns = @JoinColumn(name = "genre_id"))
	@JsonIgnoreProperties({"places"})
	private List<Genre> genres = new ArrayList<>();

	//--------------construct--------------\\

    public Place(){
        // empty for ORM
    }

	//--------------getters & setters--------------\\


	

	public String getImage(){
		 return this.image;
	}

	public LocalDateTime getNextConcert() {
		return nextConcert;
	}

	public void setNextConcert(LocalDateTime nextConcert) {
		this.nextConcert = nextConcert;
	}

	public LocalDateTime getFarthestConcert() {
		return farthestConcert;
	}

	public void setFarthestConcert(LocalDateTime farthestConcert) {
		this.farthestConcert = farthestConcert;
	}

	public int getHighestPrice() {
		return highestPrice;
	}

	public void setHighestPrice(int highestPrice) {
		this.highestPrice = highestPrice;
	}

	public int getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(int lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public void setImage (String image){
		this.image = image;
	}

	public String getAddress(){
		 return this.address;
	}

	public void setAddress (String address){
		this.address = address;
	}

	public String getPhone(){
		 return this.phone;
	}

	public void setPhone (String phone){
		this.phone = phone;
	}

	public String getName(){
		 return this.name;
	}

	public void setName (String name){
		this.name = name;
	}

	public String getCoordinates(){
		 return this.coordinates;
	}

	public void setCoordinates (String coordinates){
		this.coordinates = coordinates;
	}

	public UUID getId(){
		 return this.id;
	}

	public void setId (UUID id){
		this.id = id;
	}


	public Boolean isIsFestival() {
		return this.isFestival;
	}

	public Boolean getIsFestival() {
		return this.isFestival;
	}

	public void setIsFestival(Boolean isFestival) {
		this.isFestival = isFestival;
	}

	public int sumConcerts(){
        return this.concerts.size();
	}

	public List<Concert> getConcerts() {
		return this.concerts;
	}

	public void setConcerts(List<Concert> concerts) {
		this.concerts = concerts;
	}



	public void addConcert(Concert concert){
		this.concerts.add(concert);
	}



}

