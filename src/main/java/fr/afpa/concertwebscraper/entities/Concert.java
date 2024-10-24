package fr.afpa.concertwebscraper.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="concert")
public class Concert{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

    @Column(name = "schedule")
	private LocalDateTime schedule;

    @Column(name = "price")
	private String price;

    @Column(name = "min_price")
	private int minPrice;

    @Column(name = "max_price")
	private int maxPrice;

    @Column(name = "name", columnDefinition = "TEXT")
	private String name;

	@ManyToOne()
	@JoinColumn(name="place_id")
	private Place place;

	//--------------construct--------------\\
    public Concert (){
        // empty construct for the ORM
    }

	//--------------getters & setters--------------\\
	public LocalDateTime getSchedule(){
		 return this.schedule;
	}

	public void setSchedule (LocalDateTime schedule){
		this.schedule = schedule;
	}

	public String getPrice(){
		 return this.price;
	}

	public void setPrice (String price){
		this.price = price;
	}

	public String getName(){
		 return this.name;
	}

	public void setName (String name){
		this.name = name;
	}

	public UUID getId(){
		 return this.id;
	}

	public void setId (UUID id){
		this.id = id;
	}


	public Place getPlace() {
		return this.place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}


	public int getMinPrice() {
		return this.minPrice;
	}

	public void setMinPrice(int minPrice) {
		this.minPrice = minPrice;
	}

	public int getMaxPrice() {
		return this.maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}
	

	@Override
	public String toString() {
		return "Concert [id=" + id + ", schedule=" + schedule + ", price=" + price + ", name=" + name + ", place="
				+ place + "]";
	}
}

