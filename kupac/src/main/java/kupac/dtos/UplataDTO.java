package kupac.dtos;

public class UplataDTO {
	
	private String tekuciRacun;
	private double uplatiti;
	
	public UplataDTO() {
		
	}
	
	public UplataDTO(String tekuciRacun, double uplatiti) {
		super();
		this.tekuciRacun = tekuciRacun;
		this.uplatiti = uplatiti;
	}
	public String getTekuciRacun() {
		return tekuciRacun;
	}
	public void setTekuciRacun(String tekuciRacun) {
		this.tekuciRacun = tekuciRacun;
	}
	public double getUplatiti() {
		return uplatiti;
	}
	public void setUplatiti(double uplatiti) {
		this.uplatiti = uplatiti;
	}
	
	

}
