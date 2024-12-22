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
import com.example.carstore.Models.Cidade;
import com.example.carstore.R;

import java.util.List;

public class SpinnerCidadeAdapter extends ArrayAdapter<Cidade>
{
    private Context context;
    private List<Cidade> cidades;

    public SpinnerCidadeAdapter(@NonNull Context context, @NonNull List<Cidade> cidades)
    {
        super(context, 0, cidades);
        this.context = context;
        this.cidades = cidades;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_cidade, parent, false);
        }

        Cidade currentItem = cidades.get(position);

        TextView cidadeView = convertView.findViewById(R.id.tvSpinnerCidade);
        TextView dddView = convertView.findViewById(R.id.tvSpinnerDdd);

        String cidade = currentItem.getNome();
        String ddd = currentItem.getDdd();

        cidadeView.setText(cidade);
        dddView.setText("( "+ddd+" )");

        Log.d("SPINNER.CDD.ITEM.END", "CIDADE: "+currentItem.toString());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_cidade, parent, false);
        }

        TextView cidadeView = convertView.findViewById(R.id.tvSpinnerCidade);
        TextView dddView = convertView.findViewById(R.id.tvSpinnerDdd);

        cidadeView.setText(cidades.get(position).getNome());
        dddView.setText("( "+cidades.get(position).getDdd()+" )");

        return convertView;
    }
}
