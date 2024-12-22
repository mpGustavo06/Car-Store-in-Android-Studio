package com.example.carstore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.carstore.Models.Modelo;
import com.example.carstore.R;

import java.util.ArrayList;

public class ModelosAdapter extends ArrayAdapter<Modelo>
{
    private Context context;
    private ArrayList<Modelo> modelos;

    public ModelosAdapter(Context context, ArrayList<Modelo> modelos)
    {
        super(context, 0, modelos);
        this.context = context;
        this.modelos = modelos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_modelo, parent, false);
        }

        Modelo currentItem = modelos.get(position);

        TextView nome = convertView.findViewById(R.id.tvModelo_nome);
        TextView marca = convertView.findViewById(R.id.tvModelo_marca);
        TextView tipo = convertView.findViewById(R.id.tvModelo_tipo);

        String nomeMarca = currentItem.getMarca().getNome();

        nome.setText(currentItem.getNome());
        marca.setText(nomeMarca);
        tipo.setText(currentItem.getTipo());

        Log.d("MDL.ADP.ITEM.END", "MODELO: "+currentItem.toString());

        return convertView;
    }
}
