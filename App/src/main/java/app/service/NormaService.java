/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.converter.impl.NormaConverter;
import app.domain.Norma;
import app.domain.Vezba;
import app.dto.NormaDto;
import app.repository.NormaRepository;
import app.repository.VezbaRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service("norma-service")
public class NormaService {
    private final NormaRepository normaRepository;
    private final NormaConverter normaConverter;
    private final VezbaRepository vezbaRepository;

    public NormaService(@Qualifier("norma-repository")NormaRepository normaRepository,@Qualifier("norma-converter") NormaConverter normaConverter, @Qualifier("vezba-repository") VezbaRepository vezbaRepository) {
        this.normaRepository = normaRepository;
        this.normaConverter = normaConverter;
        this.vezbaRepository = vezbaRepository;
    }
    
    public NormaDto dodajNormu(NormaDto dto){
        try{
            Vezba vezba = vezbaRepository.nadjiPoId(dto.getIdVezbe());
            Norma entity = normaConverter.toEntity(dto);
            entity.setVezba(vezba);
            Norma sacuvana = normaRepository.sacuvaj(entity);
            return normaConverter.toDto(sacuvana);
        }catch(IllegalArgumentException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException("Sistem ne moze da sacuva normu: " + e.getMessage());
        }
    }
    
    public void obrisiNormu(Long id){
        try{
            normaRepository.obrisi(id);
        }catch(IllegalArgumentException e){
            throw e;
        }catch(Exception e){
            throw e;
        }
    }
    
    
    public List<NormaDto> izlistajSveNormeZaVezbu(Long idVezbe){
        
        List<Norma> entities = normaRepository.izlistajPoVezbi(idVezbe);
        if(entities == null)
            return null;
        List<NormaDto> dtos = new ArrayList<>();
        for(Norma n: entities){
            NormaDto dto = normaConverter.toDto(n);
            dto.setIdNorme(n.getIdNorme());
            dtos.add(dto);
        }
        return dtos;
        
    }
    
}
