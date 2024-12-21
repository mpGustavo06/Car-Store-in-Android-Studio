package com.example.carstore;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.example.carstore.Control.CarStoreAPIService;
import com.example.carstore.Models.Anuncio;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Marca;
import com.example.carstore.Models.Modelo;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.LinkedList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AnunciosActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private Cursor cursor;
    private AnunciosAdapter adapter;
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";

    private CarStoreAPIService apiService;
    private LinkedList<Anuncio> anuncios = new LinkedList<>();
    private AnunciosTableDAO anunciosDAO;

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

        //Inicialização dos componentes do layout
        list = findViewById(R.id.anunciosList);

        //Banco de dados
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        cursor = database.query("anuncios", new String[] {"_id", "descricao", "valor", "ano", "km", "idModelo", "idCidade"}, null, null, null, null, "idModelo");

        adapter =  new AnunciosAdapter(this, cursor, 0);

        //Listagem
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

        RetrofitUtils.getInstance(BASE_URL);
        apiService = RetrofitUtils.createService(CarStoreAPIService.class);

        if (apiService == null) { Log.e("Retrofit", "carStoreAPIService não foi inicializado!"); }


        //Carregar registros existentes no servidor
        anunciosDAO = new AnunciosTableDAO(getApplicationContext());
        carregarAnunciosAdapter();
    }

    public void onDestroy() {
        database.close();
        super.onDestroy();
    }

    public void pesquisar(View view)
    {

    }

    public void anunciar(View view)
    {
        Intent intent = new Intent(AnunciosActivity.this, CreateAnuncioActivity.class);
        startActivity(intent);
    }

    public void carregarAnunciosAdapter()
    {
        anuncios = anunciosDAO.carregarAnunciosServidor();
        adapter.notifyDataSetChanged();
    }

    //MÉTODOS AUXILIARES
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return;
    }

    public void limparDatabase(String table)
    {
        database.execSQL("DELETE FROM "+table+";");
        database.execSQL("DELETE FROM sqlite_sequence WHERE name='"+table+"';");

        Log.d("DB.TB."+table, "Registros deletados com sucesso.");
    }
}