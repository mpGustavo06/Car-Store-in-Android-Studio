package com.example.carstore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.carstore.Models.Anuncio;
import com.example.carstore.R;

import java.util.ArrayList;

public class AnunciosAdapter extends ArrayAdapter<Anuncio>
{
    private Context context;
    private ArrayList<Anuncio> anuncios;

    public AnunciosAdapter(Context context, ArrayList<Anuncio> anuncios)
    {
        super(context, 0, anuncios);
        this.context = context;
        this.anuncios = anuncios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_anuncio, parent, false);
        }

        Anuncio currentItem = anuncios.get(position);

        TextView modelo = convertView.findViewById(R.id.tvAnuncio_modelo);
        TextView ano = convertView.findViewById(R.id.tvAnuncio_ano);
        TextView km = convertView.findViewById(R.id.tvAnuncio_km);
        TextView valor = convertView.findViewById(R.id.tvAnuncio_valor);

        String marca = currentItem.getModelo().getMarca().getNome();
        String mdl = currentItem.getModelo().getNome();

        modelo.setText(String.format("%s %s", marca, mdl));
        ano.setText(String.valueOf(currentItem.getAno()));
        km.setText(String.valueOf(currentItem.getKm()));
        valor.setText("R$ "+String.valueOf(currentItem.getValor()));

        Log.d("ANC.ADP.ITEM.END", "ANUNCIO: "+currentItem.toString());

        return convertView;
    }
}