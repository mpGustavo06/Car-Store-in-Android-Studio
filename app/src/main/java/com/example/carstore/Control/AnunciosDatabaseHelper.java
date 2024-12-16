package com.example.carstore.Control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnunciosDatabaseHelper extends SQLiteOpenHelper {
    public AnunciosDatabaseHelper(Context context)
    {
        super(context,"anuncios", null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE anuncios (_id integer PRIMARY KEY, " +
                   "modelo varchar(200), cidade varchar(200), descricao varchar(200), " +
                   "valor float, ano integer, km integer, idModelo integer, idCidade integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
