package com.example.carstore.Models;

import java.io.Serializable;
import java.util.Objects;

public class Modelo implements Serializable
{
    private Long id;

    private String nome;
    private Marca marca;
    private String tipo;

    public Modelo(Long id, String nome, Marca marca, String tipo)
    {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    @Override
    public String toString() { return "ID: " + id + ", Nome: " + nome + ", Tipo: " + tipo; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Modelo modelo = (Modelo) o;
        return Objects.equals(id, modelo.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}