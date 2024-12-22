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
import com.example.carstore.Models.Modelo;
import com.example.carstore.R;

import java.util.List;

public class SpinnerModeloAdapter extends ArrayAdapter<Modelo>
{
    private Context context;
    private List<Modelo> modelos;

    public SpinnerModeloAdapter(@NonNull Context context, @NonNull List<Modelo> modelos)
    {
        super(context, 0, modelos);
        this.context = context;
        this.modelos = modelos;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_modelo, parent, false);
        }

        Modelo currentItem = modelos.get(position);

        TextView marca = convertView.findViewById(R.id.tvSpinnerMarca);
        TextView modelo = convertView.findViewById(R.id.tvSpinnerModelo);

        String mrc = currentItem.getMarca().getNome();
        String mdl = currentItem.getNome();

        marca.setText(mrc);
        modelo.setText(mdl);

        Log.d("SPINNER.MDL.ITEM.END", "MODELO: "+currentItem.toString());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_modelo, parent, false);
        }

        TextView marca = convertView.findViewById(R.id.tvSpinnerMarca);
        TextView modelo = convertView.findViewById(R.id.tvSpinnerModelo);

        marca.setText(modelos.get(position).getMarca().getNome());
        modelo.setText(modelos.get(position).getNome());

        return convertView;
    }
}