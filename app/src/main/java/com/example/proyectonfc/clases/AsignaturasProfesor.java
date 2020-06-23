package com.example.proyectonfc.clases;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.util.Asignatura;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsignaturasProfesor extends AppCompatActivity {

    DataBase dataBase;
    private TextView asignaturaSeleccionada;
    private  String asignatura;
    ArrayList<String> listaAsignaturas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asignaturas_profesor);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        ListView asignaturas = (ListView) findViewById(R.id.listaAsignaturas);

        consultarListaAsignaturas();
        ArrayAdapter adaptador=new ArrayAdapter
                (this,android.R.layout.simple_list_item_1, (List) listaAsignaturas);
        asignaturas.setAdapter(adaptador);


        Button buttonRecuperacion = (Button) findViewById(R.id.buttonRecuperacion);
        buttonRecuperacion.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DatosAsignaturaRecuperacion.class);
            intent.putExtra("ASIGNATURA", asignatura );
            if (asignatura == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AsignaturasProfesor.this);
                builder.setTitle("No ha seleccionado ninguna Asignatura");
                builder.setMessage("¡Debe de seleccionar alguna Asignatura!");
                builder.setNeutralButton("¡Entendido!", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else {
                startActivityForResult(intent, 0);
            }
        });

        Button buttonNormal = (Button) findViewById(R.id.buttonNormal);
        buttonNormal.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DatosAsignaturaNormal.class);
            intent.putExtra("ASIGNATURA", asignatura );
            if (asignatura == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AsignaturasProfesor.this);
                builder.setTitle("No ha seleccionado ninguna Asignatura");
                builder.setMessage("¡Debe de seleccionar alguna Asignatura!");
                builder.setNeutralButton("¡Entendido!", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else {
                startActivityForResult(intent, 0);
            }
        });

        asignaturas.setOnItemClickListener((AdapterView.OnItemClickListener) (adapterView, view, position, id) -> {
            asignatura = listaAsignaturas.get(position);
            asignaturaSeleccionada = (TextView) findViewById(R.id.asignaturaSeleccionada);
            asignaturaSeleccionada.setText(asignatura);
        });
    }

    private void consultarListaAsignaturas() {
        listaAsignaturas= new ArrayList<>();

        SQLiteDatabase db=dataBase.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT id FROM ASIGNATURA", null);

        while (cursor.moveToNext()) listaAsignaturas.add(cursor.getString(0));
    }

}

