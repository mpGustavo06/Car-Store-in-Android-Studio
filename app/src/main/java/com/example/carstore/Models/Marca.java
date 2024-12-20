package com.example.carstore.Models;

import java.io.Serializable;
import java.util.Objects;

public class Marca implements Serializable
{
    private Long id;

    private String nome;

    public Marca(Long id, String nome)
    {
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
    public String toString() { return "ID: " +id+ ", Nome: " +nome; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marca marca = (Marca) o;
        return Objects.equals(id, marca.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}