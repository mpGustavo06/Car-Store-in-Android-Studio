package com.example.carstore.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.carstore.AnunciosActivity;
import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Models.Anuncio;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Modelo;
import com.example.carstore.Utils.DatabaseUtils;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AnunciosDAO {
    private SQLiteDatabase database;
    private SQLiteDatabase dbRead;
    private CarStoreAPIService apiService;

    private ModelosDAO modelosDAO;
    private CidadesDAO cidadesDAO;

    private LinkedList<Anuncio> anunciosList = new LinkedList<>();
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";
    private String[] colunas = new String[] {"_id", "descricao", "valor", "ano", "km", "idModelo", "idCidade"};

    public AnunciosDAO(Context context)
    {
        database = new DatabaseHelper(context).getWritableDatabase();
        dbRead = new DatabaseHelper(context).getReadableDatabase();

        modelosDAO = new ModelosDAO(context);
        cidadesDAO = new CidadesDAO(context);

        Retrofit retrofit = RetrofitUtils.getInstance(BASE_URL);
        apiService = retrofit.create(CarStoreAPIService.class);
    }

    public void close()
    {
        database.close();
    }

    public long newRecord(Anuncio anuncio)
    {
        ContentValues values = new ContentValues();
        values.put("_id", anuncio.getId());
        values.put("descricao", anuncio.getDescricao());
        values.put("valor", anuncio.getValor());
        values.put("ano", anuncio.getAno());
        values.put("km", anuncio.getKm());
        values.put("idModelo", anuncio.getModelo().getId());
        values.put("idCidade", anuncio.getCidade().getId());

        return database.insert("anuncios", null, values);
    }

    public void postRecord (Anuncio anuncio)
    {
        try
        {
            Log.d("DAO.ANC.POST.TESTE", "Anuncio: "+anuncio.toString());

            Call<Void> call = apiService.createPostAnuncio(anuncio);

            call.enqueue(new Callback<Void>()
            {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response)
                {
                    if (response.code() == 200) //CREATED
                    {
                        Log.d("DAO.ANC.POST", "Anuncio inserido com sucesso ao servidor!");
                    }
                    else
                    {
                        Log.d("DAO.ANC.POST.ERROR", "Erro ao inserir anuncio ao servidor!");
                        Log.d("DAO.ANC.POST.ERROR.SRV", "STATUS: "+response.code());
                        Log.d("DAO.ANC.POST.ERROR.SRV", "MSG: "+response.message());
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
            Log.d("DAO.ANC.POST.ERROR", "Erro ao inserir anuncio ao servidor!");
            Log.d("DAO.ANC.POST.ERROR.PST", "ERROR: "+ex.getMessage());
        }
    }
//
//    public long updateRecord(Anuncio anuncio)
//    {
//
//    }

//    public void deleteRecord(Anuncio anuncio)
//    {
//
//    }

    public void carregarAnunciosServidor()
    {
        Call<List<Anuncio>> call = apiService.createGetAnuncios();

        call.enqueue(new Callback<List<Anuncio>>() {
            @Override
            public void onResponse(Call<List<Anuncio>> call, Response<List<Anuncio>> response)
            {
                if (response.code() == 200) {
                    anunciosList.clear();
                    anunciosList.addAll( response.body() );

                    LinkedList<Anuncio> anc = getAnunciosDBList();

                    for (int i = 0; i < response.body().size(); i++)
                    {
                        Anuncio anuncio = response.body().get(i);

                        if (anc.contains(anuncio))
                        {
                            //Log.d("DAO.ANC.TWIN", "Registro existente: "+anuncio.toString());
                        }
                        else if (anuncio != null && anuncio.getId() != null)
                        {
                            try
                            {
                                long id = newRecord(anuncio);
                                Log.d("DAO.ANC.INSERT", "Registro inserido com sucesso. ID: "+id);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                                Log.d("DAO.ANC.ERROR", "Erro ao inserir anuncio: "+anuncio.toString());
                            }
                        }
                    }

                    Log.d("DAO.ANC.END", "TERMINO DA VERIFICAÇÃO DE ANUNCIOS DO SERVIDOR.");
                    Log.d("DAO.ANC.END.SRVList", "LISTA DE ANUNCIOS: "+anunciosList.toString());
                }
                else
                {
                    Log.d("DAO.ANC.ERROR", "Erro ao procurar por anuncios.");
                }
            }

            @Override
            public void onFailure(Call<List<Anuncio>> call, Throwable throwable) { throwable.printStackTrace(); }
        });
    }

    public LinkedList<Anuncio> getAnunciosDBList()
    {
        LinkedList<Anuncio> anuncios = DatabaseUtils.convert(getAnunciosCursor(), getAnunciosMapper());
        Log.d("DAO.ANC.DBList", "LISTA DE ANUNCIOS: "+anuncios.toString());

        return anuncios;
    }

    public Cursor getAnunciosCursor()
    {
        Cursor cursor = database.query("anuncios", colunas, null, null, null, null, null);

        return cursor;
    }

    public DatabaseUtils.CursorMapper getAnunciosMapper()
    {
        DatabaseUtils.CursorMapper<Anuncio> anuncioMapper = new DatabaseUtils.CursorMapper<Anuncio>() {
            @Override
            public Anuncio map(Cursor cursor)
            {
                Long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                Double valor = (double) cursor.getFloat(cursor.getColumnIndexOrThrow("valor"));
                int ano = cursor.getInt(cursor.getColumnIndexOrThrow("ano"));
                int km = cursor.getInt(cursor.getColumnIndexOrThrow("km"));

                int idModelo = cursor.getInt(cursor.getColumnIndexOrThrow("idModelo"));
                //Log.d("DAO.ANC.MPP.IdModelo", "IdModelo: "+idModelo);

                Modelo modelo = modelosDAO.getModeloDBById(dbRead, idModelo);
                Log.d("DAO.ANC.MPP.GetIdModelo", "MODELO: "+modelo.toString());

                int idCidade = cursor.getInt(cursor.getColumnIndexOrThrow("idCidade"));
                //Log.d("DAO.ANC.MPP.IdCidade", "IdCidade: "+idCidade);

                Cidade cidade = cidadesDAO.getCidadeDBById(dbRead, idCidade);
                Log.d("DAO.ANC.MPP.GetIdCidade", "CIDADE: "+cidade.toString());

                return new Anuncio(id, modelo, cidade, descricao, valor, ano, km);
            }
        };

        return anuncioMapper;
    }
}