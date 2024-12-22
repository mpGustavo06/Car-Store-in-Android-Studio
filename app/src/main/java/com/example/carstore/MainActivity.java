package com.example.carstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carstore.Control.AnunciosDAO;
import com.example.carstore.Control.CidadesDAO;
import com.example.carstore.Control.MarcasDAO;
import com.example.carstore.Control.ModelosDAO;
import com.example.carstore.Utils.NetworkUtils;

public class MainActivity extends AppCompatActivity
{
    private AnunciosDAO anunciosDAO;
    private CidadesDAO cidadesDAO;
    private MarcasDAO marcasDAO;
    private ModelosDAO modelosDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Carregando registros existentes no servidor
        cidadesDAO = new CidadesDAO(getApplicationContext());
        cidadesDAO.carregarCidadesServidor();

        marcasDAO = new MarcasDAO(getApplicationContext());
        marcasDAO.carregarMarcasServidor();

        modelosDAO = new ModelosDAO(getApplicationContext());
        modelosDAO.carregarModelosServidor();

        anunciosDAO = new AnunciosDAO(getApplicationContext());
        anunciosDAO.carregarAnunciosServidor();

        //Testando se existe conexão com a internet
        if (!NetworkUtils.isConnected(this))
        {
            Toast.makeText(this, "Sem conexão com a internet. O aplicativo não pode ser usado offline.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void Anuncios(View view)
    {
        Intent intent = new Intent(MainActivity.this, AnunciosActivity.class);
        startActivity(intent);
    }

//    public void Marcas(View view)
//    {
//        Intent intent = new Intent(MainActivity.this, MarcasActivity.class);
//        startActivity(intent);
//    }
//
//    public void Modelos(View view)
//    {
//        Intent intent = new Intent(MainActivity.this, ModelosActivity.class);
//        startActivity(intent);
//    }
//
//    public void Cidades(View view)
//    {
//        Intent intent = new Intent(MainActivity.this, CidadesActivity.class);
//        startActivity(intent);
//    }
}