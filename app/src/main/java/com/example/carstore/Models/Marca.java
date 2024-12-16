package com.example.carstore.Models;

import java.io.Serializable;

public class Marca implements Serializable {
    private Long id;
    private String nome;

    public Marca(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Marca() { }

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

    @Override
    public String toString() {
        return nome;
    }
}