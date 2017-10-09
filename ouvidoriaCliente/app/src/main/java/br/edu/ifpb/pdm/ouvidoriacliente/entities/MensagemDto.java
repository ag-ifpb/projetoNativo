package br.edu.ifpb.pdm.ouvidoriacliente.entities;

import java.io.Serializable;

/**
 * Created by natarajan on 08/10/17.
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
