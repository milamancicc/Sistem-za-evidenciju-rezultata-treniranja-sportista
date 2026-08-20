/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.dto.VezbaDto;
import app.service.VezbaService;
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
@RequestMapping("/api/vezbe")
@CrossOrigin(origins = "http://localhost:5173")
public class VezbaController {
    private final VezbaService vezbaService;

    public VezbaController(@Qualifier("vezba-service")VezbaService vezbaService) {
        this.vezbaService = vezbaService;
    }
    
    
    @GetMapping
    public ResponseEntity<?> izlistajSveVezbe(){
        List<VezbaDto> vezbe = vezbaService.izlistajSveVezbe();
        return ResponseEntity.ok(vezbe);
    }
    
    
    @PostMapping
    public ResponseEntity<?> dodajVezbu(@RequestBody VezbaDto dto){
        try{
            VezbaDto sacuvana = vezbaService.dodajIliIzmeniVezbu(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(sacuvana);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Greska pri citanju vezbe: " + e.getMessage());
        }
    }
}
