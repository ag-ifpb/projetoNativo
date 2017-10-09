/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.resources;

import ifpb.pdm.ouvidoriaws.daos.exceptions.TicketException;
import ifpb.pdm.ouvidoriaws.entities.Ticket;
import ifpb.pdm.ouvidoriaws.entities.Usuario;
import ifpb.pdm.ouvidoriaws.enums.TipoTicket;
import ifpb.pdm.ouvidoriaws.services.TicketService;
import ifpb.pdm.ouvidoriaws.services.UsuarioService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author natarajan
 */

@Path("ticket")
@Stateless
public class TicketResources {
    
    @EJB
    private TicketService ticketService;
    
    @EJB
    private UsuarioService usuarioService;
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicket(@PathParam("id") Long id) {
        Ticket ticket = ticketService.getById(id);
        if (ticket  == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        return Response.ok().entity(ticket).build();
    }
    
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTicket(TicketDto ticketDto, @Context UriInfo uriInfo) throws URISyntaxException {
        
        Ticket ticket = new Ticket();
        Usuario usuario = null;
        try {
        
            usuario = usuarioService.getById(ticketDto.getFrom());
            ticket.setFrom(usuario);
            ticket.setTipo(TipoTicket.parse(ticketDto.getTipo()));
            ticket.setResume(ticketDto.getResume());

            Ticket salvar = ticketService.salvar(ticket);
            
            return Response
                    .created(new URI("/ouvidoriaws/api/ticket/" + ticket.getId()))
                    .entity(ticket)
                    .build();
            
        } catch(Exception ex) {
            if (usuario == null) {
                ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
            }
        }
        
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();

    }
    
    @DELETE
    @Path("/cancelarcliente/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelarCliente(@PathParam("id") Long id) throws URISyntaxException {

        try {

            ticketService.cancelByCliente(id);

            return Response.ok().build();
        } catch (TicketException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
        }
    }
    
    @DELETE
    @Path("/cancelarauditor/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelarAuditor(@PathParam("id") Long id) throws URISyntaxException {

        try {

            ticketService.cancelByAuditor(id);

            return Response.ok().build();
        } catch (TicketException e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
        }
    }
    
    @GET
    @Path("/allopen")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOpen() {
        List<Ticket> allOpen = ticketService.getAllOpen();
        GenericEntity<List<Ticket>> genericEntity 
                = new GenericEntity<List<Ticket>>(allOpen){};
        return Response.ok().entity(genericEntity).build();
    }
    
    @GET
    @Path("/allinactive")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllInactive() {
        List<Ticket> allOpen = ticketService.getInactives();
        GenericEntity<List<Ticket>> genericEntity 
                = new GenericEntity<List<Ticket>>(allOpen){};
        return Response.ok().entity(genericEntity).build();
    }
    
    @GET
    @Path("/byuser/{idUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllByUser(@PathParam("idUser") String idUser) {
        List<Ticket> allByUser = ticketService.getAllByUser(Long.parseLong(idUser));
        GenericEntity<List<Ticket>> genericEntity 
                = new GenericEntity<List<Ticket>>(allByUser){};
        return Response.ok().entity(genericEntity).build();
    }
    
    
    
}
