package br.edu.ifpb.pdm.ouvidoriacliente.enums;

/**
 * Created by natarajan on 05/10/17.
 */

public enum TipoUsuario {

    /**
     *
     */
    ERRO(0, "ERRO"), CLIENTE(1, "Cliente"), AUDITOR(2, "Auditor"), ADMINISTRADOR(3, "Administrador");

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
