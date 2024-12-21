package com.example.carstore.Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.carstore.Database.DatabaseHelper;
import com.example.carstore.Models.Anuncio;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Marca;
import com.example.carstore.Models.Modelo;
import com.example.carstore.Utils.DatabaseUtils;
import com.example.carstore.Utils.RetrofitUtils;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnunciosTableDAO {
    private SQLiteDatabase database;
    private SQLiteDatabase dbRead;
    private CarStoreAPIService apiService;

    private ModelosTableDAO modelosDAO;
    private CidadesTableDAO cidadesDAO;

    private LinkedList<Anuncio> anunciosList = new LinkedList<>();
    private static final String BASE_URL = "http://argo.td.utfpr.edu.br/carros/ws/";
    private String[] colunas = new String[] {"_id", "descricao", "valor", "ano", "km", "idModelo", "idCidade"};

    public AnunciosTableDAO(Context context)
    {
        database = new DatabaseHelper(context).getWritableDatabase();
        dbRead = new DatabaseHelper(context).getReadableDatabase();
        modelosDAO = new ModelosTableDAO(context);
        cidadesDAO = new CidadesTableDAO(context);
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
//
//    public long updateRecord(Anuncio anuncio)
//    {
//
//    }

//    public void deleteRecord(Anuncio anuncio)
//    {
//
//    }

    public LinkedList<Anuncio> getAnunciosList()
    {
        LinkedList<Anuncio> anuncios = DatabaseUtils.convert(getAnunciosCursor(), getAnunciosMapper());
        Log.d("DAO.ANC.DBList", "LISTA DE ANUNCIOS: "+anuncios.toString());

        return anuncios;
    }

    public LinkedList<Anuncio> carregarAnunciosServidor()
    {
        RetrofitUtils.getInstance(BASE_URL);
        apiService = RetrofitUtils.createService(CarStoreAPIService.class);

        Call<List<Anuncio>> call = apiService.createGetAnuncios();

        call.enqueue(new Callback<List<Anuncio>>() {
            @Override
            public void onResponse(Call<List<Anuncio>> call, Response<List<Anuncio>> response)
            {
                if (response.code() == 200) {
                    anunciosList.clear();
                    anunciosList.addAll( response.body() );

                    LinkedList<Anuncio> anc = getAnunciosList();

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
            public void onFailure(Call<List<Anuncio>> call, Throwable throwable)
            {
                throwable.printStackTrace();
            }
        });

        return getAnunciosList();
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

                Modelo modelo = modelosDAO.getModeloById(dbRead, idModelo);
                Log.d("DAO.ANC.MPP.GetIdModelo", "MODELO: "+modelo.toString());

                int idCidade = cursor.getInt(cursor.getColumnIndexOrThrow("idCidade"));
                //Log.d("DAO.ANC.MPP.IdCidade", "IdCidade: "+idCidade);

                Cidade cidade = cidadesDAO.getCidadeById(dbRead, idCidade);
                Log.d("DAO.ANC.MPP.GetIdCidade", "CIDADE: "+cidade.toString());

                return new Anuncio(id, modelo, cidade, descricao, valor, ano, km);
            }
        };

        return anuncioMapper;
    }
}