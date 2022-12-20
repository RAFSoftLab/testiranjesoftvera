package lojalnost.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TipKupca {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;		
	private String tip;  // srebrni, zlatni
	private int popust;
	private int minBrojKupovina;
	
	public TipKupca() {
		
	}
	
	public TipKupca(String tip, int popust, int minBrojKupovina) {
		super();
		this.tip = tip;
		this.popust = popust;
		this.minBrojKupovina = minBrojKupovina;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public int getPopust() {
		return popust;
	}
	public void setPopust(int popust) {
		this.popust = popust;
	}
	public int getMinBrojKupovina() {
		return minBrojKupovina;
	}
	public void setMinBrojKupovina(int minBrojKupovina) {
		this.minBrojKupovina = minBrojKupovina;
	}
	
	

}
