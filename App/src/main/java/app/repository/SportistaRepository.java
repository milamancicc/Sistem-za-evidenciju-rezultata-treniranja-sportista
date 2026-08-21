/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.repository;

import app.domain.EvidencijaTestiranja;
import app.domain.Klub;
import app.domain.Mesto;
import app.domain.Pol;
import app.domain.Sportista;
import app.domain.StarosnaKategorija;
import app.domain.StavkaTestiranja;
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
@Repository(value = "sportista-repository")
public class SportistaRepository {
    
    private final EntityManagerFactory emf;

    public SportistaRepository(@Qualifier(value = "emf")EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Sportista sacuvajSportistu(Sportista sportista){
        EntityManager em = emf.createEntityManager();
        
        try{
            em.getTransaction().begin();
            
            Long idMesta = sportista.getMestoPorekla() != null ? sportista.getMestoPorekla().getIdMesta() : null;
            
            if(idMesta == null)
                throw new IllegalArgumentException("ID mesta porekla ne sme biti null.");
            
            Mesto m = em.find(Mesto.class, idMesta);
            
            if(m == null)
                throw new RuntimeException("Mesto sa ID "+ idMesta + "ne postoji u bazi.");
            
            sportista.setMestoPorekla(m);
            
            if(sportista.getKlub() != null && sportista.getKlub().getIdKluba() != null){
                Long idKluba = sportista.getKlub().getIdKluba();
                Klub k = em.find(Klub.class, idKluba);
                if(k == null)
                    throw new RuntimeException("Klub sa ID " + idKluba + " ne postoji u bazi.");
                sportista.setKlub(k);
            }else{
                sportista.setKlub(null);
            }
            
            if(sportista.getId() == null){
                em.persist(sportista);
            }else{
                sportista = em.merge(sportista);
            }
            em.getTransaction().commit();
            return sportista;
        }catch(Exception e){
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
    }
    
    public void obrisiSportistu(Long id){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            List<EvidencijaTestiranja> evidencije = em.createQuery("SELECT e FROM EvidencijaTestiranja e WHERE e.sportista.id = :idSportiste", EvidencijaTestiranja.class)
                    .setParameter("idSportiste", id)
                    .getResultList();
            for(EvidencijaTestiranja e:evidencije){
                em.createQuery("DELETE FROM StavkaTestiranja s WHERE s.evidencijaTestiranja = :evidencija")
                        .setParameter("evidencija", e)
                        .executeUpdate();
                em.remove(e);
            }
            Sportista sportista = em.find(Sportista.class, id);
            if(sportista != null){
                em.remove(sportista);
            }else{
                throw  new IllegalArgumentException("Sportista ne postoji.");
            }
            
            em.getTransaction().commit();
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }
    
    public List<Sportista> pretraziPoKriterijumima(String imePrezime, Pol pol, Integer godineOd, Integer godineDo, List<StarosnaKategorija> starosneKategorije, List<Long> kluboviId, List<Long> mestaId){
        EntityManager em = emf.createEntityManager();
        
        try{
            
            String sql = "SELECT s FROM Sportista s "
                    + "LEFT JOIN s.klub k "
                    + "LEFT JOIN s.mestoPorekla m WHERE 1=1 ";
            
            if(imePrezime != null && !imePrezime.trim().isEmpty()){
                sql += "AND (LOWER(CONCAT(s.ime, ' ', s.prezime)) LIKE :imePrezime) ";
            }
            
            if(pol != null)
                sql += "AND s.pol = :pol ";
            if(godineOd != null){
                sql += "AND s.datumRodjenja <= :maxDatum ";
            }
            if(godineDo != null){
                sql += "AND s.datumRodjenja >= :minDatum ";
            }
            if(starosneKategorije != null && !starosneKategorije.isEmpty()){
                sql += "AND s.starosnaKategorija IN :stKat ";
            }  
            if(kluboviId != null && !kluboviId.isEmpty())
                sql+="AND k.idKluba IN :kId ";
            if(mestaId != null && !mestaId.isEmpty())
                sql += "AND m.idMesta IN :mId ";
            
            var query = em.createQuery(sql, Sportista.class);
            
            if(imePrezime != null && !imePrezime.trim().isEmpty()){
                query.setParameter("imePrezime", "%" + imePrezime.trim().toLowerCase() + "%");
            }
            
            if(pol != null)
                query.setParameter("pol", pol);
            if(godineOd != null){
                LocalDate maxDatumRodjenja = (godineOd != null) ? LocalDate.now().minusYears(godineOd) : null;
                query.setParameter("maxDatum", maxDatumRodjenja);
            }
            if(godineDo != null){
                LocalDate minDatumRodjenja = (godineDo != null) ? LocalDate.now().minusYears(godineDo + 1).plusDays(1) : null;
                query.setParameter("minDatum", minDatumRodjenja);
            }
            if(starosneKategorije != null && !starosneKategorije.isEmpty()){
                query.setParameter("stKat", starosneKategorije);
            }  
            if(kluboviId != null && !kluboviId.isEmpty())
                query.setParameter("kId", kluboviId);
            if(mestaId != null && !mestaId.isEmpty())
                query.setParameter("mId", mestaId);
            
            
            List<Sportista> lista = query.getResultList();
            
            return lista;
        }finally{
            em.close();
        }
    }
    
    public Sportista nadjiPoId(Long id){
        EntityManager em = emf.createEntityManager();
        try{
            return em.find(Sportista.class, id);
        }finally{
            em.close();
        }
    }
    
    
    public List<Sportista> izlistajSveSportiste(){
        EntityManager em = emf.createEntityManager();
        try{
            String query = "SELECT s FROM Sportista s ";
            return em.createQuery(query, Sportista.class)
                    .getResultList();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            em.close();
        }
    }
    
}
