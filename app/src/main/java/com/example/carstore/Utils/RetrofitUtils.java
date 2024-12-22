package com.example.carstore.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static Retrofit retrofit;

    // Método para inicializar o Retrofit
    public static Retrofit getInstance(String baseUrl)
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl) // Base URL da API
                .addConverterFactory(GsonConverterFactory.create()) // Conversor para JSON
                .build();

        return retrofit;
    }

    // Método genérico para criar o serviço da API
    public static <T> T createService(Class<T> serviceClass)
    {
        if (retrofit == null)
        {
            throw new IllegalStateException("Retrofit instance is not initialized. Call getInstance() first.");
        }

        return retrofit.create(serviceClass);
    }
}