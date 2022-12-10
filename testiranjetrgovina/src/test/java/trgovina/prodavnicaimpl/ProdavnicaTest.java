package trgovina.prodavnicaimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import trgovina.repositories.ProizvodRepository;



class ProdavnicaTest {

	// primer sa predavanja
	
	@Test
	void testUcitavanjeProizvoda() {
		// primer stub		
		ProizvodRepository proizvodRepository = mock(ProizvodRepository.class);
		when(proizvodRepository.vratiSveRazliciteProizvode()).thenReturn(List.of("sampon","sapun"));
		when(proizvodRepository.vratiStanjeZaProizvod("sampon")).thenReturn(30);
		when(proizvodRepository.vratiStanjeZaProizvod("sapun")).thenReturn(20);
		Prodavnica p = new Prodavnica();
		p.setProizvodRepo(proizvodRepository);
		p.ucitajProizvode();		
		
		int ukupanBroj = p.ukupnoProizvoda();
		
		assertEquals(50, ukupanBroj);		
	}
	
	

}
