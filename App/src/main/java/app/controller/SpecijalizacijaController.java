/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.dto.SpecijalizacijaDto;
import app.service.SpecijalizacijaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/api/specijalizacije")
@CrossOrigin(origins = "http://localhost:5173")
public class SpecijalizacijaController {
    
    private final SpecijalizacijaService specijalizacijaService;

    public SpecijalizacijaController(@Qualifier("specijalizacija-service")SpecijalizacijaService specijalizacijaService) {
        this.specijalizacijaService = specijalizacijaService;
    }
    
    @GetMapping
    public ResponseEntity<List<SpecijalizacijaDto>> nadjiSve(){
        List<SpecijalizacijaDto> specijalizacije = specijalizacijaService.nadjiSve();
        return ResponseEntity.ok(specijalizacije);
    }
    
    @PostMapping
    public ResponseEntity<?> sacuvajSpecijalizaciju(@RequestBody SpecijalizacijaDto specijalizacijaDto){
        try{
            SpecijalizacijaDto sacuvana = specijalizacijaService.sacuvajSpecijalizaciju(specijalizacijaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(sacuvana);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
