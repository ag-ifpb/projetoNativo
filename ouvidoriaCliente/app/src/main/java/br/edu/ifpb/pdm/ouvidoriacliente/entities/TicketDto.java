package br.edu.ifpb.pdm.ouvidoriacliente.entities;

import java.io.Serializable;

/**
 * Created by natarajan on 09/10/17.
 */

public class TicketDto implements Serializable{

    private int tipo;
    private Long from;
    private String resume;

    public TicketDto() {
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
