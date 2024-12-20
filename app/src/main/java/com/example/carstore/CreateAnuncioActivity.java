package com.example.carstore;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.carstore.Adapters.AnunciosAdapter;
import com.example.carstore.Control.CidadesTableDAO;
import com.example.carstore.Control.MarcasTableDAO;
import com.example.carstore.Control.ModelosTableDAO;
import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Control.AnunciosTableDAO;
import com.example.carstore.Models.Anuncio;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Marca;
import com.example.carstore.Models.Modelo;

public class CreateAnuncioActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private Cursor cursor;
    private ArrayAdapter<Modelo> adapterModelo;
    private ArrayAdapter<Cidade> adapterCidade;

    private Anuncio anuncio;
    private AnunciosAdapter adapter;
    private AnunciosTableDAO anunciosDAO;

    private Modelo modelo = new Modelo();
    private ModelosTableDAO modelosDao;

    private Cidade cidade = new Cidade();
    private CidadesTableDAO cidadesDao;

    private EditText editTextAno, editTextKm, editTextDescricao, editTextValor;
    private Button buttonAnunciar, buttonCancelar;
    private Spinner spinnerModelo, spinnerCidade;

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
        anunciosDAO = new AnunciosTableDAO(getApplicationContext());
        cidadesDao = new CidadesTableDAO(getApplicationContext());
        modelosDao = new ModelosTableDAO(getApplicationContext());

        //Inicialização dos componentes do layout
        editTextAno = findViewById(R.id.editTextAno);
        editTextKm = findViewById(R.id.editTextKm);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextValor = findViewById(R.id.editTextValor);

        buttonAnunciar = findViewById(R.id.buttonAnunciar);
        buttonCancelar = findViewById(R.id.buttonCancelar);

        spinnerModelo = findViewById(R.id.spinnerModelo);
        spinnerCidade = findViewById(R.id.spinnerCidade);

        //Banco de Dados
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        cursor = database.query("anuncios", new String[] {"_id", "descricao", "valor", "ano", "km", "idModelo", "idCidade"}, null, null, null, null, "idModelo");

        adapter =  new AnunciosAdapter(this, cursor, 0);

        //Configurando Spinners
        adapterModelo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelosDao.getModelosList());
        adapterModelo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModelo.setAdapter(adapterModelo);

        adapterCidade = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cidadesDao.getCidadesList());
        adapterCidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCidade.setAdapter(adapterCidade);

        //Eventos
        buttonAnunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                novoAnuncio(view);
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                cancelar(view);
            }
        });

        spinnerModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                Log.d("SPINNER.MDL.TST: ", "MODELO: "+modelo.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { Log.d("MODELO TESTE: ", "Nenhum modelo selecionado."); }
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
                Log.d("SPINNER.CDD.TST: ", "CIDADE: "+cidade.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { Log.d("CIDADE TESTE: ", "Nenhuma cidade selecionada."); }
        });
    }

    public void novoAnuncio(View view)
    {
        Log.d("CDD.NEW.ANC.TESTE", "CIDADE: "+cidade.toString());
        Log.d("MDL.NEW.ANC.TESTE", "MODELO: "+modelo.toString());

        anuncio = new Anuncio(
                modelo,
                cidade,
                editTextDescricao.getText().toString(),
                Double.parseDouble(editTextValor.getText().toString()),
                Integer.parseInt(editTextAno.getText().toString()),
                Integer.parseInt(editTextKm.getText().toString()));

        Log.d("NEW.ANC.TESTE", "ANUNCIO: "+anuncio.toString());

        long id = anunciosDAO.newRecord(anuncio);

        if (id > 0)
        {
            cursor.requery();
            adapter.notifyDataSetChanged();
            showToast("Registro inserido com sucesso!");
        }
        else
        {
            showToast("Erro na inserção do registro.");
            Log.d("NEW.ANC.ERROR", "Erro ao inserir o registro.");
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