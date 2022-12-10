package trgovina.kupac.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import trgovina.dtos.RacunDTO;
import trgovina.izuzeci.NedozvoljenaOperacijaNadRacunomException;
import trgovina.model.Kupac;
import trgovina.model.Racun;
import trgovina.services.EmailService;
import trgovina.services.KupacService;
import trgovina.services.RacunNotificationsService;
import trgovina.services.ProdavnicaService;

public class KupacServiceImpl implements KupacService, RacunNotificationsService{
	
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
	
	
	// zatvra racun i vraca njegov id

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

	
}
