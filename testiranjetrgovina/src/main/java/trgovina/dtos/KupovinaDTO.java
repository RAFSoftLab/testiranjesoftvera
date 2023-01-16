package trgovina.dtos;

import java.time.LocalDate;

public class KupovinaDTO {
	
	private Long idKupovine;
	private LocalDate datum;
	private String racunId;
	private boolean placen;	
	private double uplacenIznos;
	private Long kupacId;

	public KupovinaDTO() {
		
	}
	
	public KupovinaDTO(LocalDate datum, String racunId, boolean placen, double uplacenIznos, Long kupacId, Long idKupovine) {
		super();
		this.datum = datum;
		this.racunId = racunId;
		this.placen = placen;		
		this.kupacId = kupacId;
	}
	
	
	
	
	public KupovinaDTO(LocalDate datum, String racunId, boolean placen, double uplacenIznos, Long kupacId) {
		super();
		this.datum = datum;
		this.racunId = racunId;
		this.placen = placen;
		this.uplacenIznos = uplacenIznos;
		this.kupacId = kupacId;
	}




	public LocalDate getDatum() {
		return datum;
	}
	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}
	public String getRacunId() {
		return racunId;
	}
	public void setRacunId(String racunId) {
		this.racunId = racunId;
	}
	public boolean isPlacen() {
		return placen;
	}
	public void setPlacen(boolean placen) {
		this.placen = placen;
	}
	
	public Long getKupacId() {
		return kupacId;
	}
	public void setKupacId(Long kupacId) {
		this.kupacId = kupacId;
	}


	public Long getIdKupovine() {
		return idKupovine;
	}

	public void setIdKupovine(Long idKupovine) {
		this.idKupovine = idKupovine;
	}


	public double getUplacenIznos() {
		return uplacenIznos;
	}


	public void setUplacenIznos(double uplacenIznos) {
		this.uplacenIznos = uplacenIznos;
	}
	
	
	

}
