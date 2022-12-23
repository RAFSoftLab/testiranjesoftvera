package trgovina.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Racun {
	
	private int kupacId;
	private String racunId;
	private boolean zatvoren;
	private LocalDate datum;	
	private Map<String, Integer> artikli = new HashMap<>();
	
	
	public Racun(String racunId, LocalDate datumIzdavanja) {	
		this.racunId = racunId;
		this.datum = datumIzdavanja;
		this.zatvoren = false;
	}
	public String getRacunId() {
		return racunId;
	}
	public void setRacunId(String racunId) {
		this.racunId = racunId;
	}
	public boolean isZatvoren() {
		return zatvoren;
	}
	public void zatvori() {
		this.zatvoren = true;
	}
	public LocalDate getDatum() {
		return datum;
	}
	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}
	
	public int getBrojArtikala() {
		return artikli.size();
	}
	
	public int getKupacId() {
		return kupacId;
	}
	public void setKupacId(int kupacId) {
		this.kupacId = kupacId;
	}
	public void setZatvoren(boolean zatvoren) {
		this.zatvoren = zatvoren;
	}
	public void dodajArtikal(String proizvod, int kolicina) {
		if(artikli.containsKey(proizvod))
			artikli.put(proizvod, artikli.get(proizvod)+kolicina);			
		else
			artikli.put(proizvod, kolicina);		
	}
	
	public Map<String, Integer> getArtikli() {
		return artikli;
	}
	
	
	
	
	
	
	
	

}
