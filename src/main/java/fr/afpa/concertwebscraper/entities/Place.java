package fr.afpa.concertwebscraper.entities;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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

	@Transient
	private int sumConcerts = 5;

	//--------------construct--------------\\

    public Place(){
        // empty for ORM
    }

	//--------------getters & setters--------------\\
	public int getSumConcerts() {
		return sumConcerts;
	}

	public void setSumConcerts(int sumConcerts) {
		this.sumConcerts = sumConcerts;
	}

	public String getImage(){
		 return this.image;
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

