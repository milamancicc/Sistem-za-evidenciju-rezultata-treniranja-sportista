/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.converter.impl;

import app.converter.Converter;
import app.domain.EvidencijaTestiranja;
import app.domain.SpecijalistickiPodaci;
import app.domain.Trener;
import app.dto.EvidencijaTestiranjaDto;
import app.dto.SpecijalistickiPodaciDto;
import app.dto.TrenerDto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PC
 */
public class TrenerConverter implements Converter<Trener, TrenerDto> {

    private final SpecijalistickiPodaciConverter specijalistickiPodaciConverter;
    private final EvidencijaTestiranjaConverter evidencijaTestiranjaConverter;

    public TrenerConverter(SpecijalistickiPodaciConverter specijalistickiPodaciConverter, EvidencijaTestiranjaConverter evidencijaTestiranjaConverter) {
        this.specijalistickiPodaciConverter = specijalistickiPodaciConverter;
        this.evidencijaTestiranjaConverter = evidencijaTestiranjaConverter;
    }
    
        
    @Override
    public Trener toEntity(TrenerDto dto) {

        if(dto == null)
            return null;
        
        Trener t = new Trener();
        if(dto.getIdKorisnika() != null)
            t.setId(dto.getIdKorisnika());
        t.setKorisnickoIme(dto.getKorisnickoIme());
        t.setIme(dto.getIme());
        t.setPrezime(dto.getPrezime());
        t.setEmail(dto.getEmail());
        t.setKontakt(dto.getKontakt());
        t.setTipKorisnika(dto.getTipKorisnika());
        
        if(dto.getSpecijalistickiPodaci() != null){
            List<SpecijalistickiPodaci> specijalistickiPodaci = new ArrayList();
            for(SpecijalistickiPodaciDto spdto: dto.getSpecijalistickiPodaci()){
                specijalistickiPodaci.add(specijalistickiPodaciConverter.toEntity(spdto));
            }
            t.setSpecijalistickiPodaci(specijalistickiPodaci);
        }
        
        if(dto.getEvidencije() != null){
            List<EvidencijaTestiranja> evidencije = new ArrayList<>();
            for(EvidencijaTestiranjaDto edto: dto.getEvidencije()){
                evidencije.add(evidencijaTestiranjaConverter.toEntity(edto));
            }
            t.setEvidencije(evidencije);
        }
        
        return t;
    }

    @Override
    public TrenerDto toDto(Trener entity) {
        
        if(entity == null)
            return null;
        
        TrenerDto dto = new TrenerDto();
        dto.setIdKorisnika(entity.getId());
        dto.setSifra(entity.getSifra());
        dto.setIme(entity.getIme());
        dto.setPrezime(entity.getPrezime());
        dto.setEmail(entity.getEmail());
        dto.setKontakt(entity.getKontakt());
        dto.setTipKorisnika(entity.getTipKorisnika());
        
        if(entity.getSpecijalistickiPodaci() != null){
            List<SpecijalistickiPodaciDto> specijalistickiPodaci = new ArrayList<>();
            for(SpecijalistickiPodaci sp: entity.getSpecijalistickiPodaci()){
                specijalistickiPodaci.add(specijalistickiPodaciConverter.toDto(sp));
            }
            dto.setSpecijalistickiPodaci(specijalistickiPodaci);
        }
        
        if(entity.getEvidencije() != null){
            List<EvidencijaTestiranjaDto> evidencijeTestiranja = new ArrayList<>();
            for(EvidencijaTestiranja et: entity.getEvidencije()){
                evidencijeTestiranja.add(evidencijaTestiranjaConverter.toDto(et));
            }
            dto.setEvidencije(evidencijeTestiranja);
        }
        
        return dto;
        
    }
    
}
