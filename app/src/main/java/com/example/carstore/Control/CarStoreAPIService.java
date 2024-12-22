package com.example.carstore.Control;

import com.example.carstore.Models.Anuncio;
import com.example.carstore.Models.Cidade;
import com.example.carstore.Models.Marca;
import com.example.carstore.Models.Modelo;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CarStoreAPIService {
    ////////////////////////////////////////////// MARCAS
    @GET("marcas")
    Call<List<Marca>> createGetMarcas();

    @POST
    Call<Void> createPostMarca(@Body Marca marca);

    @PUT("marcas/{id}")
    Call<Void> createPutMarca(@Path("id") Long idMarca, @Body Marca novaMarca);

    @DELETE("marcas/{id}")
    Call<Void> createDeleteMarca(@Path("id") Long idMarca);

    ////////////////////////////////////////////// CIDADES
    @GET("cidades")
    Call<List<Cidade>> createGetCidades();

    @POST
    Call<Void> createPostCidade(@Body Cidade cidade);

    @PUT("cidades/{id}")
    Call<Void> createPutCidade(@Path("id") Long idCidade, @Body Cidade novaCidade);

    @DELETE("cidades/{id}")
    Call<Void> createDeleteCidade(@Path("id") Long idCidade);

    ////////////////////////////////////////////// MODELOS
    @GET("modelos")
    Call<List<Modelo>> createGetModelos();

    @POST("modelos")
    Call<Void> createPostModelo(@Body Modelo modelo);

    @PUT("modelos/{id}")
    Call<Void> createPutModelo(@Path("id") Long idModelo, @Body Modelo novoModelo);

    @DELETE("modelos/{id}")
    Call<Void> createDeleteModelo(@Path("id") Long idModelo);

    ////////////////////////////////////////////// ANUNCIOS
    @GET("anuncios")
    Call<List<Anuncio>> createGetAnuncios();

    @GET("anuncios")
    Call<List<Anuncio>> createGetAnunciosByFilter(
            @Query("modelo") Long id,
            @Query("ano_inicial") Integer anoInicial,
            @Query("ano_final") Integer anoFinal,
            @Query("min") Double valorMinimo,
            @Query("max") Double valorMaximo
    );

    @GET("anuncios")
    Call<List<Anuncio>> createGetAnunciosByModelo( @Query("modelo") Long id );

    @POST("anuncios")
    Call<Void> createPostAnuncio(@Body Anuncio anuncio);

    @PUT("anuncios/{id}")
    Call<Void> createPutAnuncio(@Path("id") Long idAnuncio, @Body Anuncio novoAnuncio);

    @DELETE("anuncios/{id}")
    Call<Void> createDeleteAnuncio(@Path("id") Long idAnuncio);
}