/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.converter.impl.SpecijalistickiPodaciConverter;
import app.converter.impl.SpecijalizacijaConverter;
import app.converter.impl.TrenerConverter;
import app.domain.Korisnik;
import app.domain.SpecijalistickiPodaci;
import app.domain.Specijalizacija;
import app.domain.TipKorisnika;
import app.domain.Trener;
import app.dto.SpecijalistickiPodaciDto;
import app.dto.TrenerDto;
import app.repository.SpecijalizacijaRepository;
import app.repository.TrenerRepository;
import app.security.PasswordHash;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service("trener-service")
public class TrenerService {
    
    private final TrenerRepository trenerRepository;
    private final TrenerConverter trenerConverter;
    private final SpecijalistickiPodaciConverter specijalistickiPodaciConverter;

    public TrenerService(TrenerRepository trenerRepository, TrenerConverter trenerConverter, SpecijalistickiPodaciConverter specijalistickiPodaciConverter) {
        this.trenerRepository = trenerRepository;
        this.trenerConverter = trenerConverter;
        this.specijalistickiPodaciConverter = specijalistickiPodaciConverter;
    }
    
    public TrenerDto sacuvajTrenera(TrenerDto dto, Korisnik ulogovaniKorisnik){
        try{
            if(ulogovaniKorisnik == null || ulogovaniKorisnik.getTipKorisnika() != TipKorisnika.TRENER){
                throw new SecurityException("Samo ulogovani treneri mogu unositi nove trenere u sistem.");
            }
            proveriPodatke(dto);
            dto.setTipKorisnika(TipKorisnika.TRENER);
            Trener entity = trenerConverter.toEntity(dto);
            String hashiranaSifra = PasswordHash.createHash(dto.getSifra());
            entity.setSifra(hashiranaSifra);
            Trener sacuvani = trenerRepository.sacuvajTrener(entity);
            return trenerConverter.toDto(sacuvani);
            
        }catch(IllegalArgumentException | SecurityException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException("Sistem ne moze da zapamti trenera.");
        }
    }
    
    public void dodajSpecijalistickiPodatak(SpecijalistickiPodaciDto dto){
        if(dto == null || dto.getIdTrenera() == null || dto.getIdSpecijalizacije() == null)
            throw new IllegalArgumentException("Nedostaju obavezni podaci za specijalizaciju.");
        SpecijalistickiPodaci sp = specijalistickiPodaciConverter.toEntity(dto);
        trenerRepository.dodajSpecijalistickiPodatak(dto.getIdTrenera(), sp);
        
    }
    
    public TrenerDto obrisiSpecijalistickiPodatak(Long trenerId, Long specijalizacijaId){
        if(trenerId == null || specijalizacijaId == null)
            throw new IllegalArgumentException("Nedostaju obavezni podaci.");
        Trener t = trenerRepository.obrisiSpecijalistickiPodatak(trenerId, specijalizacijaId);
        return trenerConverter.toDto(t);
    }
    
    public TrenerDto nadjiPoId(Long id){
        Trener trener = trenerRepository.nadjiPoId(id);
        if(trener == null)
            throw new RuntimeException("Trener sa ID-em " + id + " nije pronadjen.");
        
        return trenerConverter.toDto(trener);
    }
    
    private void proveriPodatke(TrenerDto dto){
        if(dto == null)
            throw new IllegalArgumentException("Podaci o treneru ne smeju biti null.");
        
        if(dto.getIme() == null || dto.getIme().isBlank())
            throw new IllegalArgumentException("Ime trenera je obavezno.");
        if(dto.getPrezime()== null || dto.getPrezime().isBlank())
            throw new IllegalArgumentException("Prezime trenera je obavezno.");
        if(dto.getKorisnickoIme()== null || dto.getKorisnickoIme().isBlank())
            throw new IllegalArgumentException("Korisnicko ime trenera je obavezno.");
        if(dto.getSifra()== null || dto.getSifra().isBlank())
            throw new IllegalArgumentException("Sifra trenera je obavezno.");
        if(dto.getEmail()== null || dto.getEmail().isBlank())
            throw new IllegalArgumentException("Email trenera je obavezno.");
        
    }
    
    
    
    
}
