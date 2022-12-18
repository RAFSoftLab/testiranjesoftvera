package trgovina.services;


import trgovina.model.Kupac;


public interface KupacService {
	
	public boolean kupi(Kupac k, String proizvod, int kolicina);
	
	// vraca id racuna
	public String otvoriRacun(Kupac k);
		
	public String zatvoriRacun(Kupac k);
	
	
	
	
	
	

}
