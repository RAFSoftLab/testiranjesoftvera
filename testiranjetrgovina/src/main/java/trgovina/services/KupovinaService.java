package trgovina.services;


import trgovina.model.Kupac;


public interface KupovinaService {
	
	public boolean kupi(int idKupca, String proizvod, int kolicina);
	
	// vraca id racuna
	public String otvoriRacun(int idKupca);
		
	public String zatvoriRacun(String idRacuna);
	
	
	
	
	
	

}
