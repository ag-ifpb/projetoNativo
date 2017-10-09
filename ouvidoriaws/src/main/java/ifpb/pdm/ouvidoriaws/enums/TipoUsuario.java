/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pdm.ouvidoriaws.enums;

/**
 *
 * @author natarajan
 */
public enum TipoUsuario {
    
    /**
     *
     */
    CLIENTE(1, "Cliente"), AUDITOR(2, "Auditor"), ADMINISTRADOR(3, "Administrador");
    
    private final int id;
    private final String description;
    
    TipoUsuario(int type, String description) {
        this.id = type;
        this.description = description;
    }
    
    public int getId() {
        return id;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static TipoUsuario parse(int id) {   
        for(TipoUsuario role : TipoUsuario.values()) {
            if(role.getId() == id) {
                return role;
            }
        } return null;
    }
    
}
