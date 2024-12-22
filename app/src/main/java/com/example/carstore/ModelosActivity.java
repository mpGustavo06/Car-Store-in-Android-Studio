package com.example.carstore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carstore.Adapters.ModelosAdapter;
import com.example.carstore.Adapters.SpinnerMarcaAdapter;
import com.example.carstore.Control.CarStoreAPIService;
import com.example.carstore.Control.MarcasDAO;
import com.example.carstore.Control.ModelosDAO;
import com.example.carstore.Models.Anuncio;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Marca;
import com.example.carstore.Models.Modelo;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelosActivity extends AppCompatActivity
{
    private EditText edNomeModelo, edTipoModelo;
    private ListView modelosListView;
    private Button buttonCadastrar, buttonAlterar, buttonRemover, buttonLimpar;
    private Spinner spinnerMarca;

    private Modelo modelo;
    private ModelosDAO modelosDAO;
    private ModelosAdapter modelosAdapter;
    private ArrayList<Modelo> modelosList = new ArrayList<>();

    private Marca marca = new Marca();
    private MarcasDAO marcasDao;
    private SpinnerMarcaAdapter spinnerMarcaAdapter;

    private CarStoreAPIService apiService;
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modelos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicialização do Retrofit
        RetrofitUtils.getInstance(BASE_URL);
        apiService = RetrofitUtils.createService(CarStoreAPIService.class);

        //Inicialização de MarcaDAO
        marcasDao = new MarcasDAO(getApplicationContext());
        modelosDAO = new ModelosDAO(getApplicationContext());

        //Inicialização dos componentes do layout
        edNomeModelo = findViewById(R.id.editTextMdlNome);
        edTipoModelo = findViewById(R.id.editTextMdlTipo);
        buttonCadastrar = findViewById(R.id.btnModeloCadastrar);
        buttonAlterar = findViewById(R.id.btnModeloAlterar);
        buttonRemover = findViewById(R.id.btnModeloRemover);
        buttonLimpar = findViewById(R.id.btnModeloLimpar);
        modelosListView = findViewById(R.id.modelosList);
        spinnerMarca = findViewById(R.id.spinnerMdlMarca);

        //Configurando Spinner
        spinnerMarcaAdapter = new SpinnerMarcaAdapter(this, marcasDao.getMarcasDBList());
        spinnerMarca.setAdapter(spinnerMarcaAdapter);

        //Adapter & List
        modelosAdapter = new ModelosAdapter(this, modelosList);
        modelosListView.setAdapter(modelosAdapter);
        modelosListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        getModelosServerList();

        //Eventos
        spinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

                marca = (Marca) adapterView.getItemAtPosition(position);
                Log.d("SPINNER.MRC.TESTE", "MARCA: "+marca.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { Log.d("SPINNER.MRC.ERROR", "Nenhuma marca selecionado."); }
        });

        buttonCadastrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                cadastrar(view);
            }
        });

        buttonAlterar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alterar(view);
            }
        });

        buttonRemover.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                remover(view);
            }
        });

        buttonLimpar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                limpar(view);
            }
        });
    }

    public void cadastrar(View view)
    {
        modelo = new Modelo(
                marca,
                edNomeModelo.getText().toString(),
                edTipoModelo.getText().toString()
        );

        Log.d("NEW.MDL.TESTE", "MODELO: "+modelo.toString());

        long id = modelosDAO.newRecord(modelo);
        modelosDAO.postRecord(modelo);

        if (id > 0)
        {
            showToast("Modelo criado com sucesso!");
            Log.d("NEW.MDL.INSERT.DB", "Inserido com sucesso. ID: "+id);

            modelosList.add(modelo);
            modelosAdapter.notifyDataSetChanged();
        }
        else
        {
            showToast("Erro ao criar um novo modelo!");
            Log.d("NEW.MDL.ERROR.DB", "Erro ao inserir o registro.");
        }

        limpar(view);
    }

    public void alterar(View view)
    {

    }

    public void remover(View view)
    {

    }

    public void limpar(View view)
    {
        edNomeModelo.setText("");
        edTipoModelo.setText("");
        spinnerMarca.setSelection(0);
    }

    public void getModelosServerList()
    {
        Call<List<Modelo>> call = apiService.createGetModelos();

        call.enqueue(new Callback<List<Modelo>>()
        {
            @Override
            public void onResponse(Call<List<Modelo>> call, Response<List<Modelo>> response)
            {
                if (response.code() == 200)
                {
                    modelosList.clear();
                    modelosList.addAll(response.body());
                    modelosAdapter.notifyDataSetChanged();
                    Log.d("ACT.MDL.RESPONSE", "LISTA DE MODELOS: "+modelosList.toString());
                }
                else
                {
                    Log.d("ACT.MDL.RESPONSE.ERROR", "Erro ao procurar por modelos.");
                }
            }

            @Override
            public void onFailure(Call<List<Modelo>> call, Throwable throwable)
            {
                throwable.printStackTrace();
            }
        });
    }

    //MÉTODOS AUXILIARES
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return;
    }
}