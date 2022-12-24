package trgovina.serviceimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import trgovina.dtos.ProizvodDTO;
import trgovina.serviceconsumers.InventarServiceConsumer;

// jedinicni testovi za inventar prodavnice

class ProdavnicaInventarServiceImplUnitTest {
	
	
	
	@Test
	void testStanjeZaProizvod() {	
		InventarServiceConsumer inventarServiceConsumer = mock(InventarServiceConsumer.class);
		List<ProizvodDTO> proizvodi = new ArrayList<>();
		proizvodi.add(new ProizvodDTO("sampon","kozmetika",100.0,90));
		proizvodi.add(new ProizvodDTO("sapun","kozetika",50.0,50));
		proizvodi.add(new ProizvodDTO("hleb","prehrambeni",40.0,80));
		proizvodi.add(new ProizvodDTO("mleko","prehrambeni",30.0,100));
		when(inventarServiceConsumer.vratiSveProizvode()).thenReturn(proizvodi);
		when(inventarServiceConsumer.vratiProizvodePoNazivu("sampon")).thenReturn(proizvodi.get(0));
		ProdavnicaInventarServiceImpl prodavnicaInventarService = new ProdavnicaInventarServiceImpl(inventarServiceConsumer);
		assertEquals(90,prodavnicaInventarService.vratiStanjeZaProizvod("sampon"));
	}
	
	// granicni slucajevi
	
	@Test
	void testBrojRazlicitihProizvodaBezProizvoda() {	
		InventarServiceConsumer inventarServiceConsumer = mock(InventarServiceConsumer.class);
		ProdavnicaInventarServiceImpl prodavnicaInventarService;
		when(inventarServiceConsumer.vratiSveProizvode()).thenReturn(new ArrayList<>());
		prodavnicaInventarService = new ProdavnicaInventarServiceImpl(inventarServiceConsumer);
		
		assertEquals(0,prodavnicaInventarService.vratiBrojRazlicitihProizvoda());
	}
	
	@Test
	void testBrojRazlicitihProizvodaNullProizvodi() {	
		InventarServiceConsumer inventarServiceConsumer = mock(InventarServiceConsumer.class);
		ProdavnicaInventarServiceImpl prodavnicaInventarService;
		when(inventarServiceConsumer.vratiSveProizvode()).thenReturn(null);
		prodavnicaInventarService = new ProdavnicaInventarServiceImpl(inventarServiceConsumer);
		
		assertEquals(0,prodavnicaInventarService.vratiBrojRazlicitihProizvoda());
	}
	
	
	
	
	
}
