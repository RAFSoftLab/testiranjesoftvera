package trgovina.repositories;

import java.util.List;

public interface ProizvodRepository {
	
	public int vratiBrojRazlicitihProizvoda();
	
	public List<String> vratiSveRazliciteProizvode();
	
	public int vratiStanjeZaProizvod(String proizvod);
	
	public boolean sacuvajProizvod(String naziv, int kolicina, float cena);

}
