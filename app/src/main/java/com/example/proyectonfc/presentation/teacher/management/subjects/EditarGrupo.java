package com.example.proyectonfc.presentation.teacher.management.subjects;


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

public class EditarGrupo extends AppCompatActivity {

    private Button buttonModificarGrupo;
    DataBase dataBase;
    private String identificador;
    private String grupo;
    private String entrada;
    private String salida;
    private String aula;
    private TextView textIdentificador;
    private TextView textGrupo;
    private EditText textEntrada;
    private EditText textSalida;
    private EditText textAula;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_grupo);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        identificador = getIntent().getStringExtra( "IDENTIFICADORGRUPO");
        consultarListaGrupos();

        buttonModificarGrupo = (Button) findViewById(R.id.buttonModificarGrupo);
        buttonModificarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String entrada = textEntrada.getText().toString();
                String salida = textSalida.getText().toString();
                String aula = textAula.getText().toString();
                dataBase.ActualizarGrupo(identificador, entrada, salida, aula);
                Toast.makeText(getApplicationContext(), "GRUPO EDITADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                onBackPressed();

            }


        });
    }


    private void consultarListaGrupos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();



        //select * from alumnos
        Cursor cursor=db.rawQuery("SELECT * FROM GRUPO WHERE id ="+"'"+identificador+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            grupo = cursor.getString(1);
            entrada = cursor.getString(2);
            salida = cursor.getString(3);
            aula = cursor.getString(4);

        }

        textIdentificador = (TextView) findViewById(R.id.textIdentificador);
        textIdentificador.setText(identificador);
        textGrupo = (TextView) findViewById(R.id.textGrupo);
        textGrupo.setText(grupo);
        textEntrada = (EditText) findViewById(R.id.textEntrada);
        textEntrada.setText(entrada);
        textSalida = (EditText) findViewById(R.id.textSalida);
        textSalida.setText(salida);
        textAula = (EditText) findViewById(R.id.textAula);
        textAula.setText(aula);

    }

}
