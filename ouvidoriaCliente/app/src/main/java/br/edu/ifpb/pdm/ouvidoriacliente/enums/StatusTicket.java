package br.edu.ifpb.pdm.ouvidoriacliente.enums;

/**
 * Created by natarajan on 06/10/17.
 */

public enum StatusTicket {

    OPEN(1, "Aberto"), REPLICATED(2, "Respondido pelo Auditor"), CLOSED(3, "Fechado");

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
