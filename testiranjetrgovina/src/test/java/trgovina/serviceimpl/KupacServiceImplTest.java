package trgovina.serviceimpl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import trgovina.izuzeci.NedozvoljenaOperacijaNadRacunomException;
import trgovina.model.Kupac;




class KupacServiceImplTest {

	@Test
	void testKupacServiceOtvaranjeRacuna() {
		Prodavnica prodavnica = new Prodavnica();	
		KupacServiceImpl kupacService = new KupacServiceImpl(prodavnica);
		Kupac k = new Kupac("Marko", "Markovic");
			
		
		String brojRacuna = kupacService.otvoriRacun(k);
		
		assertTrue(brojRacuna.startsWith("MM"));
		assertTrue(kupacService.getAktivniRacuni().containsKey(k));
		assertTrue(kupacService.getAktivniRacuni().get(k).getRacunId().equals(brojRacuna));
		
	}
	
	
	@Test
	void testKupacServiceOtvaranjeRacunaMockGenerator() {
		// generator broja racuna ima nedeterministicko ponasanje, mozemo taj deo da laziramo
		Kupac k = new Kupac("Marko", "Markovic");
		Prodavnica prodavnica = mock(Prodavnica.class);
		when(prodavnica.noviBrojRacuna(k)).thenReturn("MM12345");
		KupacServiceImpl kupacService = new KupacServiceImpl(prodavnica);
		
		
		
		String brojRacuna = kupacService.otvoriRacun(k);		
		
		assertTrue(kupacService.getAktivniRacuni().containsKey(k));
		assertTrue(kupacService.getAktivniRacuni().get(k).getRacunId().equals(brojRacuna));	
	}
	
	@Test
	void testKupacServiceZatvaranjeRacuna() {	
		Prodavnica prodavnica = new Prodavnica();	
		KupacServiceImpl kupacService = new KupacServiceImpl(prodavnica);
		Kupac k = new Kupac("Marko", "Markovic");			
		String brojRacuna = kupacService.otvoriRacun(k);
		
		kupacService.zatvoriRacun(k);
		
		assertEquals(0,kupacService.getAktivniRacuni().size());
		assertEquals(1,k.getZatvoreniRacuni().size());
		assertEquals(brojRacuna,k.getZatvoreniRacuni().get(0).getRacunId());			
	}
	
	// za vezbu - da studenti sami rade
	
	@Test
	void testKupacServiceZatvaranjeRacunaNeispravno() {	
		Prodavnica prodavnica = new Prodavnica();	
		KupacServiceImpl kupacService = new KupacServiceImpl(prodavnica);
		Kupac k = new Kupac("Marko", "Markovic");				
		
		
		assertThrows(NedozvoljenaOperacijaNadRacunomException.class,() -> kupacService.zatvoriRacun(k));
		
					
	}
	
	
	


}
