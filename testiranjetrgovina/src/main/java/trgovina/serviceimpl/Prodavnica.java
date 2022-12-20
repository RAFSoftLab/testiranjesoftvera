package trgovina.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trgovina.dtos.RacunDTO;
import trgovina.izuzeci.NedozvoljenaOperacijaNadRacunomException;
import trgovina.model.Kupac;
import trgovina.model.Racun;
import trgovina.model.TipKupca;
import trgovina.services.InventarService;
import trgovina.utils.BrojRacunaGenerator;

@Service
public class Prodavnica{

	private static final double pdv = 0.2;
	
	//private Map<String,Integer> inventar = new HashMap<String, Integer>();
	//private Map<String,Double> cene = new HashMap<String, Double>();
	private Map<TipKupca,Integer> popusti = new HashMap<TipKupca,Integer>();	
	
	private String nazivProdavnice;	
	private String ziroRacun;	
	
	private InventarService inventarService;
	
	public Prodavnica() {
		this.nazivProdavnice = "Big Shop";
		this.ziroRacun = "123456789";
	}
	
	public Prodavnica(String naziv) {
		this.nazivProdavnice = naziv;
	}
	
	@Autowired
	public void setInventarService(InventarService inventarService) {
		this.inventarService = inventarService;
	}	
	
		
	public boolean kupi(String nazivProizvoda, int kolicina) {
		List<String> sviNazivi = inventarService.vratiSveNaziveProizvoda();
		if(!sviNazivi.contains(nazivProizvoda)) {
			return false;
		}else if(inventarService.vratiStanjeZaProizvod(nazivProizvoda)>=kolicina) {
			inventarService.umanjiStanjeProizvoda(nazivProizvoda, kolicina);
			return true;			
		}else {
			return false;
		}
	}
	
		
	public int getKolicina(String naziv) {
		return inventarService.vratiStanjeZaProizvod(naziv);
	}
	
	
	
	public void dodajPopust(TipKupca tipKupca, int popust) {
		popusti.put(tipKupca, popust);
	}	
	

	public RacunDTO izdajRacun(Kupac kupac, String racunId) {
		RacunDTO racun = new RacunDTO();
		racun.setImeIPrezimeKupca(kupac.getIme() + " "+ kupac.getPrezime());
		Racun r = kupac.getRacunZaId(racunId);
		if(r == null)
			throw new NedozvoljenaOperacijaNadRacunomException("Ne postoji racun za id = "+racunId);
		racun.setDatumKupovine(r.getDatum());
		racun.setRacunId(racunId);
		Map<String, Integer> artikli = r.getArtikli();		
		double ukupnaCena = 0;		
		for(String proizvod:artikli.keySet()) {
			double cena = inventarService.vratiCenuZaProizvod(proizvod);
			ukupnaCena += cena*artikli.get(proizvod);			
		}
		racun.setUkupnaCenaBezPdv(ukupnaCena);
		double cenaSaPdv = ukupnaCena*(1+pdv);
		racun.setUkupnaCenaSaPdv(cenaSaPdv);
		if(kupac.getTip()!=null && this.popusti.containsKey(kupac.getTip())) {
			int popust = popusti.get(kupac.getTip());
			racun.setUkupnaCenaSaPopustom(cenaSaPdv*(100-popust)/100);
		}else {
			racun.setUkupnaCenaSaPopustom(cenaSaPdv);
		}
		return racun;
	}

	
	public String noviBrojRacuna(Kupac k) {
		return BrojRacunaGenerator.getInstance().generisiBroj(k, nazivProdavnice);
	}

	
	public String getZiroRacun() {		
		return ziroRacun;
	}


	
	
}
