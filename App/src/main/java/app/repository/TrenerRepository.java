/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

import app.domain.SpecijalistickiPodaci;
import app.domain.Specijalizacija;
import app.domain.Trener;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC
 */
@Repository(value = "trener-repository")
public class TrenerRepository {
    
    private final EntityManagerFactory emf;

    public TrenerRepository(@Qualifier(value = "emf")EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Trener sacuvajTrener(Trener trener){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            if(trener.getId() == null){
                em.persist(trener);
            }else{
                trener = em.merge(trener);
            }
            
            em.getTransaction().commit();
            return trener;
        }catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
    }
    
    public void dodajSpecijalistickiPodatak(Long trenerId, SpecijalistickiPodaci sp){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Trener trener = em.find(Trener.class, trenerId);
            if(trener == null)
                throw new RuntimeException("Trener za kog zelite da dodate specijalisticki podatak ne postoji.");
            Specijalizacija s = em.find(Specijalizacija.class, sp.getSpecijalizacija().getIdSpecijalizacije());
            if(s == null)
                throw new RuntimeException("Specijalizacija za koju se dodaje specijalisticki podatak ne postoji.");
            sp.setTrener(trener);
            trener.getSpecijalistickiPodaci().add(sp);
            em.persist(sp);
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
