package trgovina.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import trgovina.dtos.KupacDTO;
import trgovina.dtos.RacunDTO;
import trgovina.izuzeci.InventarExeception;
import trgovina.izuzeci.NedozvoljenaOperacijaNadRacunomException;
import trgovina.model.Kupac;
import trgovina.model.Racun;
import trgovina.model.TekuciRacun;
import trgovina.services.EmailService;
import trgovina.services.KupacService;
import trgovina.services.PlacanjeService;
import trgovina.services.RacunNotificationsService;

@Service
public class KupovinaService implements  RacunNotificationsService{
	
	/**
	 * svi trenutno otvoreni racuni, na kojima jos nije zavrsena kupovina
	 * id racuna je kljuc
	 * 
	 */
	
	private Map<String, Racun> aktivniRacuni;
	private List<Racun> zatvoreniRacuni;
	
	private Prodavnica prodavnica;
	
	@Autowired
	private KupacService kupacService;
	
	public KupovinaService(Prodavnica prodavnica) {
		this.prodavnica = prodavnica;		
		aktivniRacuni = new HashMap<>();
		zatvoreniRacuni = new ArrayList<>();
	}
	
	
	@Autowired
	public void setKupacService(KupacService kupacService) {
		this.kupacService = kupacService;
	}

	/**
	 * jedan kupac ne moze imati vise aktivnih racuna
	 */

	
	public String otvoriRacun(int idKupca) {
		if(getAktivanRacunZaKupca(idKupca)!=null) 
			return getAktivanRacunZaKupca(idKupca);  // ako vec postoji aktivan racun za tog kupca, vracamo njegov broj
		KupacDTO k = kupacService.kupacZaId(idKupca);
		if(k==null) return null;
		String racunId = prodavnica.noviBrojRacuna(k.getIme(),k.getPrezime());
		Racun racun = new Racun(racunId, LocalDate.now());
		racun.setKupacId(idKupca);		
		aktivniRacuni.put(racunId, racun);
		return racunId;
	}
	
	public Collection<Racun> getAktivniRacuni(){
		return aktivniRacuni.values();
	}
	
	
	// zatvara racun i vraca njegov id
	
	
	public String zatvoriRacun(String idRacuna) {
		Racun racunKupca =  aktivniRacuni.get(idRacuna);
		if(racunKupca==null) 
			throw new NedozvoljenaOperacijaNadRacunomException("Racun nije otvoren, ne moze se zatvoriti");		
		// TODO za kupca sacuvati racun
		racunKupca.zatvori();
		zatvoreniRacuni.add(racunKupca);
		aktivniRacuni.remove(idRacuna);
		return racunKupca.getRacunId();
		
	}
	
	/**
	 * Vraca idRacuna aktivnog za kupca sa prosledjenim id-jem ili null ako 
	 * ne postoji aktivan racun za kupca 
	 * @param idKupca
	 * @return
	 */
	
	public String getAktivanRacunZaKupca(int idKupca) {
		for(String racunId:aktivniRacuni.keySet()) {
			if(aktivniRacuni.get(racunId).getKupacId() == idKupca)
				return racunId;			
		}		
		return null;		
	}
	
	/**
	 * prvo gleda u aktivnim racunima, onda u zatvorenim
	 */
	
	
	public Racun vratiRacunZaId(String idRacuna) {
		if(aktivniRacuni.containsKey(idRacuna)) {
			return aktivniRacuni.get(idRacuna);			
		}else {
			return getZatvorenRacunPoId(idRacuna);
		}
		
	}
	
	
	/**
	 * samo dodaje proizvod sa zadatom kolicinom na aktivan racun kupca
	 * ovde se jos ne smanjuje kolicina u inventaru
	 */
	
	
	public boolean kupi(int idKupca, String proizvod, int kolicina) {
		String racunId = getAktivanRacunZaKupca(idKupca);
		if(racunId == null) {  // nije otvoren racun, prvo cemo ga otvoriti
			racunId = otvoriRacun(idKupca);
		}
		return dodajNaAktivanRacun(racunId, proizvod, kolicina);
		
	}
	
	public boolean dodajNaAktivanRacun(String racunId, String proizvod, int kolicina) {
		if(isAktivanRacun(racunId)) {
			if(prodavnica.mozeDaKupi(proizvod, kolicina)) {
				Racun racun = aktivniRacuni.get(racunId);
				racun.dodajArtikal(proizvod, kolicina);			
				return true;
			}else {
				throw new InventarExeception("Nema dovoljno proizvoda na stanju");
			}		
		}
		return false;			
	}
	
	private boolean isAktivanRacun(String idRacuna) {
		return aktivniRacuni.containsKey(idRacuna);
	}
	
	
	public List<Racun> getZatvoreniRacuni() {
		return zatvoreniRacuni;
	}
	
	private Racun getZatvorenRacunPoId(String racunId) {
		for(Racun r:zatvoreniRacuni) {
			if(r.getRacunId().equals(racunId))
				return r;
		}
		return null;
	}

	public void setZatvoreniRacuni(List<Racun> zatvoreniRacuni) {
		this.zatvoreniRacuni = zatvoreniRacuni;
	}

	public boolean posaljiRacun(Kupac k, String racunId) {
		try {
			RacunDTO racun = prodavnica.izdajRacun(k, racunId);
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
	
	public void uplatiRacun(Kupac k, String racunId) {
		RacunDTO racun = prodavnica.izdajRacun(k, racunId);
		double iznosZaPlacanje = racun.getUkupnaCenaSaPopustom();
		if(k.getUkupnoStanje()<iznosZaPlacanje)
			throw new NedozvoljenaOperacijaNadRacunomException("Kupac nema dovoljno sredstava na racunu");
		double preostaloZaPlacanje = iznosZaPlacanje;
		List<TekuciRacun> trList = k.getTekuciRacuni();
		int i = 0;
		while(preostaloZaPlacanje>0 && i<trList.size()) {
			TekuciRacun tr = trList.get(i);
			if(preostaloZaPlacanje>=tr.getStanje()) {
				placanjeService.plati(k.getIme()+k.getPrezime(), tr.getBrojRacuna(), prodavnica.getZiroRacun(), racunId, tr.getStanje());
				preostaloZaPlacanje-=tr.getStanje();
				tr.isplati(tr.getStanje());				
			}else {
				placanjeService.plati(k.getIme()+k.getPrezime(), tr.getBrojRacuna(),  prodavnica.getZiroRacun(), racunId, preostaloZaPlacanje);
				preostaloZaPlacanje=0;
				tr.isplati(tr.getStanje());		
			}
			
		}
		
	}


	


	
	
	
}
