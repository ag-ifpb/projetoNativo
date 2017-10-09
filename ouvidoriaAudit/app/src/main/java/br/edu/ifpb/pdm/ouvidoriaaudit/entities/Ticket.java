package br.edu.ifpb.pdm.ouvidoriaaudit.entities;


import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;

import java.io.Serializable;

import br.edu.ifpb.pdm.ouvidoriaaudit.enums.StatusTicket;
import br.edu.ifpb.pdm.ouvidoriaaudit.enums.TipoTicket;

/**
 * Created by natarajan on 06/10/17.
 */

public class Ticket implements Serializable {

    private Long id;
    private String createdIn;
    private String updatedIn;
    private String resume;
    private Usuario from;
    private String status;
    private String tipo;

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Ticket() {
    }

    public Ticket(Long id, String createdIn, String updatedIn, String resume, Usuario from, String status, String tipo) {
        this.id = id;
        this.createdIn = createdIn;
        this.updatedIn = updatedIn;
        this.resume = resume;
        this.from = from;
        this.status = status;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedIn() {
        return createdIn;
    }

    public void setCreatedIn(String createdIn) {
        this.createdIn = createdIn;
    }

    public String getUpdatedIn() {
        return updatedIn;
    }

    public void setUpdatedIn(String updatedIn) {
        this.updatedIn = updatedIn;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public Usuario getFrom() {
        return from;
    }

    public void setFrom(Usuario from) {
        this.from = from;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb
                .append("\n").append("ID: ").append(id).append("\n")
                .append("Criado em: ").append(dtf.format(LocalDateTime.parse(createdIn))).append("\n")
                .append("Atualizado em: ").append(dtf.format(LocalDateTime.parse(updatedIn))).append("\n")
                .append("Resumo: ").append(resume).append("\n")
                .append("Tipo: ").append(TipoTicket.valueOf(tipo).getDescription()).append("\n")
                .append("Estado: ").append(StatusTicket.valueOf(status).getDescription()).append("\n");

        return sb.toString();
    }
}
