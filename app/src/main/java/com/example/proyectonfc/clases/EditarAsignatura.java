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
import com.example.proyectonfc.util.Asignatura;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class EditarAsignatura extends AppCompatActivity {

  private Button buttonModificarAsignatura;
  DataBase dataBase;
  private String identificador;
  private String nombre;
  private String titulacion;
  private String curso;
  private String gestora;
  private String idioma;
  private String duracion;
  private TextView textIdentificador;
  private EditText textNombre;
  private EditText textTitulacion;
  private EditText textCurso;
  private EditText textGestora;
  private EditText textIdioma;
  private EditText textDuracion;

  private String asignatura;


  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.editar_asignatura);
    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
    asignatura = getIntent().getStringExtra( "ASIGNATURA");
    consultarListaAsignaturas();

    buttonModificarAsignatura = (Button) findViewById(R.id.buttonModificarAsignatura);
    buttonModificarAsignatura.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String nombre2 = textNombre.getText().toString();
        String titulacion2 = textTitulacion.getText().toString();
        String curso2 = textCurso.getText().toString();
        String gestora2 = textGestora.getText().toString();
        String idioma2 = textIdioma.getText().toString();
        String duracion2 = textDuracion.getText().toString();

        dataBase.ActualizarAsignatura(asignatura, nombre2, titulacion2, curso2, gestora2, idioma2, duracion2);
        Toast.makeText(getApplicationContext(), "ASIGNATURA EDITADA CORRECTAMENTE", Toast.LENGTH_SHORT).show();
        onBackPressed();
      }

    });

  }


  private void consultarListaAsignaturas() {
    SQLiteDatabase db=dataBase.getReadableDatabase();



    //select * from usuarios
    Cursor cursor=db.rawQuery("SELECT * FROM ASIGNATURA WHERE id="+"'"+asignatura+"'", null);

    while (cursor.moveToNext()){
      identificador = cursor.getString(0);
      nombre = cursor.getString(1);
      titulacion = cursor.getString(2);
      curso = cursor.getString(3);
      gestora = cursor.getString(4);
      idioma = cursor.getString(5);
      duracion = cursor.getString(6);

    }

    textIdentificador = (TextView) findViewById(R.id.textIdentificador);
    textIdentificador.setText(identificador);
    textNombre = (EditText) findViewById(R.id.textNombre);
    textNombre.setText(nombre);
    textTitulacion = (EditText) findViewById(R.id.textTitulacion);
    textTitulacion.setText(titulacion);
    textCurso = (EditText) findViewById(R.id.textCurso);
    textCurso.setText(curso);
    textGestora = (EditText) findViewById(R.id.textGestora);
    textGestora.setText(gestora);
    textIdioma = (EditText) findViewById(R.id.textIdioma);
    textIdioma.setText(idioma);
    textDuracion = (EditText) findViewById(R.id.textDuracion);
    textDuracion.setText(duracion);

  }

}
