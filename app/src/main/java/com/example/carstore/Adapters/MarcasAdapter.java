package com.example.carstore.Adapters;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.carstore.Models.Marca;
import com.example.carstore.R;

import java.util.ArrayList;

public class MarcasAdapter extends ArrayAdapter<Marca>
{
    private Context context;
    private ArrayList<Marca> marcas;

    public MarcasAdapter(Context context, ArrayList<Marca> marcas)
    {
        super(context, 0, marcas);
        this.context = context;
        this.marcas = marcas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_marca, parent, false);
        }

        Marca currentItem = marcas.get(position);

        TextView marca = convertView.findViewById(R.id.tvMarca_nome);

        marca.setText(currentItem.getNome());

        Log.d("MRC.ADP.ITEM.END", "MARCA: "+currentItem.toString());

        return convertView;
    }
}
