package com.example.carstore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.carstore.Adapters.MarcasAdapter;
import com.example.carstore.Control.CarStoreAPIService;
import com.example.carstore.Control.MarcasDAO;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Marca;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarcasActivity extends AppCompatActivity
{
    private EditText edNomeMarca;
    private ListView marcasListView;
    private Button buttonCadastrar, buttonAlterar, buttonRemover, buttonLimpar;

    private Marca marca;
    private MarcasDAO marcasDAO;
    private MarcasAdapter marcasAdapter;
    private ArrayList<Marca> marcasList = new ArrayList<>();

    private CarStoreAPIService apiService;
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_marcas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicialização do Retrofit
        RetrofitUtils.getInstance(BASE_URL);
        apiService = RetrofitUtils.createService(CarStoreAPIService.class);

        //Inicialização de MarcasDAO
        marcasDAO = new MarcasDAO(getApplicationContext());

        //Inicialização dos componentes do layout
        edNomeMarca = findViewById(R.id.editTextMrcNome);
        buttonCadastrar = findViewById(R.id.btnMarcaCadastrar);
        buttonAlterar = findViewById(R.id.btnMarcaAlterar);
        buttonRemover = findViewById(R.id.btnMarcaRemover);
        buttonLimpar = findViewById(R.id.btnMarcaLimpar);
        marcasListView = findViewById(R.id.marcasList);

        //Adapter & List
        marcasAdapter = new MarcasAdapter(this, marcasList);
        marcasListView.setAdapter(marcasAdapter);
        marcasListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        getMarcasServerList();
    }

    public void cadastrar(View view)
    {
        marca = new Marca(
                edNomeMarca.getText().toString()
        );

        Log.d("NEW.MRC.TESTE", "MARCA: "+marca.toString());

        long id = marcasDAO.newRecord(marca);
        marcasDAO.postRecord(marca);

        if (id > 0)
        {
            showToast("Marca criada com sucesso!");
            Log.d("NEW.MRC.INSERT.DB", "Inserido com sucesso. ID: "+id);

            marcasList.add(marca);
            marcasAdapter.notifyDataSetChanged();
        }
        else
        {
            showToast("Erro ao criar uma nova marca!");
            Log.d("NEW.MRC.ERROR.DB", "Erro ao inserir o registro.");
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
        edNomeMarca.setText("");
    }

    public void getMarcasServerList()
    {
        Call<List<Marca>> call = apiService.createGetMarcas();

        call.enqueue(new Callback<List<Marca>>()
        {
            @Override
            public void onResponse(Call<List<Marca>> call, Response<List<Marca>> response)
            {
                if (response.code() == 200)
                {
                    marcasList.clear();
                    marcasList.addAll(response.body());
                    marcasAdapter.notifyDataSetChanged();
                    Log.d("ACT.MRC.RESPONSE", "LISTA DE MARCAS: "+marcasList.toString());
                }
                else
                {
                    Log.d("ACT.MRC.RESPONSE.ERROR", "Erro ao procurar por marcas.");
                }
            }

            @Override
            public void onFailure(Call<List<Marca>> call, Throwable throwable)
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