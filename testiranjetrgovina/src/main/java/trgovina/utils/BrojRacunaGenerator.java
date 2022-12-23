package trgovina.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import trgovina.dtos.KupacDTO;
import trgovina.model.Kupac;


public class BrojRacunaGenerator {
	
	
	private static BrojRacunaGenerator instance;
	
	private BrojRacunaGenerator() {
		generisaniBrojevi = new ArrayList<>();
	}
	
	public static BrojRacunaGenerator getInstance() {
		if(instance==null) {
			instance = new BrojRacunaGenerator();
		}
		return instance;
	}
	
	private List<String> generisaniBrojevi;
	
	public String generisiBroj(String ime, String prezime, String nazivProdavnice) {
		Random rand = new Random();	
		String racunId = "";
		while(true) {		   
		   int slucajanBroj = rand.nextInt(100000)+1000000;
		   racunId = ime.substring(0,1) + prezime.substring(0,1)+nazivProdavnice.substring(0,3)+slucajanBroj;
		   racunId = racunId.toUpperCase();
		   if(!generisaniBrojevi.contains(racunId)) {
			   generisaniBrojevi.add(racunId);
			   break;			   
		   }
		}
		return racunId;		
	}
	
	
	
	
	

}
