/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.domain.Mesto;
import app.service.MestoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/api/mesta")
@CrossOrigin(origins = "http://localhost:5173")
public class MestoController {
    private final MestoService mestoService;

    public MestoController(@Qualifier("mesto-service")MestoService mestoService) {
        this.mestoService = mestoService;
    }
    
    @GetMapping
    public ResponseEntity<?> izlistajSvaMesta(){
        List<Mesto> mesta =mestoService.izlistajSvaMesta();
        return ResponseEntity.ok(mesta);
    }
    
    @PostMapping
    public ResponseEntity<?> dodajMesto(@RequestBody Mesto mesto){
        try{
            Mesto sacuvano = mestoService.dodaj(mesto);
            return ResponseEntity.status(HttpStatus.CREATED).body(sacuvano);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Greska pri cuvanju mesta: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> nadjiPoId(@PathVariable("id") Long id){
        try{
            Mesto dto = mestoService.nadjiPoId(id);
            return ResponseEntity.ok(dto);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
