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
import trgovina.model.ProizvodKolicina;
import trgovina.model.Racun;
import trgovina.services.EmailService;
import trgovina.services.PlacanjeService;
import trgovina.services.ProdavnicaInventarService;
import trgovina.services.ProdavnicaKupacService;
import trgovina.services.ProdavnicaLojalnostService;
import trgovina.utils.BrojRacunaGenerator;

@Service
public class Prodavnica {

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
        return inventarService.vratiStanjeZaProizvod(nazivProizvoda) >= kolicina;
    }

    public int getKolicina(String naziv) {
        return inventarService.vratiStanjeZaProizvod(naziv);
    }

    /**
     * Na osnovu interne reprezentacije racuna izdaje se racun sa obracunatom cenom.
     * <p>
     * Racun mora biti zatvoren, što znaci da je zavrsena kupovina.
     * <p>
     * Svaki racun se moze izdati samo jednom i kada se izda smesta se u listu izdatiRacuni
     * <p>
     * Racun sadrzi podatke o prodavnici, naziv i ziro racun, spisak svih artikala, cenu bez i sa pdv-om,
     * kao i cenu sa popustom.
     * <p>
     * Podaci o stanju proizvoda, podaci o kupcu i popustima preuzimaju se iz odvojenih servisa.
     * <p>
     * Prilikom izdavanja racuna ponovo proveravamo da li ima dovoljno prozvoda, ako ne postoji dovoljna kolicina izdaje se koliko
     * ima na stanju, ako proizvoda uopste nema na stanju, taj artikal se ne nalazi na racunu i ne naplacuje se.
     * <p>
     * Prilikom izdavanja racuna vrsi se umanjenje stanja artikala u inventaru.
     *
     * @param r
     * @return
     */

    public RacunDTO izdajRacun(Racun r) throws InventarException {
        if (r.isZatvoren() == false)
            return null;
        RacunDTO racun = izdatRacun(r.getRacunId());
        if (racun != null)
            return racun;
        racun = new RacunDTO();
        racun.setIdKupca(r.getKupacId());
        racun.setNazivProdavnice(nazivProdavnice);
        racun.setZiroRacunProdavnice(ziroRacunProdavnice);
        KupacDTO kupac = kupacService.kupacZaId(r.getKupacId());
        racun.setImeIPrezimeKupca(kupac.getIme() + " " + kupac.getPrezime());
        racun.setDatumKupovine(r.getDatum());
        racun.setRacunId(r.getRacunId());
        Map<String, Integer> artikli = new HashMap<>(); // pravimo praznu mapu artikala za racun, jer nece ici svi artikli, ako nekih nema na stanju
        racun.setArtikli(artikli);
        double ukupnaCena = 0;
        for (String proizvod : r.getArtikli().keySet()) {
            int stanje = inventarService.vratiStanjeZaProizvod(proizvod);
            int brojArtikalaNaRacunu = 0;
            if (stanje > 0 && stanje <= r.getBrojProizvoda(proizvod)) {
                brojArtikalaNaRacunu = stanje;
            } else {
                brojArtikalaNaRacunu = r.getBrojProizvoda(proizvod);
            }
            if (brojArtikalaNaRacunu > 0) {
                artikli.put(proizvod, brojArtikalaNaRacunu);
                double cena = inventarService.vratiCenuZaProizvod(proizvod);
                inventarService.umanjiStanjeProizvoda(proizvod, brojArtikalaNaRacunu);
                ukupnaCena += cena * brojArtikalaNaRacunu;
            }
        }
        racun.setUkupnaCenaBezPdv(ukupnaCena);
        double cenaSaPdv = ukupnaCena * (1 + pdv);
        racun.setUkupnaCenaSaPdv(cenaSaPdv);
        int popust = lojalnostService.vratiPopustZaKupca(kupac.getEmail());
        racun.setUkupnaCenaSaPopustom(cenaSaPdv * (100 - popust) / 100);
        izdatiRacuni.add(racun);
        return racun;
    }

    /**
     * Kreira se racun za kupca sa cenom i salje mu se na email.
     * <p>
     * Za slanje emaila koristi se servis koji još uvek nije implementiran.
     *
     * @param r
     */

    public void izdajRacunEmailom(Racun r) throws InventarException {
        RacunDTO racun = izdajRacun(r);
        KupacDTO kupac = kupacService.kupacZaId(r.getKupacId());
        prepareAndSendRacun(kupac.getEmail(), kupac.getIme() + " " + kupac.getPrezime(), racun);
    }


    public RacunDTO izdatRacun(String racunId) {
        for (RacunDTO r : izdatiRacuni) {
            if (r.getRacunId().equals(racunId))
                return r;
        }
        return null;
    }

    /**
     * Operacija realizuje vracanje prethodno kupljenog proizvoda. Prosledjuje se id izdatog racuna, naziv i kolicina proizvoda koji se
     * vracaju. Operacija vraca logicku vrednost koja oznacava da li je vracaje uspesno izvrseno.
     * <p>
     * Ako ne postoji izdat racun sa prosledjenim id-jem, ili postoji ali ne sadrzi dovoljno prozvoda operacija vraca false.
     * <p>
     * Ako racun postoji razlikujemo dva slucaja, vraceni proizvod je jedini na racunu ili na racunu ima i drugih proizvoda i u ova dva slucaja se razlicito
     * azuriraju liste izdatiRacuni i racuniVraceno (polja ove klase).
     * <p>
     * Ako je vraceni proizvod jedini na racunu, taj racun se brise iz liste izdatiRacuni i ubacuje se u listu racuniVraceno.
     * <p>
     * Ako na racunu ima drugih artikala, onda se stari originalni racun ubacuje u listu racuniVraceno, a sa izdatog racuna se izbacuju vraceni artikli (ukupna cena se ne obracunava ponovo).
     * <p>
     * Zatim se azurira stanje inventara, odnosno uvecava se stanje za vraceni proizvod preko servisa inventar.
     * <p>
     * Na kraju se vrsi povracaj novca i slanje obavestenja kupcu putem emaila. Prvo se preuzima kupovina za dati racun iz servisa kupac iz koje se cita da li je racun placen.
     * <p>
     * Ako je racun placen, obracunava se iznos koji se vraca, na osnovu cene proizvoda uz umanjenje ako je kupac imao popust (pdv se ne uzima u obzir).
     * Podaci o popustu se preuzimaju iz servisa lojalnost.
     * <p>
     * Zatim se preuzima tekuci racun kupca, preko servisa kupac. Preko servisa za placanje (koji jos uvek nije implementiran) vrsi se placanje sa racuna prodavnice
     * na tekuci racun kupca sa id-jem racuna kao pozivom na broj.
     * <p>
     * Na kraju se kupcu salje email preko servisa emailService (jos uvek nije implementiran). Naslov email poruke treba da sadrzi "Povracaj proizvoda za racun "+racunId.
     * <p>
     * U slucaju da novac nije vracen (ako nije placen ili je doslo do greske u servisu za placanje), tekst poruke treba da sadrzi string "novac nije vracen",
     * ako novac jeste vracen, tekst poruke treba da sadrzi string "vracen novac".
     */

    public boolean vracanjeProizvoda(String racunId, String nazivProizvoda, int kolicina) {
        RacunDTO racunZaProizvod = izdatRacun(racunId);
        if (racunZaProizvod == null || !racunZaProizvod.sadrziProizvodUKolicini(nazivProizvoda, kolicina))
            return false;
        // ako je vraceni proizvod jedini na racunu u datoj kolicini
        if (racunZaProizvod.getArtikli().size() == 1 && racunZaProizvod.getArtikli().get(nazivProizvoda) != null && racunZaProizvod.getArtikli().get(nazivProizvoda) == kolicina) {
            izdatiRacuni.remove(racunZaProizvod);
            racuniVraceno.add(racunZaProizvod);
        } else {
            racuniVraceno.add(racunZaProizvod);
            racunZaProizvod.smanjiKolicinu(nazivProizvoda, kolicina);
        }
        inventarService.uvecajStanjeProizvoda(nazivProizvoda, kolicina);

        // povracaj novca i slanje obavestenja emailom

        KupovinaDTO kupovinaVracanje = kupacService.vratiKupovinuZaRacunId(racunId);
        KupacDTO kupac = kupacService.kupacZaId(racunZaProizvod.getIdKupca());
        String naslov = "Povracaj proizvoda za racun " + racunId;
        String poruka = "";
        boolean vracenNovac = false;

        if (kupovinaVracanje != null && kupovinaVracanje.isPlacen()) {         // povracaj novca, ako je prethodna kupovina placena
            // obracun koliko treba novca da se vrati
            double cena = inventarService.vratiCenuZaProizvod(nazivProizvoda);
            double iznosZaVracanje = cena * kolicina;
            int popust = lojalnostService.vratiPopustZaKupca(kupac.getEmail());
            iznosZaVracanje = iznosZaVracanje * (100 - popust) / 100;
            String tekuciRacun = kupacService.vratiTekuciRacunZaIdKupca(racunZaProizvod.getIdKupca());
            vracenNovac = placanjeService.plati(nazivProdavnice, ziroRacunProdavnice, tekuciRacun, racunZaProizvod.getRacunId(), iznosZaVracanje);
        }

        if (vracenNovac)
            poruka = "Uspesno vracanje proizvoda, vracen novac";
        else
            poruka = "Uspesno vracanje proizvoda, novac nije vracen";
        emailService.sendEmail(kupac.getEmail(), naslov, poruka);
        return true;
    }


    /**
     * Operacija odradjuje placaje racuna, celog ili samo jedne rate i vraca da li je uspesno placeno.
     * <p>
     * Prosledjeni racun mora da postoji u listi izdatih racuna.
     * <p>
     * Prvo se obracunava iznos za uplatu koji moze biti ceo iznos (ukupna cena sa racuna sa popustom), ili samo jedna rata, ako je prosledjeni broj rata veci od 1.
     * <p>
     * Zatim se iz servisa kupac preuzima pozeljna struktura uplate koja sadrzi listu objekata UplataDTO koja sadrzi broj tekuceg racuna i iznos koji treba skinuti
     * kupcu sa tog tekuceg racuna (uplata se vrsi sa vise racuna).
     * <p>
     * Ukoliko je struktura uplate navalidna (null), placanje je neuspesno.
     * <p>
     * Ako struktura uplate nije null, realizuju se uplate redom sa takucih racuna kupca na racun prodavnice. Uplate se vrse pleko servisa placanje koji jos uvek nije implementiran.
     * U momentu placanja moze da se desi da neka uplata ne prodje (metoda za placanje vraca false). Sabiraju se sve uspesne uplate.
     * <p>
     * Ukoliko je ukupan iznos uspesnih uplata manji od iznosa za placanje, razlika se belezi kupcu u rezervisana sredstva, preko servisa kupac. Osim ako je ukupan uplacen
     * iznos jednak 0, u tom slucaju placanje se smatra neuspesnim.
     * <p>
     * Takodje, ukupan iznos preostalih rata za uplatu se kupcu belezi kao rezervisan.
     * <p>
     * Ukoliko je barem nesto placeno, uplata se smatra uspesnom i u tom slucaju se kreira nova kupovina za kupca u servisu kupac u kojoj se cuvaju datum kupovine sa racuna,
     * belezi se uspesna uplata i ukupan uplacen iznos.
     * <p>
     * U slucaju da je uplata neuspesna, preko servisa ineventar se svi artikli sa racuna vracaju na stanje, a racun se brise iz liste izdatih racuna.
     */
    public boolean platiRacun(RacunDTO racun, int brojRata) {
        if (!izdatiRacuni.contains(racun))
            return false;
        double iznosZaUplatu = racun.getUkupnaCenaSaPopustom();
        if (brojRata > 1) {
            iznosZaUplatu = iznosZaUplatu / brojRata;
        }
        List<UplataDTO> strukturaUplate = kupacService.vratiStrukturuUplate(racun.getIdKupca(), iznosZaUplatu);
        boolean uspesno = true;
        double ukupnoPlaceno = 0;
        if (strukturaUplate == null) {
            uspesno = false;
        } else {
            KupacDTO kupac = kupacService.kupacZaId(racun.getIdKupca());
            for (UplataDTO uplata : strukturaUplate) {
                boolean uspesnaJedna = placanjeService.plati(kupac.getIme() + kupac.getPrezime(), uplata.getTekuciRacun(), ziroRacunProdavnice, racun.getRacunId(), uplata.getUplatiti());
                if (uspesnaJedna) {
                    ukupnoPlaceno += uplata.getUplatiti();
                    kupacService.smanjiStanjeNaRacunu(uplata.getTekuciRacun(), uplata.getUplatiti());
                }
            }
            if (ukupnoPlaceno < iznosZaUplatu) {
                if (ukupnoPlaceno == 0) {
                    uspesno = false;
                } else {
                    kupacService.dodajRezervisanoZaKupca(racun.getIdKupca(), iznosZaUplatu - ukupnoPlaceno);
                    uspesno = true;
                }
            }
        }
        if (brojRata > 1) {
            double iznosZaRezervisanje = racun.getUkupnaCenaSaPopustom() - iznosZaUplatu;
            kupacService.dodajRezervisanoZaKupca(racun.getIdKupca(), iznosZaRezervisanje);
        }
        if (uspesno) {
            KupovinaDTO kupovina = new KupovinaDTO(racun.getDatumKupovine(), racun.getRacunId(), uspesno, ukupnoPlaceno, Long.valueOf(racun.getIdKupca()));
            kupacService.sacuvajKupovinu(kupovina);
        } else {
            for (String naziv : racun.getArtikli().keySet()) {
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
        String subject = "Racun " + racun.getRacunId() + "za " + imePrezime;
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


    /**
     * Operacija izvrsava placanje prosledjenog racuna (@param racun) od strane više kupaca. Bez obzira koji id kupca stoji na računu,
     * plaćanje se deli na kupce prema procentu plaćanja koji je prosleđen u @param procentiKupca. U mapi se kao
     * ključ prosleđuje id kupca, a vrednost je procenat iznosa na računu koji taj kupac plaća
     * <p>
     * Operacija vraća u mapi za svakog id kupca koji iznos je uplatio.
     * <p>
     * Prosledjeni racun mora da postoji u listi izdatih racuna. Takođe, zbir svih procenata plaćanja za kupce mora biti 100,
     * inače se prosleđuje izuzetak IllegalArgumentException.
     * <p>
     * Prvo se obracunava ukupan iznos za uplatu koji se dobija tako što se iz računa preuzme ukupna cena sa pdv-om i na nju
     * se obračuna najveći popust svih kupaca koji učestvuju u uplati. Popust za svakog kupca se preuzima iz servisa lojalnosti.
     * <p>
     * Zatim se za svakog kupca preko kupac servisa preuzima njegov tekući račun na kome ima dovoljno sredstava da se izvrši
     * uplata njegovog procenta računa. Ako barem za jednog kupca ne postoji takav račun, plaćanje se ne može izvršiti
     * ni sa jednog dela računa, ni za jednog kupca, metoda vraća null.
     * <p>
     * Ako svi kupci imaju račun sa dovoljno sredstava, vrši se plaćanje preko servisa koji još uvek nije implementiran.
     * Plaćanje se vrši za svakog kupca posebno, sa tekuceg racuna kupca na racuna prodavnice sa id-jem racuna kao pozivom na broj.
     * Kao uplatilac se prosleđuje ime i prezime kupca, a iznos se obračunava procenatualno za svakog kupca.
     * <p>
     * Ako je servis za plaćanje vratio da je uspesna uplata, preko kupac servisa se smanjuju sredstva na tekucem racunu
     * za uplaceni iznos i uplata se evidetira u rezultujućoj mapi.
     */


    public Map<Integer, Double> deljenoPlacanjeRacunaProcenti(RacunDTO racun, Map<Integer, Double> procentiKupca) throws IllegalArgumentException {
        if (!izdatiRacuni.contains(racun))
            throw new IllegalArgumentException("Ne postoji racun");
        double sumaProcenata = procentiKupca.values().stream().mapToDouble(el -> el.doubleValue()).sum();
        if (sumaProcenata != 100)
            throw new IllegalArgumentException("Suma procenata nije jednaka 100");
        double iznosSaPdvom = racun.getUkupnaCenaSaPdv();
        int najveciPopust = 0;
        for (Integer kupacId : procentiKupca.keySet()) {
            KupacDTO kupac = kupacService.kupacZaId(kupacId);
            int popust = lojalnostService.vratiPopustZaKupca(kupac.getEmail());
            if (popust > najveciPopust)
                najveciPopust = popust;
        }
        double iznosZaUplatu = iznosSaPdvom * (100 - najveciPopust) / 100;
        System.out.println("Iznos za uplatu " + iznosZaUplatu);
        Map<String, Double> tekuciRacuniZaUplatu = new HashMap<>();
        Map<String, Integer> racuniKupca = new HashMap<>();
        for (int idKupca : procentiKupca.keySet()) {
            double procenatKupca = procentiKupca.get(idKupca);
            double uplataKupca = iznosZaUplatu * (procenatKupca / 100);
            // preuzimamo racun kupca na kome ima dovoljno sredstava
            String tekuciRacun = kupacService.vratiRacunZaIsplatu(idKupca, uplataKupca);
            if (tekuciRacun == null)
                return null;
            tekuciRacuniZaUplatu.put(tekuciRacun, uplataKupca);
            racuniKupca.put(tekuciRacun, idKupca);
        }
        Map<Integer, Double> retVal = new HashMap<>();
        for (String tRacun : tekuciRacuniZaUplatu.keySet()) {
            KupacDTO kupac = kupacService.kupacZaId(racuniKupca.get(tRacun));
            boolean uspesno = placanjeService.plati(kupac.getIme() + kupac.getPrezime(), tRacun, ziroRacunProdavnice, racun.getRacunId(), tekuciRacuniZaUplatu.get(tRacun));
            if (uspesno) {
                kupacService.smanjiStanjeNaRacunu(tRacun, tekuciRacuniZaUplatu.get(tRacun));
                retVal.put(racuniKupca.get(tRacun), tekuciRacuniZaUplatu.get(tRacun));
            }
        }
        return retVal;
    }


    /**
     * Operacija izvrsava placanje prosledjenog racuna (@param racun) od strane više kupaca gde svaki kupac plaća određenu
     * listu proizvoda sa računa.  Bez obzira koji id kupca stoji na računu, plaćanje se deli na kupce prema mapi koja
     * je prosleđena u @param artikliKupca. U mapi se kao ključ prosleđuje idKupca, a vrednosti su lista proizvoda sa količinima
     * koje taj kupac plaća sa računa.
     * <p>
     * Operacija vraća u mapi za svakog id kupca koji iznos je uplatio.
     * <p>
     * Prosledjeni racun mora da postoji u listi izdatih racuna. Takođe, ukupna količina svih proizvoda u prosleđenoj mapi treba
     * da odgovara količini kupljenih proizvoda na računu, inače se prosleđuje izuzetak IllegalArgumentException.
     * <p>
     * Svaki kupac plaća ukupnu cenu svih svojih proizvoda sa obračunatim pdv-om od 20% i svima se obračunava isti popust,
     * a to je najmanji popust kupaca iz prosleđene mape. Ako barem jedan od prosleđenih kupaca nema popust, ne obračunava
     * se popust nikome.
     * <p>
     * Za svakog kupca se preko kupac servisa preuzima njegov tekući račun na kome ima dovoljno sredstava da se izvrši
     * uplata njegovih proizvoda. Ako barem za jednog kupca ne postoji takav račun, plaćanje se ne može izvršiti
     * ni sa jednog dela računa,ni za jednog kupca, metoda vraća null.
     * <p>
     * Ako svi kupci imaju račun sa dovoljno sredstava, vrši se plaćanje preko servisa koji još uvek nije implementiran.
     * Plaćanje se vrši sa tekuceg racuna kupca na racuna prodavnice sa id-jem racuna kao pozivom na broj.
     * Kao uplatilac se prosleđuje ime i prezime kupca, a iznos prethodno obračunata cena za izabrane proizvode kupca.
     * <p>
     * Ako je servis za plaćanje vratio da je uspesna uplata, preko kupac servisa se smanjuju sredstva na tekucem racunu
     * za uplaceni iznos i uplata se evidetira u rezultujućoj mapi.
     */


    public Map<Integer, Double> deljenoPlacanjeRacunaArtikli(RacunDTO racun, Map<Integer, List<ProizvodKolicina>> artikliKupca) throws IllegalArgumentException {
        if (!izdatiRacuni.contains(racun))
            throw new IllegalArgumentException("Ne postoji racun");

        Map<String, Integer> artikliZaPlacanje = new HashMap<>();
        for (List<ProizvodKolicina> pkList : artikliKupca.values()) {
            for (ProizvodKolicina pk : pkList) {
                if (!artikliZaPlacanje.containsKey(pk.getNazivProizvoda()))
                    artikliZaPlacanje.put(pk.getNazivProizvoda(), pk.getKolicina());
                else
                    artikliZaPlacanje.put(pk.getNazivProizvoda(), artikliZaPlacanje.get(pk.getNazivProizvoda()) + pk.getKolicina());
            }
        }
        // provera da li je ukupna kolicina odgovara onoj na računu za svaki proizvod
        for (String proizvod : artikliZaPlacanje.keySet()) {
            if (racun.getArtikli().get(proizvod) != artikliZaPlacanje.get(proizvod))
                throw new IllegalArgumentException("Pogresna kolicina artikala");
        }

        int najmanjiPopust = 100;
        for (Integer kupacId : artikliKupca.keySet()) {
            KupacDTO kupac = kupacService.kupacZaId(kupacId);
            int popust = lojalnostService.vratiPopustZaKupca(kupac.getEmail());
            if (popust < najmanjiPopust)
                najmanjiPopust = popust;
        }
        Map<Integer, Double> retVal = new HashMap<>();
        Map<String, Double> tekuciRacuniZaUplatu = new HashMap<>();
        Map<String, Integer> racuniKupca = new HashMap<>();
        for (int idKupca : artikliKupca.keySet()) {
            double uplataZaKupca = 0.0;
            for (ProizvodKolicina pk : artikliKupca.get(idKupca)) {
                double cena = inventarService.vratiCenuZaProizvod(pk.getNazivProizvoda());
                uplataZaKupca += pk.getKolicina() * cena * 1.2;
            }
            uplataZaKupca = uplataZaKupca * (100 - najmanjiPopust) / 100;
            // preuzimamo racun kupca na kome ima dovoljno sredstava
            String tekuciRacun = kupacService.vratiRacunZaIsplatu(idKupca, uplataZaKupca);
            if (tekuciRacun == null)
                return null;
            tekuciRacuniZaUplatu.put(tekuciRacun, uplataZaKupca);
            racuniKupca.put(tekuciRacun, idKupca);
        }
        for (String tRacun : tekuciRacuniZaUplatu.keySet()) {
            KupacDTO kupac = kupacService.kupacZaId(racuniKupca.get(tRacun));
            boolean uspesno = placanjeService.plati(kupac.getIme() + kupac.getPrezime(), tRacun, ziroRacunProdavnice, racun.getRacunId(), tekuciRacuniZaUplatu.get(tRacun));
            if (uspesno) {
                kupacService.smanjiStanjeNaRacunu(tRacun, tekuciRacuniZaUplatu.get(tRacun));
                retVal.put(racuniKupca.get(tRacun), tekuciRacuniZaUplatu.get(tRacun));
            }
        }
        return retVal;
    }


}
