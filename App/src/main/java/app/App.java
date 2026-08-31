/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package app;

import app.config.AppConfig;
import app.domain.Korisnik;
import app.domain.Pol;
import app.domain.StarosnaKategorija;
import app.domain.TipKorisnika;
import app.dto.EvidencijaTestiranjaDto;
import app.dto.SpecijalistickiPodaciDto;
import app.dto.SpecijalizacijaDto;
import app.dto.SportistaDto;
import app.dto.TrenerDto;
import app.service.EvidencijaTestiranjaService;
import app.service.LoginService;
import app.service.SpecijalizacijaService;
import app.service.SportistaService;
import app.service.TrenerService;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author PC
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class App {

    private final EvidencijaTestiranjaService evidencijaTestiranjaService;
    private final SportistaService sportistaService;
    private final LoginService loginService;
    private final SpecijalizacijaService specijalizacijaService;
    private final TrenerService trenerService;
    
    @Autowired
    public App(@Qualifier(value = "evidencijaTestiranja-service")EvidencijaTestiranjaService evidencijaTestiranjaService,
            @Qualifier(value = "sportista-service")SportistaService sportistaService,
            @Qualifier(value = "login-service") LoginService loginService,
            @Qualifier(value = "specijalizacija-service") SpecijalizacijaService specijalizacijaService,
            @Qualifier(value = "trener-service") TrenerService trenerService) {
        this.evidencijaTestiranjaService = evidencijaTestiranjaService;
        this.sportistaService = sportistaService;
        this.loginService = loginService;
        this.specijalizacijaService = specijalizacijaService;
        this.trenerService = trenerService;
    }
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }
    
    
}
