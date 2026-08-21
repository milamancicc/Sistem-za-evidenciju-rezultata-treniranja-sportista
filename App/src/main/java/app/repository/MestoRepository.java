/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

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
@Repository("mesto-repository")
public class MestoRepository {
    private final EntityManagerFactory emf;

    public MestoRepository(@Qualifier("emf")EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public List<Mesto> izlistajSvaMesta(){
        EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT m FROM Mesto m ORDER BY m.naziv ASC", Mesto.class).getResultList();
    }
    
    public Mesto nadjiPoId(Long id){
        EntityManager em = emf.createEntityManager();
        return em.find(Mesto.class, id);
    }
    
    public Mesto dodaj(Mesto mesto){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            if(mesto.getIdMesta() == null)
                em.persist(mesto);
            else{
                mesto = em.merge(mesto);
            }
            em.getTransaction().commit();
            return mesto;
        }catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
    }
}
