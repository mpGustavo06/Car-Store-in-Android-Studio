package com.example.carstore.Utils;

import android.util.Log;

public class ObjectConverterUtils
{
    //Converte um objeto genérico em uma instância do tipo especificado.
    public static <T> T convert(Object obj, Class<T> clazz)
    {
        if (clazz.isInstance(obj))
        {
            return clazz.cast(obj); // Faz o cast seguro
        }
        else
        {
            Log.d("OBJECT.CONVERTER","Erro: O objeto não é uma instância de " + clazz.getSimpleName());
            return null; // Retorna null se o tipo for incompatível
        }
    }
}
