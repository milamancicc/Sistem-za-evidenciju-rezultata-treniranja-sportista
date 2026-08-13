/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.converter.impl;

import app.converter.Converter;
import app.domain.EvidencijaTestiranja;
import app.domain.Sportista;
import app.domain.StavkaTestiranja;
import app.domain.StavkaTestiranjaId;
import app.domain.Trener;
import app.dto.EvidencijaTestiranjaDto;
import app.dto.StavkaTestiranjaDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author PC
 */
@Component
public class EvidencijaTestiranjaConverter implements Converter<EvidencijaTestiranja, EvidencijaTestiranjaDto> {

    private final StavkaTestiranjaConverter stavkaConverter;

    @Autowired
    public EvidencijaTestiranjaConverter(StavkaTestiranjaConverter stavkaConverter) {
        this.stavkaConverter = stavkaConverter;
    }
    
    
    
    @Override
    public EvidencijaTestiranja toEntity(EvidencijaTestiranjaDto dto) {
        
        if(dto == null)
            return null;
        
        EvidencijaTestiranja entity = new EvidencijaTestiranja();
        if(dto.getIdTestiranja() != null)
            entity.setIdTestiranja(dto.getIdTestiranja());
        entity.setDatum(dto.getDatum());
        
        if(dto.getTrenerId() != null){
            Trener trener = new Trener();
            trener.setId(dto.getTrenerId());
            entity.setTrener(trener);
        }
        
        if(dto.getSportistaId() != null){
            Sportista sportista = new Sportista();
            sportista.setId(dto.getSportistaId());
            entity.setSportista(sportista);
        }
        
        if(dto.getStavke() != null){
            List<StavkaTestiranja> stavke = new ArrayList<>();
            int brojac = 1;
            for(StavkaTestiranjaDto stavkaDto: dto.getStavke()){
                StavkaTestiranja st = stavkaConverter.toEntity(stavkaDto);
                if(st != null){
                    st.setEvidencijaTestiranja(entity);
                    if(st.getId() == null)
                        st.setId(new StavkaTestiranjaId());
                    st.getId().setRb(brojac++);
                    stavke.add(st);
                }
            }
            entity.setStavke(stavke);
        }
        return entity;
    }

    @Override
    public EvidencijaTestiranjaDto toDto(EvidencijaTestiranja entity) {

        if(entity == null)
            return null;
        
        EvidencijaTestiranjaDto dto = new EvidencijaTestiranjaDto();
        
        if(entity.getIdTestiranja() != null)
            dto.setIdTestiranja(entity.getIdTestiranja());
        dto.setDatum(entity.getDatum());
        dto.setBrojTestova(entity.getBrojTestova());
        dto.setBrojPolozenih(entity.getBrojPolozenih());
        dto.setBrojPalih(entity.getBrojPalih());
        dto.setProsaoTestiranje(entity.isProsaoTestiranje());
        dto.setRezultatTestiranja(entity.getRezultatTestiranja());
        
        if(entity.getTrener() != null)
            dto.setTrenerId(entity.getTrener().getId());
        
        
        if(entity.getSportista() != null)
            dto.setSportistaId(entity.getSportista().getId());
        
        if(entity.getStavke() != null){
            List<StavkaTestiranjaDto> stavkeDto = new ArrayList<>();
            for(StavkaTestiranja st: entity.getStavke()){
                StavkaTestiranjaDto stDto = stavkaConverter.toDto(st);
                if(stDto != null)
                    stavkeDto.add(stDto);
            }
            dto.setStavke(stavkeDto);
        }
        return dto;
    }
    
}
