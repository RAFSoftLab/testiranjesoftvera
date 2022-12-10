package trgovina.model;

public class TekuciRacun {
	
	private String brojRacuna;
	private double stanje;	
	
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
