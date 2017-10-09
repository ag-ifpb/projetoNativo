package br.edu.ifpb.pdm.ouvidoriaadm.entity;

/**
 * Created by natarajan on 05/10/17.
 */

public class ErrorMessage {

    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}