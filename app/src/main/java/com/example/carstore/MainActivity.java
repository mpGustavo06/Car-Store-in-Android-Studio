package com.example.carstore;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Utils.NetworkUtils;

public class MainActivity extends AppCompatActivity
{
    private AnunciosDAO anunciosDAO;
    private CidadesDAO cidadesDAO;
    private MarcasDAO marcasDAO;
    private ModelosDAO modelosDAO;

    private Button buttonAnuncios, buttonMarcas, buttonModelos, buttonCidades;

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

        //Referenciando os botões da tela principal
        buttonAnuncios = findViewById(R.id.buttonCadAnuncios);
        buttonMarcas = findViewById(R.id.buttonCadMarcas);
        buttonModelos = findViewById(R.id.buttonCadModelos);
        buttonCidades = findViewById(R.id.buttonCadCidades);

        resetTables();

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
            finish();
        }

        //Eventos
        buttonAnuncios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, AnunciosActivity.class);
                startActivity(intent);
            }
        });

        buttonModelos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, ModelosActivity.class);
                startActivity(intent);
            }
        });

        buttonMarcas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, MarcasActivity.class);
                startActivity(intent);
            }
        });

        buttonCidades.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, CidadesActivity.class);
                startActivity(intent);
            }
        });
    }

    public void resetTables()
    {
        Log.d("MAIN.RESET", "Resetando tabelas...");
        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
        db.execSQL("DELETE FROM anuncios");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='anuncios'");

        db.execSQL("DELETE FROM cidades");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='cidades'");

        db.execSQL("DELETE FROM marcas");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='marcas'");

        db.execSQL("DELETE FROM modelos");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='modelos'");
        db.close();
    }
}