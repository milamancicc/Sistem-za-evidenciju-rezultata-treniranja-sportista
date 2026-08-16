/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.converter.impl;

import app.converter.Converter;

import app.converter.Converter;
import app.domain.Specijalizacija;
import app.dto.SpecijalizacijaDto;
import org.springframework.stereotype.Component;

/**
 *
 * @author PC
 */
@Component
public class SpecijalizacijaConverter implements Converter<Specijalizacija, SpecijalizacijaDto> {

    @Override
    public Specijalizacija toEntity(SpecijalizacijaDto dto) {

        if(dto == null)
            return null;
        Specijalizacija entity = new Specijalizacija();
        if(dto.getIdSpecijalizacije() != null)
            entity.setIdSpecijalizacije(dto.getIdSpecijalizacije());
        entity.setNaziv(dto.getNaziv());
        entity.setOpis(dto.getOpis());
        return entity;
    }

    @Override
    public SpecijalizacijaDto toDto(Specijalizacija entity) {
        
        if(entity == null)
            return null;
        return new SpecijalizacijaDto(entity.getIdSpecijalizacije(), entity.getNaziv(), entity.getOpis());
        
    }
    
}
