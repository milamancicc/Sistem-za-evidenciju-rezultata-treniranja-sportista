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
import jakarta.persistence.Table;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "Specijalizacija")
public class Specijalizacija {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSpecijalizacije;
    
    @Column(nullable = false)
    private String naziv;
    
    @Column
    private String opis;

    public Specijalizacija() {
    }

    public Specijalizacija(String naziv, String opis) {
        this.naziv = naziv;
        this.opis = opis;
    }

    public Long getIdSpecijalizacije() {
        return idSpecijalizacije;
    }

    public void setIdSpecijalizacije(Long idSpecijalizacije) {
        this.idSpecijalizacije = idSpecijalizacije;
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
