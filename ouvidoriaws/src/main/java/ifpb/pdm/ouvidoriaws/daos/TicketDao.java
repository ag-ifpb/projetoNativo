/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.daos;

import ifpb.pdm.ouvidoriaws.entities.Ticket;
import ifpb.pdm.ouvidoriaws.enums.StatusTicket;
import java.time.LocalDateTime;
import java.util.List;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author natarajan
 */

@DataSourceDefinition(
        name = "java:app/ouvidoria-data-source",
        className = "org.postgresql.Driver",
//        url = "jdbc:postgresql://banco-ouvidoria:5432/ouvidoria",        
        url = "jdbc:postgresql://localhost:5432/ouvidoria",
        user = "postgres",
        password = "12345"
)

@Stateless
public class TicketDao {
    @PersistenceContext
    private EntityManager em;
    
    public Ticket salvar(Ticket ticket) {
        em.persist(ticket);
        em.flush();
        return ticket;
    }
    
    public Ticket atualizar(Ticket ticket) {
        Ticket merge = em.merge(ticket);
        em.flush();
        return merge;
    }
    

    public Ticket getById(Long id) {
        return em.find(Ticket.class, id);
    }
    
    public List<Ticket> getAllOpen() {
        TypedQuery<Ticket> query = em
                .createQuery("SELECT t"
                        + " FROM Ticket t"
                        + " WHERE t.status <> :ticketStatus "
                        + " ORDER BY t.updatedIn DESC"
                        , Ticket.class)
                .setParameter("ticketStatus", StatusTicket.CLOSED.getId());

        return query.getResultList();        
    }
    
    public List<Ticket> getInactive() {
        
        LocalDateTime dateTime10daysBefore = LocalDateTime.now().minusDays(10);                            
        
        TypedQuery<Ticket> query = em
                .createQuery("SELECT t"
                        + " FROM Ticket t"
//                        + " WHERE t.status = :ticketStatus and date (t.updatedin) < now() - INTERVAL '10 DAY'"
                        + " WHERE t.status = :ticketStatus and t.updatedIn < :daysBefore"
                        , Ticket.class)
                .setParameter("ticketStatus", StatusTicket.OPEN.getId())
                .setParameter("daysBefore", dateTime10daysBefore);

        return query.getResultList();        
    }

    public List<Ticket> getAllByUser(Long userId) {
        TypedQuery<Ticket> query = em
                .createQuery("SELECT t"
                        + " FROM Ticket t"
                        + " WHERE "
                        + " t.status <> :ticketStatus "
                        + " AND "
                        + " t.from.id = :idUser "
                        + " ORDER BY t.id"
                        , Ticket.class)
                .setParameter("ticketStatus", StatusTicket.CLOSED.getId())
                .setParameter("idUser", userId);

        return query.getResultList();        
    }
    
}
