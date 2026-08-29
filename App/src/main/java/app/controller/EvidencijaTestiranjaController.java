/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.domain.EvidencijaTestiranja;
import app.dto.EvidencijaTestiranjaDto;
import app.dto.StavkaTestiranjaDto;
import app.service.EvidencijaTestiranjaService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api/evidencije")
@CrossOrigin(origins = "http://localhost:5173")
public class EvidencijaTestiranjaController {
    
    private final EvidencijaTestiranjaService evidencijaTestiranjaService;

    public EvidencijaTestiranjaController(@Qualifier(value = "evidencijaTestiranja-service")EvidencijaTestiranjaService evidencijaTestiranjaService) {
        this.evidencijaTestiranjaService = evidencijaTestiranjaService;
    }
    
    @GetMapping("/trener/{idTrenera}")
    public ResponseEntity<?> izlistajSveEvidencijeOdTrenera(@PathVariable("idTrenera") Long idTrenera){
        try{
            List<EvidencijaTestiranjaDto> lista = evidencijaTestiranjaService.pretraziPoTreneru(idTrenera);
            return ResponseEntity.ok(lista);
        }catch(RuntimeException e){
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> nadjiPoId(@PathVariable("id") Long id){
        try{
            EvidencijaTestiranjaDto dto = evidencijaTestiranjaService.nadjiPoId(id);
            return ResponseEntity.ok(dto);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> obrisi(@PathVariable("id")Long id){
        try{
            evidencijaTestiranjaService.obrisi(id);
            return ResponseEntity.ok().body("Evidencija je uspesno obrisana");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Greska pri brisanju evidencije.");
        }
    }
    
    @DeleteMapping("/{idTestiranja}/stavke/{rb}")
    public ResponseEntity<?> obrisiStavku(@PathVariable("idTestiranja") Long idTestiranja, @PathVariable("rb") int rb){
        try{
            evidencijaTestiranjaService.obrisiStavku(idTestiranja, rb);
            return ResponseEntity.ok("Stavka sa rednim brojem "+ rb + " je uspesno obrisana.");
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greska prilikom brisanja stavke: "+ e.getMessage());
        }
    }
    
    @PutMapping("/{idTestiranja}/stavke/{rb}")
    public ResponseEntity<?> izmeniStavku(@PathVariable("idTestiranja")Long idTestiranja, @PathVariable("rb") int rb, @Valid @RequestBody StavkaTestiranjaDto stavkaTestiranjaDto){
        try{
            EvidencijaTestiranjaDto reloaded = evidencijaTestiranjaService.izmeniStavku(idTestiranja, rb, stavkaTestiranjaDto);
            return ResponseEntity.ok(reloaded);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greska prilikom izmene stavke: "+e.getMessage());
        }
    }
    
    
    @PostMapping("/{idTestiranja}/stavke")
    public ResponseEntity<?> dodajStavku(@PathVariable("idTestiranja") Long idTestiranja, @Valid @RequestBody StavkaTestiranjaDto dto){
        try{
            EvidencijaTestiranjaDto reloaded = evidencijaTestiranjaService.dodajStavku(idTestiranja, dto);
            return ResponseEntity.ok(reloaded);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greska prilikom cuvanja stavke: " + e.getMessage());
        }
    }
    
    
    @GetMapping("/pretraga")
    public ResponseEntity<?> pretraziEvidencijePoKriterijumima(@RequestParam(name = "idTrenera",required = false)Long idTrenera,
            @RequestParam(name = "idSportiste",required = false) Long idSportiste,
            @RequestParam(name = "datum",required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum,
            @RequestParam(name = "prosaoTestiranje",required = false)Boolean prosaoTestiranje,
            @RequestParam(name = "rezultatTestiranja",required = false)Double rezultatTestiranja){
        try{
            List<EvidencijaTestiranjaDto> rezultat = evidencijaTestiranjaService.pretraziPoKriterijumima(idTrenera, idSportiste, datum, prosaoTestiranje, rezultatTestiranja);
            return ResponseEntity.ok(rezultat);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<?> sacuvajEvidenciju(@RequestBody EvidencijaTestiranjaDto dto){
        try{
            EvidencijaTestiranjaDto nova = evidencijaTestiranjaService.sacuvajEvidencijuTestiranja(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nova);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greska pri kreiranju evidencije: " + e.getMessage());
        }
    }
    
    
    @PostMapping("/izracunaj-stavku")
    public ResponseEntity<?> izracunajStavku(@RequestParam("sportistaId") Long sportistaId, @Valid @RequestBody StavkaTestiranjaDto stavkaTestiranjaDto){
        try{
            StavkaTestiranjaDto obradjena = evidencijaTestiranjaService.pripremiStavku(sportistaId, stavkaTestiranjaDto);
            return ResponseEntity.ok(obradjena);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
