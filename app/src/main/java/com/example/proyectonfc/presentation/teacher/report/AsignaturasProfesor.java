package com.example.proyectonfc.presentation.teacher.report;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

import java.util.ArrayList;
import java.util.List;

public class AsignaturasProfesor extends AppCompatActivity {

    DataBase dataBase;
    ArrayList<String> listaAsignaturas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asignaturas_profesor);

        dataBase = new DataBase(getApplicationContext());
        ListView asignaturas = (ListView) findViewById(R.id.listaAsignaturas);

        consultarListaAsignaturas();
        ArrayAdapter adaptador=new ArrayAdapter(this,android.R.layout.simple_list_item_1, (List) listaAsignaturas);
        asignaturas.setAdapter(adaptador);

        asignaturas.setOnItemClickListener( (adapterView, view, position, id) -> {
            String asignatura = listaAsignaturas.get(position);
            asistenciaNormal(asignatura, view.getContext());
        });
    }

    private void asistenciaNormal(String asignatura, Context context) {
            Intent intent = new Intent(context, DatosAsignaturaNormal.class);
            intent.putExtra("ASIGNATURA", asignatura );
            startActivity(intent);
    }


    private void consultarListaAsignaturas() {
        listaAsignaturas= new ArrayList<>();

        SQLiteDatabase db=dataBase.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT id FROM ASIGNATURA", null);

        while (cursor.moveToNext()) listaAsignaturas.add(cursor.getString(0));
    }

}

