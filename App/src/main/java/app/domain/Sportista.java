/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "Sportista")
@PrimaryKeyJoinColumn(name = "id")
public class Sportista extends Korisnik{
    
    @Column(nullable = false)
    private LocalDate datumRodjenja;
    
    @Column
    private double visina;
    
    @Column
    private double tezina;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Pol pol;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StarosnaKategorija starosnaKategorija;
    
    @ManyToOne
    @JoinColumn(name = "idMestaPorekla", nullable = false)
    private Mesto mestoPorekla;
    
    @ManyToOne
    @JoinColumn(name = "idKluba")
    private Klub klub;

    public Sportista() {
    }

    public Sportista(LocalDate datumRodjenja, double visina, double tezina, Pol pol, StarosnaKategorija starosnaKategorija, Mesto mestoPorekla, Klub klub) {
        this.datumRodjenja = datumRodjenja;
        this.visina = visina;
        this.tezina = tezina;
        this.pol = pol;
        this.starosnaKategorija = starosnaKategorija;
        this.mestoPorekla = mestoPorekla;
        this.klub = klub;
    }

    public Sportista(LocalDate datumRodjenja, double visina, double tezina, Pol pol, StarosnaKategorija starosnaKategorija, Mesto mestoPorekla, Klub klub, String korisnickoIme, String sifra, String ime, String prezime, String email, String kontakt, TipKorisnika tipKorisnika) {
        super(korisnickoIme, sifra, ime, prezime, email, kontakt, tipKorisnika);
        this.datumRodjenja = datumRodjenja;
        this.visina = visina;
        this.tezina = tezina;
        this.pol = pol;
        this.starosnaKategorija = starosnaKategorija;
        this.mestoPorekla = mestoPorekla;
        this.klub = klub;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public double getVisina() {
        return visina;
    }

    public void setVisina(double visina) {
        this.visina = visina;
    }

    public double getTezina() {
        return tezina;
    }

    public void setTezina(double tezina) {
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

    public Mesto getMestoPorekla() {
        return mestoPorekla;
    }

    public void setMestoPorekla(Mesto mestoPorekla) {
        this.mestoPorekla = mestoPorekla;
    }

    public Klub getKlub() {
        return klub;
    }

    public void setKlub(Klub klub) {
        this.klub = klub;
    }
    
    
}
