package com.example.carstore.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Utils.DatabaseUtils;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CidadesDAO
{
    private SQLiteDatabase database;
    private CarStoreAPIService apiService;

    private LinkedList<Cidade> cidadesList = new LinkedList<>();
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";
    private String[] colunas = new String[] {"_id", "nome", "ddd"};

    public CidadesDAO(Context context)
    {
        database = new DatabaseHelper(context).getWritableDatabase();

        RetrofitUtils.getInstance(BASE_URL);
        apiService = RetrofitUtils.createService(CarStoreAPIService.class);
    }

    public void close() { database.close(); }

    public long newRecord(Cidade cidade)
    {
        ContentValues values = new ContentValues();
        values.put("_id", cidade.getId());
        values.put("nome", cidade.getNome());
        values.put("ddd", cidade.getDdd());

        Log.d("DAO.CDD.INSERT.VALUES", "CIDADE: "+values.toString());

        return database.insert("cidades", null, values);
    }

    public void postRecord(Cidade cidade)
    {
        try
        {
            Log.d("DAO.CDD.POST.TESTE", "Cidade: "+cidade.toString());

            Call<Void> call = apiService.createPostCidade(cidade);

            call.enqueue(new Callback<Void>()
            {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response)
                {
                    Log.d("DAO.CDD.POST.CALL", "Entrou na CALL");
                    Log.d("DAO.CDD.POST.RESP.CODE", "STATUS: "+response.code());
                    Log.d("DAO.CDD.POST.RESP.MSG", "MSG: "+response.message());

                    if (response.code() == 201 || response.code() == 200) //CREATED
                    {
                        Log.d("DAO.CDD.POST", "Cidade inserida com sucesso ao servidor!");
                    }
                    else
                    {
                        Log.d("DAO.CDD.POST.ERROR", "Erro ao inserir cidade ao servidor!");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable)
                {
                    throwable.printStackTrace();
                }
            });
        }
        catch (Throwable ex)
        {
            Log.d("DAO.CDD.POST.ERROR.PST", "ERROR: "+ex.getMessage());
        }
    }

    public long updateRecord(Long id, Cidade cidade)
    {
        //Log.d("DAO.MRC.PUT.IDTest", "ID: "+id);

        Call<Void> call = apiService.createPutCidade(id, cidade);

        call.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                Log.d("DAO.CDD.PUT.CALL", "Entrou na CALL");
                Log.d("DAO.CDD.PUT.RESP.CODE", "STATUS: "+response.code());
                Log.d("DAO.CDD.PUT.RESP.MSG", "MSG: "+response.message());

                if (response.code() == 200) //OK
                {
                    Log.d("DAO.CDD.PUT", "Cidade atualizada com sucesso do servidor!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable)
            {
                throwable.printStackTrace();
            }
        });

        ContentValues values = new ContentValues();
        values.put("nome", cidade.getNome());
        values.put("ddd", cidade.getDdd());

        return database.update("cidades", values, "_id = ?", new String[] {String.valueOf(id)});
    }

    public void deleteRecord(Cidade cidade)
    {
        Call<Void> call = apiService.createDeleteCidade(cidade.getId());

        call.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                Log.d("DAO.CDD.DELETE.CALL", "Entrou na CALL");
                Log.d("DAO.CDD.DELETE.RESP.CODE", "STATUS: "+response.code());
                Log.d("DAO.CDD.DELETE.RESP.MSG", "MSG: "+response.message());

                if (response.code() == 204) //DELETED
                {
                    Log.d("DAO.CDD.DELETE", "Cidade removida com sucesso do servidor!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable)
            {
                throwable.printStackTrace();
            }
        });

        database.delete("cidades", "_id = ?", new String[] {String.valueOf(cidade.getId())});
    }

    public void carregarCidadesServidor()
    {
        Call<List<Cidade>> call = apiService.createGetCidades();

        call.enqueue(new Callback<List<Cidade>>() {
            @Override
            public void onResponse(Call<List<Cidade>> call, Response<List<Cidade>> response)
            {
                if (response.code() == 200) {
                    cidadesList.clear();
                    cidadesList.addAll( response.body() );

                    LinkedList<Cidade> cdd = getCidadesDBList();

                    for (int i = 0; i < response.body().size(); i++)
                    {
                        Cidade cidade = response.body().get(i);

                        if (cdd.contains(cidade))
                        {
                            //Log.d("DAO.CDD.TWIN", "Registro existente: "+cidade.toString());
                        }
                        else if (cidade != null && cidade.getId() != null)
                        {
                            try
                            {
                                long id = newRecord(cidade);
                                Log.d("DAO.CDD.INSERT", "Registro inserido com sucesso. ID: "+id);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                                Log.d("DAO.CDD.ERROR", "Erro ao inserir cidade: "+cidade.toString());
                            }
                        }
                    }

                    Log.d("DAO.CDD.END", "TERMINO DA VERIFICAÇÃO DE CIDADES DO SERVIDOR.");
                    Log.d("DAO.CDD.END.SRVList", "LISTA DE CIDADES: "+cidadesList.toString());
                }
                else
                {
                    Log.d("DAO.CDD.ERROR", "Erro ao procurar por cidades.");
                }
            }

            @Override
            public void onFailure(Call<List<Cidade>> call, Throwable throwable)
            {
                throwable.printStackTrace();
            }
        });
    }

    public LinkedList<Cidade> getCidadesDBList()
    {
        LinkedList<Cidade> cidades = DatabaseUtils.convert(getCidadesCursor(), getCidadesMapper());

        Log.d("DAO.CDD.DBList", "LISTA DE CIDADES: "+cidades.toString());
        return cidades;
    }

    public Cidade getCidadeDBById(SQLiteDatabase db, int id)
    {
        Cidade cidade = (Cidade) DatabaseUtils.buscarPorId(db, "cidades", "_id", id, getCidadesMapper());
        Log.d("DAO.CDD.GetIdCidade", "CIDADE: "+cidade.toString());

        return cidade;
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
