/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.dto;

/**
 *
 * @author PC
 */
public class SpecijalistickiPodaciDto {
    
    private Long idSpecijalizacije;
    private Long idTrenera;
    private Integer godinaPostizanja;
    private String nazivSpecijalizacije;

    public SpecijalistickiPodaciDto() {
    }

    public SpecijalistickiPodaciDto(Long idSpecijalizacije, Long idTrenera, Integer godinaPostizanja, String nazivSpecijalizacije) {
        this.idSpecijalizacije = idSpecijalizacije;
        this.idTrenera = idTrenera;
        this.godinaPostizanja = godinaPostizanja;
        this.nazivSpecijalizacije = nazivSpecijalizacije;
    }

    public Long getIdSpecijalizacije() {
        return idSpecijalizacije;
    }

    public void setIdSpecijalizacije(Long idSpecijalizacije) {
        this.idSpecijalizacije = idSpecijalizacije;
    }

    public Long getIdTrenera() {
        return idTrenera;
    }

    public void setIdTrenera(Long idTrenera) {
        this.idTrenera = idTrenera;
    }

    public Integer getGodinaPostizanja() {
        return godinaPostizanja;
    }

    public void setGodinaPostizanja(Integer godinaPostizanja) {
        this.godinaPostizanja = godinaPostizanja;
    }

    public String getNazivSpecijalizacije() {
        return nazivSpecijalizacije;
    }

    public void setNazivSpecijalizacije(String nazivSpecijalizacije) {
        this.nazivSpecijalizacije = nazivSpecijalizacije;
    }
    
    
    
}
