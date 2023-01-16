package trgovina.services;

import java.util.List;

import trgovina.izuzeci.InventarException;

public interface ProdavnicaInventarService {	

	
	public int vratiBrojRazlicitihProizvoda();
	
	public List<String> vratiSveNaziveProizvoda();
	
	public int vratiStanjeZaProizvod(String proizvod);
	
	public double vratiCenuZaProizvod(String proizvod) throws InventarException;
	
	public void umanjiStanjeProizvoda(String proizvod, int umanjenje);
	
	public void uvecajStanjeProizvoda(String proizvod, int uvecanje);

}
