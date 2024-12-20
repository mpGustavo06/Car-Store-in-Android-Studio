package com.example.carstore.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Models.Marca;
import com.example.carstore.Utils.DatabaseUtils;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarcasTableDAO
{
    private SQLiteDatabase database;
    private CarStoreAPIService apiService;

    private LinkedList<Marca> marcasList = new LinkedList<>();
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";
    private String[] colunas = new String[] {"_id", "nome"};

    public MarcasTableDAO(Context context)
    {
        database = new DatabaseHelper(context).getWritableDatabase();
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

        return database.insert("marcas", null, values);
    }
//
//    public long updateRecord(Marca marca)
//    {
//
//    }

//    public void deleteRecord(Marca marca)
//    {
//
//    }

    public LinkedList<Marca> getMarcasList()
    {
        LinkedList<Marca> marcas = DatabaseUtils.convert(getMarcasCursor(), getMarcasMapper());

        Log.d("DAO.MRC.DBList", "LISTA DE MARCAS: "+marcas.toString());
        return marcas;
    }

    public Marca getMarcaById(SQLiteDatabase db, int id)
    {
        Marca marca = (Marca) DatabaseUtils.buscarPorId(db, "marcas", "_id", id, getMarcasMapper());
        //Log.d("DAO.MRC.GetIdMarca", "MARCA: "+marca.toString());

        return marca;
    }

    public void carregarMarcasServidor()
    {
        RetrofitUtils.getInstance(BASE_URL);
        apiService = RetrofitUtils.createService(CarStoreAPIService.class);

        Call<List<Marca>> call = apiService.createGetMarcas();

        call.enqueue(new Callback<List<Marca>>() {
            @Override
            public void onResponse(Call<List<Marca>> call, Response<List<Marca>> response)
            {
                if (response.code() == 200) {
                    marcasList.clear();
                    marcasList.addAll( response.body() );

                    LinkedList<Marca> mrc = getMarcasList();

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
