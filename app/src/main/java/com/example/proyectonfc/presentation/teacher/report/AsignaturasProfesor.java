package com.example.proyectonfc.presentation.teacher.report;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.use_cases.CommandVoiceActivity;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

public class AsignaturasProfesor extends AppCompatActivity {

    DataBase dataBase;
    ArrayList<String> listaAsignaturas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asignaturas_profesor);
        Slidr.attach(this);

        dataBase = new DataBase(getApplicationContext());
        ListView asignaturas = findViewById(R.id.listaAsignaturas);

        consultarListaAsignaturas();
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaAsignaturas);
        asignaturas.setAdapter(adaptador);

        asignaturas.setOnItemClickListener( (adapterView, view, position, id) -> {
            String asignatura = listaAsignaturas.get(position);
            asistenciaNormal(asignatura, view.getContext());
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_voice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.command_voice:
                Intent intent = new Intent(this, CommandVoiceActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

