package kupac.model;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
public class Rezervacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne
    private Kupac kupac;

    private String nazivProizvoda;

    private int kolicina;





    public Rezervacija() {
    }

    public Rezervacija(Kupac kupac, String nazivProizvoda, int kolicina) {
        this.kupac = kupac;
        this.nazivProizvoda = nazivProizvoda;
        this.kolicina = kolicina;


    }

    public String getNazivProizvoda() {
        return nazivProizvoda;
    }

    public void setNazivProizvoda(String nazivProizvoda) {
        this.nazivProizvoda = nazivProizvoda;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }


    public void setKupac(Kupac kupac) {
        this.kupac = kupac;
    }

    public Kupac getKupac() {
        return kupac;
    }

    public Long getId() {
        return id;
    }


}