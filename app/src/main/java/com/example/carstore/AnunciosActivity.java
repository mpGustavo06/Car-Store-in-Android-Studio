package com.example.carstore;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.carstore.Control.AnunciosDatabaseHelper;
import com.example.carstore.Control.CarStoreAPIService;
import com.example.carstore.Models.Anuncio;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnunciosActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;
    private Retrofit retrofit;
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";

    private CarStoreAPIService apiService;
    private LinkedList<Anuncio> anuncios = new LinkedList<>();

    private ListView list;
    int idEditing = -1;

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

        //Banco de dados
        AnunciosDatabaseHelper dbHelper = new AnunciosDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        cursor = database.query("anuncios", new String[] {"_id", "modelo", "cidade", "descricao", "valor", "ano", "km", "idModelo", "idCidade"}, null, null, null, null, "modelo");

        adapter =  new SimpleCursorAdapter(this, android.R.layout.simple_list_item_single_choice, cursor, new String[] {"modelo"}, new int[] {android.R.id.text1});

        //Listagem
        list = findViewById(R.id.anunciosList);
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                idEditing = (int) id;

                cursor.moveToPosition(position);
                //editTextPesquisar.setText(cursor.getString(1));

                return false;
            }
        });

        //Criação do Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(CarStoreAPIService.class);

        if (apiService == null) { Log.e("Retrofit", "carStoreAPIService não foi inicializado!"); }

        //Carregar anuncios existentes no servidor
        carregarAnunciosServidor();
    }

    public void onDestroy() {
        database.close();
        super.onDestroy();
    }

    public void pesquisar(View view)
    {

    }

    public void carregarAnunciosServidor()
    {
        Call<List<Anuncio>> call = apiService.createGetAnuncios();

        call.enqueue(new Callback<List<Anuncio>>() {
            @Override
            public void onResponse(Call<List<Anuncio>> call, Response<List<Anuncio>> response)
            {
                if (response.code() == 200)
                {
                    anuncios.clear();
                    anuncios.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    Log.d("RESPOSTA", anuncios.toString());
                }
                else
                {
                    showToast("Erro na busca. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Anuncio>> call, Throwable throwable)
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