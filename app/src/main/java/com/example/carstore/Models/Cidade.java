package com.example.carstore.Models;

import java.io.Serializable;
import java.util.Objects;

public class Cidade implements Serializable
{
    private Long id;
    private String nome;
    private String ddd;

    public Cidade(Long id, String nome, String ddd)
    {
        this.id = id;
        this.nome = nome;
        this.ddd = ddd;
    }

    public Cidade() { }

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

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    @Override
    public String toString() { return "ID: " +id+ ", Nome: " +nome+ ", DDD: " +ddd; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cidade cidade = (Cidade) o;
        return Objects.equals(id, cidade.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}