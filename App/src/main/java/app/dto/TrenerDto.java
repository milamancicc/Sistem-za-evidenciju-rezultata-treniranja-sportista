/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.dto;

import app.domain.TipKorisnika;
import java.util.List;

/**
 *
 * @author PC
 */
public class TrenerDto {
    
    private Long idKorisnika;
    private String ime;
    private String prezime;
    private String korisnickoIme;
    private String sifra;
    private String email;
    private String kontakt;
    private TipKorisnika tipKorisnika;
    
    private List<SpecijalistickiPodaciDto> specijalistickiPodaci;
    private List<EvidencijaTestiranjaDto> evidencije;

    public TrenerDto() {
    }

    public TrenerDto(Long idKorisnika, String ime, String prezime, String korisnickoIme, String sifra, String email, String kontakt, TipKorisnika tipKorisnika, List<SpecijalistickiPodaciDto> specijalistickiPodaci, List<EvidencijaTestiranjaDto> evidencije) {
        this.idKorisnika = idKorisnika;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.sifra = sifra;
        this.email = email;
        this.kontakt = kontakt;
        this.tipKorisnika = tipKorisnika;
        this.specijalistickiPodaci = specijalistickiPodaci;
        this.evidencije = evidencije;
    }

    public TrenerDto(String ime, String prezime, String korisnickoIme, String sifra, String email, String kontakt, TipKorisnika tipKorisnika, List<SpecijalistickiPodaciDto> specijalistickiPodaci, List<EvidencijaTestiranjaDto> evidencije) {
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.sifra = sifra;
        this.email = email;
        this.kontakt = kontakt;
        this.tipKorisnika = tipKorisnika;
        this.specijalistickiPodaci = specijalistickiPodaci;
        this.evidencije = evidencije;
    }
    
    

    public Long getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(Long idKorisnika) {
        this.idKorisnika = idKorisnika;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public TipKorisnika getTipKorisnika() {
        return tipKorisnika;
    }

    public void setTipKorisnika(TipKorisnika tipKorisnika) {
        this.tipKorisnika = tipKorisnika;
    }

    public List<SpecijalistickiPodaciDto> getSpecijalistickiPodaci() {
        return specijalistickiPodaci;
    }

    public void setSpecijalistickiPodaci(List<SpecijalistickiPodaciDto> specijalistickiPodaci) {
        this.specijalistickiPodaci = specijalistickiPodaci;
    }

    public List<EvidencijaTestiranjaDto> getEvidencije() {
        return evidencije;
    }

    public void setEvidencije(List<EvidencijaTestiranjaDto> evidencije) {
        this.evidencije = evidencije;
    }
    
    
    
}
