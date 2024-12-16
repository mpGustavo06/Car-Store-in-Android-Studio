package com.example.carstore.Models;

import java.io.Serializable;

public class Anuncio implements Serializable {
    private Long id;
    private Modelo modelo;
    private Cidade cidade;
    private String descricao;
    private Double valor;
    private Integer ano;
    private Integer km;
    private Long idModelo;
    private Long idCidade;

    public Anuncio(Long id, Modelo modelo, Cidade cidade, String descricao, Double valor, Integer ano, Integer km, Long idModelo, Long idCidade) {
        this.id = id;
        this.modelo = modelo;
        this.cidade = cidade;
        this.descricao = descricao;
        this.valor = valor;
        this.ano = ano;
        this.km = km;
        this.idModelo = idModelo;
        this.idCidade = idCidade;
    }

    public Anuncio() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Long idModelo) {
        this.idModelo = idModelo;
    }

    public Long getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(Long idCidade) {
        this.idCidade = idCidade;
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
}