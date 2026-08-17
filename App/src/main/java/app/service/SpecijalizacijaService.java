/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.converter.impl.SpecijalizacijaConverter;
import app.domain.Specijalizacija;
import app.dto.SpecijalizacijaDto;
import app.repository.SpecijalizacijaRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service("specijalizacija-service")
public class SpecijalizacijaService {
    
    private final SpecijalizacijaRepository specijalizacijaRepository;
    private final SpecijalizacijaConverter specijalizacijaConverter;

    public SpecijalizacijaService(SpecijalizacijaRepository specijalizacijaRepository, SpecijalizacijaConverter specijalizacijaConverter) {
        this.specijalizacijaRepository = specijalizacijaRepository;
        this.specijalizacijaConverter = specijalizacijaConverter;
    }
    
    public SpecijalizacijaDto sacuvajSpecijalizaciju(SpecijalizacijaDto dto){
        try{
            proveriPodatke(dto);
            Specijalizacija entity = specijalizacijaConverter.toEntity(dto);
            Specijalizacija sacuvana = specijalizacijaRepository.sacuvajSpecijalizaciju(entity);
            return specijalizacijaConverter.toDto(sacuvana);
        }catch(Exception e){
            throw new RuntimeException("Sistem ne moze da zapamti specijalizaciju.");
        }
    }
    
    
    private void proveriPodatke(SpecijalizacijaDto dto){
        if(dto ==null)
            throw new IllegalArgumentException("Podaci o specijalizaciji ne sme biti null.");
        if(dto.getNaziv() == null || dto.getNaziv().isBlank())
            throw new IllegalArgumentException("Naziv specijalizacije je obavezan.");
    }
}
