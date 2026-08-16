/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author PC
 */
@Entity
@Table(name = "SpecijalistickiPodaci")
public class SpecijalistickiPodaci {
    
    @EmbeddedId
    private SpecijalistickiPodaciId id = new SpecijalistickiPodaciId();
    
    @ManyToOne
    @MapsId("trenerId")
    @JoinColumn(name = "idTrenera")
    private Trener trener;
    
    @ManyToOne
    @MapsId("specijalizacijaId")
    @JoinColumn(name = "specijalizacijaId")
    private Specijalizacija specijalizacija;
    
    @Column
    private Integer godinaPostizanja;

    public SpecijalistickiPodaci() {
    }

    public SpecijalistickiPodaci(Integer godinaPostizanja) {
        this.godinaPostizanja = godinaPostizanja;
    }

    public SpecijalistickiPodaciId getId() {
        return id;
    }

    public void setId(SpecijalistickiPodaciId id) {
        this.id = id;
    }

    public Trener getTrener() {
        return trener;
    }

    public void setTrener(Trener trener) {
        this.trener = trener;
    }

    public Specijalizacija getSpecijalizacija() {
        return specijalizacija;
    }

    public void setSpecijalizacija(Specijalizacija specijalizacija) {
        this.specijalizacija = specijalizacija;
    }

    public Integer getGodinaPostizanja() {
        return godinaPostizanja;
    }

    public void setGodinaPostizanja(Integer godinaPostizanja) {
        this.godinaPostizanja = godinaPostizanja;
    }
    
    
    
}

@Embeddable
class SpecijalistickiPodaciId implements Serializable {
    private Long trenerId;
    private Long specijalizacijaId;

    public SpecijalistickiPodaciId() {}

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.trenerId);
        hash = 23 * hash + Objects.hashCode(this.specijalizacijaId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpecijalistickiPodaciId other = (SpecijalistickiPodaciId) obj;
        if (!Objects.equals(this.trenerId, other.trenerId)) {
            return false;
        }
        return Objects.equals(this.specijalizacijaId, other.specijalizacijaId);
    }
    
    
}