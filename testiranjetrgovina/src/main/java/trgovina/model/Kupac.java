package trgovina.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Kupac {	
	
	private TipKupca tip;
	private String ime;
	private String prezime;
	private String email;	
	private List<Racun> zatvoreniRacuni = new ArrayList<>();
	
	
	public Kupac() {	
		
	}	
	
	public Kupac(String ime, String prezime) {
		super();
		this.ime = ime;
		this.prezime = prezime;
	}
	
	public TipKupca getTip() {
		return tip;
	}

	public void setTip(TipKupca tip) {
		this.tip = tip;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}	

	public boolean addZatvereniRacun(Racun e) {
		if(zatvoreniRacuni==null) {
			zatvoreniRacuni = new ArrayList<>();
		}
		return zatvoreniRacuni.add(e);
	}


	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	
	public Map<String, Integer> getArtiklliZaRacun(String racunId){
		for(Racun r:zatvoreniRacuni) {
			if(r.getRacunId().equals(racunId))
				return r.getArtikli();			
		}
		return null;
	}
	
	public Racun getRacunZaId(String racunId){
		for(Racun r:zatvoreniRacuni) {
			if(r.getRacunId().equals(racunId))
				return r;			
		}
		return null;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public int hashCode() {
		return Objects.hash(email);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kupac other = (Kupac) obj;
		return Objects.equals(email, other.email);
	}

	public List<Racun> getZatvoreniRacuni() {
		return zatvoreniRacuni;
	}
	
	
	
	
	

}
