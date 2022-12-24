package trgovina.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import trgovina.dtos.KupacDTO;
import trgovina.dtos.RacunDTO;
import trgovina.izuzeci.InventarException;
import trgovina.model.Racun;
import trgovina.services.EmailService;
import trgovina.services.PlacanjeService;
import trgovina.services.ProdavnicaInventarService;
import trgovina.services.ProdavnicaKupacService;
import trgovina.services.ProdavnicaLojalnostService;
import trgovina.utils.BrojRacunaGenerator;

@Service
public class Prodavnica{

	private static final double pdv = 0.2;		
	
	private String nazivProdavnice;	
	private String ziroRacun;	
	
	private ProdavnicaInventarService inventarService;
	private ProdavnicaKupacService kupacService;
	private ProdavnicaLojalnostService lojalnostService;
	
	private List<RacunDTO> izdatiRacuni;
	
	public Prodavnica() {
		this.nazivProdavnice = "Big Shop";
		this.ziroRacun = "123456789";
		izdatiRacuni = new ArrayList<>();
	}
	
	public Prodavnica(String naziv) {
		this.nazivProdavnice = naziv;
	}
	
	@Autowired
	public void setInventarService(ProdavnicaInventarService inventarService) {
		this.inventarService = inventarService;
	}	
	
	@Autowired
	public void setKupacService(ProdavnicaKupacService kupacService) {
		this.kupacService = kupacService;
	}	
		
	@Autowired
	public void setLojalnostService(ProdavnicaLojalnostService lojalnostService) {
		this.lojalnostService = lojalnostService;
	}

	public boolean mozeDaKupi(String nazivProizvoda, int kolicina) {		
		return inventarService.vratiStanjeZaProizvod(nazivProizvoda)>=kolicina;			
	}
			
	public int getKolicina(String naziv) {
		return inventarService.vratiStanjeZaProizvod(naziv);
	}	
	
	/**
	 * Na osnovu interne reprezentacije racuna izdaje se racun sa obracunatom cenom. 
	 * 
	 * Racun mora biti zatvoren što znaci da je zavrsena kupovina. 
	 * 
	 * Svaki racun se moze izdati samo jednom i kada se izda smesta se u listu izdatiRacuni 
	 * 
	 * Racun sadrzi podatke o prodavnici, naziv i ziro racun, spisak svih artikala, cenu bez i sa pdv-om,
	 * kao i cenu sa popustom.
	 * 
	 * Podaci o stanju proizvoda, podaci o kupcu i popustima preuzimaju se iz odvojenih servisa.  
	 * 
	 * Prilikom izdavanja racuna ponovo proveravamo da li ima dovoljno prozvoda, ako ne postoji dovoljna kolicina izdaje se koliko 
	 * ima stanju, ako proizvoda uopste nema na stanju, taj artikal se ne nalazi na racunu i ne naplacuje se.
	 * 
	 * Prilkom izdavanja racuna vrsi se umanjenje stanja artikala u inventaru. 
	 * 
	 * 
	 * @param r
	 * @return
	 */

	public RacunDTO izdajRacun(Racun r) throws InventarException {
		if(r.isZatvoren()==false)
			return null;		
		RacunDTO racun = izdatRacun(r.getRacunId());
		if(racun!=null)
			return racun;			
		racun = new RacunDTO();
		racun.setNazivProdavnice(nazivProdavnice);		
		racun.setZiroRacunProdavnice(ziroRacun);
		KupacDTO kupac = kupacService.kupacZaId(r.getKupacId());
		racun.setImeIPrezimeKupca(kupac.getIme() + " "+ kupac.getPrezime());		
		racun.setDatumKupovine(r.getDatum());
		racun.setRacunId(r.getRacunId());
		Map<String, Integer> artikli = new HashMap<>(); // pravimo praznu mapu artikala za racun, jer nece ici svi artikli, ako nekih nema na stanju
		racun.setArtikli(artikli);
		double ukupnaCena = 0;		
		for(String proizvod:r.getArtikli().keySet()) {
			int stanje = inventarService.vratiStanjeZaProizvod(proizvod);
			int brojArtikalaNaRacunu = 0;				
			if(stanje>0 && stanje<=r.getBrojProizvoda(proizvod)) {
				brojArtikalaNaRacunu = stanje;				
			}else {
				brojArtikalaNaRacunu = r.getBrojProizvoda(proizvod);				
			}
			if(brojArtikalaNaRacunu>0) {
				artikli.put(proizvod, brojArtikalaNaRacunu);
				double cena = inventarService.vratiCenuZaProizvod(proizvod);
				inventarService.umanjiStanjeProizvoda(proizvod, brojArtikalaNaRacunu);
				ukupnaCena += cena*brojArtikalaNaRacunu;
			}						
		}
		racun.setUkupnaCenaBezPdv(ukupnaCena);
		double cenaSaPdv = ukupnaCena*(1+pdv);
		racun.setUkupnaCenaSaPdv(cenaSaPdv);		
		int popust = lojalnostService.vratiPopustZaKupca(kupac.getEmail());				
		racun.setUkupnaCenaSaPopustom(cenaSaPdv*(100-popust)/100);		
		izdatiRacuni.add(racun);
		return racun;
	}
	
