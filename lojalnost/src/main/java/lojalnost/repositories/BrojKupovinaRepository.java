package lojalnost.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import lojalnost.model.BrojKupovina;

public interface BrojKupovinaRepository extends CrudRepository<BrojKupovina, Long> {
	
	@Query("select b from BrojKupovina b where b.email like :email")
	BrojKupovina getBrojKupovinaZaEmail(String email);

}
