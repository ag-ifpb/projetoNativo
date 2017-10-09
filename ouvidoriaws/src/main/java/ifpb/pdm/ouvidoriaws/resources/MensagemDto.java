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

public class MensagemDto implements Serializable {

    private Long ticketId;
    private Long fromId;
    private String text;

    public MensagemDto() {
    }

    public MensagemDto(Long ticketId, Long fromId, String text) {
        this.ticketId = ticketId;
        this.fromId = fromId;
        this.text = text;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
    
    
}
