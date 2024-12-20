package com.example.carstore.Control;

import com.example.carstore.Models.Anuncio;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Marca;
import com.example.carstore.Models.Modelo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CarStoreAPIService {
    //Marcas
    @GET("marcas")
    Call<List<Marca>> createGetMarcas();

    //Cidades
    @GET("cidades")
    Call<List<Cidade>> createGetCidades();

    //Modelos
    @GET("modelos")
    Call<List<Modelo>> createGetModelos();

    @GET("modelos")
    Call<List<Modelo>> createGetModelosByMarca(@Query("marca") Long idMarca);

    @GET("modelos/{id}")
    Call<Modelo> createGetModeloById(@Path("id") Long idModelo);

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
    Call<List<Anuncio>> createGetAnuncios();

    @POST("anuncios")
    Call<Void> createPostAnuncio(@Body Anuncio anuncio);
}
