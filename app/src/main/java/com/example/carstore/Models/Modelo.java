package com.example.carstore.Models;

import java.io.Serializable;

public class Modelo implements Serializable {
    private Long id;
    private String nome;
    private Long idMarca;
    private Marca marca;

    public Modelo(Long id, String nome, Long idMarca, Marca marca) {
        this.id = id;
        this.nome = nome;
        this.idMarca = idMarca;
        this.marca = marca;
    }

    public Modelo() { }

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

    public Long getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Long idMarca) {
        this.idMarca = idMarca;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
}