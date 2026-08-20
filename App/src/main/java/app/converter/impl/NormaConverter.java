/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.converter.impl;

import app.converter.Converter;
import app.domain.Norma;
import app.dto.NormaDto;
import org.springframework.stereotype.Component;

/**
 *
 * @author PC
 */
@Component
public class NormaConverter implements Converter<Norma, NormaDto> {

    @Override
    public Norma toEntity(NormaDto dto) {
        if(dto == null)
            return null;
        Norma entity = new Norma();
        if(dto.getIdNorme() != null)
            entity.setIdNorme(dto.getIdNorme());
        entity.setNorma(dto.getNorma());
        entity.setPol(dto.getPol());
        entity.setStarosnaKategorija(dto.getStarosnaKategorija());
        return entity;
    }

    @Override
    public NormaDto toDto(Norma entity) {
        if(entity == null)
            return null;
        Long idVezbe = (entity.getVezba() != null) ? entity.getVezba().getIdVezbe() : null;
        NormaDto dto = new NormaDto();
        if(entity.getIdNorme() == null)
            dto.setIdNorme(entity.getIdNorme());
        dto.setIdVezbe(idVezbe);
        dto.setNorma(entity.getNorma());
        dto.setPol(entity.getPol());
        dto.setStarosnaKategorija(entity.getStarosnaKategorija());
        return dto;
    }
    
}
