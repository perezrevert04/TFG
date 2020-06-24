package com.example.proyectonfc.clases;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
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

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        ListView asignaturas = (ListView) findViewById(R.id.listaAsignaturas);

        consultarListaAsignaturas();
        ArrayAdapter adaptador=new ArrayAdapter(this,android.R.layout.simple_list_item_1, (List) listaAsignaturas);
        asignaturas.setAdapter(adaptador);


//        buttonRecuperacion.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), DatosAsignaturaRecuperacion.class);
//            intent.putExtra("ASIGNATURA", asignatura );
//            if (asignatura == null) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(AsignaturasProfesor.this);
//                builder.setTitle("No ha seleccionado ninguna Asignatura");
//                builder.setMessage("¡Debe de seleccionar alguna Asignatura!");
//                builder.setNeutralButton("¡Entendido!", null);
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }else {
//                startActivityForResult(intent, 0);
//            }
//        });
//
//        buttonNormal.setOnClickListener(v -> {
//            Intent intent = new Intent(v.getContext(), DatosAsignaturaNormal.class);
//            intent.putExtra("ASIGNATURA", asignatura );
//            if (asignatura == null) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(AsignaturasProfesor.this);
//                builder.setTitle("No ha seleccionado ninguna Asignatura");
//                builder.setMessage("¡Debe de seleccionar alguna Asignatura!");
//                builder.setNeutralButton("¡Entendido!", null);
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }else {
//                startActivityForResult(intent, 0);
//            }
//        });

        asignaturas.setOnItemClickListener( (adapterView, view, position, id) -> {
            String asignatura = listaAsignaturas.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(asignatura).setMessage("Seleccione el tipo de asistencia:");
            builder.setNegativeButton("Recuperación", (dialog, id1) -> {
                asistenciaRecuperacion(asignatura, view.getContext());
            });
            builder.setPositiveButton("Normal", (dialog, id1) -> {
                asistenciaNormal(asignatura, view.getContext());

            });

            AlertDialog dialog = builder.create();
            dialog.show();

        });
    }

    private void asistenciaNormal(String asignatura, Context context) {
            Intent intent = new Intent(context, DatosAsignaturaNormal.class);
            intent.putExtra("ASIGNATURA", asignatura );
            startActivity(intent);
    }

    private void asistenciaRecuperacion(String asignatura, Context context) {
        Intent intent = new Intent(context, DatosAsignaturaRecuperacion.class);
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

