/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.converter.impl;

import app.converter.Converter;
import app.domain.Norma;
import app.domain.Pol;
import app.domain.StarosnaKategorija;
import app.domain.StavkaTestiranja;
import app.domain.StavkaTestiranjaId;
import app.domain.Vezba;
import app.dto.NormaDto;
import app.dto.StavkaTestiranjaDto;
import app.repository.NormaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author PC
 */
@Component
public class StavkaTestiranjaConverter implements Converter<StavkaTestiranja, StavkaTestiranjaDto> {

    private final NormaRepository nr;

    @Autowired
    public StavkaTestiranjaConverter(@Qualifier("norma-repository")NormaRepository nr) {
        this.nr = nr;
    }
    
    
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
        entity.setId(new StavkaTestiranjaId());
        entity.getId().setRb(dto.getRb());
        
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
            dto.setVezbaNaziv(entity.getVezba().getNaziv());
            Norma norma = nr.pretraziPoVezbiPoluIStarosnojKategoriji(entity.getVezba(), entity.getEvidencijaTestiranja().getSportista().getPol(), entity.getEvidencijaTestiranja().getSportista().getStarosnaKategorija());
            dto.setNorma(norma.getNorma());
        }
        
        return dto;
    }
    
}
