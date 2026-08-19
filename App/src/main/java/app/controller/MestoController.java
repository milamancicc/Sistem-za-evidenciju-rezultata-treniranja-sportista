/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.domain.Mesto;
import app.service.MestoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
