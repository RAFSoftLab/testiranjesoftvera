package trgovina.model;



public class Kupovina {
	
	private String naziv;
	private int kolicina;
	private Racun racun;

	
	public Kupovina(String naziv, int kolicina, Racun racun) {
		super();
		this.naziv = naziv;
		this.kolicina = kolicina;
		this.racun = racun;
	}
	
	
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public int getKolicina() {
		return kolicina;
	}
	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}
	
	
	

}
