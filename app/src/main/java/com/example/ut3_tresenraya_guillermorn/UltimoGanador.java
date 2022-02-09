package com.example.ut3_tresenraya_guillermorn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class UltimoGanador extends AppCompatActivity {

    HelperSQL hsql = null;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultimo_ganador);

        hsql= new HelperSQL(this);
        db=hsql.getReadableDatabase();

        Cursor cursor = db.rawQuery(" select max(id),jugador1,jugador2,dificultad, resultado from partidas;", null);

        int id;
        String j1, j2, dif, rs;

        TextView edtxtMU = findViewById(R.id.tvUltimaPartida);

        edtxtMU.append("JUG1 --   JUG2  --   DIF  --  GANADOR \n-----------------------------------------------------------------");
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {

            id = Integer.parseInt(cursor.getString(0));
            j1 = cursor.getString(1);
            j2 = cursor.getString(2);
            dif = cursor.getString(3);
            rs = cursor.getString(4);

            edtxtMU.append("\n" + j1 + "      " + j2 + "        " + dif + "        " + rs);

            cursor.moveToNext();


        }

    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    public void actUltPart(MenuItem menuI){
        Intent i = new Intent(getApplicationContext(),UltimoGanador.class);
        startActivity(i);
    }

    public void actRankingM(MenuItem menuI){
        Intent i = new Intent(getApplicationContext(),Ranking.class);
        startActivity(i);
    }

    public void actPartidasM(MenuItem menuI){
        Intent i = new Intent(getApplicationContext(), Partidas.class);
        startActivity(i);
    }
    public void actInicioM(MenuItem menuI){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}