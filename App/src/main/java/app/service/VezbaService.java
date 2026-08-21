/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.converter.impl.VezbaConverter;
import app.domain.Vezba;
import app.dto.VezbaDto;
import app.repository.VezbaRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service("vezba-service")
public class VezbaService {
    private final VezbaRepository vezbaRepository;
    private final VezbaConverter vezbaConverter;

    public VezbaService(@Qualifier("vezba-repository")VezbaRepository vezbaRepository,
            @Qualifier("vezba-converter") VezbaConverter vezbaConverter) {
        this.vezbaRepository = vezbaRepository;
        this.vezbaConverter = vezbaConverter;
    }
    
    public VezbaDto dodajIliIzmeniVezbu(VezbaDto dto){
        try{
            Vezba entity = vezbaConverter.toEntity(dto);
            return vezbaConverter.toDto(vezbaRepository.sacuvaj(entity));
        }catch(IllegalArgumentException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException("Sistem ne moze da zapamti vezbu: " + e.getMessage());
        }
    }
    
    public void obrisiVezbu(Long id){
        try{
            vezbaRepository.obrisi(id);
        }catch(IllegalArgumentException e){
            throw e;
        }catch(Exception e){
            throw e;
        }
    }
    
    public List<VezbaDto> izlistajSveVezbe(){
        List<Vezba> entities = vezbaRepository.izlistajSve();
        if(entities == null)
            return null;
        List<VezbaDto> dtos = new ArrayList<>();
        for(Vezba v:entities){
            dtos.add(vezbaConverter.toDto(v));
        }
        return dtos;
    }
}
