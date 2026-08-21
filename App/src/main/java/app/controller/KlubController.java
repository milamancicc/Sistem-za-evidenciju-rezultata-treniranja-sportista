/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.domain.Klub;
import app.service.KlubService;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/klubovi")
@CrossOrigin(origins = "http://localhost:5173")
public class KlubController {
    private final KlubService klubService;

    public KlubController(@Qualifier("klub-service")KlubService klubService) {
        this.klubService = klubService;
    }
    @GetMapping
    public ResponseEntity<?> izlistajSveKlubove(){
        List<Klub> klubovi = klubService.izlistajSveKlubove();
        return ResponseEntity.ok(klubovi);
    }
    
    
    @PostMapping
    public ResponseEntity<?> dodaj(@RequestBody Klub klub){
        try{
            Klub sacuvano = klubService.dodaj(klub);
            return ResponseEntity.status(HttpStatus.CREATED).body(sacuvano);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Greska pri cuvanju kluba: " + e.getMessage());
        }
    }
}
