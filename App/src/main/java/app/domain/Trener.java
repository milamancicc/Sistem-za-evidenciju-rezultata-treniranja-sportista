/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.List;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "Trener")
@PrimaryKeyJoinColumn(name = "id")
public class Trener extends Korisnik {
    
    @OneToMany(mappedBy = "trener")
    private List<SpecijalistickiPodaci> specijalistickiPodaci;
    
    @OneToMany(mappedBy = "trener")
    private List<EvidencijaTestiranja> evidencije;

    public Trener() {
        setTipKorisnika(TipKorisnika.TRENER);
    }

    public Trener(List<SpecijalistickiPodaci> specijalistickiPodaci, List<EvidencijaTestiranja> evidencije, String korisnickoIme, String sifra, String ime, String prezime, String email, String kontakt, TipKorisnika tipKorisnika) {
        super(korisnickoIme, sifra, ime, prezime, email, kontakt, tipKorisnika);
        this.specijalistickiPodaci = specijalistickiPodaci;
        this.evidencije = evidencije;
    }

    public List<SpecijalistickiPodaci> getSpecijalistickiPodaci() {
        return specijalistickiPodaci;
    }

    public void setSpecijalistickiPodaci(List<SpecijalistickiPodaci> specijalistickiPodaci) {
        this.specijalistickiPodaci = specijalistickiPodaci;
    }

    public List<EvidencijaTestiranja> getEvidencije() {
        return evidencije;
    }

    public void setEvidencije(List<EvidencijaTestiranja> evidencije) {
        this.evidencije = evidencije;
    }
    
    
    
}
