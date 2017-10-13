/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.daos;

import ifpb.pdm.ouvidoriaws.entities.Mensagem;
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
public class MensagemDao {
    
    @PersistenceContext
    private EntityManager em;
    
    public Mensagem salvar(Mensagem mensagem) {
        em.persist(mensagem);
        return mensagem;
    }
    
    
    public Mensagem getById(Long id) {
        return em.find(Mensagem.class, id);
    }
    
    
    public Mensagem getLastMensagemByTicket(Long ticketId) {
        TypedQuery<Mensagem> query = em
                .createQuery("SELECT m"
                        + " FROM Mensagem m"
                        + " WHERE m.ticket.id = :ticket "
                        + " ORDER BY m.id DESC "
                        , Mensagem.class)
                .setParameter("ticket", ticketId);

        List<Mensagem> resultList = query.setMaxResults(1).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }
    
}
