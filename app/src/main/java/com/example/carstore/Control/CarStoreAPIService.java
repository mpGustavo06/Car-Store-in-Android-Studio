package com.example.carstore.Control;

import com.example.carstore.Models.Anuncio;
import com.example.carstore.Models.Marca;
import com.example.carstore.Models.Modelo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface CarStoreAPIService {
    //Marcas
    @GET("marcas")
    Call<List<Marca>> createGetMarcas();

    //Modelos
    @GET("modelos")
    Call<List<Modelo>> createGetModelos(@Query("marca") Long idMarca);

    @GET("modelos/{id}")
    Call<Modelo> createGetModelo(@Path("id") Long idModelo);

//    @POST("modelos")
//    Call<Void> createPostModelo(@Body Modelo modelo);
//
//    @PUT("modelos/{id}")
//    Call<Void> createPutModelo(@Path("id") Long idModelo, @Body Modelo novoModelo);
//
//    @DELETE("modelos/{id}")
//    Call<Void> createDeleteModelo(@Path("id") Long idModelo);

    //Anuncios
    @GET("anuncios")
    Call<List<Anuncio>> buscarAnuncios(@QueryMap Map<String, String> filter);
}
