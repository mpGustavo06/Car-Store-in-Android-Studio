package com.example.carstore;

import android.content.Intent;
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

import com.example.carstore.Adapters.AnunciosAdapter;
import com.example.carstore.Adapters.SpinnerModeloAdapter;
import com.example.carstore.Control.CarStoreAPIService;
import com.example.carstore.Control.ModelosDAO;
import com.example.carstore.Models.Anuncio;
import com.example.carstore.Models.Modelo;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnunciosActivity extends AppCompatActivity
{
    private EditText edAnoInicial, edAnoFinal, edValorMin, edValorMax;
    private ListView anunciosListView;
    private Button buttonPesquisar, buttonAnunciar, buttonLimpar;
    private Spinner spinnerModelo;

    private AnunciosAdapter anunciosAdapter;
    private ArrayList<Anuncio> anunciosList = new ArrayList<>();

    private Modelo modelo = new Modelo();
    private ModelosDAO modelosDao;
    private SpinnerModeloAdapter spinnerModeloAdapter;

    int idEditing = -1;
    private CarStoreAPIService apiService;
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anuncios);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicialização do Retrofit
        RetrofitUtils.getInstance(BASE_URL);
        apiService = RetrofitUtils.createService(CarStoreAPIService.class);

        //Inicialização de ModeloDAO
        modelosDao = new ModelosDAO(getApplicationContext());

        //Inicialização dos componentes do layout
        anunciosListView = findViewById(R.id.anunciosList);
        buttonAnunciar = findViewById(R.id.btnAnunciar);
        buttonPesquisar = findViewById(R.id.btnPesquisar);
        buttonLimpar = findViewById(R.id.btnLimpar);
        edAnoInicial = findViewById(R.id.editTextFilterAnoInicial);
        edAnoFinal = findViewById(R.id.editTextFilterAnoFinal);
        edValorMin = findViewById(R.id.editTextFilterValorMin);
        edValorMax = findViewById(R.id.editTextFilterValorMax);
        spinnerModelo = findViewById(R.id.spinnerFilterModelo);

        //Configurando Spinner
        spinnerModeloAdapter = new SpinnerModeloAdapter(this, modelosDao.getModelosDBList());
        spinnerModelo.setAdapter(spinnerModeloAdapter);

        //Adapter & List
        anunciosAdapter = new AnunciosAdapter(this, anunciosList);
        anunciosListView.setAdapter(anunciosAdapter);
        anunciosListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        getAnunciosServerList();

        //Eventos
        anunciosListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                idEditing = (int) id;

                return false;
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

        buttonAnunciar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                anunciar(view);
            }
        });

        buttonPesquisar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) { pesquisar(view); }
        });

        buttonLimpar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) { limpar(view); }
        });
    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    public void anunciar(View view)
    {
        Intent intent = new Intent(AnunciosActivity.this, CreateAnuncioActivity.class);
        startActivity(intent);
    }

    public void remover(View view)
    {
//        Call<Void> call = apiService.createDeleteAnuncio();
    }

    public void alterar(View view)
    {

    }

    public void limpar(View view)
    {
        edAnoInicial.setText("");
        edAnoFinal.setText("");
        edValorMin.setText("");
        edValorMax.setText("");
        spinnerModelo.setSelection(0);

        getAnunciosServerList();
    }

    public void pesquisar(View view)
    {
        Log.d("ANC.FILTER.TESTE", "ENTROU NO METODO");

        Modelo mdlSpinner = new Modelo();
        Integer anoInicial = 0;
        Integer anoFinal = 0;
        Double valorMin = 0.0;
        Double valorMax = 0.0;

        try
        {
            mdlSpinner = modelo;
            anoInicial = Integer.valueOf(edAnoInicial.getText().toString());
            anoFinal = Integer.valueOf(edAnoFinal.getText().toString());
            valorMin = Double.valueOf(edValorMin.getText().toString());
            valorMin = Double.valueOf(edValorMax.getText().toString());
        }
        catch (Exception ex)
        {
            Log.d("FILTER.EXEPTION", "Mensagem: "+ex.getMessage());
        }

        Call<List<Anuncio>> call = null;

        if (mdlSpinner != null && anoInicial > 0 && anoFinal > 0 && valorMin > 0.0 && valorMax > 0.0) //Filtragem com todos os filtros
        {
           call = apiService.createGetAnunciosByFilter(mdlSpinner.getId(), anoInicial, anoFinal, valorMin, valorMax);
        }
        else if (mdlSpinner != null && anoInicial <= 0 && anoFinal <= 0 && valorMin <= 0.0 && valorMax <= 0.0) //Filtragem com modelo
        {
           call = apiService.createGetAnunciosByModelo(mdlSpinner.getId());
        }
        else
        {
            showToast("Erro ao realizar a filtragem, verifique os campos.");
            return;
        }

        call.enqueue(new Callback<List<Anuncio>>()
        {
            @Override
            public void onResponse(Call<List<Anuncio>> call, Response<List<Anuncio>> response)
            {
                Log.d("FILTER.CALL", "Entrou na CALL");
                Log.d("FILTER.CALL.CODE", "STATUS: "+response.code());
                Log.d("FILTER.CALL.MESSAGE", "MSG: "+response.message());
                if (response.code() == 200)
                {
                    if (response.body() != null)
                    {
                        anunciosList.clear();
                        anunciosList.addAll(response.body());
                        anunciosAdapter.notifyDataSetChanged();
                        Log.d("ANC.FILTER.RESPONSE", "LISTA DE ANUNCIOS: "+anunciosList.toString());
                    }
                    else
                    {
                        showToast("Não existem anuncios deste veiculo!");
                        Log.d("ANC.FILTER.RESPONSE.ERROR", "Resposta vazia");
                        getAnunciosServerList();
                    }
                }
                else
                {
                    Log.d("ANC.FILTER.RESPONSE.ERROR", "Erro ao procurar por anuncios");
                }
            }

            @Override
            public void onFailure(Call<List<Anuncio>> call, Throwable throwable) { throwable.printStackTrace(); }
        });
    }

    public void getAnunciosServerList()
    {
        Call<List<Anuncio>> call = apiService.createGetAnuncios();

        call.enqueue(new Callback<List<Anuncio>>() {
            @Override
            public void onResponse(Call<List<Anuncio>> call, Response<List<Anuncio>> response)
            {
                if (response.code() == 200)
                {
                    anunciosList.clear();
                    anunciosList.addAll(response.body());
                    anunciosAdapter.notifyDataSetChanged();
                    Log.d("ACT.ANC.RESPONSE", "LISTA DE ANUNCIOS: "+anunciosList.toString());
                }
                else
                {
                    Log.d("ACT.ANC.RESPONSE.ERROR", "Erro ao procurar por anuncios.");
                }
            }

            @Override
            public void onFailure(Call<List<Anuncio>> call, Throwable throwable) {
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