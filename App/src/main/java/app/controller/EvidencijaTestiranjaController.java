/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.dto.EvidencijaTestiranjaDto;
import app.service.EvidencijaTestiranjaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
}
