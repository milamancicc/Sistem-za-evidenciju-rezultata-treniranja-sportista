/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.domain.Mesto;
import app.repository.MestoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service("mesto-service")
public class MestoService {
    private final MestoRepository mestoRepository;

    public MestoService(@Qualifier("mesto-repository")MestoRepository mestoRepository) {
        this.mestoRepository = mestoRepository;
    }
    
    public List<Mesto> izlistajSvaMesta(){
        return mestoRepository.izlistajSvaMesta();
    }
    
    public Mesto dodaj(Mesto mesto){
        try{
            return mestoRepository.dodaj(mesto);
        }catch(IllegalArgumentException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException("Sistem ne moze da zapamti mesto.");
        }
    }
}
