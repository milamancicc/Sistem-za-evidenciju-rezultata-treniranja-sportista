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
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC
 */
@Repository(value = "evidencijaTestiranja-repository")
public class EvidencijaTestiranjaRepository{
    
    private final EntityManagerFactory emf;

    public EvidencijaTestiranjaRepository(EntityManagerFactory emf) {
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
    
}
