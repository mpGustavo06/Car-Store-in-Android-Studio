package com.example.carstore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carstore.Adapters.SpinnerCidadeAdapter;
import com.example.carstore.Adapters.SpinnerModeloAdapter;
import com.example.carstore.Control.CidadesDAO;
import com.example.carstore.Control.ModelosDAO;
import com.example.carstore.Control.AnunciosDAO;
import com.example.carstore.Models.Anuncio;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Modelo;

public class CreateAnuncioActivity extends AppCompatActivity
{
    private EditText editTextAno, editTextKm, editTextDescricao, editTextValor;
    private Button buttonAnunciar, buttonCancelar;
    private Spinner spinnerModelo, spinnerCidade;

    private Anuncio anuncio;
    private AnunciosDAO anunciosDAO;

    private Modelo modelo = new Modelo();
    private SpinnerModeloAdapter spinnerModeloAdapter;
    private ModelosDAO modelosDao;

    private Cidade cidade = new Cidade();
    private SpinnerCidadeAdapter spinnerCidadeAdapter;
    private CidadesDAO cidadesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anunciar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Mantendo registros do servidor
        anunciosDAO = new AnunciosDAO(getApplicationContext());
        cidadesDao = new CidadesDAO(getApplicationContext());
        modelosDao = new ModelosDAO(getApplicationContext());

        //Inicialização dos componentes do layout
        editTextAno = findViewById(R.id.editTextAno);
        editTextKm = findViewById(R.id.editTextKm);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextValor = findViewById(R.id.editTextValor);

        buttonAnunciar = findViewById(R.id.buttonAnunciar);
        buttonCancelar = findViewById(R.id.buttonCancelar);

        spinnerModelo = findViewById(R.id.spinnerModelo);
        spinnerCidade = findViewById(R.id.spinnerCidade);

        //Configurando Spinners
        spinnerModeloAdapter = new SpinnerModeloAdapter(this, modelosDao.getModelosDBList());
        spinnerModelo.setAdapter(spinnerModeloAdapter);

        spinnerCidadeAdapter = new SpinnerCidadeAdapter(this, cidadesDao.getCidadesDBList());
        spinnerCidade.setAdapter(spinnerCidadeAdapter);

        //Eventos
        buttonAnunciar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                novoAnuncio(view);
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                cancelar(view);
            }
        });

        spinnerModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            boolean isFirstSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                if (isFirstSelection)
                {
                    isFirstSelection = false;
                    return;
                }

                modelo = (Modelo) adapterView.getItemAtPosition(position);
                Log.d("SPINNER.MDL.TESTE", "MODELO: "+modelo.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { Log.d("SPINNER.MDL.ERROR", "Nenhum modelo selecionado."); }
        });

        spinnerCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            boolean isFirstSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                if (isFirstSelection)
                {
                    isFirstSelection = false;
                    return;
                }

                cidade = (Cidade) adapterView.getItemAtPosition(position);
                Log.d("SPINNER.CDD.TESTE", "CIDADE: "+cidade.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { Log.d("SPINNER.CDD.ERROR", "Nenhuma cidade selecionada."); }
        });
    }

    public void novoAnuncio(View view)
    {
        anuncio = new Anuncio(
            modelo,
            cidade,
            editTextDescricao.getText().toString(),
            Double.parseDouble(editTextValor.getText().toString()),
            Integer.parseInt(editTextAno.getText().toString()),
            Integer.parseInt(editTextKm.getText().toString()));

        Log.d("NEW.ANC.TESTE", "ANUNCIO: "+anuncio.toString());

        long id = 0;

        id = anunciosDAO.newRecord(anuncio);
        anunciosDAO.postRecord(anuncio);

        if (id > 0)
        {
            showToast("Anuncio criado com sucesso no BANCO DE DADOS!");
            Log.d("NEW.ANC.INSERT.DB", "Inserido com sucesso.");
        }
        else
        {
            showToast("Erro ao criar um novo anuncio no BANDO DE DADOS!");
            Log.d("NEW.ANC.ERROR.DB", "Erro ao inserir o registro.");
        }

        limpar();
    }

    public void cancelar(View view)
    {
        limpar();
    }

    //MÉTODOS AUXILIARES
    public void limpar()
    {
        editTextAno.setText("");
        editTextKm.setText("");
        editTextDescricao.setText("");
        editTextValor.setText("");
        spinnerModelo.setSelection(0);
        spinnerCidade.setSelection(0);
    }

    public void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return;
    }
}