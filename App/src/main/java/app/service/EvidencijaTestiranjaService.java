/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.converter.impl.EvidencijaTestiranjaConverter;
import app.converter.impl.StavkaTestiranjaConverter;
import app.domain.EvidencijaTestiranja;
import app.domain.JedinicaMere;
import app.domain.Norma;
import app.domain.Sportista;
import app.domain.StavkaTestiranja;
import app.domain.StavkaTestiranjaId;
import app.dto.EvidencijaTestiranjaDto;
import app.dto.SportistaDto;
import app.dto.StavkaTestiranjaDto;
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
    private final StavkaTestiranjaConverter stavkaTestiranjaConverter;
    private final SportistaRepository sportistaRepository;
    
    @Autowired

    public EvidencijaTestiranjaService(EvidencijaTestiranjaRepository evidencijaTestiranjaRepository, EvidencijaTestiranjaConverter evidencijaTestiranjaConverter, NormaRepository normaRepository, StavkaTestiranjaConverter stavkaTestiranjaConverter, SportistaRepository sportistaRepository) {
        this.evidencijaTestiranjaRepository = evidencijaTestiranjaRepository;
        this.evidencijaTestiranjaConverter = evidencijaTestiranjaConverter;
        this.normaRepository = normaRepository;
        this.stavkaTestiranjaConverter= stavkaTestiranjaConverter;
        this.sportistaRepository = sportistaRepository;
    }

    public EvidencijaTestiranjaDto sacuvajEvidencijuTestiranja(EvidencijaTestiranjaDto dto){
        EvidencijaTestiranja entity = evidencijaTestiranjaConverter.toEntity(dto);
        izracunajStatistiku(entity);
        EvidencijaTestiranja saved = evidencijaTestiranjaRepository.sacuvajEvidencijuTestiranja(entity);
        
        return evidencijaTestiranjaConverter.toDto(saved);
    }
    
    public void obrisi(Long id){
        evidencijaTestiranjaRepository.obrisi(id);
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
    
    public void obrisiStavku(Long idTestiranja, int rb){
        EvidencijaTestiranja evidencija = evidencijaTestiranjaRepository.nadjiPoId(idTestiranja);
        if(evidencija == null)
            throw new RuntimeException("Evidencija sa ID-jem " + idTestiranja + " ne postoji.");
        evidencijaTestiranjaRepository.obrisiStavku(idTestiranja, rb);
        EvidencijaTestiranja reloaded = evidencijaTestiranjaRepository.nadjiPoId(idTestiranja);
        if(reloaded != null){
            izracunajStatistiku(reloaded);
            evidencijaTestiranjaRepository.sacuvajEvidencijuTestiranja(reloaded);
        }
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
    
    public EvidencijaTestiranjaDto izmeniStavku(Long idTestiranja, int rb, StavkaTestiranjaDto dto){
        EvidencijaTestiranja evidencijaTestiranja = evidencijaTestiranjaRepository.nadjiPoId(idTestiranja);
        if(evidencijaTestiranja == null)
            throw new RuntimeException("Evidencija testiranja sa ID-jem: " + idTestiranja + " ne postoji.");
        StavkaTestiranja st = evidencijaTestiranja.getStavke().stream()
                .filter(s-> s.getId().getRb() == rb)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Stavka sa rednim brojem " + rb + " ne postoji u evidenciji."));

        st.setKomentar(dto.getKomentar());
        st.setOstvareniRezultat(dto.getOstvareniRezultat());
        Sportista sportista = evidencijaTestiranja.getSportista();
        st.setProsaoTest(proveriProlaznostStavke(sportista, st));
        izracunajStatistiku(evidencijaTestiranja);
        return evidencijaTestiranjaConverter.toDto(
                evidencijaTestiranjaRepository.sacuvajEvidencijuTestiranja(evidencijaTestiranja));
    }
    
    
    
    public EvidencijaTestiranjaDto dodajStavku(Long idTestiranja,StavkaTestiranjaDto dto){
        EvidencijaTestiranja evidencijaTestiranja = evidencijaTestiranjaRepository.nadjiPoId(idTestiranja);
        int sledeciRb = 1;
        if(evidencijaTestiranja.getStavke() != null && !evidencijaTestiranja.getStavke().isEmpty()){
            for(StavkaTestiranja s: evidencijaTestiranja.getStavke()){
                sledeciRb = s.getId().getRb()+1;
            }
        }
        dto.setRb(sledeciRb);
        StavkaTestiranja stavkaTestiranja = stavkaTestiranjaConverter.toEntity(dto);
        stavkaTestiranja.getId().setEvidencijaId(idTestiranja);
        stavkaTestiranja.setEvidencijaTestiranja(evidencijaTestiranja);
        stavkaTestiranja.setProsaoTest(proveriProlaznostStavke(evidencijaTestiranja.getSportista(), stavkaTestiranja));
        EvidencijaTestiranja et = evidencijaTestiranjaRepository.dodajStavku(stavkaTestiranja);
//        et.getStavke().add(stavkaTestiranja);
        et=evidencijaTestiranjaRepository.nadjiPoId(idTestiranja);
        izracunajStatistiku(et);
        et = evidencijaTestiranjaRepository.sacuvajEvidencijuTestiranja(et);
        return evidencijaTestiranjaConverter.toDto(et);
    }
    
    
    public StavkaTestiranjaDto pripremiStavku(Long sportistaId, StavkaTestiranjaDto stavkaDto){
        Sportista sportista = sportistaRepository.nadjiPoId(sportistaId);
        StavkaTestiranja stavka = stavkaTestiranjaConverter.toEntity(stavkaDto);
        boolean prosao = proveriProlaznostStavke(sportista, stavka);
        stavka.setProsaoTest(prosao);
        Norma norma = normaRepository.pretraziPoVezbiPoluIStarosnojKategoriji(stavka.getVezba(), sportista.getPol(), sportista.getStarosnaKategorija());
        StavkaTestiranjaDto rez = stavkaTestiranjaConverter.toDto(stavka);
        rez.setProsaoTest(prosao);
        rez.setNorma(norma != null ? norma.getNorma(): 0.0);
        rez.setVezbaNaziv(stavka.getVezba().getNaziv());
        return rez;
    }
}
