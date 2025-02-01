package kupac.repositories;

import kupac.model.Kupac;
import kupac.model.Rezervacija;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RezervacijaRepository extends CrudRepository<Rezervacija, Long> {
    List<Rezervacija> findByKupac(Kupac kupac);
}
