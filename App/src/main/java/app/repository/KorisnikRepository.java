/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

import app.domain.Korisnik;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author PC
 */
@Repository(value = "korisnik-repository")
public class KorisnikRepository {
    private final EntityManagerFactory emf;

    public KorisnikRepository(@Qualifier(value = "emf")EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Korisnik nadjiPoKorisnickomImenu(String korisnickoIme){
        EntityManager em = emf.createEntityManager();
        try{
            String query = "SELECT k FROM Korisnik k WHERE k.korisnickoIme = :korisnickoIme";
            return (Korisnik) em.createQuery(query)
                    .setParameter("korisnickoIme", korisnickoIme)
                    .getSingleResult();
            
        }catch(NoResultException e){
            return null;
        }finally{
            em.close();
        }
    }
}
