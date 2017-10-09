/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.services;

import ifpb.pdm.ouvidoriaws.daos.MensagemDao;
import ifpb.pdm.ouvidoriaws.daos.TicketDao;
import ifpb.pdm.ouvidoriaws.daos.UsuarioDao;
import ifpb.pdm.ouvidoriaws.entities.Mensagem;
import ifpb.pdm.ouvidoriaws.entities.Ticket;
import ifpb.pdm.ouvidoriaws.entities.Usuario;
import ifpb.pdm.ouvidoriaws.enums.StatusTicket;
import ifpb.pdm.ouvidoriaws.pubnub.PubNubClientService;
import ifpb.pdm.ouvidoriaws.resources.ComposedLastMessage;
import ifpb.pdm.ouvidoriaws.resources.MensagemDto;
import java.time.LocalDateTime;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author natarajan
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@Stateless
public class MensagemService {

    @EJB
    private TicketDao ticketDao;
    @EJB
    private MensagemDao mensagemDao;
    @EJB
    private UsuarioDao usuarioDao;
    
    
    /*
    pegar uma mensagem pelo di
    */
    public Mensagem getById(Long id) {
        return mensagemDao.getById(id);
    }

    /*
    salvar uma mensagem de cliente
    */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Mensagem salvarMensagemCliente(MensagemDto mensagemDto) {
        Mensagem mensagemSalva = null;
        try {
            Usuario cliente = usuarioDao.getById(mensagemDto.getFromId());
            Ticket ticket = ticketDao.getById(mensagemDto.getTicketId());
            if (ticket.getStatus() == StatusTicket.REPLICATED) {
                Mensagem mensagem = new Mensagem();
                mensagem.setFrom(cliente);
                mensagem.setTicket(ticket);
                mensagem.setText(mensagemDto.getText());
                mensagemSalva = mensagemDao.salvar(mensagem);
                ticket.setUpdatedIn(LocalDateTime.now());
                ticket.setStatus(StatusTicket.OPEN);
                ticketDao.atualizar(ticket);
                // aquimandar atualizar o pubnub - cliente
                PubNubClientService.instance().sendMessageResponseByClient(ticket);
            } else {
                throw new IllegalArgumentException("O tickt não está replicado.");
            }
            
        } catch (EntityNotFoundException | IllegalArgumentException ex) {
            throw new EJBException(ex);
        }
        return mensagemSalva;
    }
    
    /*
    salvar uma mensagem de auditor
    */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Mensagem salvarMensagemAuditor(MensagemDto mensagemDto) {
        
        Usuario auditor = usuarioDao.getAuditor();
        
        Mensagem mensagemSalva = null;
        try {
            Usuario cliente = usuarioDao.getById(mensagemDto.getFromId());
            Ticket ticket = ticketDao.getById(mensagemDto.getTicketId());
            if (ticket.getStatus() == StatusTicket.OPEN) {
                Mensagem mensagem = new Mensagem();
                mensagem.setFrom(auditor);
                mensagem.setTicket(ticket);
                mensagem.setText(mensagemDto.getText());
                
                mensagemSalva = mensagemDao.salvar(mensagem);
                ticket.setUpdatedIn(LocalDateTime.now());
                ticket.setStatus(StatusTicket.REPLICATED);
                ticketDao.atualizar(ticket);
                // aquimandar atualizar o pubnub - auditor
                PubNubClientService.instance().sendMessageResponseByAuditor(ticket);
            } else {
                throw new IllegalArgumentException("O tickt não está aberto.");
            }
            
        } catch (EntityNotFoundException | IllegalArgumentException ex) {
            throw new EJBException(ex);
        }
        return mensagemSalva;
    }
    
    public Mensagem getLastMensagem(Long idTicket) {
        return mensagemDao.getLastMensagemByTicket(idTicket);
    }
    
    public ComposedLastMessage getComposedLastMensagem(Long idTicket) {
        Mensagem lastMensagemByTicket = mensagemDao.getLastMensagemByTicket(idTicket);
        Ticket ticket = ticketDao.getById(idTicket);
        return new ComposedLastMessage(lastMensagemByTicket, ticket);
    }
    
}
