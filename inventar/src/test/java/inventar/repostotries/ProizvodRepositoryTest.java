package inventar.repostotries;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import inventar.model.Proizvod;
import inventar.repositories.ProizvodRepository;

@SpringBootTest
class ProizvodRepositoryTest {
	
	@Autowired
	ProizvodRepository proizvodRepo;

	@Test
	void testProizvodPoNazivu() {
		List<Proizvod> proizvodi = proizvodRepo.vratiProizvodZaNaziv("sampon");
		
		assertEquals(1, proizvodi.size());
		assertEquals("sampon", proizvodi.get(0).getNaziv());
	}

}
