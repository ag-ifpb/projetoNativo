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
public enum StatusTicket {
    
    /**
     *
     */
    OPEN(1, "Aberta"), REPLICATED(2, "Respondida"), CLOSED(3, "Fechada");
    
    private final int id;
    private final String description;
    
    StatusTicket(int type, String description) {
        this.id = type;
        this.description = description;
    }
    
    public int getId() {
        return id;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static StatusTicket parse(int id) {   
        for(StatusTicket role : StatusTicket.values()) {
            if(role.getId() == id) {
                return role;
            }
        } return null;
    }
    
}
