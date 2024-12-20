package com.example.carstore.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.LinkedList;

public class DatabaseUtils<T>
{
    // Interface para mapear Cursor para um objeto
    public interface CursorMapper<T> {
        T map(Cursor cursor);
    }

    //Método genérico para converter Cursor em LinkedList
    public static <T> LinkedList<T> convert(Cursor cursor, CursorMapper<T> mapper) {
        LinkedList<T> list = new LinkedList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Usa o mapper para criar objetos a partir do Cursor
                list.add(mapper.map(cursor));
            } while (cursor.moveToNext());
        }

        // Fecha o Cursor
        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    //Método genérico para buscar um registro pelo ID.
    public static <T> T buscarPorId(SQLiteDatabase db, String tabela, String idColuna, Long id, CursorMapper<T> mapper) {
        // Cria a cláusula WHERE e os argumentos
        String selecao = idColuna + " = ?";
        String[] argumentos = {String.valueOf(id)};

        // Consulta o banco de dados
        Cursor cursor = db.query(tabela, null, selecao, argumentos, null, null, null);

        T objeto = null;

        // Mapeia o Cursor para o objeto, se encontrado
        if (cursor != null && cursor.moveToFirst()) {
            objeto = mapper.map(cursor);
        }

        // Fecha o Cursor
        if (cursor != null) {
            cursor.close();
        }

        return objeto;
    }
}