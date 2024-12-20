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

import com.example.carstore.Control.CidadesTableDAO;
import com.example.carstore.Control.MarcasTableDAO;
import com.example.carstore.Control.ModelosTableDAO;
import com.example.carstore.Utils.NetworkUtils;

public class MainActivity extends AppCompatActivity
{
    private CidadesTableDAO cidadesDAO;
    private MarcasTableDAO marcasDAO;
    private ModelosTableDAO modelosDAO;

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

        cidadesDAO = new CidadesTableDAO(getApplicationContext());
        cidadesDAO.carregarCidadesServidor();

        marcasDAO = new MarcasTableDAO(getApplicationContext());
        marcasDAO.carregarMarcasServidor();

        modelosDAO = new ModelosTableDAO(getApplicationContext());
        modelosDAO.carregarModelosServidor();

        //Testando se existe conexão com a internet
        if (!NetworkUtils.isConnected(this))
        {
            Toast.makeText(this, "Sem conexão com a internet. O aplicativo não pode ser usado offline.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void cadastroAnuncio(View view)
    {
        Intent intent = new Intent(MainActivity.this, AnunciosActivity.class);
        startActivity(intent);
    }
}