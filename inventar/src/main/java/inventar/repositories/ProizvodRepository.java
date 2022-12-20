package inventar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import inventar.model.Proizvod;

public interface ProizvodRepository extends CrudRepository<Proizvod, Long> {
	
	
	
	@Query("select p from Proizvod p where p.naziv like :naziv")
	List<Proizvod> vratiProizvodZaNaziv(String naziv);

}
