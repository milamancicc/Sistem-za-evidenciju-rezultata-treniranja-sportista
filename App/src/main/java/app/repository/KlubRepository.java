/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

import app.domain.Klub;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC
 */
@Repository("klub-repository")
public class KlubRepository {
    private final EntityManagerFactory emf;

    public KlubRepository(@Qualifier("emf")EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public List<Klub> izlistajKlubove(){
        EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT k FROM Klub k ORDER BY k.naziv ASC", Klub.class).getResultList();
    }
    
    public Klub nadjiPoId(Long id){
        EntityManager em = emf.createEntityManager();
        return em.find(Klub.class, id);
    }
}
