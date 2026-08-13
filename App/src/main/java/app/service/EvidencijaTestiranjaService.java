/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.converter.impl.EvidencijaTestiranjaConverter;
import app.domain.EvidencijaTestiranja;
import app.dto.EvidencijaTestiranjaDto;
import app.repository.EvidencijaTestiranjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class EvidencijaTestiranjaService {
    
    private final EvidencijaTestiranjaRepository evidencijaTestiranjaRepository;
    private final EvidencijaTestiranjaConverter evidencijaTestiranjaConverter;
    
    @Autowired

    public EvidencijaTestiranjaService(EvidencijaTestiranjaRepository evidencijaTestiranjaRepository, EvidencijaTestiranjaConverter evidencijaTestiranjaConverter) {
        this.evidencijaTestiranjaRepository = evidencijaTestiranjaRepository;
        this.evidencijaTestiranjaConverter = evidencijaTestiranjaConverter;
    }

    public EvidencijaTestiranjaDto sacuvajEvidencijuTestiranja(EvidencijaTestiranjaDto dto){
        EvidencijaTestiranja entity = evidencijaTestiranjaConverter.toEntity(dto);
        EvidencijaTestiranja saved = evidencijaTestiranjaRepository.sacuvajEvidencijuTestiranja(entity);
        
        return evidencijaTestiranjaConverter.toDto(saved);
    }
    
}
