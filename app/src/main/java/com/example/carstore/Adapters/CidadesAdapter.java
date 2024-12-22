package com.example.carstore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Marca;
import com.example.carstore.R;

import java.util.ArrayList;

public class CidadesAdapter extends ArrayAdapter<Cidade>
{
    private Context context;
    private ArrayList<Cidade> cidades;

    public CidadesAdapter(Context context, ArrayList<Cidade> cidades)
    {
        super(context, 0, cidades);
        this.context = context;
        this.cidades = cidades;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cidade, parent, false);
        }

        Cidade currentItem = cidades.get(position);

        TextView cidade = convertView.findViewById(R.id.tvCidade_nome);
        TextView ddd = convertView.findViewById(R.id.tvCidade_ddd);

        cidade.setText(currentItem.getNome());
        ddd.setText(currentItem.getDdd());

        Log.d("CDD.ADP.ITEM.END", "CIDADE: "+currentItem.toString());

        return convertView;
    }
}
