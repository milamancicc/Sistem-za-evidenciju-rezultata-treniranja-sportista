/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.dto.SportistaDto;
import app.service.SportistaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api/sportisti")
@CrossOrigin(origins = "http://localhost:5173")
public class SportistaController {
    private final SportistaService sportistaService;

    public SportistaController(@Qualifier(value = "sportista-service")SportistaService sportistaService) {
        this.sportistaService = sportistaService;
    }
    
    @GetMapping
    public ResponseEntity<?> izlistajSveSportiste(){
        try{
            
            List<SportistaDto> sportisti = sportistaService.izlistajSveSPortiste();
            
            if(sportisti == null || sportisti.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nisu pronadjeni sportisti za zadatog trenera.");
            return ResponseEntity.ok(sportisti);
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Doslo je do greske na serveru.");
        }
            
    }
    
    
    @PostMapping
    public ResponseEntity<?> sacuvajSportistu(@RequestBody SportistaDto dto){
        try{
            if(dto == null)
                return ResponseEntity.badRequest().body("Podaci o sportisti ne mogu biti prazni.");
            SportistaDto sacuvan = sportistaService.sacuvajSportistu(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(sacuvan);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Doslo je do greske prilikom kreiranja sportiste");
        }
    }
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> obrisiSportistu(@PathVariable("id") Long id){
        try{
            sportistaService.obrisiSportistu(id);
            return ResponseEntity.ok("Sportista je uspesno obrisan.");
            
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Greska pri brisanju sportiste: " + e.getMessage());
        }
    }
}
