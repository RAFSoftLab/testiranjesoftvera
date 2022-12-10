package trgovina.services;

public interface PlacanjeService {
	
	public boolean plati(String uplatilac, String brojRacunaUplatioca, String brojRacunaPrimaoca, String pozivNaBroj, double iznos);
	
	

}
