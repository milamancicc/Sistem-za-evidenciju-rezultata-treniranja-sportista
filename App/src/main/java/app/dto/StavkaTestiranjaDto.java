/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.dto;

import jakarta.validation.constraints.Min;

/**
 *
 * @author PC
 */
public class StavkaTestiranjaDto {
    
    private int rb;
    @Min(value = 0, message = "Ostvareni rezultat mora biti veci ili jednak nuli.")
    private double ostvareniRezultat;
    private boolean prosaoTest;
    private String komentar;
    private Long vezbaId;
    private String vezbaNaziv;
    private double norma;

    public double getNorma() {
        return norma;
    }

    public void setNorma(double norma) {
        this.norma = norma;
    }

    public String getVezbaNaziv() {
        return vezbaNaziv;
    }

    public void setVezbaNaziv(String vezbaNaziv) {
        this.vezbaNaziv = vezbaNaziv;
    }

    public StavkaTestiranjaDto() {
    }

    public StavkaTestiranjaDto(int rb, double ostvareniRezultat, boolean prosaoTest, String komentar, Long vezbaId) {
        this.rb = rb;
        this.ostvareniRezultat = ostvareniRezultat;
        this.prosaoTest = prosaoTest;
        this.komentar = komentar;
        this.vezbaId = vezbaId;
    }

    public StavkaTestiranjaDto(double ostvareniRezultat, boolean prosaoTest, String komentar, Long vezbaId) {
        this.ostvareniRezultat = ostvareniRezultat;
        this.prosaoTest = prosaoTest;
        this.komentar = komentar;
        this.vezbaId = vezbaId;
    }
    
    

    public int getRb() {
        return rb;
    }

    public void setRb(int rb) {
        this.rb = rb;
    }

    public double getOstvareniRezultat() {
        return ostvareniRezultat;
    }

    public void setOstvareniRezultat(double ostvareniRezultat) {
        this.ostvareniRezultat = ostvareniRezultat;
    }

    public boolean isProsaoTest() {
        return prosaoTest;
    }

    public void setProsaoTest(boolean prosaoTest) {
        this.prosaoTest = prosaoTest;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public Long getVezbaId() {
        return vezbaId;
    }

    public void setVezbaId(Long vezbaId) {
        this.vezbaId = vezbaId;
    }
       
}
