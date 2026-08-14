/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.dto;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author PC
 */
public class EvidencijaTestiranjaDto {
    
    private Long idTestiranja;
    private LocalDate datum;
    private int brojTestova;
    private int brojPolozenih;
    private int brojPalih;
    private Boolean prosaoTestiranje;
    private double rezultatTestiranja;
    private Long trenerId;
    private Long sportistaId;
    private List<StavkaTestiranjaDto> stavke;

    public EvidencijaTestiranjaDto() {
    }

    public EvidencijaTestiranjaDto(LocalDate datum, int brojTestova, int brojPolozenih, int brojPalih, Boolean prosaoTestiranje, double rezultatTestiranja, Long trenerId, Long sportistaId, List<StavkaTestiranjaDto> stavke) {
        this.datum = datum;
        this.brojTestova = brojTestova;
        this.brojPolozenih = brojPolozenih;
        this.brojPalih = brojPalih;
        this.prosaoTestiranje = prosaoTestiranje;
        this.rezultatTestiranja = rezultatTestiranja;
        this.trenerId = trenerId;
        this.sportistaId = sportistaId;
        this.stavke = stavke;
    }

    
    
    public EvidencijaTestiranjaDto(Long idTestiranja, LocalDate datum, int brojTestova, int brojPolozenih, int brojPalih, boolean prosaoTestiranje, double rezultatTestiranja, Long trenerId, Long sportistaId, List<StavkaTestiranjaDto> stavke) {
        this.idTestiranja = idTestiranja;
        this.datum = datum;
        this.brojTestova = brojTestova;
        this.brojPolozenih = brojPolozenih;
        this.brojPalih = brojPalih;
        this.prosaoTestiranje = prosaoTestiranje;
        this.rezultatTestiranja = rezultatTestiranja;
        this.trenerId = trenerId;
        this.sportistaId = sportistaId;
        this.stavke = stavke;
    }

    public EvidencijaTestiranjaDto(LocalDate datum, Long trenerId, Long sportistaId, List<StavkaTestiranjaDto> stavke) {
        this.datum = datum;
        this.trenerId = trenerId;
        this.sportistaId = sportistaId;
        this.stavke = stavke;
    }
    
    

    public Long getIdTestiranja() {
        return idTestiranja;
    }

    public void setIdTestiranja(Long idTestiranja) {
        this.idTestiranja = idTestiranja;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public int getBrojTestova() {
        return brojTestova;
    }

    public void setBrojTestova(int brojTestova) {
        this.brojTestova = brojTestova;
    }

    public int getBrojPolozenih() {
        return brojPolozenih;
    }

    public void setBrojPolozenih(int brojPolozenih) {
        this.brojPolozenih = brojPolozenih;
    }

    public int getBrojPalih() {
        return brojPalih;
    }

    public void setBrojPalih(int brojPalih) {
        this.brojPalih = brojPalih;
    }

    public Boolean isProsaoTestiranje() {
        return prosaoTestiranje;
    }

    public void setProsaoTestiranje(Boolean prosaoTestiranje) {
        this.prosaoTestiranje = prosaoTestiranje;
    }

    public double getRezultatTestiranja() {
        return rezultatTestiranja;
    }

    public void setRezultatTestiranja(double rezultatTestiranja) {
        this.rezultatTestiranja = rezultatTestiranja;
    }

    public Long getTrenerId() {
        return trenerId;
    }

    public void setTrenerId(Long trenerId) {
        this.trenerId = trenerId;
    }

    public Long getSportistaId() {
        return sportistaId;
    }

    public void setSportistaId(Long sportistaId) {
        this.sportistaId = sportistaId;
    }

    public List<StavkaTestiranjaDto> getStavke() {
        return stavke;
    }

    public void setStavke(List<StavkaTestiranjaDto> stavke) {
        this.stavke = stavke;
    }
    
    
}
