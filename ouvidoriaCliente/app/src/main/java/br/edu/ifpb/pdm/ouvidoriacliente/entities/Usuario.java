package br.edu.ifpb.pdm.ouvidoriacliente.entities;

import java.io.Serializable;

import br.edu.ifpb.pdm.ouvidoriacliente.enums.TipoUsuario;

/**
 * Created by natarajan on 05/10/17.
 */

public class Usuario implements Serializable{

    private Long id;

    private String nome;
    private String email;

    private String tipo;

    public Usuario() {
        this.tipo = "CLIENTE";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoString() {
        Integer tipoInt = Integer.parseInt(tipo);
        return TipoUsuario.parse(tipoInt).getDescription();
    }

    public int getTipoUsuarioInt() {
        return Integer.parseInt(tipo);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb
                .append("ID: ").append(id).append("\n")
                .append("Nome: ").append(nome).append("\n")
                .append("Email: ").append(email).append("\n")
                .append("Tipo: ").append(getTipoString());

        return sb.toString();
    }


}
