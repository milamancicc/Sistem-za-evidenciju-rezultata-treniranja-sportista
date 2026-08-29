/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "StavkaTestiranja")
public class StavkaTestiranja {
    
    @EmbeddedId
    private StavkaTestiranjaId id = new StavkaTestiranjaId();
    
    @ManyToOne
    @MapsId("evidencijaId")
    @JoinColumn(name = "idTestiranja")
    private EvidencijaTestiranja evidencijaTestiranja;
    
    @Column
    @Min(value = 0, message = "Ostvareni rezultat ne sme biti manji od nule")
    private double ostvareniRezultat;
    
    @Column
    private boolean prosaoTest;
    
    @Column
    private String komentar;
    
    @ManyToOne
    @JoinColumn(name = "idVezbe", nullable = false)
    private Vezba vezba;

    public StavkaTestiranja() {
    }

    public StavkaTestiranja(EvidencijaTestiranja evidencijaTestiranja, double ostvareniRezultat, boolean prosaoTest, String komentar, Vezba vezba) {
        this.evidencijaTestiranja = evidencijaTestiranja;
        this.ostvareniRezultat = ostvareniRezultat;
        this.prosaoTest = prosaoTest;
        this.komentar = komentar;
        this.vezba = vezba;
    }

    public StavkaTestiranjaId getId() {
        return id;
    }

    public void setId(StavkaTestiranjaId id) {
        this.id = id;
    }

    public EvidencijaTestiranja getEvidencijaTestiranja() {
        return evidencijaTestiranja;
    }

    public void setEvidencijaTestiranja(EvidencijaTestiranja evidencijaTestiranja) {
        this.evidencijaTestiranja = evidencijaTestiranja;
    }

    public double getOstvareniRezultat() {
        return ostvareniRezultat;
    }

    public void setOstvareniRezultat(double ostvareniRezultat) {
        this.ostvareniRezultat = ostvareniRezultat;
    }

    public boolean isProsaoTest() {
        return prosaoTest;
    }

    public void setProsaoTest(boolean prosaoTest) {
        this.prosaoTest = prosaoTest;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public Vezba getVezba() {
        return vezba;
    }

    public void setVezba(Vezba vezba) {
        this.vezba = vezba;
    }
    
    
            
}

