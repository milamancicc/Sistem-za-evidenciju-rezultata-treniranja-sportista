/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.converter.impl;

import app.converter.Converter;
import app.domain.Vezba;
import app.dto.VezbaDto;
import org.springframework.stereotype.Component;

/**
 *
 * @author PC
 */
@Component
public class VezbaConverter implements Converter<Vezba, VezbaDto> {

    @Override
    public Vezba toEntity(VezbaDto dto) {
        if(dto == null)
            return null;
        Vezba entity = new Vezba();
        if(dto.getIdVezbe() != null)
            entity.setIdVezbe(dto.getIdVezbe());
        entity.setNaziv(dto.getNaziv());
        if(dto.getOpis() != null)
            entity.setOpis(dto.getOpis());
        entity.setJedinicaMere(dto.getJedinicaMere());
        return entity;
   }

    @Override
    public VezbaDto toDto(Vezba entity) {
        if(entity == null)
            return null;
        VezbaDto dto = new VezbaDto();
        dto.setIdVezbe(entity.getIdVezbe());
        dto.setNaziv(entity.getNaziv());
        if(entity.getOpis() != null)
            dto.setOpis(entity.getOpis());
        dto.setJedinicaMere(entity.getJedinicaMere());
        return dto;
    }
    
}
