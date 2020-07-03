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

public class DatosAlumno extends AppCompatActivity {

    private Button buttonEditar;
    DataBase dataBase;
    ArrayList<String> listaAsignaturas;
    ArrayList<Asignatura> asignaturasList;
    private String identificador;
    private String dni;
    private String nombre;
    private String alumno;

    private TextView textAlumno;
    private TextView textIdentificador;
    private TextView textDni;
    private TextView textNombre;

    private String asignatura;
    private String dniAlumno;

    @Override
    public void onResume() {
        super.onResume();
        consultarListaAlumnos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_alumno);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);



        //Intent intent = new Intent(DatosAsignatura.this.getApplicationContext(), DatosAsignatura.class);
        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dniAlumno = getIntent().getStringExtra( "DNIALUMNO");

        consultarListaAlumnos();





        buttonEditar = (Button) findViewById(R.id.buttonEditar);
        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), EditarAlumno.class);
                intent.putExtra("ASIGNATURA", asignatura );
                intent.putExtra("DNIALUMNO", dniAlumno );
                intent.putExtra("IDENTIFICADORALUMNO", identificador );
                startActivityForResult(intent, 0);
            }

        });


    }

    private void consultarListaAlumnos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();



        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM ALUMNO WHERE dni = "+"'"+dniAlumno+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            dni = cursor.getString(1);
            nombre = cursor.getString(2);
            alumno = cursor.getString(2);

        }

        textIdentificador = (TextView) findViewById(R.id.textIdentificador);
        textIdentificador.setText(identificador);
        textDni = (TextView) findViewById(R.id.textDni);
        textDni.setText(dni);
        textNombre = (TextView) findViewById(R.id.textNombre);
        textNombre.setText(nombre);
        textAlumno = (TextView) findViewById(R.id.textAlumno);
        textAlumno.setText(alumno);


    }


}
