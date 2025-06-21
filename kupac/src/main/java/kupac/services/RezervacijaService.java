package kupac.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import kupac.dtos.KupacDTO;
import kupac.dtos.RezervacijaDTO;
import kupac.model.Kupac;
import kupac.model.Rezervacija;
import kupac.repositories.RezervacijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import kupac.repositories.KupacRepository;

@Service
public class RezervacijaService {

    private RezervacijaRepository rezervacijaRepo;
    private KupacRepository kupacRepo;

    @Autowired
    public RezervacijaService(RezervacijaRepository rezervacijaRepo, KupacRepository kupacRepo) {
        this.rezervacijaRepo = rezervacijaRepo;
        this.kupacRepo = kupacRepo;
    }

    // kreiranje nove rezervacije
    public boolean sacuvajRezervaciju(RezervacijaDTO rezervacijaDTO) {
        Optional<Kupac> kupacOpt = kupacRepo.findById(rezervacijaDTO.getKupacId());
        if (kupacOpt.isPresent()) {
            Kupac kupac = kupacOpt.get();
            Rezervacija rezervacija = new Rezervacija();
            rezervacija.setKupac(kupac);
            rezervacija.setKolicina(rezervacijaDTO.getKolicina());
            rezervacija.setNazivProizvoda(rezervacijaDTO.getNazivProizvoda());

            rezervacijaRepo.save(rezervacija);
            return true;
        }
        return false;
    }


    public List<RezervacijaDTO> getAllRezervacije() {
        Iterable<Rezervacija> rezervacije = rezervacijaRepo.findAll();
        List<RezervacijaDTO> retVal = new ArrayList<>();
        rezervacije.forEach(r -> retVal.add(new RezervacijaDTO(r.getId(), r.getKupac().getId(), r.getNazivProizvoda(), r.getKolicina())));

        return retVal;
    }

    public void obrisiRezeraviciju(Long idRezervacija) {
        rezervacijaRepo.deleteById(idRezervacija);
    }

    public RezervacijaDTO vratiZaId(Long id) {
        return rezervacijaRepo.findById(id)
                .map(r -> new RezervacijaDTO(r.getId(), r.getKupac().getId(), r.getNazivProizvoda(), r.getKolicina()))
                .orElse(null); // Ako nema rezervacije, vraća null
    }


}