package com.example.proyectonfc.clases;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.util.Asignatura;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class DatosAsignaturaNormal extends AppCompatActivity {

    private Button buttonAsistencia;
    DataBase dataBase;
    ArrayList<String> listaAsignaturas;
    ArrayList<Asignatura> asignaturasList;
    private String identificador;
    private String nombreAsignatura;
    private String identificadorGrupo;
    private String nombre;
    private String titulacion;
    private String grupo;
    private String curso;
    private String gestora;
    private String idioma;
    private String duracion;
    private String espacio;
    private TextView text;
    private TextView textIdentificadorGrupo;
    private TextView textIdentificador;
    private TextView textNombre;
    private TextView textTitulacion;
    private TextView textGrupo;
    private TextView textCurso;
    private TextView textGestora;
    private TextView textIdioma;
    private TextView textDuracion;
    private TextView textAulaGrupo;
    private TextView textHoraEntradaGrupo;
    private TextView textHoraSalidaGrupo;
    private TextView textAsignatura;
    private TextView textAsigcnatura;
    private String asignatura;
    private Calendar horaActualMenos;
    private Calendar horaActualMas;
    private Calendar horaActual;
    private String grupoGrupo;;
    private String horaInicioGrupo;
    private String horaFinGrupo;
    private String aulaGrupo;
    private String nombreprofesor;
    private String dniprofesor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actualizar,menu);
        return true;

    }


    public boolean onOptionsItemSelected(MenuItem menu)
    {
        // gracias a la id, sabemos que item es el que se oprime, en este caso usamos un switch
        switch (menu.getItemId())
        {
            case R.id.MenuActualizar:
                Intent intent = new Intent(DatosAsignaturaNormal.this, DatosAsignaturaNormal.class);
                asignatura = getIntent().getStringExtra( "ASIGNATURA");
                intent.putExtra("ASIGNATURA", asignatura );
                startActivity(intent);
                this.finish();
                return true;

            default:
        }
        return super.onOptionsItemSelected(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_asignatura_normal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        dataBase = new DataBase(getApplicationContext(), "DB5.db", null, 1);



        //Intent intent = new Intent(DatosAsignatura.this.getApplicationContext(), DatosAsignatura.class);
        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        nombreprofesor = getIntent().getStringExtra( "NOMBREPROFESOR");
        dniprofesor = getIntent().getStringExtra( "DNIPROFESOR");


        consultarListaAsignaturas();
        consultarHoraMenos();






        buttonAsistencia = (Button) findViewById(R.id.buttonAsistencia);
        buttonAsistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), RegistroAlumnos.class);

                intent.putExtra("ASIGNATURA", asignatura );
                intent.putExtra("NOMBRE", nombre );
                intent.putExtra("TITULACION", titulacion );
                intent.putExtra("GRUPO", grupoGrupo );
                intent.putExtra("CURSO", curso );
                intent.putExtra("GESTORA", gestora );
                intent.putExtra("IDIOMA", idioma );
                intent.putExtra("DURACION", duracion );
                intent.putExtra("HORAINICIO", horaInicioGrupo );
                intent.putExtra("HORAFIN", horaFinGrupo );
                intent.putExtra("AULA", aulaGrupo );
                intent.putExtra("NOMBREPROFESOR", nombreprofesor );
                intent.putExtra("DNIPROFESOR", dniprofesor );

                try{
                    startActivityForResult(intent, 0);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "LA ASIGNATURA SELECCIONADA ANTERIORMENTE NO TIENE UN GRUPO CON ESTE HORARIO", Toast.LENGTH_SHORT).show();
                }


            }

        });

    }

    private void consultarListaAsignaturas() {
        SQLiteDatabase db=dataBase.getReadableDatabase();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM ASIGNATURA WHERE id="+"'"+asignatura+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            nombreAsignatura = cursor.getString(1);
            nombre = cursor.getString(1);
            titulacion = cursor.getString(2);
            curso = cursor.getString(3);
            gestora = cursor.getString(4);
            idioma = cursor.getString(5);
            duracion = cursor.getString(6);

        }

        textAsignatura = (TextView) findViewById(R.id.textAsignatura);
        textAsignatura.setText(nombreAsignatura);
        textIdentificador = (TextView) findViewById(R.id.textIdentificador);
        textIdentificador.setText(identificador);
        textNombre = (TextView) findViewById(R.id.textNombre);
        textNombre.setText(nombre);
        textTitulacion = (TextView) findViewById(R.id.textTitulacion);
        textTitulacion.setText(titulacion);
        textCurso = (TextView) findViewById(R.id.textCurso);
        textCurso.setText(curso);
        textGestora = (TextView) findViewById(R.id.textGestora);
        textGestora.setText(gestora);
        textIdioma = (TextView) findViewById(R.id.textIdioma);
        textIdioma.setText(idioma);
        textDuracion = (TextView) findViewById(R.id.textDuracion);
        textDuracion.setText(duracion);
    }

    public Date RestarHoras(Date fecha, int minutos){

        horaActualMenos = Calendar.getInstance();
        horaActualMenos.setTime(fecha); // Configuramos la fecha que se recibe
        horaActualMenos.add(Calendar.MINUTE, minutos);  // numero de horas a añadir, o restar en caso de horas<0
        return horaActualMenos.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas

    }

    public Date SumarHoras(Date fecha, int minutos){


         // Configuramos la fecha que se recibe
        horaActualMas.add(Calendar.MINUTE, minutos);  // numero de horas a añadir, o restar en caso de horas<0
        return horaActualMas.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas

    }



    private void consultarHoraMenos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();



        Calendar calendarioIgual = Calendar.getInstance();
        calendarioIgual.add(Calendar.MINUTE, 0);

        String horaIgual =  new SimpleDateFormat("HH:mm").format(calendarioIgual.getTime());

        Calendar calendarioMas = Calendar.getInstance();
        calendarioMas.add(Calendar.MINUTE, 0);

        String horaMas =  new SimpleDateFormat("HH").format(calendarioMas.getTime());






        //select * from asignatura
        Cursor cursor=db.rawQuery("SELECT * FROM GRUPO WHERE h_entrada BETWEEN "+"'"+horaMas+":00' AND "+"'"+horaIgual+"'", null);

        while (cursor.moveToNext()){
            identificadorGrupo = cursor.getString(0);
            grupoGrupo = cursor.getString(1);
            horaInicioGrupo = cursor.getString(2);
            horaFinGrupo = cursor.getString(3);
            aulaGrupo = cursor.getString(4);
        }


        textGrupo = (TextView) findViewById(R.id.textGrupo);
        textGrupo.setText(grupoGrupo);
        textHoraEntradaGrupo = (TextView) findViewById(R.id.textHoraEntradaGrupo);
        textHoraEntradaGrupo.setText(horaInicioGrupo);
        textHoraSalidaGrupo = (TextView) findViewById(R.id.textHoraSalidaGrupo);
        textHoraSalidaGrupo.setText(horaFinGrupo);
        textAulaGrupo = (TextView) findViewById(R.id.textAulaGrupo);
        textAulaGrupo.setText(aulaGrupo);
    }

}
