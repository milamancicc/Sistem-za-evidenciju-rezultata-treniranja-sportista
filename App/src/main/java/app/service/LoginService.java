/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.domain.Korisnik;
import app.domain.Sportista;
import app.domain.Trener;
import app.dto.LoginResponse;
import app.repository.KorisnikRepository;
import app.security.JwtUtil;
import app.security.PasswordHash;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service("login-service")
public class LoginService {
    private KorisnikRepository korisnikRepository;
    private final JwtUtil jwtUtil;

    public LoginService(KorisnikRepository korisnikRepository, JwtUtil jwtUtil) {
        this.korisnikRepository = korisnikRepository;
        this.jwtUtil = jwtUtil;
    }
       
    
    public LoginResponse login(String korisnickoIme, String unetaSifra, String izabranTipKorisnika) throws NoSuchAlgorithmException, InvalidKeySpecException{
        if(korisnickoIme == null || unetaSifra == null)
            throw new IllegalArgumentException("Korisnicko ime i sifra su obavezni.");
        Korisnik korisnik = korisnikRepository.nadjiPoKorisnickomImenu(korisnickoIme);
        if(korisnik == null)
            throw new RuntimeException("Korisnik sa ovim korisnickim imenom ne postoji.");
        if(!"trener".equalsIgnoreCase(izabranTipKorisnika) && !"sportista".equalsIgnoreCase(izabranTipKorisnika))
            throw new RuntimeException("Korisnik nije ni trener ni sportista.");
        if("trener".equalsIgnoreCase(izabranTipKorisnika) && !(korisnik instanceof Trener))
            throw new RuntimeException("Ovo je korisnicko ime sportiste! Molimo Vas prijavite se na stranici za sportiste");
        if("sportista".equalsIgnoreCase(izabranTipKorisnika) && !(korisnik instanceof Sportista))
            throw new RuntimeException("Ovo je korisnicko ime trenera! Molimo Vas prijavite se na stranici za trenere.");
        boolean tacnaSifra = PasswordHash.validatePassword(unetaSifra, korisnik.getSifra());
        if(!tacnaSifra)
            throw new RuntimeException("Pogresna sifra.");
        
        String token = jwtUtil.generateToken(korisnik.getKorisnickoIme(), izabranTipKorisnika);
        return new LoginResponse(token, korisnik.getId(), korisnik.getKorisnickoIme(), korisnik.getIme(), korisnik.getPrezime(), izabranTipKorisnika);
    }
}
