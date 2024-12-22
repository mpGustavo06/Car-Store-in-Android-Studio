package com.example.carstore.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Models.Marca;
import com.example.carstore.Models.Modelo;
import com.example.carstore.Utils.DatabaseUtils;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ModelosDAO {
    private SQLiteDatabase database;
    private SQLiteDatabase dbRead;
    private CarStoreAPIService apiService;
    private MarcasDAO marcasDAO;

    private LinkedList<Modelo> modelosList = new LinkedList<>();
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";
    private String[] colunas = new String[] {"_id", "nome", "tipo", "idMarca"};

    public ModelosDAO(Context context)
    {
        database = new DatabaseHelper(context).getWritableDatabase();
        dbRead = new DatabaseHelper(context).getReadableDatabase();

        marcasDAO = new MarcasDAO(context);

        Retrofit retrofit = RetrofitUtils.getInstance(BASE_URL);
        apiService = retrofit.create(CarStoreAPIService.class);
    }

    public void close()
    {
        database.close();
    }

    public long newRecord(Modelo modelo)
    {
        ContentValues values = new ContentValues();
        values.put("_id", modelo.getId());
        values.put("nome", modelo.getNome());
        values.put("tipo", modelo.getTipo());
        values.put("idMarca", modelo.getMarca().getId());

        Log.d("DAO.MDL.INSERT.VALUES", "MODELO: "+values.toString());

        return database.insert("modelos", null, values);
    }

    public void postRecord(Modelo modelo)
    {
        try
        {
            Log.d("DAO.MDL.POST.TESTE", "Modelo: "+modelo.toString());

            Call<Void> call = apiService.createPostModelo(modelo);

            call.enqueue(new Callback<Void>()
            {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response)
                {
                    Log.d("DAO.MDL.POST.CALL", "Entrou na CALL");
                    Log.d("DAO.MDL.POST.RESP.CODE", "STATUS: "+response.code());
                    Log.d("DAO.MDL.POST.RESP.MSG", "MSG: "+response.message());

                    if (response.code() == 200) //CREATED
                    {
                        Log.d("DAO.MDL.POST", "Modelo inserido com sucesso ao servidor!");
                    }
                    else
                    {
                        Log.d("DAO.MDL.POST.ERROR", "Erro ao inserir modelo ao servidor!");
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
            Log.d("DAO.MDL.POST.ERROR.PST", "ERROR: "+ex.getMessage());
        }
    }

//    public long updateRecord(Modelo modelo)
//    {
//
//    }

//    public void deleteRecord(Modelo modelo)
//    {
//
//    }

    public void carregarModelosServidor()
    {
        RetrofitUtils.getInstance(BASE_URL);
        apiService = RetrofitUtils.createService(CarStoreAPIService.class);

        Call<List<Modelo>> call = apiService.createGetModelos();

        call.enqueue(new Callback<List<Modelo>>() {
            @Override
            public void onResponse(Call<List<Modelo>> call, Response<List<Modelo>> response)
            {
                if (response.code() == 200) {
                    modelosList.clear();
                    modelosList.addAll( response.body() );

                    LinkedList<Modelo> mdl = getModelosDBList();

                    for (int i = 0; i < response.body().size(); i++)
                    {
                        Modelo modelo = response.body().get(i);

                        if (mdl.contains(modelo))
                        {
                            //Log.d("DAO.MDL.TWIN", "Registro existente: "+modelo.toString());
                        }
                        else if (modelo != null && modelo.getId() != null)
                        {
                            try
                            {
                                long id = newRecord(modelo);
                                Log.d("DAO.MDL.INSERT", "Registro inserido com sucesso. ID: "+id);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                                Log.d("DAO.MDL.ERROR", "Erro ao inserir modelo: "+modelo.toString());
                            }
                        }
                    }

                    Log.d("DAO.MDL.END", "TERMINO DA VERIFICAÇÃO DE MODELOS DO SERVIDOR.");
                    Log.d("DAO.MDL.END.SRVList", "LISTA DE MODELOS: "+modelosList.toString());
                }
                else
                {
                    Log.d("DAO.MDL.ERROR", "Erro ao procurar por modelos.");
                }
            }

            @Override
            public void onFailure(Call<List<Modelo>> call, Throwable throwable) { throwable.printStackTrace(); }
        });
    }

    public LinkedList<Modelo> getModelosDBList()
    {
        LinkedList<Modelo> modelos = DatabaseUtils.convert(getModelosCursor(), getModelosMapper());
        Log.d("DAO.MDL.DBList", "LISTA DE MODELOS: "+modelos.toString());

        return modelos;
    }

    public Modelo getModeloDBById(SQLiteDatabase db, int id)
    {
        Modelo modelo = (Modelo) DatabaseUtils.buscarPorId(db, "modelos", "_id", id, getModelosMapper());
        Log.d("DAO.MDL.GetModeloId", "MODELO: "+modelo.toString());

        return modelo;
    }

    public Cursor getModelosCursor()
    {
        Cursor cursor = database.query("modelos", colunas, null, null, null, null, null);

        return cursor;
    }

    public DatabaseUtils.CursorMapper getModelosMapper()
    {
        DatabaseUtils.CursorMapper<Modelo> modeloMapper = new DatabaseUtils.CursorMapper<Modelo>() {
            @Override
            public Modelo map(Cursor cursor)
            {
                Long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));

                int idMarca = cursor.getInt(cursor.getColumnIndexOrThrow("idMarca"));
                //Log.d("DAO.MDL.MPP.IdMarca", "IdMarca:"+idMarca);

                Marca marca = marcasDAO.getMarcaDBById(dbRead, idMarca);
                //Log.d("DAO.MDL.MPP.GetIdMarca", "MARCA: "+marca.toString());

                return new Modelo(id, nome, marca, tipo);
            }
        };

        return modeloMapper;
    }
}
