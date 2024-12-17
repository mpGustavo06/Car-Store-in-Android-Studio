package com.example.carstore.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.carstore.Models.Anuncio;

public class AnunciosTableDAO {
    private SQLiteDatabase database;

    public AnunciosTableDAO(Context context)
    {
        database = new AnunciosDatabaseHelper(context).getWritableDatabase();
    }

    public void close()
    {
        database.close();
    }

    public long newRecord(Anuncio anuncio)
    {
        ContentValues values = new ContentValues();
        values.put("modelo", anuncio.getModelo().getNome());
        values.put("cidade", anuncio.getCidade().getNome());
        values.put("descricao", anuncio.getDescricao());
        values.put("valor", anuncio.getValor());
        values.put("ano", anuncio.getAno());
        values.put("km", anuncio.getKm());
        values.put("idModelo", anuncio.getIdModelo());
        values.put("idCidade", anuncio.getIdCidade());

        return database.insert("anuncios", null, values);
    }
}
