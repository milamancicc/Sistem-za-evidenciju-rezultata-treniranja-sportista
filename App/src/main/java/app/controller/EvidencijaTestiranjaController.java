/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.domain.EvidencijaTestiranja;
import app.dto.EvidencijaTestiranjaDto;
import app.dto.StavkaTestiranjaDto;
import app.service.EvidencijaTestiranjaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<?> izmeniStavku(@PathVariable("idTestiranja")Long idTestiranja, @PathVariable("rb") int rb, @RequestBody StavkaTestiranjaDto stavkaTestiranjaDto){
        try{
            EvidencijaTestiranja reloaded = evidencijaTestiranjaService.izmeniStavku(idTestiranja, rb, stavkaTestiranjaDto);
            return ResponseEntity.ok(reloaded);
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greska prilikom izmene stavke: "+e.getMessage());
        }
    }
}
