/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.converter.impl;

import app.converter.Converter;
import app.domain.StavkaTestiranja;
import app.domain.StavkaTestiranjaId;
import app.domain.Vezba;
import app.dto.StavkaTestiranjaDto;
import org.springframework.stereotype.Component;

/**
 *
 * @author PC
 */
@Component
public class StavkaTestiranjaConverter implements Converter<StavkaTestiranja, StavkaTestiranjaDto> {

    @Override
    public StavkaTestiranja toEntity(StavkaTestiranjaDto dto) {
        if(dto == null)
            return null;
        StavkaTestiranja entity = new StavkaTestiranja();
        
        entity.setOstvareniRezultat(dto.getOstvareniRezultat());
        entity.setProsaoTest(dto.isProsaoTest());
        entity.setKomentar(dto.getKomentar());
        if(dto.getVezbaId() != null){
            Vezba vezba = new Vezba();
            vezba.setIdVezbe(dto.getVezbaId());
            entity.setVezba(vezba);
        }
        
        return entity;
    }

    @Override
    public StavkaTestiranjaDto toDto(StavkaTestiranja entity) {
        
        if(entity == null)
            return null;
        
        StavkaTestiranjaDto dto = new StavkaTestiranjaDto();
        
        if(entity.getId() != null)
            dto.setRb(entity.getId().getRb());
        
        dto.setOstvareniRezultat(entity.getOstvareniRezultat());
        dto.setProsaoTest(entity.isProsaoTest());
        dto.setKomentar(entity.getKomentar());
        
        if(entity.getVezba() != null){
            dto.setVezbaId(entity.getVezba().getIdVezbe());
        }
        
        return dto;
    }
    
}
