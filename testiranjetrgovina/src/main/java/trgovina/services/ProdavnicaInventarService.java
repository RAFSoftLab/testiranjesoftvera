package trgovina.services;

import java.util.List;

public interface ProdavnicaInventarService {	

	
	public int vratiBrojRazlicitihProizvoda();
	
	public List<String> vratiSveNaziveProizvoda();
	
	public int vratiStanjeZaProizvod(String proizvod);
	
	public double vratiCenuZaProizvod(String proizvod);
	
	public void umanjiStanjeProizvoda(String proizvod, int umanjenje);

}
