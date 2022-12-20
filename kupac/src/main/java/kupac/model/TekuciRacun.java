package kupac.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TekuciRacun {
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	
	private String brojRacuna;
	private double stanje;	
	
	public TekuciRacun() {		
		
	}
	
	public TekuciRacun(String brojRacuna) {
		super();
		this.brojRacuna = brojRacuna;
	}
	public String getBrojRacuna() {
		return brojRacuna;
	}
	public void setBrojRacuna(String brojRacuna) {
		this.brojRacuna = brojRacuna;
	}
	public double getStanje() {
		return stanje;
	}
	public void setStanje(double stanje) {
		this.stanje = stanje;
	} 
	
	public void isplati(double iznos) {
		stanje-=iznos;
	}
	
	public void uplati(double iznos) {
		stanje+=iznos;
	}
	
	

}
