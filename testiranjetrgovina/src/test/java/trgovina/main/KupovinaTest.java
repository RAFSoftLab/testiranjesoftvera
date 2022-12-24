package trgovina.main;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import trgovina.dtos.KupacDTO;
import trgovina.izuzeci.NedozvoljenaOperacijaNadRacunomException;
import trgovina.main.KupovinaService;
import trgovina.main.Prodavnica;
import trgovina.services.ProdavnicaKupacService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class KupovinaTest {

	@Test
	void testkupovinaServiceOtvaranjeRacuna() {
		Prodavnica prodavnica = new Prodavnica();	
		KupovinaService kupovinaService = new KupovinaService(prodavnica);	
		ProdavnicaKupacService kupacService = mock(ProdavnicaKupacService.class);
		when(kupacService.kupacZaId(1)).thenReturn(new KupacDTO(Long.valueOf(1), "Marko", "Markovic", "mmarkovic@gmail.com"));
		kupovinaService.setKupacService(kupacService);
		
		String brojRacuna = kupovinaService.otvoriRacun(1);
		
		assertTrue(brojRacuna.startsWith("MM"));
		assertNotNull(kupovinaService.getAktivanRacunZaKupca(1));
		
		
	}
	
	
	@Test
	void testkupovinaServiceOtvaranjeRacunaMockGenerator() {
		// generator broja racuna ima nedeterministicko ponasanje, mozemo taj deo da laziramo		
		Prodavnica prodavnica = mock(Prodavnica.class);
		when(prodavnica.noviBrojRacuna("Marko","Markovic")).thenReturn("MM12345");
		KupovinaService kupovinaService = new KupovinaService(prodavnica);	
		ProdavnicaKupacService kupacService = mock(ProdavnicaKupacService.class);
		when(kupacService.kupacZaId(1)).thenReturn(new KupacDTO(Long.valueOf(1), "Marko", "Markovic", "mmarkovic@gmail.com"));
		kupovinaService.setKupacService(kupacService);
		
		String brojRacuna = kupovinaService.otvoriRacun(1);		
		
		assertEquals("MM12345",kupovinaService.getAktivanRacunZaKupca(1));			
	}
	
	@Test
	void testkupovinaServiceZatvaranjeRacuna() {	
		Prodavnica prodavnica = new Prodavnica();	
		KupovinaService kupovinaService = new KupovinaService(prodavnica);
		ProdavnicaKupacService kupacService = mock(ProdavnicaKupacService.class);
		when(kupacService.kupacZaId(1)).thenReturn(new KupacDTO(Long.valueOf(1), "Marko", "Markovic", "mmarkovic@gmail.com"));
		kupovinaService.setKupacService(kupacService);
		String brojRacuna = kupovinaService.otvoriRacun(1);
		
		kupovinaService.zatvoriRacun(brojRacuna);
		
		assertNull(kupovinaService.getAktivanRacunZaKupca(1));
		assertEquals(1,kupovinaService.getZatvoreniRacuni().size());
		assertEquals(brojRacuna,kupovinaService.getZatvoreniRacuni().get(0).getRacunId());			
	}
	
	// za vezbu - da studenti sami rade
	
	@Test
	void testkupovinaServiceZatvaranjeRacunaNeispravno() {	
		Prodavnica prodavnica = new Prodavnica();	
		KupovinaService kupovinaService = new KupovinaService(prodavnica);		
		
		assertThrows(NedozvoljenaOperacijaNadRacunomException.class,() -> kupovinaService.zatvoriRacun("MM1234"));
		
					
	}
	
	
	


}
