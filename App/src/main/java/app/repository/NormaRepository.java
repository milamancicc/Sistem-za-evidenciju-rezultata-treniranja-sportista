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
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC
 */
@Repository(value = "norma-repository")
public class NormaRepository {
    
    private final EntityManagerFactory emf;

    public NormaRepository(EntityManagerFactory emf) {
        this.emf = emf;
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
