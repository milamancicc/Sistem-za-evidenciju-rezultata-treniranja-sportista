/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

import app.domain.Vezba;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC
 */
@Repository("vezba-repository")
public class VezbaRepository {
    private final EntityManagerFactory emf;

    public VezbaRepository(@Qualifier("emf")EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Vezba sacuvaj(Vezba vezba){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            if(vezba.getIdVezbe() == null){
                em.persist(vezba);
            }
            else{
                vezba = em.merge(vezba);
            }
            em.getTransaction().commit();
            return vezba;
        }catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
        
    }
    
    
    
    public Vezba nadjiPoId(Long id){
        EntityManager em = emf.createEntityManager();
        Vezba vezba = em.find(Vezba.class, id);
        if(vezba == null)
            return null;
        return vezba;
    }
    
    
    public List<Vezba> izlistajSve(){
        EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT v FROM Vezba v", Vezba.class).setHint("jakarta.persistence.cache.retrieveMode", "BYPASS")
                .setHint("jakarta.persistence.cache.storeMode", "BYPASS").getResultList();
    }
    
    public void obrisi(Long id){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Vezba vezba = em.find(Vezba.class, id);
            if(vezba != null)
                em.remove(vezba);
            else{
                throw new IllegalArgumentException("Vezba ne postoji.");
            }
            em.getTransaction().commit();
        }catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
        
    }
}
