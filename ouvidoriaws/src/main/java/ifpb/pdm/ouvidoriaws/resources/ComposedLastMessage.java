/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.resources;

import ifpb.pdm.ouvidoriaws.entities.Mensagem;
import ifpb.pdm.ouvidoriaws.entities.Ticket;

/**
 *
 * @author natarajan
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
