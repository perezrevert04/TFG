package com.example.proyectonfc.clases;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

import androidx.appcompat.app.AppCompatActivity;

public class Asignaturas extends AppCompatActivity {

    private Button buttonDatosAsignatura;
    private Button buttonProfesores;
    private Button buttonAlumnos;
    private Button buttonGrupos;
    DataBase dataBase;
    private String asignatura;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asignaturas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);



        buttonDatosAsignatura= (Button) findViewById(R.id.buttonDatosAsignatura);
        buttonDatosAsignatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DatosAsignatura.class);
                asignatura = getIntent().getStringExtra( "ASIGNATURA");
                intent.putExtra("ASIGNATURA", asignatura );
                startActivityForResult(intent, 0);
            }

        });

        buttonProfesores = (Button) findViewById(R.id.buttonProfesores);
        buttonProfesores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Profesores.class);
                asignatura = getIntent().getStringExtra( "ASIGNATURA");
                intent.putExtra("ASIGNATURA", asignatura );
                startActivityForResult(intent, 0);
            }

        });

        buttonAlumnos= (Button) findViewById(R.id.buttonAlumnos);
        buttonAlumnos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Alumnos.class);
                asignatura = getIntent().getStringExtra( "ASIGNATURA");
                intent.putExtra("ASIGNATURA", asignatura );
                startActivityForResult(intent, 0);
            }
        });

        buttonGrupos= (Button) findViewById(R.id.buttonGrupos);
        buttonGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Grupos.class);
                asignatura = getIntent().getStringExtra( "ASIGNATURA");
                intent.putExtra("ASIGNATURA", asignatura );
                startActivityForResult(intent, 0);
            }
        });
    }
}
