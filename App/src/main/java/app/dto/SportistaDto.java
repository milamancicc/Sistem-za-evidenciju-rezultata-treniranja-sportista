/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.dto;

import app.domain.Pol;
import app.domain.StarosnaKategorija;
import app.domain.TipKorisnika;
import java.time.LocalDate;

/**
 *
 * @author PC
 */
public class SportistaDto {
    
    private Long id;
    private String korisnickoIme;
    private String sifra;
    private String ime;
    private String prezime;
    private String email;
    private String kontakt;
    private TipKorisnika tipKorisnika;
    private LocalDate datumRodjenja;
    private Double visina;
    private Double tezina;
    private Pol pol;
    private StarosnaKategorija starosnaKategorija;
    private Long idMestoPorekla;
    private Long idKluba;

    public SportistaDto() {
    }

    public SportistaDto(String korisnickoIme, String sifra, String ime, String prezime, String email, String kontakt, TipKorisnika tipKorisnika, LocalDate datumRodjenja, Double visina, Double tezina, Pol pol, StarosnaKategorija starosnaKategorija, Long idMestoPorekla, Long idKluba) {
        this.korisnickoIme = korisnickoIme;
        this.sifra = sifra;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.kontakt = kontakt;
        this.tipKorisnika = tipKorisnika;
        this.datumRodjenja = datumRodjenja;
        this.visina = visina;
        this.tezina = tezina;
        this.pol = pol;
        this.starosnaKategorija = starosnaKategorija;
        this.idMestoPorekla = idMestoPorekla;
        this.idKluba = idKluba;
    }

    public SportistaDto(String korisnickoIme, String sifra, String ime, String prezime, String email, String kontakt, LocalDate datumRodjenja, Double visina, Double tezina, Pol pol, Long idMestoPorekla, Long idKluba) {
        this.korisnickoIme = korisnickoIme;
        this.sifra = sifra;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.kontakt = kontakt;
        this.datumRodjenja = datumRodjenja;
        this.visina = visina;
        this.tezina = tezina;
        this.pol = pol;
        this.idMestoPorekla = idMestoPorekla;
        this.idKluba = idKluba;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public Double getVisina() {
        return visina;
    }

    public void setVisina(Double visina) {
        this.visina = visina;
    }

    public Double getTezina() {
        return tezina;
    }

    public void setTezina(Double tezina) {
        this.tezina = tezina;
    }

    public Pol getPol() {
        return pol;
    }

    public void setPol(Pol pol) {
        this.pol = pol;
    }

    public StarosnaKategorija getStarosnaKategorija() {
        return starosnaKategorija;
    }

    public void setStarosnaKategorija(StarosnaKategorija starosnaKategorija) {
        this.starosnaKategorija = starosnaKategorija;
    }

    public Long getIdMestoPorekla() {
        return idMestoPorekla;
    }

    public void setIdMestoPorekla(Long idMestoPorekla) {
        this.idMestoPorekla = idMestoPorekla;
    }

    public Long getIdKluba() {
        return idKluba;
    }

    public void setIdKluba(Long idKluba) {
        this.idKluba = idKluba;
    }

    
    
}
