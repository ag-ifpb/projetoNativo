/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.services;

import ifpb.pdm.ouvidoriaws.daos.UsuarioDao;
import ifpb.pdm.ouvidoriaws.daos.exceptions.AuditorException;
import ifpb.pdm.ouvidoriaws.entities.Usuario;
import ifpb.pdm.ouvidoriaws.enums.TipoUsuario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

/**
 *
 * @author natarajan
 */
@Stateless
public class UsuarioService {

    @EJB
    private UsuarioDao usuarioDao;
    
    public Usuario salvar(Usuario usuario) throws AuditorException {
        
        if (usuario.getTipoUsuario() == TipoUsuario.AUDITOR) {
            Usuario auditor = usuarioDao.getAuditor();
        
            if (auditor != null) {
                throw new AuditorException("Não pode haver mais de um auditor.");
            }
        } else if (usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
            throw new AuditorException("Cadastro inválido.");
        }
        
        
        if (!usuarioDao.exists(usuario)) {
            return usuarioDao.salvar(usuario);
        } else {
            throw new AuditorException("Já existe usuário com o email informado");
        }
        
    }
    
    
    public Usuario deletarAuditor(Long id) throws AuditorException {
        Usuario usuario = getById(id);
        Usuario auditor = usuarioDao.getAuditor();
        
        if (auditor != null && auditor.equals(usuario)) {
            usuario.setTipoUsuario(TipoUsuario.CLIENTE.getId());
            return usuarioDao.atualizar(usuario);
        } else {
            throw new AuditorException("Não foi possível tornar o usuário em cliente. Verifique se o usuário é mesmo auditor.");
        }
    }
    
    
    public Usuario tornarAuditor(Long id) throws EJBException, AuditorException{
        Usuario auditor = usuarioDao.getAuditor();
        Usuario usuario = getById(id);
        
        if (usuario == null) {
            throw new EJBException("Usuário inexistente.");
        }
        
        if (auditor != null) {
//            new ApplicationException(id, ins)
            
            throw new AuditorException("Não pode haver mais de um auditor.");
        } else {
            
            usuario.setTipoUsuario(TipoUsuario.AUDITOR.getId());
            return usuarioDao.atualizar(usuario);
        }
    }
    
    public Usuario getById(Long id) {
        return usuarioDao.getById(id);
    }
    
    public Usuario getClientByEmail(String email) {
        return usuarioDao.getClientByEmail(email);
    }
    
    public List<Usuario> getAll() {
        return usuarioDao.getAll();
    }
    
    
}
