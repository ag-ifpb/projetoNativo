/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.services;

import ifpb.pdm.ouvidoriaws.daos.TicketDao;
import ifpb.pdm.ouvidoriaws.daos.exceptions.TicketException;
import ifpb.pdm.ouvidoriaws.entities.Ticket;
import ifpb.pdm.ouvidoriaws.enums.StatusTicket;
import ifpb.pdm.ouvidoriaws.pubnub.PubNubClientService;
//import ifpb.pdm.ouvidoriaws.pubnub.PubNubClientService;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author natarajan
 */
@Stateless
public class TicketService {

    @EJB
    private TicketDao ticketDao;
    
    /*
    
    */
    public Ticket getById(Long id) {
        return ticketDao.getById(id);
    }

    /*
    
    */
    public Ticket salvar(Ticket ticket) {
        ticket.setStatus(StatusTicket.OPEN);
        ticket.setCreatedIn(LocalDateTime.now());
        ticket.setUpdatedIn(ticket.getCreatedIn());
        
        //aqui ainda tem que mandar mensagem para pubnub
        Ticket ticketSalvo = ticketDao.salvar(ticket);        
        PubNubClientService instance = PubNubClientService.instance();
        instance.sendMessageCreatedTicket(ticketSalvo);
        
        return ticketSalvo;
    }
    
    /*
    
    */
    public Ticket cancelar(Long id) {
        Ticket ticket = getById(id);
        ticket.setUpdatedIn(LocalDateTime.now());
        return ticketDao.atualizar(ticket);
    }
    
    /*
    
    */
    public List<Ticket> getAllOpen(){
        return ticketDao.getAllOpen();
    }
    
    /*
    
    */
    public List<Ticket> getInactives(){
        return ticketDao.getInactive();
    }
    
    /*
    
    */
    public List<Ticket> getAllByUser(Long userId){
        return ticketDao.getAllByUser(userId);
    }
    
    /*
    
    */
    public void cancelByCliente(Long id) throws TicketException {
        Ticket ticket = getById(id);
        if (ticket.getStatus() == StatusTicket.OPEN) {
            ticket.setUpdatedIn(LocalDateTime.now());
            ticket.setStatus(StatusTicket.CLOSED);
            ticketDao.atualizar(ticket);
        } else {
            throw new TicketException("Ticket não está aberto");
        }
    }
    
    /*
    
    */
    public void cancelByAuditor(Long id) throws TicketException {
        Ticket ticket = getById(id);
        LocalDateTime after10days = LocalDateTime.now().minusDays(10);
        
        if (ticket.getStatus() == StatusTicket.OPEN && ticket.getUpdatedIn().compareTo(after10days) <= 0) {
            ticket.setUpdatedIn(LocalDateTime.now());
            ticket.setStatus(StatusTicket.CLOSED);
            ticketDao.atualizar(ticket);
        } else {
            throw new TicketException("Ticket não está aberto e com última atualização há mais de 10 dias");
        }
    }
    
    
    
    
    
    
    
}
