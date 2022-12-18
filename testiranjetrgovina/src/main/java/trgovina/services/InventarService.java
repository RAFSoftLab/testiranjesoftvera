package trgovina.services;

import java.util.List;

public interface InventarService {	

	
	public int vratiBrojRazlicitihProizvoda();
	
	public List<String> vratiSveNaziveProizvoda();
	
	public int vratiStanjeZaProizvod(String proizvod);
	
	public double vratiCenuZaProizvod(String proizvod);

}
