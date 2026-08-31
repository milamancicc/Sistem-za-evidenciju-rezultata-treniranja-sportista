/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class AktivniKorisniciService {
    
    private final ConcurrentHashMap<String, LocalDateTime> aktivniKorisnici = new ConcurrentHashMap<>();
    
    public void zabeleziAktivnost(String korisnickoIme){
        aktivniKorisnici.put(korisnickoIme, LocalDateTime.now());
    }
    
    public List<String> getAktivniKorisnici(){
        LocalDateTime granica = LocalDateTime.now().minusMinutes(60);
        aktivniKorisnici.entrySet().removeIf(entry -> entry.getValue().isBefore(granica));
        return aktivniKorisnici.keySet().stream().collect(Collectors.toList());
    }
    
    public void ukloniKorisnika(String korisnickoIme){
        if(korisnickoIme != null){
            aktivniKorisnici.remove(korisnickoIme);
        }
    }
}
