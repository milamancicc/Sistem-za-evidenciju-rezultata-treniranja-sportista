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
@Table(name = "Mesto")
public class Mesto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesta;
    
    @Column(nullable = false)
    private String naziv;

    
    
    public Mesto() {
    }

    public Mesto(String naziv) {
        this.naziv = naziv;
    }

    public Long getIdMesta() {
        return idMesta;
    }

    public void setIdMesta(Long idMesta) {
        this.idMesta = idMesta;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
    
    
}
