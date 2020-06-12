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

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.util.Asignatura;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class DatosAsignatura extends AppCompatActivity {

    private Button buttonEditar;
    DataBase dataBase;
    ArrayList<String> listaAsignaturas;
    ArrayList<Asignatura> asignaturasList;
    private String identificador;
    private String nombre;
    private String nombreAsignatura;
    private String titulacion;
    private String curso;
    private String gestora;
    private String idioma;
    private String duracion;
    private TextView text;
    private TextView textIdentificador;
    private TextView textNombre;
    private TextView textTitulacion;
    private TextView textCurso;
    private TextView textGestora;
    private TextView textIdioma;
    private TextView textDuracion;
    private TextView textAsignatura;
    private String asignatura;


    @Override
    public void onResume() {
        super.onResume();
        consultarListaAsignaturas();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_asignatura);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);



        //Intent intent = new Intent(DatosAsignatura.this.getApplicationContext(), DatosAsignatura.class);
        asignatura = getIntent().getStringExtra( "ASIGNATURA");

        consultarListaAsignaturas();






        buttonEditar = (Button) findViewById(R.id.buttonEditar);
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), EditarAsignatura.class);
                intent.putExtra("ASIGNATURA", asignatura );
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
