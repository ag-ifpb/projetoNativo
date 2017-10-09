package br.edu.ifpb.pdm.ouvidoriaaudit.entities;

/**
 * Created by natarajan on 08/10/17.
 */

public class ComposedLastMessage {

    private Mensagem mensagem;
    private Ticket ticket;

    public ComposedLastMessage() {
    }

    public ComposedLastMessage(Mensagem mensagem, Ticket ticket) {
        this.mensagem = mensagem;
        this.ticket = ticket;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }



}