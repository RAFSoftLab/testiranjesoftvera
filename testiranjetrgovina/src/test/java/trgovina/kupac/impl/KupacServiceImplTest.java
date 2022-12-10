package trgovina.kupac.impl;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import trgovina.izuzeci.NedozvoljenaOperacijaNadRacunomException;
import trgovina.model.Kupac;
import trgovina.prodavnicaimpl.Prodavnica;
import trgovina.services.ProdavnicaService;

class KupacServiceImplTest {

	@Test
	void testKupacServiceOtvaranjeRacuna() {
		KupacServiceImpl kupacService = new KupacServiceImpl();
		Kupac k = new Kupac("Marko", "Markovic");
		ProdavnicaService prodavnica = new Prodavnica();		
		
		String brojRacuna = kupacService.otvoriRacun(k,prodavnica);
		
		assertTrue(brojRacuna.startsWith("MM"));
		assertTrue(kupacService.getAktivniRacuni().containsKey(k));
		assertTrue(kupacService.getAktivniRacuni().get(k).getRacunId().equals(brojRacuna));
		
	}
	
	
	@Test
	void testKupacServiceOtvaranjeRacunaMockGenerator() {
		// generator broja racuna ima nedeterministicko ponasanje, mozemo taj deo da laziramo
		
		KupacServiceImpl kupacService = new KupacServiceImpl();
		Kupac k = new Kupac("Marko", "Markovic");
		ProdavnicaService prodavnica = mock(ProdavnicaService.class);
		when(prodavnica.noviBrojRacuna(k)).thenReturn("MM12345");
		
		String brojRacuna = kupacService.otvoriRacun(k,prodavnica);		
		
		assertTrue(kupacService.getAktivniRacuni().containsKey(k));
		assertTrue(kupacService.getAktivniRacuni().get(k).getRacunId().equals(brojRacuna));
		verify(prodavnica).noviBrojRacuna(k);
		
	}
	
	@Test
	void testKupacServiceZatvaranjeRacuna() {	
		
		KupacServiceImpl kupacService = new KupacServiceImpl();
		Kupac k = new Kupac("Marko", "Markovic");
		ProdavnicaService prodavnica = new Prodavnica();		
		String brojRacuna = kupacService.otvoriRacun(k,prodavnica);
		
		kupacService.zatvoriRacun(k,prodavnica);
		
		assertEquals(0,kupacService.getAktivniRacuni().size());
		assertEquals(1,k.getZatvoreniRacuni().size());
		assertEquals(brojRacuna,k.getZatvoreniRacuni().get(0).getRacunId());			
	}
	
	// za vezbu - da studenti sami rade
	
	@Test
	void testKupacServiceZatvaranjeRacunaNesipravno() {	
		
		KupacServiceImpl kupacService = new KupacServiceImpl();
		Kupac k = new Kupac("Marko", "Markovic");
		ProdavnicaService prodavnica = new Prodavnica();				
		
		
		assertThrows(NedozvoljenaOperacijaNadRacunomException.class,() -> kupacService.zatvoriRacun(k,prodavnica));
		
					
	}
	
	
	


}
