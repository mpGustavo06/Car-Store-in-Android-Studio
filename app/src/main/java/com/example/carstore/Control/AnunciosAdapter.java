package com.example.carstore.Control;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.example.carstore.Models.Anuncio;
import com.example.carstore.R;
import java.util.ArrayList;

public class AnunciosAdapter extends CursorAdapter {
    private Context context;
    private Cursor anuncios;

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
        TextView modelo = view.findViewById(R.id.tv_modelo);
        TextView ano = view.findViewById(R.id.tv_ano);
        TextView km = view.findViewById(R.id.tv_km);
        TextView valor = view.findViewById(R.id.tv_valor);

        modelo.setText(cursor.getString(cursor.getColumnIndexOrThrow("modelo")));
        ano.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("ano"))));
        km.setText(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("km"))));
        valor.setText("R$ "+cursor.getString(cursor.getColumnIndexOrThrow("valor")));
    }
}