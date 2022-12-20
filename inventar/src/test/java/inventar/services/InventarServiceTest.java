package inventar.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import inventar.dtos.ProizvodDTO;
import inventar.repositories.ProizvodRepository;


@SpringBootTest
class InventarServiceTest {
	
	@Autowired
	ProizvodRepository proizvodRepo;
	
	@Autowired
	InventarService inventarService;


	@Test
	void testSmanjiKolicinuIspravno() {
		boolean uspesno = inventarService.smanjiKolicinuNaStanju("sampon", 50);		
		ProizvodDTO p = inventarService.getProizvodZaNaziv("sampon");
		
		assertTrue(uspesno);
		assertEquals(50, p.getStanje());
	}
	
	@Test
	void testSmanjiKolicinuNeIspravno() {
		boolean uspesno = inventarService.smanjiKolicinuNaStanju("sapun", 100);		
		ProizvodDTO p = inventarService.getProizvodZaNaziv("sapun");
		
		assertFalse(uspesno);
		assertEquals(80, p.getStanje());
	}

}
