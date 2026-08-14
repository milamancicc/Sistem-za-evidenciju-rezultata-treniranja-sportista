/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package app;

import app.config.AppConfig;
import app.domain.Pol;
import app.domain.StarosnaKategorija;
import app.domain.TipKorisnika;
import app.dto.EvidencijaTestiranjaDto;
import app.dto.SportistaDto;
import app.dto.StavkaTestiranjaDto;
import app.service.EvidencijaTestiranjaService;
import app.service.SportistaService;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author PC
 */
@Component
public class App {

    private final EvidencijaTestiranjaService evidencijaTestiranjaService;
    private final SportistaService sportistaService;
    
    @Autowired
    public App(@Qualifier(value = "evidencijaTestiranja-service")EvidencijaTestiranjaService evidencijaTestiranjaService,
            @Qualifier(value = "sportista-service")SportistaService sportistaService) {
        this.evidencijaTestiranjaService = evidencijaTestiranjaService;
        this.sportistaService = sportistaService;
    }
    
    public static void main(String[] args) {
        System.out.println("Hello World!");
        ApplicationContext container = new AnnotationConfigApplicationContext(AppConfig.class);
        App app = container.getBean(App.class);
        
        
        System.out.println("Uspesno!");
    }
    
    public EvidencijaTestiranjaDto sacuvajEvidencijuTestiranja(EvidencijaTestiranjaDto dto){
        return evidencijaTestiranjaService.sacuvajEvidencijuTestiranja(dto);
    }
    
    public List<EvidencijaTestiranjaDto> pretraziPoTreneru(Long idTrenera){
        return evidencijaTestiranjaService.pretraziPoTreneru(idTrenera);
    }
    
    public List<EvidencijaTestiranjaDto> pretraziPoKriterijumima(Long idTrenera, Long idSportiste, LocalDate datum, Boolean prosaoTestiranje, Double rezultatTestiranja){
        return evidencijaTestiranjaService.pretraziPoKriterijumima(idTrenera, idSportiste, datum, prosaoTestiranje, rezultatTestiranja);
    }
    
    public EvidencijaTestiranjaDto nadjiEvidencijuTestiranjaPoId(Long id){
        return evidencijaTestiranjaService.nadjiPoId(id);
    }

    public SportistaDto sacuvajSportistu(SportistaDto dto){
        return sportistaService.sacuvajSportistu(dto);
    }
    
    public SportistaDto nadjiSportistuPoId(Long id){
        return sportistaService.nadjiPoId(id);
    }
    
    public List<SportistaDto> pretraziSportistePoKriterijumima(String imePrezime, Pol pol, Integer godineOd, Integer godineDo, List<StarosnaKategorija> starosneKategorije, List<Long> kluboviId, List<Long> mestaId){
        return sportistaService.pretraziPoKriterijumima(imePrezime, pol, godineOd, godineDo, starosneKategorije, kluboviId, mestaId);
    }
    
}
