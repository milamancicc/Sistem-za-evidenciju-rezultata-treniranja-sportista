/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.controller;

import app.domain.Korisnik;
import app.dto.LoginRequest;
import app.dto.LoginResponse;
import app.service.AktivniKorisniciService;
import app.service.LoginService;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {
    private final LoginService loginService;
    private final AktivniKorisniciService aktivniKorisniciService;

    public LoginController(@Qualifier(value = "login-service")LoginService loginService, AktivniKorisniciService aktivniKorisniciService) {
        this.loginService = loginService;
        this.aktivniKorisniciService = aktivniKorisniciService;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            LoginResponse response = loginService.login(loginRequest.getKorisnickoIme(), loginRequest.getSifra(), loginRequest.getTipKorisnika());
            return ResponseEntity.ok(response);
        }catch(IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }
        catch(RuntimeException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", ex.getMessage()));
        } 
        catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","Greska prilikom provere lozinke."));
        }
    }
    
    @GetMapping("/aktivni")
    public ResponseEntity<?> getAktivniKorisnici(){
        List<String> aktivni = aktivniKorisniciService.getAktivniKorisnici();
        return ResponseEntity.ok(aktivni);
    }
    
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request){
        String korisnickoIme = request.get("korisnickoIme");
        aktivniKorisniciService.ukloniKorisnika(korisnickoIme);
        return ResponseEntity.ok().build();
    }
}
