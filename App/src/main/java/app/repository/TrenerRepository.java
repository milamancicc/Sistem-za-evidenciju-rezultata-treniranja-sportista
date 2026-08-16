/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

import app.domain.Trener;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC
 */
@Repository(value = "trener-repository")
public class TrenerRepository {
    
    private final EntityManagerFactory emf;

    public TrenerRepository(EntityManagerFactory emf) {
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
    
}
