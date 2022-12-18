package trgovina.factories;

import java.util.List;
import trgovina.model.TipKupca;
import trgovina.serviceimpl.Prodavnica;
import trgovina.services.ProdavnicaService;

public class ProdavnicaObjectMother {
	
	/*
	 * 
	 */
	
	public static ProdavnicaService createProdavnicaSa20Sampona() {
		ProdavnicaService prodavnica = new Prodavnica();
		prodavnica.dodajProizvod("sampon", 20);
		return prodavnica;
	}
	
	// malo uopstenija varijanta
	
	public static ProdavnicaService createProdavnicaSaNSampona(int n) {
		ProdavnicaService prodavnica = new Prodavnica();
		prodavnica.dodajProizvod("sampon", n);
		return prodavnica;
	}
	
	
	public static ProdavnicaService createProdavnicaSaNSamponaICenom(int n, double cena) {
		ProdavnicaService prodavnica = new Prodavnica();
		prodavnica.dodajProizvod("sampon", n);
		prodavnica.postaviCenu("sampon", cena);
		return prodavnica;
	}
	
	public static ProdavnicaService createProdavnicaSaNazivomSaNSamponaICenom(int n, double cena) {
		ProdavnicaService prodavnica = new Prodavnica("Moja prodavnica");		
		prodavnica.dodajProizvod("sampon", n);
		prodavnica.postaviCenu("sampon", cena);
		return prodavnica;
	}
	
	
	// uopstenija varijanta
	
	public static ProdavnicaService createProdavnicaSaNazivomSaNProizvodaICenom(String proizvod, int n, double cena) {
		ProdavnicaService prodavnica = new Prodavnica("Moja prodavnica");		
		prodavnica.dodajProizvod(proizvod, n);
		prodavnica.postaviCenu(proizvod, cena);
		return prodavnica;
	}
	
	// kombinacija sa builderom
	
	public static ProdavnicaService createProdavnicaSaNazivomSaNProizvodaICenomB(String proizvod, int n, double cena) {
		return new ProdavnicaTestBuilder()			
				.inventar(proizvod, n)
				.cena(proizvod, cena)
				.popust(TipKupca.REGULARAN, 20)
				.popust(TipKupca.POVREMEN, 5)
				.getProdavnica();
	}
	
	public static ProdavnicaService createProdavnicaSaNazivomSaNProizvodaICenomB(List<String> proizvodi, List<Integer> kolicine, List<Double> cene) {
		ProdavnicaTestBuilder builder = new ProdavnicaTestBuilder();
		builder.inventar(proizvodi, kolicine).cene(proizvodi, cene);		
	    builder.popust(TipKupca.REGULARAN, 20)
			   .popust(TipKupca.POVREMEN, 5);		
		return builder.getProdavnica();
	}
	
	
	

}
