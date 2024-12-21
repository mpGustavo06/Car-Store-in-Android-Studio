package com.example.carstore.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.carstore.Control.ModelosTableDAO;
import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Models.Modelo;
import com.example.carstore.R;


public class AnunciosAdapter extends CursorAdapter
{
    private ModelosTableDAO modelosDAO;
    private SQLiteDatabase dbRead;

    public AnunciosAdapter(Context context, Cursor cursor, int flags)
    {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.item_anuncio, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        modelosDAO = new ModelosTableDAO(context);
        dbRead = new DatabaseHelper(context).getReadableDatabase();

        TextView modelo = view.findViewById(R.id.tv_modelo);
        TextView ano = view.findViewById(R.id.tv_ano);
        TextView km = view.findViewById(R.id.tv_km);
        TextView valor = view.findViewById(R.id.tv_valor);

        int idModelo = cursor.getInt(cursor.getColumnIndexOrThrow("idModelo"));
        Modelo mdl = modelosDAO.getModeloById(dbRead, idModelo);

        modelo.setText(mdl.getMarca().getNome()+" "+mdl.getNome());
        ano.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("ano"))));
        km.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("km"))));
        valor.setText("R$ "+cursor.getString(cursor.getColumnIndexOrThrow("valor")));
    }
}