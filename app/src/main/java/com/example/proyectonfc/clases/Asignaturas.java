package com.example.proyectonfc.clases;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

public class Asignaturas extends AppCompatActivity {

    private Button buttonDatosAsignatura;
    private Button buttonAlumnos;
    private Button buttonGrupos;
    DataBase dataBase;
    private String asignatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asignaturas);
        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);

        buttonDatosAsignatura= (Button) findViewById(R.id.buttonDatosAsignatura);
        buttonDatosAsignatura.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DatosAsignatura.class);
            asignatura = getIntent().getStringExtra( "ASIGNATURA");
            intent.putExtra("ASIGNATURA", asignatura );
            startActivityForResult(intent, 0);
        });

        buttonAlumnos= (Button) findViewById(R.id.buttonAlumnos);
        buttonAlumnos.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Alumnos.class);
            asignatura = getIntent().getStringExtra( "ASIGNATURA");
            intent.putExtra("ASIGNATURA", asignatura );
            startActivityForResult(intent, 0);
        });

        buttonGrupos= (Button) findViewById(R.id.buttonGrupos);
        buttonGrupos.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Grupos.class);
            asignatura = getIntent().getStringExtra( "ASIGNATURA");
            intent.putExtra("ASIGNATURA", asignatura );
            startActivityForResult(intent, 0);
        });
    }
}
