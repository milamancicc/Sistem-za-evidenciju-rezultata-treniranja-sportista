/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

import app.domain.Norma;
import app.domain.Pol;
import app.domain.StarosnaKategorija;
import app.domain.Vezba;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC
 */
@Repository(value = "norma-repository")
public class NormaRepository {
    
    private final EntityManagerFactory emf;

    public NormaRepository(@Qualifier(value = "emf")EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Norma sacuvaj(Norma norma){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            if(norma.getIdNorme() == null){
                em.persist(norma);
            }else{
                norma = em.merge(norma);
            }
            em.getTransaction().commit();
            return norma;
        }catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
        
    }
    
    public Norma nadjiPoId(Long id){
        EntityManager em = emf.createEntityManager();
        Norma norma = em.find(Norma.class, id);
        if(norma == null)
            return null;
        return norma;
    }
    
    public List<Norma> izlistajPoVezbi(Long idVezbe){
        EntityManager em = emf.createEntityManager();
        return em.createQuery("SELECT n FROM Norma n WHERE n.vezba.idVezbe = :idVezbe", Norma.class)
                .setParameter("idVezbe", idVezbe)
                .setHint("jakarta.persistence.cache.retrieveMode", "BYPASS")
                .setHint("jakarta.persistence.cache.storeMode", "BYPASS")
                .getResultList();
    }
    
    
    public void obrisi(Long id){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Norma norma = em.find(Norma.class, id);
            if(norma!= null)
                em.remove(norma);
            else{
                throw new IllegalArgumentException("Norma ne postoji.");
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
    
    public Norma pretraziPoVezbiPoluIStarosnojKategoriji(Vezba vezba, Pol pol, StarosnaKategorija starosnaKategorija){
        EntityManager em = emf.createEntityManager();
        try{
            String query = "SELECT n FROM Norma n "
                    + "WHERE n.vezba = :vezba "
                    + "AND n.pol = :pol "
                    + "AND n.starosnaKategorija = :starosnaKategorija";
            
            List<Norma> rezultati = em.createQuery(query, Norma.class)
                    .setParameter("vezba", vezba)
                    .setParameter("pol", pol)
                    .setParameter("starosnaKategorija", starosnaKategorija)
                    .getResultList();
            
            if(rezultati.isEmpty())
                return null;
            return rezultati.get(0);
            
        }catch(Exception e){
            return null;
        }finally{
            em.close();
        }
    }
    
}
