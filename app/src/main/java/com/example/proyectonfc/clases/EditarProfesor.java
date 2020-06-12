package com.example.proyectonfc.clases;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

import androidx.appcompat.app.AppCompatActivity;

public class EditarProfesor extends AppCompatActivity {

    private Button buttonModificarProfesor;
    DataBase dataBase;
    private String identificador;
    private String dni;
    private String nombre;
    private TextView textIdentificador;
    private EditText textDni;
    private EditText textNombre;
    private String asignatura;
    private String dniProfesor;
    private String identificadorProfesor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_profesor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dniProfesor = getIntent().getStringExtra( "DNIPROFESOR");
        identificadorProfesor = getIntent().getStringExtra( "IDENTIFICADORPROFESOR");
        consultarListaProfesores();

        buttonModificarProfesor = (Button) findViewById(R.id.buttonModificarProfesor);
        buttonModificarProfesor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre2 = textNombre.getText().toString();
                String dni2 = textDni.getText().toString();

                dataBase.ActualizarProfesor(identificadorProfesor, nombre2, dni2);
                Toast.makeText(getApplicationContext(), "PROFESOR EDITADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

        });

    }


    private void consultarListaProfesores() {
        SQLiteDatabase db=dataBase.getReadableDatabase();



        //select * from profesores
        Cursor cursor=db.rawQuery("SELECT * FROM PROFESOR WHERE dni = "+"'"+dniProfesor+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            dni = cursor.getString(1);
            nombre = cursor.getString(2);

        }

        textIdentificador = (TextView) findViewById(R.id.textIdentificador);
        textIdentificador.setText(identificador);
        textNombre = (EditText) findViewById(R.id.textNombre);
        textNombre.setText(nombre);
        textDni = (EditText) findViewById(R.id.textDni);
        textDni.setText(dni);

    }

}
