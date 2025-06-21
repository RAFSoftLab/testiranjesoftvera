package trgovina.services;

import java.util.List;

import trgovina.dtos.KupacDTO;
import trgovina.dtos.KupovinaDTO;
import trgovina.dtos.RezervacijaDTO;
import trgovina.dtos.UplataDTO;

public interface ProdavnicaKupacService {

    public KupacDTO kupacZaId(int id);

    public List<KupovinaDTO> kupovineZaKupca(int id);

    public void obrisiKupovinu(Long idKupovine);

    public String vratiTekuciRacunZaIdKupca(int kupacId);

    public List<UplataDTO> vratiStrukturuUplate(int kupacId, double iznosZaUplatu);

    public void smanjiStanjeNaRacunu(String tekuciRacun, double umanjenje);

    public void povecajStanjeNaRacunu(String tekuciRacun, double povecanje);

    public void dodajRezervisanoZaKupca(int kupacId, double iznosZaRezervisanje);

    public void sacuvajKupovinu(KupovinaDTO kupovina);

    public KupovinaDTO vratiKupovinuZaRacunId(String racunId);

    public String vratiRacunZaIsplatu(int kupacId, double iznosZaUplatu);

    public void dodajRezervaciju(RezervacijaDTO rezervacija);

    public void obrisiRezervaciju(long rezervacijaID);

    public RezervacijaDTO vratiRezervacijuZaId(long rezervacijaID);
}
