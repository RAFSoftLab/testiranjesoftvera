package trgovina.kupac.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trgovina.dtos.RacunDTO;
import trgovina.izuzeci.NedozvoljenaOperacijaNadRacunomException;
import trgovina.model.Kupac;
import trgovina.model.Racun;
import trgovina.model.TekuciRacun;
import trgovina.services.EmailService;
import trgovina.services.KupacService;
import trgovina.services.PlacanjeService;
import trgovina.services.RacunNotificationsService;
import trgovina.services.ProdavnicaService;

public class KupacServiceImpl implements KupacService, RacunNotificationsService{
	
	/**
	 * svi trenutno otvoreni racuni, na kojima jos nije zavrsena kupovina
	 */
	
	private Map<Kupac, Racun> aktivniRacuni;
	
	public KupacServiceImpl() {
		aktivniRacuni = new HashMap<>();
		
	}
	
	@Override
	public String otvoriRacun(Kupac k, ProdavnicaService prodavnica) {
		String racunId = prodavnica.noviBrojRacuna(k);
		Racun racun = new Racun(racunId, LocalDate.now());
		aktivniRacuni.put(k, racun);
		return racunId;
	}
	
	public Map<Kupac,Racun> getAktivniRacuni(){
		return aktivniRacuni;
	}
	
	
	// zatvara racun i vraca njegov id

	@Override
	public String zatvoriRacun(Kupac k, ProdavnicaService p) {
		Racun racunKupca =  aktivniRacuni.get(k);
		if(racunKupca==null) 
			throw new NedozvoljenaOperacijaNadRacunomException("Racun nije otvoren, ne moze se zatvoriti");
		k.addZatvereniRacun(racunKupca);
		aktivniRacuni.remove(k);
		return racunKupca.getRacunId();
	}
	
	@Override
	public boolean kupi(Kupac k, ProdavnicaService prodavnica, String proizvod, int kolicina) {
		if(aktivniRacuni.get(k)==null) {  // nije otvoren racun, prvo cemo ga otvoriti
			otvoriRacun(k, prodavnica);
		}
		if(prodavnica.kupi(proizvod, kolicina)) {
			Racun racun = aktivniRacuni.get(k);
			racun.dodajArtikal(proizvod, kolicina);			
			return true;
		}
		return false;
		
	}
	
	public boolean posaljiRacun(Kupac k, ProdavnicaService p, String racunId) {
		try {
			RacunDTO racun = p.izdajRacun(k, racunId);
			return prepareAndSendMessage(k,racun);
		}catch(NedozvoljenaOperacijaNadRacunomException e) {
			return false;	
		}		
		
		
	}
	
	
	private EmailService emailService;	

	@Override
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;		
	}


	@Override
	public boolean prepareAndSendMessage(Kupac k, RacunDTO racun) {		
		String subject = "Racun "+racun.getRacunId();
		String message = racun.getPrintableRacun();
		return emailService.sendEmail(k.getEmail(), subject, message);
		
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
	
	public void uplatiRacun(Kupac k, String racunId, ProdavnicaService p) {
		RacunDTO racun = p.izdajRacun(k, racunId);
		double iznosZaPlacanje = racun.getUkupnaCenaSaPopustom();
		if(k.getUkupnoStanje()<iznosZaPlacanje)
			throw new NedozvoljenaOperacijaNadRacunomException("Kupac nema dovoljno sredstava na racunu");
		double preostaloZaPlacanje = iznosZaPlacanje;
		List<TekuciRacun> trList = k.getTekuciRacuni();
		int i = 0;
		while(preostaloZaPlacanje>0 && i<trList.size()) {
			TekuciRacun tr = trList.get(i);
			if(preostaloZaPlacanje>=tr.getStanje()) {
				placanjeService.plati(k.getIme()+k.getPrezime(), tr.getBrojRacuna(), p.getZiroRacun(), racunId, tr.getStanje());
				preostaloZaPlacanje-=tr.getStanje();
				tr.isplati(tr.getStanje());				
			}else {
				placanjeService.plati(k.getIme()+k.getPrezime(), tr.getBrojRacuna(),  p.getZiroRacun(), racunId, preostaloZaPlacanje);
				preostaloZaPlacanje=0;
				tr.isplati(tr.getStanje());		
			}
			
		}
		
	}

	
}
