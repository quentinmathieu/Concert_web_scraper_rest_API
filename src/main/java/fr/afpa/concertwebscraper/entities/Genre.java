package fr.afpa.concertwebscraper.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="genre")
public class Genre{
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

    @Column(name = "name")
	private String name;

	@OneToMany(mappedBy = "genre")
	@JsonIgnoreProperties({"genre"})
	private List<Concert> concerts;


	//--------------construct--------------\\

    public Genre(){
        // empty for ORM
    }

	//--------------getters & setters--------------\\


	public UUID getId(){
		 return this.id;
	}

	public void setId (UUID id){
		this.id = id;
	}



	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Concert> getConcerts() {
		return this.concerts;
	}

	public void setConcerts(List<Concert> concerts) {
		this.concerts = concerts;
	}



	public int sumConcerts(){
        return 0;
	}

}

