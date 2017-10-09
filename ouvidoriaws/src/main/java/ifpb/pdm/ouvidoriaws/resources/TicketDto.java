/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.resources;

import java.io.Serializable;

/**
 *
 * @author natarajan
 */

public class TicketDto implements Serializable {

    private String resume;
    private Long from;
    private int tipo;

    public TicketDto() {
    }
    
    public TicketDto(String resume, Long from, int tipo) {
        this.resume = resume;
        this.from = from;
        this.tipo = tipo;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }
    
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    
    
}
