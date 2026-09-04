/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.dto;

import app.domain.Pol;
import app.domain.StarosnaKategorija;
import jakarta.validation.constraints.Min;

/**
 *
 * @author PC
 */
public class NormaDto {
    private Long idNorme;
    @Min(value = 0, message = "Norma mora biti veca ili jednaka nuli.")
    private double norma;
    private Pol pol;
    private StarosnaKategorija starosnaKategorija;
    private Long idVezbe;

    public NormaDto() {
    }

    public NormaDto(Long idNorme, double norma, Pol pol, StarosnaKategorija starosnaKategorija, Long idVezbe) {
        this.idNorme = idNorme;
        this.norma = norma;
        this.pol = pol;
        this.starosnaKategorija = starosnaKategorija;
        this.idVezbe = idVezbe;
    }

    public Long getIdVezbe() {
        return idVezbe;
    }

    public void setIdVezbe(Long idVezbe) {
        this.idVezbe = idVezbe;
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
    
    
}
