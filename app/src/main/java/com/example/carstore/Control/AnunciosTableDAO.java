package com.example.carstore.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Models.Anuncio;

import java.util.LinkedList;

public class AnunciosTableDAO {
    private SQLiteDatabase database;

    public AnunciosTableDAO(Context context)
    {
        database = new DatabaseHelper(context).getWritableDatabase();
    }

    public void close()
    {
        database.close();
    }

    public long newRecord(Anuncio anuncio)
    {
        ContentValues values = new ContentValues();
        values.put("_id", anuncio.getId());
        values.put("descricao", anuncio.getDescricao());
        values.put("valor", anuncio.getValor());
        values.put("ano", anuncio.getAno());
        values.put("km", anuncio.getKm());
        values.put("idModelo", anuncio.getModelo().getId());
        values.put("idCidade", anuncio.getCidade().getId());

        return database.insert("anuncios", null, values);
    }
//
//    public long updateRecord(Anuncio anuncio)
//    {
//
//    }

//    public void deleteRecord(Anuncio anuncio)
//    {
//
//    }
}