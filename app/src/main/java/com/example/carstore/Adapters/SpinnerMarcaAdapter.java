package com.example.carstore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.carstore.Models.Marca;
import com.example.carstore.R;

import java.util.List;

public class SpinnerMarcaAdapter extends ArrayAdapter<Marca>
{
    private Context context;
    private List<Marca> marcas;

    public SpinnerMarcaAdapter(@NonNull Context context, @NonNull List<Marca> marcas)
    {
        super(context, 0, marcas);
        this.context = context;
        this.marcas = marcas;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_marca, parent, false);
        }

        Marca currentItem = marcas.get(position);

        TextView marcaView = convertView.findViewById(R.id.tvSpinnerMarca);

        marcaView.setText(currentItem.getNome());

        Log.d("SPINNER.MRC.ITEM.END", "MARCA: "+currentItem.toString());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_marca, parent, false);
        }

        TextView marcaView = convertView.findViewById(R.id.tvSpinnerMarca);

        marcaView.setText(marcas.get(position).getNome());

        return convertView;
    }
}
