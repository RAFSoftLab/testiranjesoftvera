package trgovina.dtos;

import java.time.LocalDate;
import java.util.Map;

public class RacunDTO {
	
	private String imeIPrezimeKupca;
	private String racunId;
	private LocalDate datumKupovine;
	private String nazivProdavnice;
	private String ziroRacunProdavnice;
	// naziv i kolicina kupljenih artikala
	private Map<String, Integer> artikli;
	private double ukupnaCenaBezPdv;
	private double ukupnaCenaSaPdv;
	private double ukupnaCenaSaPopustom;
	
	
	public String getImeIPrezimeKupca() {
		return imeIPrezimeKupca;
	}
	public void setImeIPrezimeKupca(String imeIPrezimeKupca) {
		this.imeIPrezimeKupca = imeIPrezimeKupca;
	}
	public LocalDate getDatumKupovine() {
		return datumKupovine;
	}
	public void setDatumKupovine(LocalDate datumKupovine) {
		this.datumKupovine = datumKupovine;
	}
	public String getNazivProdavnice() {
		return nazivProdavnice;
	}
	public void setNazivProdavnice(String nazivProdavnice) {
		this.nazivProdavnice = nazivProdavnice;
	}
	public Map<String, Integer> getArtikli() {
		return artikli;
	}
	public void setArtikli(Map<String, Integer> artikli) {
		this.artikli = artikli;
	}
	public double getUkupnaCenaBezPdv() {
		return ukupnaCenaBezPdv;
	}
	public void setUkupnaCenaBezPdv(double ukupnaCenaBezPdv) {
		this.ukupnaCenaBezPdv = ukupnaCenaBezPdv;
	}
	public double getUkupnaCenaSaPdv() {
		return ukupnaCenaSaPdv;
	}
	public void setUkupnaCenaSaPdv(double ukupnaCenaSaPdv) {
		this.ukupnaCenaSaPdv = ukupnaCenaSaPdv;
	}
	public double getUkupnaCenaSaPopustom() {
		return ukupnaCenaSaPopustom;
	}
	public void setUkupnaCenaSaPopustom(double ukupnaCenaSaPopustom) {
		this.ukupnaCenaSaPopustom = ukupnaCenaSaPopustom;
	}
	@Override
	public String toString() {
		return "RacunDTO [imeIPrezimeKupca=" + imeIPrezimeKupca + ", datumKupovine=" + datumKupovine
				+ ", nazivProdavnice=" + nazivProdavnice + ", artikli=" + artikli + ", ukupnaCenaBezPdv="
				+ ukupnaCenaBezPdv + ", ukupnaCenaSaPdv=" + ukupnaCenaSaPdv + ", ukupnaCenaSaPopustom="
				+ ukupnaCenaSaPopustom + "]";
	}
	
	public String getPrintableRacun() {
		// TODO 
		
		return null;
	}
	public String getRacunId() {
		return racunId;
	}
	public void setRacunId(String racunId) {
		this.racunId = racunId;
	}
	public String getZiroRacunProdavnice() {
		return ziroRacunProdavnice;
	}
	public void setZiroRacunProdavnice(String ziroRacunProdavnice) {
		this.ziroRacunProdavnice = ziroRacunProdavnice;
	}
	
	
	
	
	
	
	
	
	

}
