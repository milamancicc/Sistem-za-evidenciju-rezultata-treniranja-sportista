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
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;
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
            String query = "SELECT COUNT(sp) FROM SpecijalistickiPodaci sp "
                    + "WHERE sp.trener.id = :tId AND sp.specijalizacija.idSpecijalizacije = :sId";
            Long count = em.createQuery(query, Long.class).setParameter("tId", trenerId).setParameter("sId", s.getIdSpecijalizacije()).getSingleResult();
            if(count > 0){
                throw new RuntimeException("Vec imate unetu ovu specijalizaciju.");
            }
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
    
    public Trener obrisiSpecijalistickiPodatak(Long trenerId, Long specijalizacijaId){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            Trener trener = em.find(Trener.class, trenerId);
            if(trener == null)
                throw new RuntimeException("Trener nije pronadjen.");
            
            int brojObrisanih = em.createQuery("DELETE FROM SpecijalistickiPodaci sp WHERE sp.id.trenerId = :tId AND sp.id.specijalizacijaId = :sId")
                    .setParameter("tId", trenerId)
                    .setParameter("sId", specijalizacijaId)
                    .executeUpdate();
            if(brojObrisanih == 0)
                throw new RuntimeException("Specijalisticki podatak nije pronadjen u bazi.");
            em.getTransaction().commit();
            
        }catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
        return nadjiPoId(trenerId);
    }
    
    public Trener nadjiPoId(Long id){
        EntityManager em = emf.createEntityManager();
        try{
            String query = "SELECT DISTINCT t FROM Trener t "
                    + "LEFT JOIN FETCH t.specijalistickiPodaci sp "
                    + "LEFT JOIN FETCH sp.specijalizacija "
                    + "WHERE t.id = :trenerId";
            TypedQuery<Trener> q1 = em.createQuery(query, Trener.class).setParameter("trenerId", id);
            q1.setHint("jakarta.persistence.cache.storeMode", "REFRESH");
            q1.setHint("jakarta.persistence.cache.retrieveMode", "BYPASS");
            Trener trener = q1.getSingleResult();
            
            TypedQuery<Trener> q2 = em.createQuery("SELECT DISTINCT t FROM Trener t "
                    + "LEFT JOIN FETCH t.evidencije et "
                    + "WHERE t = :trener", Trener.class);
            q2.setParameter("trener", trener);
            q2.setHint("jakarta.persistence.cache.storeMode", "REFRESH");
            q2.setHint("jakarta.persistence.cache.retrieveMode", "BYPASS");
            
            return q2.getSingleResult();
        }catch(NoResultException e){
            return null;
        }finally{
            em.close();
        }
    }
    
    public List<Trener> izlistajSve(){
        EntityManager em = emf.createEntityManager();
        try{
            String query = "SELECT t FROM Trener t ";
            return em.createQuery(query, Trener.class)
                    .getResultList();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            em.close();
        }
    }
    
}
