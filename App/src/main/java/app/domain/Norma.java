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
import jakarta.persistence.Table;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "Norma")
public class Norma {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNorme;
    
    @Column
    private double norma;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Pol pol;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StarosnaKategorija starosnaKategorija;
    
    @ManyToOne
    @JoinColumn(name = "idVezbe", nullable = false)
    private Vezba vezba;

    public Norma() {
    }

    public Norma(double norma, Pol pol, StarosnaKategorija starosnaKategorija, Vezba vezba) {
        this.norma = norma;
        this.pol = pol;
        this.starosnaKategorija = starosnaKategorija;
        this.vezba = vezba;
    }

    public Long getIdNorme() {
        return idNorme;
    }

    public void setIdNorme(Long idNorme) {
        this.idNorme = idNorme;
    }

    public double getNorma() {
        return norma;
    }

    public void setNorma(double norma) {
        this.norma = norma;
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

    public Vezba getVezba() {
        return vezba;
    }

    public void setVezba(Vezba vezba) {
        this.vezba = vezba;
    }
    
}
