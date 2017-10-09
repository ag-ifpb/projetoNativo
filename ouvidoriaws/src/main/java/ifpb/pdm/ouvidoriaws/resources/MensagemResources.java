/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.resources;

import ifpb.pdm.ouvidoriaws.daos.exceptions.TicketException;
import ifpb.pdm.ouvidoriaws.entities.Mensagem;
import ifpb.pdm.ouvidoriaws.entities.Ticket;
import ifpb.pdm.ouvidoriaws.entities.Usuario;
import ifpb.pdm.ouvidoriaws.enums.TipoTicket;
import ifpb.pdm.ouvidoriaws.services.MensagemService;
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

@Path("mensagem")
@Stateless
public class MensagemResources {
    
    @EJB
    private MensagemService mensagemService;
    
    
    @GET
    @Path("/last/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTicket(@PathParam("id") Long id) {
//        Mensagem lastMensagem = mensagemService.getLastMensagem(id);
        ComposedLastMessage lastMessage = mensagemService.getComposedLastMensagem(id);
        if (lastMessage  == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        return Response.ok().entity(lastMessage).build();
    }
    
    
    @POST
    @Path("/cliente/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response responseCliente(MensagemDto mensagemDto, @Context UriInfo uriInfo) throws URISyntaxException {
        
        try {
            
            Mensagem mensagemSalva = mensagemService.salvarMensagemCliente(mensagemDto);
            return Response
                    .created(new URI("/ouvidoriaws/api/mensagem/" + mensagemSalva.getTicket().getId()))
                    .entity(mensagemSalva)
                    .build();
            
        } catch(Exception ex) {
            
            ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
        }

    }
    
    @POST
    @Path("/auditor/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response responseAuditor(MensagemDto mensagemDto, @Context UriInfo uriInfo) throws URISyntaxException {
        
        try {
            
            Mensagem mensagemSalva = mensagemService.salvarMensagemAuditor(mensagemDto);
            return Response
                    .created(new URI("/ouvidoriaws/api/mensagem/" + mensagemSalva.getTicket().getId()))
                    .entity(mensagemSalva)
                    .build();
            
        } catch(Exception ex) {
            
            ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
        }

    }
    
    
    
    
    
}
