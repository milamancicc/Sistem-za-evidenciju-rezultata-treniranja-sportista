/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author PC
 */
@Embeddable
public class StavkaTestiranjaId implements Serializable {
    private Long evidencijaId;
    private int rb;

    public StavkaTestiranjaId() {}

    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.evidencijaId);
        hash = 41 * hash + this.rb;
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
        final StavkaTestiranjaId other = (StavkaTestiranjaId) obj;
        if (this.rb != other.rb) {
            return false;
        }
        return Objects.equals(this.evidencijaId, other.evidencijaId);
    }

    public Long getEvidencijaId() {
        return evidencijaId;
    }

    public void setEvidencijaId(Long evidencijaId) {
        this.evidencijaId = evidencijaId;
    }

    public int getRb() {
        return rb;
    }

    public void setRb(int rb) {
        this.rb = rb;
    }
    
    
}

