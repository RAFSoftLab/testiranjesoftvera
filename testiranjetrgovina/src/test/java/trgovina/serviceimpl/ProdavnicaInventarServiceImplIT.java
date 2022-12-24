package trgovina.serviceimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

// integracioini test za prodavnica inventar service

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import trgovina.serviceconsumers.InventarServiceConsumer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProdavnicaInventarServiceImplIT {
	
	
	private ProdavnicaInventarServiceImpl prodavnicaInventarService;
	
	@Autowired
	private InventarServiceConsumer inventarServiceConsumer;
	
	
	@BeforeEach
	public void prepareService() {					
		prodavnicaInventarService = new ProdavnicaInventarServiceImpl(inventarServiceConsumer);
		
	}
	
	@Test
	void testBrojRazlicitihProizvoda() {		
		assertEquals(4,prodavnicaInventarService.vratiBrojRazlicitihProizvoda());
	}
	
	@Test
	void testStanjeZaProizvod() {		
		assertEquals(100,prodavnicaInventarService.vratiStanjeZaProizvod("sampon"));
	}

}
