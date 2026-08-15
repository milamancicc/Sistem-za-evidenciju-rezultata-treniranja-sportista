/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.config;

import app.converter.impl.EvidencijaTestiranjaConverter;
import app.converter.impl.SportistaConverter;
import app.converter.impl.StavkaTestiranjaConverter;
import app.repository.EvidencijaTestiranjaRepository;
import app.repository.KorisnikRepository;
import app.repository.SportistaRepository;
import app.service.EvidencijaTestiranjaService;
import app.service.LoginService;
import app.service.SportistaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author PC
 */
@ComponentScan(basePackages = {"app"})
public class AppConfig {
    
    @Bean(value = "emf")
    public EntityManagerFactory getEntityManagerFactory(){
        return Persistence.createEntityManagerFactory("AppPU");
    }
    
    @Bean
    public StavkaTestiranjaConverter stavkaTestiranjaConverter(){
        return new StavkaTestiranjaConverter();
    }
    
    @Bean
    public EvidencijaTestiranjaConverter evidencijaTestiranjaConverter(StavkaTestiranjaConverter stavkaTestiranjaConverter){
        return new EvidencijaTestiranjaConverter(stavkaTestiranjaConverter);
    }
    
    @Bean
    public SportistaConverter sportistaConverter(){
        return new SportistaConverter();
    }
    
    @Bean(value = "evidencijaTestiranja-service")
    public EvidencijaTestiranjaService evidencijaTestiranjaService(EvidencijaTestiranjaRepository repository, EvidencijaTestiranjaConverter converter){
        return new EvidencijaTestiranjaService(repository, converter);
    }
    
    @Bean(value = "sportista-service")
    public SportistaService sportistaService(SportistaRepository sportistaRepository, SportistaConverter sportistaConverter){
        return new SportistaService(sportistaRepository, sportistaConverter);
    }
    
    @Bean(value = "login-service")
    public LoginService loginService(KorisnikRepository korisnikRepository){
        return new LoginService(korisnikRepository);
    }
    
}
