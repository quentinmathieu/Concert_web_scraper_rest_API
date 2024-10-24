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
	private Long price;

    @Column(name = "name")
	private String name;

	@ManyToOne()
	@JoinColumn(name="place_id")
	private Place place;

	//--------------construct--------------\\
    public Concert (){
        // empty construct for the ORM
    }

	public Concert (LocalDateTime schedule, Long price, String name, UUID id){
		this.schedule = schedule;
		this.price = price;
		this.name = name;
		this.id = id;
	}

	//--------------getters & setters--------------\\
	public LocalDateTime getSchedule(){
		 return this.schedule;
	}

	public void setSchedule (LocalDateTime schedule){
		this.schedule = schedule;
	}

	public Long getPrice(){
		 return this.price;
	}

	public void setPrice (Long price){
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

}

