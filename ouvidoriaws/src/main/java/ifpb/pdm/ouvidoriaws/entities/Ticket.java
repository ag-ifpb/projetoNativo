/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.entities;

import ifpb.pdm.ouvidoriaws.enums.StatusTicket;
import ifpb.pdm.ouvidoriaws.enums.TipoTicket;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author natarajan
 */
@Entity
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdIn;
    private LocalDateTime updatedIn;
    
    private String resume;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario from;
    
    private int status;
    
    private int tipo;

    public Ticket() {
//        this.createdIn = LocalDateTime.now();
//        this.updatedIn = createdIn;
        this.status = StatusTicket.OPEN.getId();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedIn() {
        return createdIn;
    }

    public void setCreatedIn(LocalDateTime createdIn) {
        this.createdIn = createdIn;
    }

    public LocalDateTime getUpdatedIn() {
        return updatedIn;
    }

    public void setUpdatedIn(LocalDateTime updatedIn) {
        this.updatedIn = updatedIn;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Usuario getFrom() {
        return from;
    }

    public void setFrom(Usuario from) {
        this.from = from;
    }

    public StatusTicket getStatus() {
        return StatusTicket.parse(this.status);
    }

    public void setStatus(StatusTicket status) {
        this.status = status.getId();
    }
    
    public TipoTicket getTipo() {
        return TipoTicket.parse(this.tipo);
    }

    public void setTipo(TipoTicket tipo) {
        this.tipo = tipo.getId();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.createdIn);
        hash = 59 * hash + Objects.hashCode(this.updatedIn);
        hash = 59 * hash + Objects.hashCode(this.resume);
        hash = 59 * hash + Objects.hashCode(this.from);
        hash = 59 * hash + this.status;
        hash = 59 * hash + this.tipo;
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
        final Ticket other = (Ticket) obj;
        if (this.status != other.status) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        if (!Objects.equals(this.resume, other.resume)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.createdIn, other.createdIn)) {
            return false;
        }
        if (!Objects.equals(this.updatedIn, other.updatedIn)) {
            return false;
        }
        if (!Objects.equals(this.from, other.from)) {
            return false;
        }
        return true;
    }
    
    
    
}
