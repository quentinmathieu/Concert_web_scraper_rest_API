package fr.afpa.concertwebscraper.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="place")
public class Place{
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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	//--------------construct--------------\\

    public Place(){
        // empty for the ORM
    }

	public Place (String image, String address, String phone, String name, String coordinates, UUID id){
		this.image = image;
		this.address = address;
		this.phone = phone;
		this.name = name;
		this.coordinates = coordinates;
		this.id = id;
	}
	public Place (String name){
		this.name = name;
	}

	//--------------getters & setters--------------\\
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


	public int sumConcerts(){
        return 0;
	}
}

