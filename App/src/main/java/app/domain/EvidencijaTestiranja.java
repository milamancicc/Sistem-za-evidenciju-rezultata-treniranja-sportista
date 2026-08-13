/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "EvidencijaTestiranja")
public class EvidencijaTestiranja {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTestiranja")
    private Long idTestiranja;
    
    @Column 
    private LocalDate datum;
    
    @Column 
    private int brojTestova;
    
    @Column 
    private int brojPolozenih;
    
    @Column
    private int brojPalih;
    
    @Column 
    private boolean prosaoTestiranje;
    
    @Column 
    private double rezultatTestiranja;
    
    @ManyToOne
    @JoinColumn(name = "idTrenera", nullable = false)
    private Trener trener;
    
    @ManyToOne
    @JoinColumn(name = "idSportiste", nullable = false)
    private Sportista sportista;
    
    @OneToMany(mappedBy = "evidencijaTestiranja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StavkaTestiranja> stavke;

    public EvidencijaTestiranja() {
    }

    public EvidencijaTestiranja(Long idTestiranja, LocalDate datum, int brojTestova, int brojPolozenih, int brojPalih, boolean prosaoTestiranje, double rezultatTestiranja, Trener trener, Sportista sportista, List<StavkaTestiranja> stavke) {
        this.idTestiranja = idTestiranja;
        this.datum = datum;
        this.brojTestova = brojTestova;
        this.brojPolozenih = brojPolozenih;
        this.brojPalih = brojPalih;
        this.prosaoTestiranje = prosaoTestiranje;
        this.rezultatTestiranja = rezultatTestiranja;
        this.trener = trener;
        this.sportista = sportista;
        this.stavke = stavke;
    }

    public Long getIdTestiranja() {
        return idTestiranja;
    }

    public void setIdTestiranja(Long idTestiranja) {
        this.idTestiranja = idTestiranja;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public int getBrojTestova() {
        return brojTestova;
    }

    public void setBrojTestova(int brojTestova) {
        this.brojTestova = brojTestova;
    }

    public int getBrojPolozenih() {
        return brojPolozenih;
    }

    public void setBrojPolozenih(int brojPolozenih) {
        this.brojPolozenih = brojPolozenih;
    }

    public int getBrojPalih() {
        return brojPalih;
    }

    public void setBrojPalih(int brojPalih) {
        this.brojPalih = brojPalih;
    }

    public boolean isProsaoTestiranje() {
        return prosaoTestiranje;
    }

    public void setProsaoTestiranje(boolean prosaoTestiranje) {
        this.prosaoTestiranje = prosaoTestiranje;
    }

    public double getRezultatTestiranja() {
        return rezultatTestiranja;
    }

    public void setRezultatTestiranja(double rezultatTestiranja) {
        this.rezultatTestiranja = rezultatTestiranja;
    }

    public Trener getTrener() {
        return trener;
    }

    public void setTrener(Trener trener) {
        this.trener = trener;
    }

    public Sportista getSportista() {
        return sportista;
    }

    public void setSportista(Sportista sportista) {
        this.sportista = sportista;
    }

    public List<StavkaTestiranja> getStavke() {
        return stavke;
    }

    public void setStavke(List<StavkaTestiranja> stavke) {
        this.stavke = stavke;
    }
    
    
}
