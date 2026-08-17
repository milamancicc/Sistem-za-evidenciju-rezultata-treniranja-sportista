/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

import app.domain.EvidencijaTestiranja;
import app.domain.Sportista;
import app.domain.Trener;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC
 */
@Repository(value = "evidencijaTestiranja-repository")
public class EvidencijaTestiranjaRepository{
    
    private final EntityManagerFactory emf;

    public EvidencijaTestiranjaRepository(@Qualifier(value = "emf") EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EvidencijaTestiranja sacuvajEvidencijuTestiranja(EvidencijaTestiranja evidencijaTestiranja){
        EntityManager em = emf.createEntityManager();
        
        try{
            em.getTransaction().begin();
            Long sportistaId = evidencijaTestiranja.getSportista() != null ? evidencijaTestiranja.getSportista().getId() : null;
            Long trenerId = evidencijaTestiranja.getTrener() != null ? evidencijaTestiranja.getTrener().getId() : null;

            if (sportistaId == null || trenerId == null) {
                throw new IllegalArgumentException("Sportista ID i Trener ID ne smeju biti null!");
            }

            Sportista s = em.find(Sportista.class, sportistaId);
            Trener t = em.find(Trener.class, trenerId);

            if (s == null) {
                throw new RuntimeException("Sportista sa ID " + sportistaId + " ne postoji u bazi!");
            }
            if (t == null) {
                throw new RuntimeException("Trener sa ID " + trenerId + " ne postoji u bazi!");
            }

            evidencijaTestiranja.setSportista(s);
            evidencijaTestiranja.setTrener(t);
            if(evidencijaTestiranja.getIdTestiranja() == null){
                em.persist(evidencijaTestiranja);
            }else{
                evidencijaTestiranja = em.merge(evidencijaTestiranja);
            }
            em.getTransaction().commit();
            return evidencijaTestiranja;
        }catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
    }
    
    public List<EvidencijaTestiranja> pretraziPoTreneru(Long trenerId){
        EntityManager em = emf.createEntityManager();
        try{
            if(trenerId == null)
                throw new IllegalArgumentException("Id trenera ne sme biti null");
            List<EvidencijaTestiranja> lista = em.createQuery("SELECT e FROM EvidencijaTestiranja e WHERE e.trener.id = :tId", EvidencijaTestiranja.class).setParameter("tId", trenerId).getResultList();
            return lista;
            
        }finally{
            em.close();
        }
    }
    
    public EvidencijaTestiranja nadjiPoId(Long id){
        EntityManager em = emf.createEntityManager();
        try{
            String query = "SELECT DISTINCT e FROM EvidencijaTestiranja e "
                    + "LEFT JOIN FETCH e.stavke "
                    + "WHERE e.idTestiranja = :id";
            List<EvidencijaTestiranja> lista = em.createQuery(query , EvidencijaTestiranja.class)
                    .setParameter("id", id)
                    .getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        }finally{
            em.close();
        }
    }
    
    public List<EvidencijaTestiranja> pretraziPoKriterijumima(Long idTrenera, Long idSportiste, LocalDate datum, Boolean prosaoTestiranje, Double  rezultatTestiranja){
        EntityManager em = emf.createEntityManager();
        
        try{
            String query = "SELECT DISTINCT e FROM EvidencijaTestiranja e "
                    + "LEFT JOIN FETCH e.stavke "
                    + "WHERE (:idSportiste IS NULL OR e.sportista.id = :idSportiste) "
                    + "AND (:idTrenera IS NULL OR e.trener.id = :idTrenera) "
                    + "AND (:datum IS NULL OR e.datum >= :datum) "
                    + "AND (:prosaoTestiranje IS NULL OR e.prosaoTestiranje = :prosaoTestiranje) "
                    + "AND (:rezultatTestiranja IS NULL OR e.rezultatTestiranja >= :rezultatTestiranja)";
            return em.createQuery(query, EvidencijaTestiranja.class)
                    .setParameter("idTrenera", idTrenera)
                    .setParameter("idSportiste", idSportiste)
                    .setParameter("datum", datum)
                    .setParameter("prosaoTestiranje", prosaoTestiranje)
                    .setParameter("rezultatTestiranja", rezultatTestiranja)
                    .getResultList();
        }finally{
            em.close();
        }
    }
       
}
