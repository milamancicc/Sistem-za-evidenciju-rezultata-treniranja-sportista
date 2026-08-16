/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

import app.domain.Specijalizacija;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC
 */
@Repository(value = "specijalizacija-repository")
public class SpecijalizacijaRepository {
    
    private final EntityManagerFactory emf;

    public SpecijalizacijaRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Specijalizacija sacuvajSpecijalizaciju(Specijalizacija specijalizacija){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            
            if(specijalizacija.getIdSpecijalizacije() == null){
                em.persist(specijalizacija);
            }else{
                specijalizacija = em.merge(specijalizacija);
            }
            
            em.getTransaction().commit();
            return specijalizacija;
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }
    
    public Specijalizacija nadjiPoNazivu(String naziv){
        EntityManager em  = emf.createEntityManager();
        try{
            String query = "SELECT s FROM Specijalizacija s WHERE LOWER(s.naziv) = LOWER(:naziv)";
            return em.createQuery(query, Specijalizacija.class)
                    .setParameter("naziv", naziv)
                    .getSingleResult();
        }catch(NoResultException e){
            return null;
        }finally{
            em.close();
        }
    }
    
}
