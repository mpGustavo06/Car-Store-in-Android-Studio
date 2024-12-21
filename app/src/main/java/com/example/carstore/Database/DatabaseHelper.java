package com.example.carstore.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public DatabaseHelper(Context context)
    {
        super(context,"carstore", null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE anuncios (_id integer PRIMARY KEY AUTOINCREMENT, " +
                    "descricao text, valor float, ano integer, km integer, idModelo integer, idCidade integer, " +
                    "FOREIGN KEY(idModelo) REFERENCES modelos(_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY(idCidade) REFERENCES cidades(_id) ON DELETE CASCADE );");

        db.execSQL("CREATE TABLE cidades (_id integer PRIMARY KEY AUTOINCREMENT, " +
                    "nome text, ddd text);");

        db.execSQL("CREATE TABLE marcas (_id integer PRIMARY KEY AUTOINCREMENT, " +
                    "nome text);");

        db.execSQL("CREATE TABLE modelos (_id integer PRIMARY KEY AUTOINCREMENT, " +
                    "nome text, tipo text, idMarca integer, " +
                    "FOREIGN KEY(idMarca) REFERENCES marcas(_id) ON DELETE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