	/**
	 * 
	 * Kreira se racun za kupca sa cenom i salje mu se na email.
	 * 
	 * Za slanje emaila koristi se servis koji još uvek nije implementiran. 
	 * 
	 * @param r
	 */
	
	public void izdajRacunEmailom(Racun r) throws InventarException{
		RacunDTO racun = izdajRacun(r);
		KupacDTO kupac = kupacService.kupacZaId(r.getKupacId());
		prepareAndSendMessage(kupac.getEmail(), kupac.getIme()+" "+kupac.getPrezime(), racun);		
	}
	
	private RacunDTO izdatRacun(String racunId) {
		for(RacunDTO r:izdatiRacuni) {
			if(r.getRacunId().equals(racunId))
				return r;
		}
		return null;
	}

	
	public String noviBrojRacuna(String imeKupca, String prezimeKupca) {
		return BrojRacunaGenerator.getInstance().generisiBroj(imeKupca, prezimeKupca, nazivProdavnice);
	}

	
	public String getZiroRacun() {		
		return ziroRacun;
	}

	private EmailService emailService;	

	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;		
	}


	
	public boolean prepareAndSendMessage(String email, String imePrezime, RacunDTO racun) {		
		String subject = "Racun "+racun.getRacunId() + "za "+imePrezime;
		String message = racun.getPrintableRacun();
		return emailService.sendEmail(email, subject, message);
		
	}
	
	private PlacanjeService placanjeService;
	
	public void setPlacanjeService(PlacanjeService placanjeService) {
		this.placanjeService = placanjeService;
	}
	
	/**
	 * racun mora biti zatvoren i kupac mora imati dovoljno para na nekom od tekucih racuna
	 * ako nema dovoljno para na jednom racunu pravi se vise uplata sa razlicitih racuna 
	 * 
	 * TODO testirati
	 * 
	 */
	
	/*
	public void uplatiRacun(Kupac k, String racunId) {
		RacunDTO racun = izdajRacun(k, racunId);
		double iznosZaPlacanje = racun.getUkupnaCenaSaPopustom();
		if(k.getUkupnoStanje()<iznosZaPlacanje)
			throw new NedozvoljenaOperacijaNadRacunomException("Kupac nema dovoljno sredstava na racunu");
		double preostaloZaPlacanje = iznosZaPlacanje;
		List<TekuciRacun> trList = k.getTekuciRacuni();
		int i = 0;
		while(preostaloZaPlacanje>0 && i<trList.size()) {
			TekuciRacun tr = trList.get(i);
			if(preostaloZaPlacanje>=tr.getStanje()) {
				placanjeService.plati(k.getIme()+k.getPrezime(), tr.getBrojRacuna(), getZiroRacun(), racunId, tr.getStanje());
				preostaloZaPlacanje-=tr.getStanje();
				tr.isplati(tr.getStanje());				
			}else {
				placanjeService.plati(k.getIme()+k.getPrezime(), tr.getBrojRacuna(), getZiroRacun(), racunId, preostaloZaPlacanje);
				preostaloZaPlacanje=0;
				tr.isplati(tr.getStanje());		
			}
			i++;
			
		}
		
	}
	*/
	
	/*
	
	public boolean posaljiRacun(Kupac k, String racunId) {
		try {
			RacunDTO racun = izdajRacun(k, racunId);
			return prepareAndSendMessage(k,racun);
		}catch(NedozvoljenaOperacijaNadRacunomException e) {
			return false;	
		}	
	}
	*/
	
	
}
