package trgovina.services;

import trgovina.dtos.RacunDTO;
import trgovina.model.Kupac;


public interface ProdavnicaService {
	
	public void dodajProizvod(String nazivProizvoda, int kolicina);		
	
	public boolean kupi(String nazivProizvoda, int kolicina);
	
	public int getKolicina(String naziv);
	
	public void postaviCenu(String proizvod, double cena);
	
	public String noviBrojRacuna(Kupac kupac);
	
	public RacunDTO izdajRacun(Kupac kupac, String racunId);
	
	
	
	 
}


