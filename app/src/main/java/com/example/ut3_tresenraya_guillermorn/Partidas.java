package com.example.ut3_tresenraya_guillermorn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Partidas extends AppCompatActivity {
    ListView lvPart;
    HelperSQL hsql = null;
    SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidas);

        hsql= new HelperSQL(this);
        db=hsql.getReadableDatabase();
        lvPart = findViewById(R.id.lvPartidas);
        ArrayList<String> arr = new ArrayList<>();

        Cursor fila = db.rawQuery("select * from partidas", null);

        if(fila.moveToFirst()){
            do{
                arr.add(fila.getString(0)+ "  -->     " +fila.getString(1)+ "  --  " + fila.getString(2)+ " -- " + fila.getString(3)+ "   --    " + fila.getString(4));

            }while(fila.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        lvPart.setAdapter(adapter);
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