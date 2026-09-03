/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.domain.Korisnik;
import app.dto.SpecijalistickiPodaciDto;
import app.dto.TrenerDto;
import app.repository.KorisnikRepository;
import app.service.TrenerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api/treneri")
@CrossOrigin(origins = "http://localhost:5173")
public class TrenerController {
    
    private final TrenerService trenerService;
    private final KorisnikRepository korisnikRepository;
    
    public TrenerController(@Qualifier(value = "trener-service")TrenerService trenerService, @Qualifier(value ="korisnik-repository") KorisnikRepository korisnikRepository) {
        this.trenerService = trenerService;
        this.korisnikRepository = korisnikRepository;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getTrenerPoId(@PathVariable("id") Long id){
        try{
            TrenerDto trenerDto = trenerService.nadjiPoId(id);
            return ResponseEntity.ok(trenerDto);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Doslo je do greske pri preuzimanju podataka o treneru.");
        }
    }
    
    
    @PostMapping("/specijalizacije")
    public ResponseEntity<?> dodajSpecijalistickiPodatak(@RequestBody SpecijalistickiPodaciDto dto){
        try{
            trenerService.dodajSpecijalistickiPodatak(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Specijalisticki podatak je uspesno dodat.");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sistem ne moze da sacuva specijalisticki podatak.");
        }
    }
    
    
    @DeleteMapping("/specijalizacije")
    public ResponseEntity<?> obrisiSpecijalistickiPodatak(@RequestParam("idTrenera") Long idTrenera, @RequestParam("idSpecijalizacije") Long idSpecijalizacije){
        try{
            TrenerDto dto = trenerService.obrisiSpecijalistickiPodatak(idTrenera, idSpecijalizacije);
            return ResponseEntity.ok(dto);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Greska pri brisanju " + e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<?> dodajTrenera(@RequestBody TrenerDto trenerDto, @RequestParam("korisnickoIme") String korisnickoIme){
        try{
            Korisnik ulogovaniKorisnik = korisnikRepository.nadjiPoKorisnickomImenu(korisnickoIme);
            TrenerDto sacuvan = trenerService.sacuvajTrenera(trenerDto, ulogovaniKorisnik);
            return ResponseEntity.status(HttpStatus.CREATED).body(sacuvan);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> izlistajSveTrenere(){
        try{
            
            List<TrenerDto> treneri = trenerService.izlistajSve();
            
            if(treneri == null || treneri.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nisu pronadjeni treneri.");
            return ResponseEntity.ok(treneri);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Doslo je do greske na serveru.");
        }
            
    }
}
