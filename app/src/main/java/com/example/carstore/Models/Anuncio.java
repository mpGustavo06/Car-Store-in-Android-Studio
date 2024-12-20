package com.example.carstore.Models;

import java.io.Serializable;
import java.util.Objects;

public class Anuncio implements Serializable
{
    private Long id;

    private Modelo modelo;
    private Cidade cidade;

    private String descricao;
    private Double valor;
    private Integer ano;
    private Integer km;

    public Anuncio(Long id, Modelo modelo, Cidade cidade, String descricao, Double valor, Integer ano, Integer km) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.ano = ano;
        this.km = km;
        this.modelo = modelo;
        this.cidade = cidade;
    }

    public Anuncio() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return  "Modelo: " + modelo +
                ", Cidade: " + cidade +
                ", Descricao: " + descricao +
                ", Valor: " + valor +
                ", Ano: " + ano +
                ", Km: " + km;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anuncio anuncio = (Anuncio) o;
        return Objects.equals(id, anuncio.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}