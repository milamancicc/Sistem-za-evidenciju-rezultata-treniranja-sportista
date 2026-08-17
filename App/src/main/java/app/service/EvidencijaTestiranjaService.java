/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.converter.impl.EvidencijaTestiranjaConverter;
import app.domain.EvidencijaTestiranja;
import app.domain.JedinicaMere;
import app.domain.Norma;
import app.domain.Pol;
import app.domain.Sportista;
import app.domain.StarosnaKategorija;
import app.domain.StavkaTestiranja;
import app.dto.EvidencijaTestiranjaDto;
import app.dto.SportistaDto;
import app.repository.EvidencijaTestiranjaRepository;
import app.repository.NormaRepository;
import app.repository.SportistaRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service("evidencijaTestiranja-service")
public class EvidencijaTestiranjaService {
    
    private final EvidencijaTestiranjaRepository evidencijaTestiranjaRepository;
    private final EvidencijaTestiranjaConverter evidencijaTestiranjaConverter;
    private final NormaRepository normaRepository;
    
    @Autowired

    public EvidencijaTestiranjaService(EvidencijaTestiranjaRepository evidencijaTestiranjaRepository, EvidencijaTestiranjaConverter evidencijaTestiranjaConverter, NormaRepository normaRepository) {
        this.evidencijaTestiranjaRepository = evidencijaTestiranjaRepository;
        this.evidencijaTestiranjaConverter = evidencijaTestiranjaConverter;
        this.normaRepository = normaRepository;
    }

    public EvidencijaTestiranjaDto sacuvajEvidencijuTestiranja(EvidencijaTestiranjaDto dto){
        EvidencijaTestiranja entity = evidencijaTestiranjaConverter.toEntity(dto);
        izracunajStatistiku(entity);
        EvidencijaTestiranja saved = evidencijaTestiranjaRepository.sacuvajEvidencijuTestiranja(entity);
        
        return evidencijaTestiranjaConverter.toDto(saved);
    }
    
    public List<EvidencijaTestiranjaDto> pretraziPoTreneru(Long idTrenera){
        List<EvidencijaTestiranja> lista = evidencijaTestiranjaRepository.pretraziPoTreneru(idTrenera);
        if(lista == null || lista.isEmpty()){
            throw new RuntimeException("Sistem ne moze da nadje evidencije testiranja po zadatim kriterijumima.");
        }
        List<EvidencijaTestiranjaDto> listaDto = new ArrayList<>();
        for(EvidencijaTestiranja et: lista){
            EvidencijaTestiranjaDto dto = evidencijaTestiranjaConverter.toDto(et);
            dto.setImeIPrezimeSportiste(et.getSportista().getIme() + " " + et.getSportista().getPrezime());
            listaDto.add(dto);
        }
        return listaDto;
    }
    
    public List<EvidencijaTestiranjaDto> pretraziPoKriterijumima(Long idTrenera, Long idSportiste, LocalDate datum, Boolean prosaoTestiranje, Double rezultatTestiranja){
        List<EvidencijaTestiranja> lista = evidencijaTestiranjaRepository.pretraziPoKriterijumima(idTrenera, idSportiste, datum, prosaoTestiranje, rezultatTestiranja);
        if(lista == null || lista.isEmpty()){
            throw new RuntimeException("Sistem ne moze da nadje evidencije testiranja po zadatim kriterijumima.");
        }
        List<EvidencijaTestiranjaDto> listaDto = new ArrayList<>();
        for(EvidencijaTestiranja et: lista){
            listaDto.add(evidencijaTestiranjaConverter.toDto(et));
        }
        return listaDto;
    }
    
    public EvidencijaTestiranjaDto nadjiPoId(Long id){
        EvidencijaTestiranja et = evidencijaTestiranjaRepository.nadjiPoId(id);
        if(et == null)
            throw new RuntimeException("Sistem ne moze da nadje evidenciju testiranja.");
        return evidencijaTestiranjaConverter.toDto(et);
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
        
        Sportista sportista = entity.getSportista();
        int polozeni = 0;
        int pali = 0;
        
        for(StavkaTestiranja st: entity.getStavke()){
            boolean prosaoStavku = proveriProlaznostStavke(sportista, st);
            st.setProsaoTest(prosaoStavku);
            
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
    
    private boolean proveriProlaznostStavke(Sportista sportista, StavkaTestiranja stavkaTestiranja){
        if(sportista == null || stavkaTestiranja.getVezba() == null)
            return false;
        Norma norma = normaRepository.pretraziPoVezbiPoluIStarosnojKategoriji(stavkaTestiranja.getVezba(), sportista.getPol(), sportista.getStarosnaKategorija());
        
        if(norma == null)
            return true;
        
        double ostvareniRezultat = stavkaTestiranja.getOstvareniRezultat();
        double ciljnaNorma = norma.getNorma();
        JedinicaMere jedinicaMere = stavkaTestiranja.getVezba().getJedinicaMere();
        
        if(jedinicaMere == JedinicaMere.SEKUNDA || jedinicaMere == JedinicaMere.MINUT){
            return ostvareniRezultat <= ciljnaNorma;
        }
        return ostvareniRezultat >= ciljnaNorma;
        
    }
    
}
