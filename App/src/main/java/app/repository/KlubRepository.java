/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

import app.domain.Klub;
import app.domain.Mesto;
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
        try{
            return em.createQuery("SELECT k FROM Klub k ORDER BY k.naziv ASC", Klub.class).getResultList();
        }finally{
            em.close();
        }
        
    }
    
    public Klub nadjiPoId(Long id){
        EntityManager em = emf.createEntityManager();
        try{
            return em.find(Klub.class, id);
        }finally{
            em.close();
        }
    }
    
    public Klub dodaj(Klub klub){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            if(klub.getMesto() != null && klub.getMesto().getIdMesta() != null){
                Mesto mesto = em.find(Mesto.class, klub.getMesto().getIdMesta());
                if(mesto != null){
                    klub.setMesto(mesto);
                }else{
                    throw new RuntimeException("Mesto koje ste izabrali ne postoji u bazi.");
                }
            }
            if(klub.getIdKluba() == null)
                em.persist(klub);
            else{
                klub = em.merge(klub);
            }
            em.getTransaction().commit();
            return klub;
        }catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
    }
}
