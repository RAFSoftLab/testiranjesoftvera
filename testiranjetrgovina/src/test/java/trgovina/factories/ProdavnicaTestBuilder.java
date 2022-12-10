package trgovina.factories;

import java.util.List;

import trgovina.model.TipKupca;
import trgovina.prodavnicaimpl.Prodavnica;

public class ProdavnicaTestBuilder {
	
	// nije pravi Builder patern
	
	private Prodavnica prodavnica;
	
	public ProdavnicaTestBuilder() {
		prodavnica = new Prodavnica();
	}
	
	
	
	
	public ProdavnicaTestBuilder inventar(String proizvod, int kolicina) {
		this.prodavnica.dodajProizvod(proizvod, kolicina);
		return this;
	}
	
	public ProdavnicaTestBuilder inventar(List<String> proizvodi, List<Integer> kolicine) {
		for(String p:proizvodi) {		
			this.prodavnica.dodajProizvod(p, kolicine.get(proizvodi.indexOf(p)));
		}
		return this;
	}
	
	
	public ProdavnicaTestBuilder cena(String proizvod, double cena) {
		this.prodavnica.postaviCenu(proizvod, cena);
		return this;
	}
	
	public ProdavnicaTestBuilder cene(List<String> proizvodi, List<Double> cene) {
		for(String p:proizvodi) {		
			this.prodavnica.postaviCenu(p,cene.get(proizvodi.indexOf(p)) );
		}		
		return this;
	}
	
	
	public ProdavnicaTestBuilder popust(TipKupca tipKupca, int popust) {
		this.prodavnica.dodajPopust(tipKupca, popust);
		return this;
	}
	
	
	
	public Prodavnica getProdavnica() {
		return prodavnica;
	}
	

}
