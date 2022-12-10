package trgovina.services;


import trgovina.model.Kupac;


public interface KupacService {
	
	public boolean kupi(Kupac k, ProdavnicaService prodavnica, String proizvod, int kolicina);
	
	// vraca id racuna
	public String otvoriRacun(Kupac k, ProdavnicaService p);
		
	public String zatvoriRacun(Kupac k, ProdavnicaService p);
	
	
	
	
	
	

}
