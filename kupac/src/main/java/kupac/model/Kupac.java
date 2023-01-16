package kupac.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Kupac {	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	
	private String ime;
	private String prezime;
	private String email;	
	@Column(nullable = true)
	private double rezervisanaSredstva;
	@OneToMany(mappedBy="kupac", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<TekuciRacun> tekuciRacuni = new ArrayList<>();
	@OneToMany
	private List<Kupovina> kupovine = new ArrayList<>();

	
	
	public Kupac() {			
	}	
	
	public Kupac(String ime, String prezime) {
		super();
		this.ime = ime;
		this.prezime = prezime;
	}	
	
	
	
	public Kupac(String ime, String prezime, String email) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setPrezime(String prezime) {
		this.prezime = prezime;
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

	public List<TekuciRacun> getTekuciRacuni() {
		return tekuciRacuni;
	}

	public void setTekuciRacuni(List<TekuciRacun> tekuciRacuni) {
		this.tekuciRacuni = tekuciRacuni;
	}
	
	// ukupno stanje na svim racunima
	
	public double getUkupnoStanje() {
		double rez = 0;
		for(TekuciRacun tr:tekuciRacuni) {
			rez+=tr.getStanje();
		}
		return rez;
	}

	public List<Kupovina> getKupovine() {
		return kupovine;
	}

	public void setKupovine(List<Kupovina> kupovine) {
		this.kupovine = kupovine;
	}

	public double getRezervisanaSredstva() {
		return rezervisanaSredstva;
	}

	public void setRezervisanaSredstva(double rezervisanaSredstva) {
		this.rezervisanaSredstva = rezervisanaSredstva;
	}
		
	
	
	
	
	
	
	

}
