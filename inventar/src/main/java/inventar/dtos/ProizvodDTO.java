package inventar.dtos;

public class ProizvodDTO {
	
	private String naziv;
	
	private String kategorija;  // prehrambeni, kozmetika
	
	private double cena;
	
	private int stanje;
	
	

	public ProizvodDTO(String naziv, String kategorija, double cena, int stanje) {	
		this.naziv = naziv;
		this.kategorija = kategorija;
		this.cena = cena;
		this.stanje = stanje;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getKategorija() {
		return kategorija;
	}

	public void setKategorija(String kategorija) {
		this.kategorija = kategorija;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public int getStanje() {
		return stanje;
	}

	public void setStanje(int stanje) {
		this.stanje = stanje;
	}
	
	
	
}
