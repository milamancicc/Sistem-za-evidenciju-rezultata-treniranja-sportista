/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.dto.NormaDto;
import app.service.NormaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
@RequestMapping("/api/norme")
@CrossOrigin(origins = "http://localhost:5173")
public class NormaController {
    private final NormaService normaService;

    public NormaController(@Qualifier("norma-service")NormaService normaService) {
        this.normaService = normaService;
    }
    
    @GetMapping("/vezba/{idVezbe}")
    public  ResponseEntity<?> izlistajNormeZaVezbu(@PathVariable("idVezbe") Long idVezbe){
        List<NormaDto> norme = normaService.izlistajSveNormeZaVezbu(idVezbe);
        return ResponseEntity.ok(norme);
    }
    
    
    @PostMapping
    public ResponseEntity<?> dodajNormu(@Valid @RequestBody NormaDto dto){
        try{
            NormaDto sacuvana = normaService.dodajNormu(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(sacuvana);
        }catch(Exception e){
            if(e.getMessage()!= null && e.getMessage().contains("1062"))
                return ResponseEntity.badRequest().body("Norma za izabranu vežbu, pol i starosnu kategoriju već postoji.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greska pri cuvanju norme: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> obrisiNormu(@PathVariable("id") Long id){
        try{
            normaService.obrisiNormu(id);
            return ResponseEntity.ok().body("Norma je uspesno obrisana.");
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Greska pri brisanju norme: " + e.getMessage());
        }
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        String greska = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(greska);
    }
}
