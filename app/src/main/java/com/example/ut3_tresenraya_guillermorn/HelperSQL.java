package com.example.ut3_tresenraya_guillermorn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class HelperSQL extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dbJuego.db";
    //TABLA PARTIDAS
    private static final String SQL_CREATE_ENTRIES1 = "CREATE TABLE IF NOT EXISTS partidas(id integer PRIMARY KEY AUTOINCREMENT, jugador1 text, jugador2 text, dificultad text, resultado text)";
    //TABLA USUARIOS
    private static final String SQL_CREATE_ENTRIES2 = "CREATE TABLE IF NOT EXISTS usuarios(id integer PRIMARY KEY AUTOINCREMENT, usuario text, numeroPartidas text, puntos text)";

    public HelperSQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES1);
        db.execSQL(SQL_CREATE_ENTRIES2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //TABLA PARTIDAS
        db.execSQL("DROP TABLE IF EXISTS partidas");
        db.execSQL(SQL_CREATE_ENTRIES1);

        //TABLA USUARIOS
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL(SQL_CREATE_ENTRIES2);
    }
}
