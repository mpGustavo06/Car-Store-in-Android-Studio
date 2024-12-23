package com.example.carstore.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Models.Marca;
import com.example.carstore.Utils.DatabaseUtils;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MarcasDAO
{
    private SQLiteDatabase database;
    private CarStoreAPIService apiService;

    private LinkedList<Marca> marcasList = new LinkedList<>();
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";
    private String[] colunas = new String[] {"_id", "nome"};

    public MarcasDAO(Context context)
    {
        database = new DatabaseHelper(context).getWritableDatabase();

        Retrofit retrofit = RetrofitUtils.getInstance(BASE_URL);
        apiService = retrofit.create(CarStoreAPIService.class);
    }

    public void close()
    {
        database.close();
    }

    public long newRecord(Marca marca)
    {
        ContentValues values = new ContentValues();
        values.put("_id", marca.getId());
        values.put("nome", marca.getNome());

        Log.d("DAO.MRC.INSERT.VALUES", "MARCA: "+values.toString());

        return database.insert("marcas", null, values);
    }

    public void postRecord(Marca marca)
    {
        try
        {
            Log.d("DAO.MRC.POST.TESTE", "Marca: "+marca.toString());

            Call<Void> call = apiService.createPostMarca(marca);

            call.enqueue(new Callback<Void>()
            {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response)
                {
                    Log.d("DAO.MRC.POST.CALL", "Entrou na CALL");
                    Log.d("DAO.MRC.POST.RESP.CODE", "STATUS: "+response.code());
                    Log.d("DAO.MRC.POST.RESP.MSG", "MSG: "+response.message());

                    if (response.code() == 201 || response.code() == 200) //CREATED
                    {
                        Log.d("DAO.MRC.POST", "Marca inserida com sucesso ao servidor!");
                    }
                    else
                    {
                        Log.d("DAO.MRC.POST.ERROR", "Erro ao inserir marca ao servidor!");
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
            Log.d("DAO.MRC.POST.ERROR.PST", "ERROR: "+ex.getMessage());
        }
    }

    public long updateRecord(Long id, Marca marca)
    {
        //Log.d("DAO.MRC.PUT.IDTest", "ID: "+id);

        Call<Void> call = apiService.createPutMarca(id, marca);

        call.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                Log.d("DAO.MRC.PUT.CALL", "Entrou na CALL");
                Log.d("DAO.MRC.PUT.RESP.CODE", "STATUS: "+response.code());
                Log.d("DAO.MRC.PUT.RESP.MSG", "MSG: "+response.message());

                if (response.code() == 200) //OK
                {
                    Log.d("DAO.MRC.PUT", "Marca atualizada com sucesso do servidor!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable)
            {
                throwable.printStackTrace();
            }
        });

        ContentValues values = new ContentValues();
        values.put("nome", marca.getNome());

        return database.update("marcas", values, "_id = ?", new String[] {String.valueOf(id)});
    }

    public void deleteRecord(Marca marca)
    {
        Call<Void> call = apiService.createDeleteMarca(marca.getId());

        call.enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                Log.d("DAO.MRC.DELETE.CALL", "Entrou na CALL");
                Log.d("DAO.MRC.DELETE.RESP.CODE", "STATUS: "+response.code());
                Log.d("DAO.MRC.DELETE.RESP.MSG", "MSG: "+response.message());

                if (response.code() == 204) //DELETED
                {
                    Log.d("DAO.MRC.DELETE", "Marca removida com sucesso do servidor!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable)
            {
                throwable.printStackTrace();
            }
        });

        database.delete("marcas", "_id = ?", new String[] {String.valueOf(marca.getId())});
    }

    public void carregarMarcasServidor()
    {
        Call<List<Marca>> call = apiService.createGetMarcas();

        call.enqueue(new Callback<List<Marca>>() {
            @Override
            public void onResponse(Call<List<Marca>> call, Response<List<Marca>> response)
            {
                if (response.code() == 200) {
                    marcasList.clear();
                    marcasList.addAll( response.body() );

                    LinkedList<Marca> mrc = getMarcasDBList();

                    for (int i = 0; i < response.body().size(); i++)
                    {
                        Marca marca = response.body().get(i);

                        if (mrc.contains(marca))
                        {
                            //Log.d("DAO.MRC.TWIN", "Registro existente: "+marca.toString());
                        }
                        else if (marca != null && marca.getId() != null)
                        {
                            try
                            {
                                long id = newRecord(marca);
                                Log.d("DAO.MRC.INSERT", "Registro inserido com sucesso. ID: "+id);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                                Log.d("DAO.MRC.ERROR", "Erro ao inserir marca: "+marca.toString());
                            }
                        }
                    }

                    Log.d("DAO.MRC.END", "TERMINO DA VERIFICAÇÃO DE MARCAS DO SERVIDOR.");
                    Log.d("DAO.MRC.END.SRVList", "LISTA DE MARCAS: "+marcasList.toString());
                }
                else
                {
                    Log.d("DAO.MRC.ERROR", "Erro ao procurar por marcas.");
                }
            }

            @Override
            public void onFailure(Call<List<Marca>> call, Throwable throwable)
            {
                throwable.printStackTrace();
            }
        });
    }

    public LinkedList<Marca> getMarcasDBList()
    {
        LinkedList<Marca> marcas = DatabaseUtils.convert(getMarcasCursor(), getMarcasMapper());

        Log.d("DAO.MRC.DBList", "LISTA DE MARCAS: "+marcas.toString());
        return marcas;
    }

    public Marca getMarcaDBById(SQLiteDatabase db, int id)
    {
        Marca marca = (Marca) DatabaseUtils.buscarPorId(db, "marcas", "_id", id, getMarcasMapper());
        //Log.d("DAO.MRC.GetIdMarca", "MARCA: "+marca.toString());

        return marca;
    }

    public Cursor getMarcasCursor()
    {
        Cursor cursor = database.query("marcas", colunas, null, null, null, null, null);

        return cursor;
    }

    public DatabaseUtils.CursorMapper getMarcasMapper()
    {
        DatabaseUtils.CursorMapper<Marca> marcaMapper = new DatabaseUtils.CursorMapper<Marca>() {
            @Override
            public Marca map(Cursor cursor)
            {
                Long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                return new Marca(id, nome);
            }
        };

        return marcaMapper;
    }
}
