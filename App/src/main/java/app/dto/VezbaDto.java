/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.dto;

import app.domain.JedinicaMere;

/**
 *
 * @author PC
 */
public class VezbaDto {
    private Long idVezbe;
    private String naziv;
    private String opis;
    private JedinicaMere jedinicaMere;

    public VezbaDto() {
    }

    public VezbaDto(Long idVezbe, String naziv, String opis, JedinicaMere jedinicaMere) {
        this.idVezbe = idVezbe;
        this.naziv = naziv;
        this.opis = opis;
        this.jedinicaMere = jedinicaMere;
    }

    public JedinicaMere getJedinicaMere() {
        return jedinicaMere;
    }

    public void setJedinicaMere(JedinicaMere jedinicaMere) {
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
    
    
}
