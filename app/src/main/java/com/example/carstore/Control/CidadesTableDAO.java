package com.example.carstore.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Marca;
import com.example.carstore.Utils.DatabaseUtils;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CidadesTableDAO {
    private SQLiteDatabase database;
    private CarStoreAPIService apiService;
    private LinkedList<Cidade> cidadesList = new LinkedList<>();
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";
    private String[] colunas = new String[] {"_id", "nome", "ddd"};

    public CidadesTableDAO(Context context)
    {
        database = new DatabaseHelper(context).getWritableDatabase();
    }

    public void close()
    {
        database.close();
    }

    public long newRecord(Cidade cidade)
    {
        ContentValues values = new ContentValues();
        values.put("_id", cidade.getId());
        values.put("nome", cidade.getNome());
        values.put("ddd", cidade.getDdd());

        return database.insert("cidades", null, values);
    }
//
//    public long updateRecord(Cidade cidade)
//    {
//
//    }

//    public void deleteRecord(Cidade cidade)
//    {
//
//    }

    public LinkedList<Cidade> getCidadesList()
    {
        LinkedList<Cidade> cidades = DatabaseUtils.convert(getCidadesCursor(), getCidadesMapper());

        //Log.d("TB.DAO.CIDADES", cidades.toString());
        return cidades;
    }

    public void carregarCidadesServidor()
    {
        RetrofitUtils.getInstance(BASE_URL);
        apiService = RetrofitUtils.createService(CarStoreAPIService.class);

        Call<List<Cidade>> call = apiService.createGetCidades();

        call.enqueue(new Callback<List<Cidade>>() {
            @Override
            public void onResponse(Call<List<Cidade>> call, Response<List<Cidade>> response)
            {
                if (response.code() == 200) {
                    cidadesList.clear();
                    cidadesList.addAll( response.body() );

                    for (int i = 0; i < response.body().size(); i++)
                    {
                        Cidade cidade = response.body().get(i);

                        if (getCidadesList().contains(cidade))
                        {
                            Log.d("TB.DAO.CIDADES", "Registro existente: "+cidade.toString());
                        }
                        else if (cidade != null && cidade.getId() != null)
                        {
                            try
                            {
                                long id = newRecord(cidade);
                                Log.d("TABLE CIDADES", "Registro inserido com sucesso. ID: "+id);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                                Log.d("TABLE CIDADES", "Erro ao inserir. ID: "+cidade.getId());
                            }
                        }
                    }

                    Log.d("TB.DAO.CIDADES", "TERMINO DA VERIFICAÇÃO DE CIDADES EXISTENTES. LISTA: "+cidadesList.toString());
                }
                else
                {
                    Log.d("TB.DAO.CIDADES", "Erro ao procurar por cidades.");
                }
            }

            @Override
            public void onFailure(Call<List<Cidade>> call, Throwable throwable)
            {
                throwable.printStackTrace();
            }
        });
    }

    public Cursor getCidadesCursor()
    {
        Cursor cursor = database.query("cidades", colunas, null, null, null, null, null);

        return cursor;
    }

    public DatabaseUtils.CursorMapper getCidadesMapper()
    {
        DatabaseUtils.CursorMapper<Cidade> cidadeMapper = new DatabaseUtils.CursorMapper<Cidade>() {
            @Override
            public Cidade map(Cursor cursor)
            {
                Long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                String ddd = cursor.getString(cursor.getColumnIndexOrThrow("ddd"));
                return new Cidade(id, nome, ddd);
            }
        };

        return cidadeMapper;
    }
}
