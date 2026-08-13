/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.converter.impl.EvidencijaTestiranjaConverter;
import app.domain.EvidencijaTestiranja;
import app.domain.StavkaTestiranja;
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
        izracunajStatistiku(entity);
        EvidencijaTestiranja saved = evidencijaTestiranjaRepository.sacuvajEvidencijuTestiranja(entity);
        
        return evidencijaTestiranjaConverter.toDto(saved);
    }
    
    private void izracunajStatistiku(EvidencijaTestiranja entity){
        
        if(entity.getStavke() == null || entity.getStavke().isEmpty()){
            entity.setBrojTestova(0);
            entity.setBrojPolozenih(0);
            entity.setBrojPalih(0);
            entity.setProsaoTestiranje(false);
            entity.setRezultatTestiranja(0);
            return;
        }
        
        int polozeni = 0;
        int pali = 0;
        
        for(StavkaTestiranja st: entity.getStavke()){
            if(st.isProsaoTest()){
                polozeni++;
            }else
                pali++;
        }
        
        double rezultat = ((double) polozeni * 100 ) / entity.getStavke().size();
        rezultat = Math.round(rezultat*100.0)/100.0;
        boolean prosao = rezultat >= 70;
        
        entity.setBrojTestova(entity.getStavke().size());
        entity.setBrojPolozenih(polozeni);
        entity.setBrojPalih(pali);
        entity.setProsaoTestiranje(prosao);
        entity.setRezultatTestiranja(rezultat);
        
    }
    
}
