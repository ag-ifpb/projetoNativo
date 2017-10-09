package br.edu.ifpb.pdm.ouvidoriaaudit.entities;

import java.io.Serializable;

/**
 * Created by natarajan on 06/10/17.
 */

public class Mensagem implements Serializable{

    private Long id;
    private String text;
    private Usuario from;
    private Ticket ticket;

    public Mensagem(Long id) {
        this.id = id;
    }

    public Mensagem(Long id, String text, Usuario from, Ticket ticket) {
        this.id = id;
        this.text = text;
        this.from = from;
        this.ticket = ticket;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Usuario getFrom() {
        return from;
    }

    public void setFrom(Usuario from) {
        this.from = from;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
            sb
//                .append("ID: ").append(id).append("\n")
                .append("Usuário: ").append(from.getNome()).append("(").append(from.getTipoString()).append(")").append("\n")
                .append("Mensagem Enviada: ").append("\n")
                .append(text).append("\n");

        return sb.toString();
    }
}
