package trgovina.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import trgovina.dtos.KupacDTO;
import trgovina.dtos.KupovinaDTO;
import trgovina.dtos.RacunDTO;
import trgovina.dtos.UplataDTO;
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
	private String ziroRacunProdavnice;	
	
	private ProdavnicaInventarService inventarService;
	private ProdavnicaKupacService kupacService;
	private ProdavnicaLojalnostService lojalnostService;
	
	private List<RacunDTO> izdatiRacuni;
	private List<RacunDTO> racuniVraceno;  // racuni na kojima je nesto vraceno
	
	public Prodavnica() {
		this.nazivProdavnice = "Big Shop";
		this.ziroRacunProdavnice = "123456789";
		izdatiRacuni = new ArrayList<>();
		racuniVraceno = new ArrayList<>();
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
	 * Racun mora biti zatvoren, što znaci da je zavrsena kupovina. 
	 * 
	 * Svaki racun se moze izdati samo jednom i kada se izda smesta se u listu izdatiRacuni 
	 * 
	 * Racun sadrzi podatke o prodavnici, naziv i ziro racun, spisak svih artikala, cenu bez i sa pdv-om,
	 * kao i cenu sa popustom.
	 * 
	 * Podaci o stanju proizvoda, podaci o kupcu i popustima preuzimaju se iz odvojenih servisa.  
	 * 
	 * Prilikom izdavanja racuna ponovo proveravamo da li ima dovoljno prozvoda, ako ne postoji dovoljna kolicina izdaje se koliko 
	 * ima na stanju, ako proizvoda uopste nema na stanju, taj artikal se ne nalazi na racunu i ne naplacuje se.
	 * 
	 * Prilikom izdavanja racuna vrsi se umanjenje stanja artikala u inventaru. 
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
		racun.setIdKupca(r.getKupacId());
		racun.setNazivProdavnice(nazivProdavnice);		
		racun.setZiroRacunProdavnice(ziroRacunProdavnice);
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
		prepareAndSendRacun(kupac.getEmail(), kupac.getIme()+" "+kupac.getPrezime(), racun);		
	}
	
	
	
	private RacunDTO izdatRacun(String racunId) {
		for(RacunDTO r:izdatiRacuni) {
			if(r.getRacunId().equals(racunId))
				return r;
		}
		return null;
	}
	
	/**
	 * Operacija realizuje vracanje prethodno kupljenog proizvoda. Prosledjuje se id izdatog racuna, naziv i kolicina proizvoda koji se 
	 * vracaju. Operacija vraca logicku vrednost koja oznacava da li je vracaje uspesno izvrseno. 
	 * 
	 * Ako ne postoji izdat racun sa prosledjenim id-jem, ili postoji ali ne sadrzi dovoljno prozvoda operacija vraca false.
	 * 
	 * Ako racun postoji razlikujemo dva slucaja, vraceni proizvod je jedini na racunu ili na racunu ima i drugih proizvoda i u ova dva slucaja se razlicito
	 * azuriraju liste izdatiRacuni i racuniVraceno (polja ove klase). 
	 * 
	 * Ako je vraceni proizvod jedini na racunu, taj racun se brise iz liste izdatiRacuni i ubacuje se u listu racuniVraceno.
	 * 
	 * Ako na racunu ima drugih artikala, onda se stari originalni racun ubacuje u listu racuniVraceno, a sa izdatog racuna se izbacuju vraceni artikli (ukupna cena se ne obracunava ponovo). 
	 * 
	 * Zatim se azurira stanje inventara, odnosno uvecava se stanje za vraceni proizvod preko servisa inventar. 
	 * 
	 * Na kraju se vrsi povracaj novca i slanje obavestenja kupcu putem emaila. Prvo se preuzima kupovina za dati racun iz servisa kupac iz koje se cita da li je racun placen.
	 * 
	 * Ako je racun placen, obracunava se iznos koji se vraca, na osnovu cene proizvoda uz umanjenje ako je kupac imao popust (pdv se ne uzima u obzir). 
	 * Podaci o popustu se preuzimaju iz servisa lojalnost. 
	 * 
	 * Zatim se preuzima tekuci racun kupca, preko servisa kupac. Preko servisa za placanje (koji jos uvek nije implementiran) vrsi se placanje sa racuna prodavnice
	 * na tekuci racun kupca sa id-jem racuna kao pozivom na broj. 
	 * 
	 * Na kraju se kupcu salje email preko servisa emailService (jos uvek nije implementiran). Naslov email poruke treba da sadrzi "Povracaj proizvoda za racun "+racunId.
	 *  
	 * U slucaju da novac nije vracen (ako nije placen ili je doslo do greske u servisu za placanje), tekst poruke treba da sadrzi string "novac nije vracen", 
	 * ako novac jeste vracen, tekst poruke treba da sadrzi string "vracen novac". 
	 * 
	 *  
	 * 
	 *
	 */
	
	public boolean vracanjeProizvoda(String racunId, String nazivProizvoda, int kolicina) {		
		RacunDTO racunZaProizvod = izdatRacun(racunId);
		if(racunZaProizvod==null || !racunZaProizvod.sadrziProizvodUKolicini(nazivProizvoda, kolicina))
			return false;  
		// ako je vraceni proizvod jedini na racunu u datoj kolicini
		if(racunZaProizvod.getArtikli().size()==1 && racunZaProizvod.getArtikli().get(nazivProizvoda)!=null && racunZaProizvod.getArtikli().get(nazivProizvoda)==kolicina) {
			izdatiRacuni.remove(racunZaProizvod);
			racuniVraceno.add(racunZaProizvod);
		}else {
			racuniVraceno.add(racunZaProizvod);
			racunZaProizvod.smanjiKolicinu(nazivProizvoda, kolicina);
		}
		inventarService.uvecajStanjeProizvoda(nazivProizvoda, kolicina);		
		
		// povracaj novca i slanje obavestenja emailom
		
		KupovinaDTO kupovinaVracanje = kupacService.vratiKupovinuZaRacunId(racunId);		
		KupacDTO kupac = kupacService.kupacZaId(racunZaProizvod.getIdKupca());		
		String naslov = "Povracaj proizvoda za racun "+racunId;
		String poruka = "";
		boolean vracenNovac = false;
		
		if(kupovinaVracanje!=null && kupovinaVracanje.isPlacen()) {		 // povracaj novca, ako je prethodna kupovina placena
			// obracun koliko treba novca da se vrati
			double cena = inventarService.vratiCenuZaProizvod(nazivProizvoda);
			double iznosZaVracanje = cena*kolicina;
			int popust = lojalnostService.vratiPopustZaKupca(kupac.getEmail());
			iznosZaVracanje = iznosZaVracanje*(100-popust)/100;			
			String tekuciRacun = kupacService.vratiTekuciRacunZaIdKupca(racunZaProizvod.getIdKupca());			
			vracenNovac = placanjeService.plati(nazivProdavnice,ziroRacunProdavnice,tekuciRacun, racunZaProizvod.getRacunId(),iznosZaVracanje);
		}
			
		if(vracenNovac)							
			poruka = "Uspesno vracanje proizvoda, vracen novac";
		else			
			poruka = "Uspesno vracanje proizvoda, novac nije vracen";				
		emailService.sendEmail(kupac.getEmail(), naslov, poruka);			
		return true;				
	}
	
	
	
	
	
	
	
	
	/**
	 * Operacija odradjuje placaje racuna, celog ili samo jedne rate i vraca da li je uspesno placeno. 
	 *  
	 * Prosledjeni racun mora da postoji u listi izdatih racuna.
	 * 
	 * Prvo se obracunava iznos za uplatu koji moze biti ceo iznos (ukupna cena sa racuna sa popustom), ili samo jedna rata, ako je prosledjeni broj rata veci od 1. 
	 * 
	 * Zatim se iz servisa kupac preuzima pozeljna struktura uplate koja sadrzi listu objekata UplataDTO koja sadrzi broj tekuceg racuna i iznos koji treba skinuti
	 * kupcu sa tog tekuceg racuna (uplata se vrsi sa vise racuna).
	 * 
	 * Ukoliko je struktura uplate navalidna (null), placanje je neuspesno.  
	 * 
	 * Ako struktura uplate nije null, realizuju se uplate redom sa takucih racuna kupca na racun prodavnice. Uplate se vrse pleko servisa placanje koji jos uvek nije implementiran.    
	 * U momentu placanja moze da se desi da neka uplata ne prodje (metoda za placanje vraca false). Sabiraju se sve uspesne uplate.
	 * 
	 * Ukoliko je ukupan iznos uspesnih uplata manji od iznosa za placanje, razlika se belezi kupcu u rezervisana sredstva, preko servisa kupac. Osim ako je ukupan uplacen 
	 * iznos jednak 0, u tom slucaju placanje se smatra neuspesnim. 
	 * 
	 * Takodje, ukupan iznos preostalih rata za uplatu se kupcu belezi kao rezervisan.  
	 * 
	 * Ukoliko je barem nesto placeno, uplata se smatra uspesnom i u tom slucaju se kreira nova kupovina za kupca u servisu kupac u kojoj se cuvaju datum kupovine sa racuna,
	 * belezi se uspesna uplata i ukupan uplacen iznos.    
	 * 
	 * U slucaju da je uplata neuspesna, preko servisa ineventar se svi artikli sa racuna vracaju na stanje, a racun se brise iz liste izdatih racuna.
	 *
	 
	 */
	public boolean platiRacun(RacunDTO racun, int brojRata) {	
		if(!izdatiRacuni.contains(racun))
			return false;
		double iznosZaUplatu = racun.getUkupnaCenaSaPopustom();
		if(brojRata>1) {
			iznosZaUplatu = iznosZaUplatu/brojRata;
		}		
		List<UplataDTO> strukturaUplate = kupacService.vratiStrukturuUplate(racun.getIdKupca(), iznosZaUplatu);
		boolean uspesno = true;
		double ukupnoPlaceno = 0;
		if(strukturaUplate==null) {
			uspesno = false;				
		}else {				
			KupacDTO kupac = kupacService.kupacZaId(racun.getIdKupca());			
			for(UplataDTO uplata:strukturaUplate) {
				boolean uspesnaJedna = placanjeService.plati(kupac.getIme()+kupac.getPrezime(), uplata.getTekuciRacun(), ziroRacunProdavnice, racun.getRacunId(), uplata.getUplatiti());
				if(uspesnaJedna) {
					ukupnoPlaceno+=uplata.getUplatiti();
					kupacService.smanjiStanjeNaRacunu(uplata.getTekuciRacun(), uplata.getUplatiti());
				}
			}
			if(ukupnoPlaceno<iznosZaUplatu) {
				if(ukupnoPlaceno==0) {					
					uspesno = false;					
				}else {			
				    kupacService.dodajRezervisanoZaKupca(racun.getIdKupca(), iznosZaUplatu-ukupnoPlaceno);
				    uspesno = true;
				}
			}						
		}
		if(brojRata>1) {
			double iznosZaRezervisanje = racun.getUkupnaCenaSaPopustom()-iznosZaUplatu;
			kupacService.dodajRezervisanoZaKupca(racun.getIdKupca(), iznosZaRezervisanje);
		}
		if(uspesno) {
			KupovinaDTO kupovina = new KupovinaDTO(racun.getDatumKupovine(), racun.getRacunId(), uspesno, ukupnoPlaceno, Long.valueOf(racun.getIdKupca()));
			kupacService.sacuvajKupovinu(kupovina);
		}else {
			for(String naziv:racun.getArtikli().keySet()) {
				inventarService.uvecajStanjeProizvoda(naziv, racun.getArtikli().get(naziv));
			}
			izdatiRacuni.remove(racun);
		}
		return uspesno;
	}
	
	
	
	
	public String noviBrojRacuna(String imeKupca, String prezimeKupca) {
		return BrojRacunaGenerator.getInstance().generisiBroj(imeKupca, prezimeKupca, nazivProdavnice);
	}

	
	public String getZiroRacunProdavnice() {		
		return ziroRacunProdavnice;
	}

	private EmailService emailService;	

	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;		
	}


	
	public boolean prepareAndSendRacun(String email, String imePrezime, RacunDTO racun) {		
		String subject = "Racun "+racun.getRacunId() + "za "+imePrezime;
		String message = racun.getPrintableRacun();
		return emailService.sendEmail(email, subject, message);
		
	}
	
	private PlacanjeService placanjeService;
	
	public void setPlacanjeService(PlacanjeService placanjeService) {
		this.placanjeService = placanjeService;
	}

	public List<RacunDTO> getIzdatiRacuni() {
		return izdatiRacuni;
	}	

	public List<RacunDTO> getRacuniVraceno() {
		return racuniVraceno;
	}

	
	
	
	
	
	
		
	
}
