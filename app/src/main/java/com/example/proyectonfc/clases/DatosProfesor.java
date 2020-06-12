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

public class DatosProfesor extends AppCompatActivity {

    private Button buttonEditar;
    DataBase dataBase;
    ArrayList<String> listaAsignaturas;
    ArrayList<Asignatura> asignaturasList;
    private String identificador;
    private String dni;
    private String nombre;
    private String profesor;

    private TextView textProfesor;
    private TextView textIdentificador;
    private TextView textDni;
    private TextView textNombre;

    private String asignatura;
    private String dniProfesor;

    @Override
    public void onResume() {
        super.onResume();
        consultarListaProfesores();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_profesor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);



        //Intent intent = new Intent(DatosAsignatura.this.getApplicationContext(), DatosAsignatura.class);
        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dniProfesor = getIntent().getStringExtra( "DNIPROFESOR");

        consultarListaProfesores();






        buttonEditar = (Button) findViewById(R.id.buttonEditar);
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), EditarProfesor.class);
                intent.putExtra("ASIGNATURA", asignatura );
                intent.putExtra("DNIPROFESOR", dniProfesor );
                intent.putExtra("IDENTIFICADORPROFESOR", identificador );
                startActivityForResult(intent, 0);
            }

        });

    }

    private void consultarListaProfesores() {
        SQLiteDatabase db=dataBase.getReadableDatabase();



        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM PROFESOR WHERE dni = "+"'"+dniProfesor+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            dni = cursor.getString(1);
            nombre = cursor.getString(2);
            profesor = cursor.getString(2);


        }

        textIdentificador = (TextView) findViewById(R.id.textIdentificador);
        textIdentificador.setText(identificador);
        textDni = (TextView) findViewById(R.id.textDni);
        textDni.setText(dni);
        textNombre = (TextView) findViewById(R.id.textNombre);
        textNombre.setText(nombre);
        textProfesor = (TextView) findViewById(R.id.textProfesor);
        textProfesor.setText(profesor);


    }


}
