/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package app;

import app.config.AppConfig;
import app.dto.EvidencijaTestiranjaDto;
import app.dto.StavkaTestiranjaDto;
import app.service.EvidencijaTestiranjaService;
import java.time.LocalDate;
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
    
    @Autowired
    public App(@Qualifier(value = "evidencijaTestiranja-service")EvidencijaTestiranjaService evidencijaTestiranjaService) {
        this.evidencijaTestiranjaService = evidencijaTestiranjaService;
    }
    
    public static void main(String[] args) {
        System.out.println("Hello World!");
        ApplicationContext container = new AnnotationConfigApplicationContext(AppConfig.class);
        App app = container.getBean(App.class);
        
        List<StavkaTestiranjaDto> stavke = new ArrayList<>();
        EvidencijaTestiranjaDto dto = new EvidencijaTestiranjaDto(LocalDate.now(), 2, 1, 1, true, 50, 2L, 1L, stavke);
        app.sacuvajEvidencijuTestiranja(dto);
        System.out.println("Uspesno!");
    }
    
    public EvidencijaTestiranjaDto sacuvajEvidencijuTestiranja(EvidencijaTestiranjaDto dto){
        return evidencijaTestiranjaService.sacuvajEvidencijuTestiranja(dto);
    }

}
