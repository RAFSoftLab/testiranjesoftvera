package trgovina.factories;

import trgovina.model.Kupac;
import trgovina.model.TipKupca;

public class KupacObjectMother {
	

	public static Kupac createKupacBezTipa() {
		Kupac k = new Kupac();
		k.setIme("Petar");
		k.setPrezime("Petrovic");		
		return k;
	}
	
	public static Kupac createGostKupac() {
		Kupac k = new Kupac();
		k.setIme("Petar");
		k.setPrezime("Petrovic");
		k.setTip(TipKupca.GOST);
		return k;
	}
	
	public static Kupac createRegularanKupac() {
		Kupac k = new Kupac();
		k.setIme("Petar");
		k.setPrezime("Petrovic");
		k.setTip(TipKupca.REGULARAN);
		return k;
	}
	
	public static Kupac createPovremenKupac() {
		Kupac k = new Kupac();
		k.setIme("Petar");
		k.setPrezime("Petrovic");
		k.setTip(TipKupca.POVREMEN);
		return k;
	}
	
	

}
