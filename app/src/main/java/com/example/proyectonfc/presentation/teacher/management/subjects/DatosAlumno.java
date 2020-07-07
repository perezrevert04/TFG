package com.example.proyectonfc.presentation.teacher.management.subjects;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

public class DatosAlumno extends AppCompatActivity {

    private Button buttonEditar;
    DataBase dataBase;
    private String identificador;
    private String dni;
    private String alumno;

    private TextView textAlumno;
    private TextView textIdentificador;
    private TextView textDni;

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

        dataBase = new DataBase(getApplicationContext());

        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dniAlumno = getIntent().getStringExtra( "DNIALUMNO");

        setTitle("Alumno de " + asignatura);

        consultarListaAlumnos();

        buttonEditar = findViewById(R.id.buttonEditStudent);
        buttonEditar.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditarAlumno.class);
            intent.putExtra("ASIGNATURA", asignatura );
            intent.putExtra("DNIALUMNO", dniAlumno );
            intent.putExtra("IDENTIFICADORALUMNO", identificador );
            startActivity(intent);
        });

    }

    private void consultarListaAlumnos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM ALUMNO WHERE dni = "+"'"+dniAlumno+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            dni = cursor.getString(1);
            alumno = cursor.getString(2);
        }

        textIdentificador = findViewById(R.id.textViewStudentId);
        textIdentificador.setText(identificador);
        textDni = findViewById(R.id.textViewStudentDni);
        textDni.setText(dni);
        textAlumno = findViewById(R.id.textView60);
        textAlumno.setText(alumno);

    }

}
