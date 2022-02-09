package com.example.ut3_tresenraya_guillermorn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Nombre usuario
        String nomUsu;

    //Sonido
        boolean sonido = true;

    //MediaPlayer para el sonido de las fichas
    MediaPlayer mediaFichas;
    MediaPlayer mediaFinal;

    boolean fin = false;
    //botÃ³n que se ha pulsado (1 Ã³ 2 jugadores)
    private int numJugadores;
    //array donde guardaremos las casillas del tablero
    private int[] casillas;
    //partida
    private Partida partida;

    //VARIABLES PARA ALMACENAR LOS DATOS A ALMACENAR
    String dificultad, jugador1, jugador2, rs;
    int puntos=0, partidas=1, jug;
    //BD
    HelperSQL helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Guardamos cada una de las casillas en el array
        casillas=new int[9];
        casillas[0]=R.id.a1;
        casillas[1]=R.id.a2;
        casillas[2]=R.id.a3;
        casillas[3]=R.id.b1;
        casillas[4]=R.id.b2;
        casillas[5]=R.id.b3;
        casillas[6]=R.id.c1;
        casillas[7]=R.id.c2;
        casillas[8]=R.id.c3;

        //Activar sonido de fondo
        startService(new Intent(getApplicationContext(),Musicafondo.class));

        ///CREACIÓN DE LA BASE DE DATOS

        helper= new HelperSQL(this);
        db=helper.getWritableDatabase();
    }

    /***
     * MÃ©todo asociado al evento onClick de los botones de 1 Jugador y 2 Jugadores
     * @param view
     */
    public void inicioJuego(View view) {

        EditText edt = (findViewById(R.id.nombreUsu));

        if(!edt.getText().toString().equals("")){
            ImageView imagen;

            //reseteamos el tablero
            //recorremos cada una de los elementos del array y a todos le asignamos la imagen de la casilla en blanco
            for (int casilla : casillas) {
                imagen = findViewById(casilla);
                imagen.setImageResource(R.drawable.casilla);
            }

            //determinamos quÃ© botÃ³n se ha pulsado
            numJugadores = 1;

            if(view.getId()==R.id.btnDosJugadores){
                numJugadores = 2;
            }

            //comprobamos la dificultad elegida, 0:facil, 1:dificil, 2:extrema
            RadioGroup rgDificultad=findViewById(R.id.radioGroupDificultad);

            int idDif=rgDificultad.getCheckedRadioButtonId();

            int dificultad = 0;
            this.dificultad ="Facil";
            if(idDif==R.id.rbDificil){
                dificultad=1;
                this.dificultad ="Difícil";
            }else if(idDif==R.id.rbExtremo){
                dificultad=2;
                this.dificultad ="Extrema";
            }

            //comenzamos la partida
            partida=new Partida(dificultad);

            //deshabilitar los botones del tablero mientras dura la partida
            (findViewById(R.id.btnUnJugador)).setEnabled(false);
            (findViewById(R.id.btnDosJugadores)).setEnabled(false);
            (findViewById(R.id.radioGroupDificultad)).setAlpha(0); //lo hacemos transparente
            (findViewById(R.id.nombreUsu)).setVisibility(View.INVISIBLE);
        }

    }

    /***
     * MÃ©todo asociado al evento onClick de cada una de las casillas de juego
     * @param vista
     */
    public void toqueCasilla(View vista){

        if(this.sonido == true) {
            //Reproducimos el sonido e la ficha
            if (mediaFichas == null) {
                mediaFichas = MediaPlayer.create(this, R.raw.sonidoficha);
            }
            if (!mediaFichas.isPlaying()) {
                mediaFichas.start();
            }
        }

        //sÃ³lo ejecutaremos el contenido de este mÃ©todo si la partida estÃ¡ comenzada
        if(partida==null){
            return;
        }
        else {
            //comprobamos la casilla en la que se ha pulsado y la guardamos en la variable casillaPulsada
            int casillaPulsada = 0;

            for(int i=0;i<9;i++){
                if (casillas[i]==vista.getId()){ //asignamos a la posiciÃ³n del array el id de la casilla pulsada
                    casillaPulsada=i;
                    break;
                }
            }

            //comprobamos si la casilla estÃ¡ ocupada antes de marcarla
            if(partida.casillaLibre(casillaPulsada)==false){
                return; //nos salimos porque la casilla pulsada estÃ¡ ocupada
            }

            marcarCasilla(casillaPulsada);

            //cambiamos de jugador
            int resultadoJuego=partida.turnoJuego();

            if(resultadoJuego>0){ //o bien hay empate o bien alguien ha ganado
                evaluarFinal(resultadoJuego);
                return; //salimos del mÃ©todo porque alguien ha ganado ya
            }

            //despuÃ©s de marcar la casilla que hemos pulsado hacemos que juege la mÃ¡quina
            //generamos una casilla al azar
            casillaPulsada=partida.ia();

            //hacemos que si la celda que ha elegido la mÃ¡quina estÃ¡ ocupada no siga hasta que elija una que
            //esta libre
            while (partida.casillaLibre(casillaPulsada)!=true){
                casillaPulsada=partida.ia();
            }

            //la marcamos
            marcarCasilla(casillaPulsada);

            //volvemos a cambiar el turno
            resultadoJuego=partida.turnoJuego();

            //evaluamos si el juego ha finalizado
            if(resultadoJuego>0){ //o bien hay empate o bien alguien ha ganado
                evaluarFinal(resultadoJuego);

            if(this.sonido == true) {
                if (mediaFinal == null) {
                    mediaFinal = MediaPlayer.create(this, R.raw.sonidofinal);
                }
                if (!mediaFinal.isPlaying()) {
                    mediaFinal.start();
                }
            }
        }
    }





    }

    /**
     * MÃ©todo que comprueba si alguien ha ganado el juego o ha habido empate
     * @param resultadoJuego
     */
    private void evaluarFinal(int resultadoJuego) {
        TextView tvNomUsu = findViewById(R.id.nombreUsu);
        String mensaje;
        int partidasUsu = 0, puntosUsu=0;
        boolean existeUsuario = false;

        if (resultadoJuego==1){ //ha ganado el jugador 1
            mensaje="Jugador 1 ha ganado";
            this.rs="Jug 1";

            if(this.dificultad.equals("Facil")){
                this.puntos=2;
            }else if(this.dificultad.equals("Dificil")){
                this.puntos=3;
            }else if(this.dificultad.equals("Extrema")){
                this.puntos=4;
            }

        }else if (resultadoJuego==2){//ha ganado el jugador 2
            mensaje="Jugador 2 ha ganado";
            this.rs="Jug 2";
        } else {
            mensaje="Empate";
            this.rs="Empate";
            this.puntos = 1;
        }
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();

        this.fin = true;

        //Guardamos el nombre del usuario
        this.nomUsu = tvNomUsu.getText().toString();
        this.partidas = 1;

        //INSERT PARTIDAS
        ContentValues values= new ContentValues();
        values.put("jugador1", this.nomUsu);
        values.put("jugador2", "Maquina");
        values.put("dificultad", this.dificultad);
        values.put("resultado", this.rs);
        db.insert("partidas",null, values);


        String condicion = " '"+this.nomUsu+"'";
        //Comprobar si existe el usuario
        Cursor fl = db.rawQuery("select * from usuarios where usuario="+condicion, null);

        if(fl.moveToFirst()) {
            for (int v = 0; v < fl.getCount(); v++) {

                partidasUsu = Integer.parseInt(fl.getString(2));
                puntosUsu = Integer.parseInt(fl.getString(3));

                fl.moveToNext();
            }

            partidasUsu++;
            puntosUsu=puntosUsu+this.puntos;


            existeUsuario=true;
        }else {
            existeUsuario=false;
            puntosUsu=this.puntos;
        }

        if(existeUsuario == false){
            //INSERT USUARIOS
            //Facil = 2 puntos // Dificil = 3 puntos // Extrema = 4 puntos // Empate = 1 puntos
            ContentValues values2= new ContentValues();
            values2.put("usuario", this.nomUsu);
            values2.put("numeroPartidas", Integer.toString(this.partidas));
            values2.put("puntos", Integer.toString(puntosUsu));
            db.insert("usuarios",null, values2);
        }else{
            String condicion2 = " '"+this.nomUsu+"'";
            String partidasS = " '" +Integer.toString(partidasUsu)+"'";
            db.execSQL("UPDATE usuarios SET numeroPartidas = "+partidasS+" WHERE usuario ="+condicion2);
            db.execSQL("UPDATE usuarios SET puntos = "+puntosUsu+" WHERE usuario ="+condicion2);
        }


        //finalizamos el juego
        partida=null;
        //volvemos a habilitar los controles para que se pueda volver a jugar
        (findViewById(R.id.btnUnJugador)).setEnabled(true);
        (findViewById(R.id.btnDosJugadores)).setEnabled(true);
        (findViewById(R.id.radioGroupDificultad)).setAlpha(1); //lo hacemos transparente
        (findViewById(R.id.nombreUsu)).setVisibility(View.VISIBLE);
        tvNomUsu.setText("");
    }

    /**
     * MÃ©todo que dibujarÃ¡ la casilla con un cÃ­rculo o con un aspa
     * @param casilla
     */
    private void marcarCasilla(int casilla){
        ImageView imagen;
        imagen=findViewById(casillas[casilla]); //le asignamos el id de la imagen de la casilla correspondiente a la que hay que marcar

        if(partida.jugador==1){
            imagen.setImageResource(R.drawable.circulo); //si el jugador que estÃ¡ marcando es el 1 le asignamos el cÃ­rculo a la casilla
        }else{
            imagen.setImageResource(R.drawable.aspa); //si el jugadro es el 2 dibujamos un aspa
        }

    }

    public void actPartidas(View view) {
        Intent i = new Intent(getApplicationContext(),Partidas.class);
        startActivity(i);
    }

    public void actRanking(View view) {
        Intent i = new Intent(getApplicationContext(),Ranking.class);
        startActivity(i);
    }

    public void activar_des_sonido(View view) {
        ImageButton imbtn = findViewById(R.id.btnSonido);



        if(this.sonido==true){
            this.sonido=false;
            imbtn.setImageResource(android.R.drawable.ic_lock_silent_mode);
            stopService(new Intent(getApplicationContext(),Musicafondo.class));
        }else{
            this.sonido=true;
            imbtn.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
            startService(new Intent(getApplicationContext(),Musicafondo.class));
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