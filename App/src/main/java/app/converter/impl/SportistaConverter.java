/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.converter.impl;

import app.converter.Converter;
import app.domain.Klub;
import app.domain.Mesto;
import app.domain.Sportista;
import app.domain.StarosnaKategorija;
import app.domain.TipKorisnika;
import app.dto.SportistaDto;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Component;

/**
 *
 * @author PC
 */
@Component
public class SportistaConverter implements Converter<Sportista, SportistaDto> {

    @Override
    public Sportista toEntity(SportistaDto dto) {

        if(dto == null)
            return null;
        
        Sportista entity = new Sportista();
        
        if(dto.getId() != null)
            entity.setId(dto.getId());
        entity.setKorisnickoIme(dto.getKorisnickoIme());
        entity.setSifra(dto.getSifra());
        entity.setIme(dto.getIme());
        entity.setPrezime(dto.getPrezime());
        entity.setEmail(dto.getEmail());
        entity.setKontakt(dto.getKontakt());
        entity.setTipKorisnika(dto.getTipKorisnika());
        entity.setDatumRodjenja(dto.getDatumRodjenja());
        entity.setVisina(dto.getVisina());
        entity.setTezina(dto.getTezina());
        entity.setPol(dto.getPol());
        entity.setStarosnaKategorija(dto.getStarosnaKategorija());
        
        if(dto.getIdMestoPorekla() != null){
            Mesto m = new Mesto();
            m.setIdMesta(dto.getIdMestoPorekla());
            entity.setMestoPorekla(m);
        }
        
        if(dto.getIdKluba() != null){
            Klub k = new Klub();
            k.setIdKluba(dto.getIdKluba());
            entity.setKlub(k);
        }
        
        return entity;
    }

    @Override
    public SportistaDto toDto(Sportista entity) {
        if(entity == null)
            return null;
        
        SportistaDto dto = new SportistaDto();
        
        dto.setId(entity.getId());
        dto.setKorisnickoIme(entity.getKorisnickoIme());
        dto.setSifra(entity.getSifra());
        dto.setIme(entity.getIme());
        dto.setPrezime(entity.getPrezime());
        dto.setEmail(entity.getEmail());
        dto.setKontakt(entity.getKontakt());
        dto.setTipKorisnika(entity.getTipKorisnika());
        dto.setDatumRodjenja(entity.getDatumRodjenja());
        dto.setVisina(entity.getVisina());
        dto.setTezina(entity.getTezina());
        dto.setPol(entity.getPol());
        dto.setStarosnaKategorija(entity.getStarosnaKategorija());
        
        if(entity.getMestoPorekla() != null)
            dto.setIdMestoPorekla(entity.getMestoPorekla().getIdMesta());
        
        if(entity.getKlub() != null){
            dto.setIdKluba(entity.getKlub().getIdKluba());
        }else{
            entity.setKlub(null);
        }
        
        return dto;
        
    }
    
}
