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
public enum TipoTicket {
    
    /**
     *
     */
    INFORMACAO(1, "Informação"), ELOGIO(2, "Elogio"), RECLAMACAO(3, "Reclamação");
    
    private final int id;
    private final String description;
    
    TipoTicket(int type, String description) {
        this.id = type;
        this.description = description;
    }
    
    public int getId() {
        return id;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static TipoTicket parse(int id) {   
        for(TipoTicket role : TipoTicket.values()) {
            if(role.getId() == id) {
                return role;
            }
        } return null;
    }
    
}
