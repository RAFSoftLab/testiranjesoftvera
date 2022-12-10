package trgovina.prodavnicaimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trgovina.dtos.RacunDTO;
import trgovina.izuzeci.NedozvoljenaOperacijaNadRacunomException;
import trgovina.model.Kupac;
import trgovina.model.Racun;
import trgovina.model.TipKupca;
import trgovina.repositories.ProizvodRepository;
import trgovina.services.ProdavnicaService;
import trgovina.utils.BrojRacunaGenerator;


public class Prodavnica implements ProdavnicaService{

	private static final double pdv = 0.2;
	
	private Map<String,Integer> inventar = new HashMap<String, Integer>();
	private Map<String,Double> cene = new HashMap<String, Double>();
	private Map<TipKupca,Integer> popusti = new HashMap<TipKupca,Integer>();	
	
	private String nazivProdavnice;	
	private String ziroRacun;	
	
	private ProizvodRepository proizvodRepo;
	
	public Prodavnica() {
		this.nazivProdavnice = "Big Shop";
		this.ziroRacun = "123456789";
	}
	
	public Prodavnica(String naziv) {
		this.nazivProdavnice = naziv;
	}
	
	
	public void setProizvodRepo(ProizvodRepository proizvodRepo) {
		this.proizvodRepo = proizvodRepo;		
	}
	
	/**
	 * Vraca se ukupan broj svih ucitanih proizvoda
	 */
	public void ucitajProizvode() {
		List<String> proizvodi = proizvodRepo.vratiSveRazliciteProizvode();		
		for(String str:proizvodi) {
			int stanje = proizvodRepo.vratiStanjeZaProizvod(str);
			inventar.put(str,stanje);			
		}				
	}
	
	public int ukupnoProizvoda(){
		int retVal = 0;
		for(String p:inventar.keySet()) {
			retVal+=inventar.get(p);
		}
		return retVal;
	}
	
	
	public void dodajProizvod(String nazivProizvoda, int kolicina) {
		if(inventar.containsKey(nazivProizvoda))
			inventar.put(nazivProizvoda, inventar.get(nazivProizvoda)+kolicina);
		else
			inventar.put(nazivProizvoda, kolicina);
	}
	
	public boolean kupi(String nazivProizvoda, int kolicina) {
		if(!inventar.containsKey(nazivProizvoda)) {
			return false;
		}else if(inventar.get(nazivProizvoda)>=kolicina) {
			inventar.put(nazivProizvoda, inventar.get(nazivProizvoda)-kolicina);
			return true;			
		}else {
			return false;
		}
	}
	
		
	public int getKolicina(String naziv) {
		if(inventar.containsKey(naziv))
			return inventar.get(naziv);
		return 0;
	}
	
	public void postaviCenu(String proizvod, double cena) {
		cene.put(proizvod, cena);		
	}
	
	
	public void dodajPopust(TipKupca tipKupca, int popust) {
		popusti.put(tipKupca, popust);
	}	
	
	@Override
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
			if(cene.containsKey(proizvod)) {
				ukupnaCena += cene.get(proizvod)*artikli.get(proizvod);
			}else {
				throw new IllegalArgumentException("Proizvod "+proizvod+" nema cenu");
			}
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

	@Override
	public String noviBrojRacuna(Kupac k) {
		return BrojRacunaGenerator.getInstance().generisiBroj(k, nazivProdavnice);
	}

	@Override
	public String getZiroRacun() {		
		return ziroRacun;
	}

	
	
}
