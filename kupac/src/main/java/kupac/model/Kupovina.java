package kupac.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
/*
 * kupovina na jednom racunu
 * 
 */

@Entity
public class Kupovina {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	
	private LocalDate datum;
	private String racunId;
	private boolean placen;
	private double uplacenIznos;
	@ManyToOne
	private Kupac kupac;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getDatum() {
		return datum;
	}
	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}
	public String getRacunId() {
		return racunId;
	}
	public void setRacunId(String racunId) {
		this.racunId = racunId;
	}
	public boolean isPlacen() {
		return placen;
	}
	public void setPlacen(boolean placen) {
		this.placen = placen;
	}
	public double getUplacenIznos() {
		return uplacenIznos;
	}
	public void setUplacenIznos(double uplacenIznos) {
		this.uplacenIznos = uplacenIznos;
	}
	public Kupac getKupac() {
		return kupac;
	}
	public void setKupac(Kupac kupac) {
		this.kupac = kupac;
	}
	
	
	
	
	
	

}
