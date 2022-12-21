package lojalnost.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BrojKupovina {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	
	private String email;
	private int brojKupovina;	
	
	public BrojKupovina() {	
		 
	}
	
	
	public BrojKupovina(String email) {		
		this.email = email;
		this.brojKupovina = 0; 
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getBrojKupovina() {
		return brojKupovina;
	}
	public void setBrojKupovina(int brojKupovina) {
		this.brojKupovina = brojKupovina;
	}
	
	
	
	

}
