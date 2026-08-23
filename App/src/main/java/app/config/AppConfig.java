/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.config;

import app.converter.impl.EvidencijaTestiranjaConverter;
import app.converter.impl.NormaConverter;
import app.converter.impl.SpecijalistickiPodaciConverter;
import app.converter.impl.SpecijalizacijaConverter;
import app.converter.impl.SportistaConverter;
import app.converter.impl.StavkaTestiranjaConverter;
import app.converter.impl.TrenerConverter;
import app.converter.impl.VezbaConverter;
import app.repository.EvidencijaTestiranjaRepository;
import app.repository.KlubRepository;
import app.repository.KorisnikRepository;
import app.repository.MestoRepository;
import app.repository.NormaRepository;
import app.repository.SpecijalizacijaRepository;
import app.repository.SportistaRepository;
import app.repository.TrenerRepository;
import app.repository.VezbaRepository;
import app.service.EvidencijaTestiranjaService;
import app.service.KlubService;
import app.service.LoginService;
import app.service.MestoService;
import app.service.NormaService;
import app.service.SpecijalizacijaService;
import app.service.SportistaService;
import app.service.TrenerService;
import app.service.VezbaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author PC
 */
@Configuration
@ComponentScan(basePackages = {"app"})
public class AppConfig {
    
    @Bean(value = "emf")
    public EntityManagerFactory getEntityManagerFactory(){
        return Persistence.createEntityManagerFactory("AppPU");
    }
    
    @Bean
    public StavkaTestiranjaConverter stavkaTestiranjaConverter(NormaRepository normaRepository){
        return new StavkaTestiranjaConverter(normaRepository);
    }
    
    @Bean
    public EvidencijaTestiranjaConverter evidencijaTestiranjaConverter(StavkaTestiranjaConverter stavkaTestiranjaConverter){
        return new EvidencijaTestiranjaConverter(stavkaTestiranjaConverter);
    }
    
    @Bean
    public SportistaConverter sportistaConverter(){
        return new SportistaConverter();
    }
    
    @Bean
    public SpecijalistickiPodaciConverter specijalistickiPodaciConverter(){
        return new SpecijalistickiPodaciConverter();
    }
    
    @Bean
    public TrenerConverter trenerConverter(SpecijalistickiPodaciConverter specijalistickiPodaciConverter, EvidencijaTestiranjaConverter evidencijaTestiranjaConverter){
        return new TrenerConverter(specijalistickiPodaciConverter, evidencijaTestiranjaConverter);
    }
    
    @Bean
    public VezbaConverter vezbaConverter(){
        return new VezbaConverter();
    }
    
    @Bean
    public NormaConverter normaConverter(){
        return new NormaConverter();
    }
    
    @Bean(value = "evidencijaTestiranja-service")
    public EvidencijaTestiranjaService evidencijaTestiranjaService(EvidencijaTestiranjaRepository repository, EvidencijaTestiranjaConverter converter, NormaRepository normaRepository, StavkaTestiranjaConverter stavkaTestiranjaConverter){
        return new EvidencijaTestiranjaService(repository, converter, normaRepository, stavkaTestiranjaConverter);
    }
    
    @Bean(value = "sportista-service")
    public SportistaService sportistaService(SportistaRepository sportistaRepository, SportistaConverter sportistaConverter){
        return new SportistaService(sportistaRepository, sportistaConverter);
    }
    
    @Bean(value = "login-service")
    public LoginService loginService(KorisnikRepository korisnikRepository){
        return new LoginService(korisnikRepository);
    }
    
    @Bean(value = "specijalizacija-service")
    public SpecijalizacijaService specijalizacijaService(SpecijalizacijaRepository specijalizacijaRepository, SpecijalizacijaConverter specijalizacijaConverter){
        return new SpecijalizacijaService(specijalizacijaRepository, specijalizacijaConverter);
    }
    
    @Bean (value = "trener-service")
    public TrenerService trenerService(TrenerRepository trenerRepository, TrenerConverter trenerConverter, SpecijalistickiPodaciConverter specijalistickiPodaciConverter){
        return new TrenerService(trenerRepository, trenerConverter, specijalistickiPodaciConverter);
    }
    
    @Bean(value = "mesto-service")
    public MestoService mestoService(MestoRepository mestoRepository){
        return new MestoService(mestoRepository);
    }
    
    @Bean(value = "klub-service")
    public KlubService klubService(KlubRepository klubRepository){
        return new KlubService(klubRepository);
    }
    
    @Bean(value = "vezba-service")
    public VezbaService vezbaService(VezbaRepository vezbaRepository, VezbaConverter vezbaConverter){
        return new VezbaService(vezbaRepository, vezbaConverter);
    }
    
    @Bean(value = "norma-service")
    public NormaService normaService(NormaRepository normaRepository, NormaConverter normaConverter, VezbaRepository vezbaRepository){
        return new NormaService(normaRepository, normaConverter, vezbaRepository);
    }
}
