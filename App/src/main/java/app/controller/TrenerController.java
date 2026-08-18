/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.dto.TrenerDto;
import app.service.TrenerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public TrenerController(@Qualifier(value = "trener-service")TrenerService trenerService) {
        this.trenerService = trenerService;
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
    
}
