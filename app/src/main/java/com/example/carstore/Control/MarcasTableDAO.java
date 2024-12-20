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

        //Log.d("TB.DAO.MARCAS", marcas.toString());
        return marcas;
    }

    public Marca getMarcaById(SQLiteDatabase db, Long id)
    {
        Log.d("TB.DAO.MARCAS", "ID?"+id);
        // Define a cláusula WHERE e os argumentos
        String selecao = "_id = ?";
        String[] argumentos = {String.valueOf(id)};

        // Consulta ao banco de dados
        Cursor cursor = db.query("marcas", colunas, selecao, argumentos, null, null, null);

        Marca marca = null;

        // Verifica se encontrou um registro
        if (cursor != null)
        {
            try
            {
                if (cursor.moveToFirst())
                {
                    // Recupera os dados do Cursor
                    Long idMarca = (long) cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));

                    // Cria o objeto Investimento
                    marca = new Marca(idMarca, nome);
                }
            }
            catch (Exception e)
            {
                // Loga o erro caso ocorra uma exceção
                e.printStackTrace();
            }
            finally
            {
                // Sempre feche o cursor após o uso
                cursor.close();
            }
        }

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

                    for (int i = 0; i < response.body().size(); i++)
                    {
                        Marca marca = response.body().get(i);

                        if (getMarcasList().contains(marca))
                        {
                            Log.d("TB.DAO.MARCAS", "Registro existente: "+marca.toString());
                        }
                        else if (marca != null && marca.getId() != null)
                        {
                            try
                            {
                                long id = newRecord(marca);
                                Log.d("TABLE MARCAS", "Registro inserido com sucesso. ID: "+id);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                                Log.d("TABLE MARCAS", "Erro ao inserir. ID: "+marca.getId());
                            }
                        }
                    }

                    Log.d("TB.DAO.MARCAS", "TERMINO DA VERIFICAÇÃO DE MARCAS EXISTENTES. LISTA: "+marcasList.toString());
                }
                else
                {
                    Log.d("TB.DAO.MARCAS", "Erro ao procurar por marcas.");
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
