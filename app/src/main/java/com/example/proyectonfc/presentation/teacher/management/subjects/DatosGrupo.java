package com.example.proyectonfc.presentation.teacher.management.subjects;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.model.Group;

public class DatosGrupo extends AppCompatActivity {


    DataBase dataBase;
    Button buttonEditar;
    private String identificador;
    private String nombreGrupo;
    private String grupo;
    private String h_entrada;
    private String h_salida;
    private String aula;

    private TextView textIdentificador;
    private TextView textGrupo;
    private TextView textNombreGrupo;
    private TextView textHEntrada;
    private TextView textHSalida;
    private TextView textAula;
    private String Nombregrupo;

    private String asignatura;

    private Group group;

    @Override
    public void onResume() {
        super.onResume();
        consultarListaGrupos();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_grupo);

        dataBase = new DataBase(getApplicationContext());

        //Intent intent = new Intent(DatosAsignatura.this.getApplicationContext(), DatosAsignatura.class);
        Nombregrupo = getIntent().getStringExtra( "GRUPO");
        asignatura = getIntent().getStringExtra( "ASIGNATURA");

        consultarListaGrupos();

        buttonEditar = (Button) findViewById(R.id.buttonEditar);
        buttonEditar.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), EditGroupActivity.class);
            intent.putExtra("Group", group);
            startActivity(intent);
        });

    }

    private void consultarListaGrupos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();



        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM GRUPO WHERE id LIKE"+"'"+asignatura+"%' AND grupo = '"+Nombregrupo+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            grupo = cursor.getString(1);
            nombreGrupo = cursor.getString(1);
            h_entrada = cursor.getString(2);
            h_salida = cursor.getString(3);
            aula = cursor.getString(4);

            group = new Group(identificador, grupo, aula, h_entrada);

        }

        textIdentificador = (TextView) findViewById(R.id.textIdentificador);
        textIdentificador.setText(identificador);
        textNombreGrupo = (TextView) findViewById(R.id.textNombreGrupo);
        textNombreGrupo.setText(nombreGrupo);
        textGrupo = (TextView) findViewById(R.id.textGrupo);
        textGrupo.setText(grupo);
        textHEntrada = (TextView) findViewById(R.id.textHEntrada);
        textHEntrada.setText(h_entrada);
        textHSalida = (TextView) findViewById(R.id.textHSalida);
        textHSalida.setText(h_salida);
        textAula = (TextView) findViewById(R.id.textAula);
        textAula.setText(aula);


    }


}
