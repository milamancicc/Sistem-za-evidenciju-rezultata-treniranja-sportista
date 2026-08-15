/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.domain.Korisnik;
import app.domain.Sportista;
import app.domain.Trener;
import app.repository.KorisnikRepository;
import app.security.PasswordHash;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service
public class LoginService {
    private KorisnikRepository korisnikRepository;

    public LoginService(KorisnikRepository korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
    }
       
    
    public Korisnik login(String korisnickoIme, String unetaSifra, String izabranTipKorisnika) throws NoSuchAlgorithmException, InvalidKeySpecException{
        if(korisnickoIme == null || unetaSifra == null)
            throw new IllegalArgumentException("Korisnicko ime i sifra su obavezni.");
        Korisnik korisnik = korisnikRepository.nadjiPoKorisnickomImenu(korisnickoIme);
        if(korisnik == null)
            throw new RuntimeException("Pogresno korisnicko ime.");
        boolean tacnaSifra = PasswordHash.validatePassword(unetaSifra, korisnik.getSifra());
        if(!tacnaSifra)
            throw new RuntimeException("Pogresna sifra.");
        
        if(!"trener".equalsIgnoreCase(izabranTipKorisnika) && !"sportista".equalsIgnoreCase(izabranTipKorisnika))
            throw new RuntimeException("Korisnik nije ni trener ni sportista.");
        if("trener".equalsIgnoreCase(izabranTipKorisnika) && !(korisnik instanceof Trener))
            throw new RuntimeException("Korisnik nije trener.");
        if("sportista".equalsIgnoreCase(izabranTipKorisnika) && !(korisnik instanceof Sportista))
            throw new RuntimeException("Korisnik nije sportista.");
        
            
        return korisnik;
    }
}
