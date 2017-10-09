package br.edu.ifpb.pdm.ouvidoriaaudit.enums;

/**
 * Created by natarajan on 06/10/17.
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
