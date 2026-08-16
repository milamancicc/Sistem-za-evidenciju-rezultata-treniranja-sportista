/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.converter.impl;

import app.converter.Converter;
import app.domain.SpecijalistickiPodaci;
import app.domain.Specijalizacija;
import app.domain.Trener;
import app.dto.SpecijalistickiPodaciDto;
import org.springframework.stereotype.Component;

/**
 *
 * @author PC
 */
@Component
public class SpecijalistickiPodaciConverter implements Converter<SpecijalistickiPodaci, SpecijalistickiPodaciDto> {

    @Override
    public SpecijalistickiPodaci toEntity(SpecijalistickiPodaciDto dto) {

        if(dto == null)
            return null;
        SpecijalistickiPodaci entity = new SpecijalistickiPodaci();
        entity.setGodinaPostizanja(dto.getGodinaPostizanja());
        
        if(dto.getIdTrenera() != null){
            Trener trener = new Trener();
            trener.setId(dto.getIdTrenera());
            entity.setTrener(trener);
        }
        
        if(dto.getIdSpecijalizacije() != null){
            Specijalizacija specijalizacija = new Specijalizacija();
            specijalizacija.setIdSpecijalizacije(dto.getIdSpecijalizacije());
            entity.setSpecijalizacija(specijalizacija);
        }
        
        return entity;
        
    }

    @Override
    public SpecijalistickiPodaciDto toDto(SpecijalistickiPodaci entity) {

        if(entity == null)
            return null;
        
        SpecijalistickiPodaciDto dto = new SpecijalistickiPodaciDto();
        dto.setGodinaPostizanja(entity.getGodinaPostizanja());
        if(entity.getTrener() != null)
            dto.setIdTrenera(entity.getTrener().getId());
        if(entity.getSpecijalizacija() != null){
            dto.setIdSpecijalizacije(entity.getSpecijalizacija().getIdSpecijalizacije());
            dto.setNazivSpecijalizacije(entity.getSpecijalizacija().getNaziv());
        }
        
        return dto;
        
    }
    
}
