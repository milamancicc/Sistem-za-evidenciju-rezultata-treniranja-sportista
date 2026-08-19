/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.service;

import app.domain.Klub;
import app.repository.KlubRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author PC
 */
@Service("klub-service")
public class KlubService {
    private final KlubRepository klubRepository;

    public KlubService(@Qualifier("klub-repository")KlubRepository klubRepository) {
        this.klubRepository = klubRepository;
    }
    
    public List<Klub> izlistajSveKlubove(){
        return klubRepository.izlistajKlubove();
    }
    
}
