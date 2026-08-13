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
import jakarta.persistence.Table;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "Vezba")
public class Vezba {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVezbe;
    
    @Column(nullable = false)
    private String naziv;
    
    @Column
    private String opis;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JedinicaMere jedinicaMere;

    public Vezba() {
    }

    public Vezba(String naziv, String opis, JedinicaMere jedinicaMere) {
        this.naziv = naziv;
        this.opis = opis;
        this.jedinicaMere = jedinicaMere;
    }

    public Long getIdVezbe() {
        return idVezbe;
    }

    public void setIdVezbe(Long idVezbe) {
        this.idVezbe = idVezbe;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public JedinicaMere getJedinicaMere() {
        return jedinicaMere;
    }

    public void setJedinicaMere(JedinicaMere jedinicaMere) {
        this.jedinicaMere = jedinicaMere;
    }
    
    
        
}
