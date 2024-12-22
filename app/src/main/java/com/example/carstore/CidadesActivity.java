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

import com.example.carstore.Adapters.CidadesAdapter;
import com.example.carstore.Control.CarStoreAPIService;
import com.example.carstore.Control.CidadesDAO;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Marca;
import com.example.carstore.Models.Modelo;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CidadesActivity extends AppCompatActivity
{
    private EditText edNomeCidade, edDDD;
    private ListView cidadesListView;
    private Button buttonCadastrar, buttonAlterar, buttonRemover, buttonLimpar;

    private Cidade cidade;
    private CidadesDAO cidadesDAO;
    private CidadesAdapter cidadesAdapter;
    private ArrayList<Cidade> cidadesList = new ArrayList<>();

    private CarStoreAPIService apiService;
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cidades);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicialização do Retrofit
        RetrofitUtils.getInstance(BASE_URL);
        apiService = RetrofitUtils.createService(CarStoreAPIService.class);

        //Inicialização de CidadeDAO
        cidadesDAO = new CidadesDAO(getApplicationContext());

        //Inicialização dos componentes do layout
        edNomeCidade = findViewById(R.id.editTextCddNome);
        edDDD = findViewById(R.id.editTextCddDdd);
        buttonCadastrar = findViewById(R.id.btnCidadeCadastrar);
        buttonAlterar = findViewById(R.id.btnCidadeAlterar);
        buttonRemover = findViewById(R.id.btnCidadeRemover);
        buttonLimpar = findViewById(R.id.btnCidadeLimpar);
        cidadesListView = findViewById(R.id.cidadesList);

        //Adapter & List
        cidadesAdapter = new CidadesAdapter(this, cidadesList);
        cidadesListView.setAdapter(cidadesAdapter);
        cidadesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        getCidadesServerList();

        //Eventos
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
        cidade = new Cidade(
                edNomeCidade.getText().toString(),
                edDDD.getText().toString()
        );

        Log.d("NEW.CDD.TESTE", "CIDADE: "+cidade.toString());

        long id = cidadesDAO.newRecord(cidade);
        cidadesDAO.postRecord(cidade);

        if (id > 0)
        {
            showToast("Cidade criada com sucesso!");
            Log.d("NEW.CDD.INSERT.DB", "Inserido com sucesso. ID: "+id);

            cidadesList.add(cidade);
            cidadesAdapter.notifyDataSetChanged();
        }
        else
        {
            showToast("Erro ao criar uma nova cidade!");
            Log.d("NEW.CDD.ERROR.DB", "Erro ao inserir o registro.");
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
        edNomeCidade.setText("");
        edDDD.setText("");
    }

    public void getCidadesServerList()
    {
        Call<List<Cidade>> call = apiService.createGetCidades();

        call.enqueue(new Callback<List<Cidade>>()
        {
            @Override
            public void onResponse(Call<List<Cidade>> call, Response<List<Cidade>> response)
            {
                if (response.code() == 200)
                {
                    cidadesList.clear();
                    cidadesList.addAll(response.body());
                    cidadesAdapter.notifyDataSetChanged();
                    Log.d("ACT.CDD.RESPONSE", "LISTA DE CIDADES: "+cidadesList.toString());
                }
                else
                {
                    Log.d("ACT.CDD.RESPONSE.ERROR", "Erro ao procurar por cidades.");
                }
            }

            @Override
            public void onFailure(Call<List<Cidade>> call, Throwable throwable)
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