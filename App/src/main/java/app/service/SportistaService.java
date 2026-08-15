/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.converter.impl.SportistaConverter;
import app.domain.Pol;
import app.domain.Sportista;
import app.domain.StarosnaKategorija;
import app.domain.TipKorisnika;
import app.dto.SportistaDto;
import app.repository.SportistaRepository;
import app.security.PasswordHash;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class SportistaService {
    private final SportistaRepository sportistaRepository;
    private final SportistaConverter sportistaConverter;

    public SportistaService(SportistaRepository sportistaRepository, SportistaConverter sportistaConverter) {
        this.sportistaRepository = sportistaRepository;
        this.sportistaConverter = sportistaConverter;
    }
    
    public SportistaDto sacuvajSportistu(SportistaDto dto){
        try{
            proveraPodataka(dto);
            StarosnaKategorija starosnaKategorija = odrediStarosnuKategoriju(dto.getDatumRodjenja());
            dto.setStarosnaKategorija(starosnaKategorija);
            dto.setTipKorisnika(TipKorisnika.SPORTISTA);
            String hasiranaSifra = PasswordHash.createHash(dto.getSifra());
            Sportista entity = sportistaConverter.toEntity(dto);
            entity.setSifra(hasiranaSifra);
            Sportista sacuvani = sportistaRepository.sacuvajSportistu(entity);
            return sportistaConverter.toDto(sacuvani);
        }catch(IllegalArgumentException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException("Sistem ne moze da zapamti sportistu." + e.getMessage());
        }
    }
    
    public void obrisiSportistu(Long id) throws Exception{
        sportistaRepository.obrisiSportistu(id);
        
    }
    
    public List<SportistaDto> pretraziPoKriterijumima(String imePrezime, Pol pol, Integer godineOd, Integer godineDo, List<StarosnaKategorija> starosneKategorije, List<Long> kluboviId, List<Long> mestaId){
        List<Sportista> lista = sportistaRepository.pretraziPoKriterijumima(imePrezime, pol, godineOd, godineDo, starosneKategorije, kluboviId, mestaId);
        if(lista == null || lista.isEmpty())
            throw new RuntimeException("Sistem ne moze da nadje sportiste po zadatim kriterijumima");
            
        List<SportistaDto> listaDto = new ArrayList<>();
        for(Sportista s: lista){
            listaDto.add(sportistaConverter.toDto(s));
        }

        return listaDto;
        
    }
    
    public SportistaDto nadjiPoId(Long id){
        Sportista s = sportistaRepository.nadjiPoId(id);
        if(s ==null)
            throw new RuntimeException("Sistem ne moze da nadje sportistu.");
        return sportistaConverter.toDto(s);
    }
    
    private void proveraPodataka(SportistaDto dto){
        if(dto == null)
            throw new IllegalArgumentException("Podaci o sportisti su prazni.");
        if(dto.getIme() == null)
            throw new IllegalArgumentException("Ime sportiste je obavezno polje.");
        if(dto.getPrezime()== null)
            throw new IllegalArgumentException("Prezime sportiste je obavezno polje.");
        if(dto.getKorisnickoIme()== null)
            throw new IllegalArgumentException("Korisnicko ime sportiste je obavezno polje.");
        if(dto.getSifra()== null)
            throw new IllegalArgumentException("Sifra sportiste je obavezno polje.");
        if(dto.getDatumRodjenja()== null)
            throw new IllegalArgumentException("Datum rodjenja sportiste je obavezno polje.");
        if(dto.getIdMestoPorekla()== null)
            throw new IllegalArgumentException("Mesto porekla sportiste je obavezno polje.");
        if(dto.getVisina() != null && dto.getVisina() <= 0)
            throw new IllegalArgumentException("Visina mora biti pozitivan broj");
        if(dto.getTezina() != null && dto.getTezina()<= 0)
            throw new IllegalArgumentException("Tezina mora biti pozitivan broj");
    }
    
    
    private StarosnaKategorija odrediStarosnuKategoriju(LocalDate datumRodjenja){
        int brojGodina = Period.between(datumRodjenja, LocalDate.now()).getYears();
        
        if(brojGodina < 11)
            throw new IllegalArgumentException("Sportista mora imati najmanje 11 godina za registraciju.");
        
        if(brojGodina <= 13){
            return StarosnaKategorija.PIONIR;
        }else if(brojGodina <= 15){
            return StarosnaKategorija.KADET;
        }else if(brojGodina <= 18){
            return StarosnaKategorija.JUNIOR;
        }else if(brojGodina <= 35){
            return StarosnaKategorija.SENIOR;
        }else{
            return StarosnaKategorija.VETERAN;
        }
    }
}
