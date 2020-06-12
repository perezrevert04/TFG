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
import android.widget.EditText;
import android.widget.TextView;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.util.Asignatura;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class DatosAsignaturaRecuperacion extends AppCompatActivity {

    private Button buttonAsistencia;
    DataBase dataBase;
    ArrayList<String> listaAsignaturas;
    ArrayList<Asignatura> asignaturasList;
    private String identificador;
    private String nombreAsignatura;
    private String nombre;
    private String titulacion;
    private String grupo;
    private String curso;
    private String gestora;
    private String idioma;
    private String duracion;
    private String aula;
    private String hEntrada;
    private String hSalida;
    private TextView textIdentificador;
    private TextView textNombre;
    private TextView textTitulacion;
    private TextView textCurso;
    private TextView textGestora;
    private TextView textIdioma;
    private TextView textDuracion;
    private String asignatura;
    private String nombreprofesor;
    private String dniprofesor;
    private EditText textGrupo;
    private EditText textEspacio;
    private EditText textHEntrada;
    private EditText textHoraSalida;
    private TextView textAsignatura;


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
                Intent intent = new Intent(DatosAsignaturaRecuperacion.this, DatosAsignaturaRecuperacion.class);
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
        setContentView(R.layout.datos_asignatura_recuperacion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);




        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);



        //Intent intent = new Intent(DatosAsignatura.this.getApplicationContext(), DatosAsignatura.class);
        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        nombreprofesor = getIntent().getStringExtra( "NOMBREPROFESOR");
        dniprofesor = getIntent().getStringExtra( "DNIPROFESOR");

        consultarListaAsignaturas();


        buttonAsistencia = (Button) findViewById(R.id.buttonAsistencia);
        buttonAsistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textGrupo = (EditText) findViewById(R.id.textGrupo);
                grupo = textGrupo.getText().toString();
                textEspacio = (EditText) findViewById(R.id.textEspacio);
                aula = textEspacio.getText().toString();
                textHEntrada = (EditText) findViewById(R.id.textHoraEntrada);
                hEntrada = textHEntrada.getText().toString();
                textHoraSalida = (EditText) findViewById(R.id.textHoraSalida);
                hSalida = textHoraSalida.getText().toString();;

                Intent intent = new Intent(v.getContext(), RegistroAlumnos.class);
                intent.putExtra("ASIGNATURA", asignatura );
                intent.putExtra("NOMBRE", nombre );
                intent.putExtra("TITULACION", titulacion );
                intent.putExtra("GRUPO", grupo );
                intent.putExtra("CURSO", curso );
                intent.putExtra("GESTORA", gestora );
                intent.putExtra("IDIOMA", idioma );
                intent.putExtra("DURACION", duracion );
                intent.putExtra("HORAINICIO", hEntrada );
                intent.putExtra("AULA", aula );
                intent.putExtra("NOMBREPROFESOR", nombreprofesor );
                intent.putExtra("DNIPROFESOR", dniprofesor );
                startActivityForResult(intent, 0);
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


}
