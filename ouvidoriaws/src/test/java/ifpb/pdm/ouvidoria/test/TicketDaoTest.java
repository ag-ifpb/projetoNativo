///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package ifpb.pdm.ouvidoria.test;
//
//import ifpb.pdm.ouvidoriaws.daos.TicketDao;
//import ifpb.pdm.ouvidoriaws.daos.exceptions.TicketException;
//import ifpb.pdm.ouvidoriaws.entities.Ticket;
//import ifpb.pdm.ouvidoriaws.services.TicketService;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.ejb.EJBException;
//import javax.ejb.embeddable.EJBContainer;
//import javax.naming.Context;
//
//import junit.framework.TestCase;
//import org.junit.Ignore;
//
//
//
///**
// *
// * @author natarajan
// */
//@Ignore
//public class TicketDaoTest extends TestCase{
//    
//    private EJBContainer container;
//    private Context namingContext;
//    private TicketDao ticketDao;
//    private TicketService ticketService;
//     
//    @Override
//    protected void setUp() throws Exception {
//       super.setUp();
//        container = EJBContainer.createEJBContainer();
//       namingContext = container.getContext();
//       ticketDao = (TicketDao) namingContext.lookup("java:global/classes/TicketDao!ifpb.pdm.ouvidoriaws.daos.TicketDao");
//       ticketService = (TicketService) namingContext.lookup("java:global/classes/TicketService!ifpb.pdm.ouvidoriaws.services.TicketService");
//    }
//    
//    @Override
//    protected void tearDown() throws Exception {
//       super.tearDown();
//       namingContext.close();
//       container.close();
//    }
//    
//    
////    public final void testAdd() {
////        Usuario usuario = new Usuario();
////        usuario.setEmail("teste@test");
////        usuario.setId(1L);
////        usuario.setNome("teste");
////        usuario.setTipoUsario(TipoUsuario.ADMINISTRADOR);
////        
////        Ticket ticket = new Ticket();
////        ticket.setFrom(usuario);
////        ticket.setResume("asdf");
////        ticket.setResume("asdf");
////        Ticket salvar = ticketDao.salvar(ticket);
////        
////        assertNotNull(ticket);
////    }
//    
////    public final void testInactives() {
////        
////        List<Ticket> inactive = ticketDao.getInactive();
////        
////        assertEquals(0, inactive.size());
//////        assertEquals(1, inactive.size());
//////        assertNotNull(inactive);
////    }
//    
////    public final void testOpensByUser() {
////        List<Ticket> inactive = ticketDao.getOpensByUser(1L);
////        assertEquals(1, inactive.size());
////        
////        List<Ticket> inactive2 = ticketDao.getOpensByUser(2L);
////        assertEquals(0, inactive2.size());
////    }
//    
////    public final void testCancelByClient() {    
////        ticketService.cancelByCliente(1L);
////        List<Ticket> inactive = ticketDao.getOpensByUser(1L);
////        assertEquals(0, inactive.size());    
////    }
//    
////    @Test(expected = javax.ejb.EJBException.class)
////    public final void testCancelByAuditorWithException() {    
////        try {
////            ticketService.cancelByAuditor(1L);
////            List<Ticket> inactive = ticketDao.getOpensByUser(1L);
////            assertEquals(0, inactive.size());    
////        } catch(EJBException e) {
////            
////        }
////    }
//    
//    
//    public final void testCancelByAuditorNoException() {    
//        try {
//            ticketService.cancelByAuditor(1L);
//            List<Ticket> inactive = ticketDao.getOpensByUser(1L);
//            assertEquals(0, inactive.size());    
//        } catch (TicketException ex) {
//            Logger.getLogger(TicketDaoTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//    }
//    
//    
//    
//}
