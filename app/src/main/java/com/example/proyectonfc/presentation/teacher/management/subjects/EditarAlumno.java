package com.example.proyectonfc.presentation.teacher.management.subjects;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

public class EditarAlumno extends AppCompatActivity {

    private Button buttonSave;
    DataBase dataBase;
    private String identificador;
    private String dni;
    private String nombre;
    private TextView textIdentificador;
    private EditText textDni;
    private EditText textNombre;
    private String asignatura;
    private String dniAlumno;
    private String identificadorAlumno;
    private String studentId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_alumno);

        dataBase = new DataBase(getApplicationContext());
        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dniAlumno = getIntent().getStringExtra( "DNIALUMNO");
        identificadorAlumno = getIntent().getStringExtra( "IDENTIFICADORALUMNO");
        studentId = getIntent().getStringExtra( "StudentId");
        consultarListaAlumnos();

        buttonSave = findViewById(R.id.buttonSaveStudentData);
        buttonSave.setOnClickListener(v -> {

            String nombre2 = textNombre.getText().toString();
            String dni2 = textDni.getText().toString();
            dataBase.ActualizarAlumno(identificadorAlumno, nombre2, dni2);
            Toast.makeText(getApplicationContext(), "¡Datos del alumno actualizados!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        });
    }


    private void consultarListaAlumnos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        //select * from alumnos
        Cursor cursor=db.rawQuery("SELECT * FROM ALUMNO WHERE id = "+"'"+studentId+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            dni = cursor.getString(1);
            nombre = cursor.getString(2);
        }

        textIdentificador = findViewById(R.id.textView56);
        textIdentificador.setText(identificador);
        textNombre = findViewById(R.id.textNombre);
        textNombre.setText(nombre);
        textDni = findViewById(R.id.textDni);
        textDni.setText(dni);

    }

}
