/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "Klub")
public class Klub {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKluba;
    
    @Column(nullable = false)
    private String naziv;
    
    @Column(unique = true, nullable = false)
    private String pib;
    
    @Column
    private String email;
    
    @Column
    private String kontakt;
    
    @ManyToOne
    @JoinColumn(name = "idMesta", nullable = false)
    private Mesto mesto;

    
    
    public Klub() {
    }

    public Klub(String naziv, String pib, String email, String kontakt, Mesto mesto) {
        this.naziv = naziv;
        this.pib = pib;
        this.email = email;
        this.kontakt = kontakt;
        this.mesto = mesto;
    }

    public Long getIdKluba() {
        return idKluba;
    }

    public void setIdKluba(Long idKluba) {
        this.idKluba = idKluba;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getPib() {
        return pib;
    }

    public void setPib(String pib) {
        this.pib = pib;
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

    public Mesto getMesto() {
        return mesto;
    }

    public void setMesto(Mesto mesto) {
        this.mesto = mesto;
    }
 
}
