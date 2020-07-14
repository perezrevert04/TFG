package com.example.proyectonfc.presentation.teacher.management.subjects;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.gestures.CommandVoiceActivity;
import com.example.proyectonfc.model.Alumno;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Alumnos extends AppCompatActivity {

    DataBase dataBase;
    private ListView Alumnos;
    private String asignatura;
    private String alumno;
    ArrayList<String> listaAlumnos;
    ArrayList<Alumno> alumnosList;
    ArrayList<String> identifiers;

    @Override
    public void onResume() {
        super.onResume();
        consultarListaAlumnos();
        updateAdapter();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos);

        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dataBase = new DataBase(getApplicationContext());
        Alumnos = findViewById(R.id.listaAlumnos);
        dataBase = new DataBase(getApplicationContext());
        setTitle("Alumnos de " + asignatura);

        updateAdapter();


        FloatingActionButton fab = findViewById(R.id.fabAddStudent);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(Alumnos.this, NuevoAlumno.class);
            asignatura = getIntent().getStringExtra("ASIGNATURA");
            intent.putExtra("ASIGNATURA", asignatura);
            startActivity(intent);
        });

        Alumnos.setOnItemClickListener((adapterView, view, position, id) -> {
            alumno = listaAlumnos.get(position);

            Intent intent = new Intent(view.getContext(), DatosAlumno.class);
            intent.putExtra("ASIGNATURA", asignatura );
            intent.putExtra("StudentId", identifiers.get(position) );
            startActivity(intent);
        });

        Alumnos.setOnItemLongClickListener( (adapterView, view, position, id) -> {
            alumno = listaAlumnos.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(Alumnos.this);
            builder.setTitle(alumno);
            builder.setMessage("¿Desea eliminar este alumno de la asignatura " + asignatura + "?");
            builder.setNegativeButton("No", null);

            builder.setPositiveButton("Sí", (dialog, which) -> {
                dataBase.deleteStudent(identifiers.get(position));
                updateAdapter();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
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

    private void consultarListaAlumnos() {
        SQLiteDatabase db = dataBase.getReadableDatabase();

        Alumno alumno;
        alumnosList = new ArrayList<>();
        identifiers = new ArrayList<>();
        //select * from usuarios

        Cursor cursor=db.rawQuery("SELECT * FROM ALUMNO WHERE id LIKE"+"'"+asignatura+"%' " + "ORDER BY nombre", null);

        while (cursor.moveToNext()){
            alumno =new Alumno();
            alumno.setNombre(cursor.getString(2));

            alumnosList.add(alumno);
            identifiers.add(cursor.getString(0));
        }

        obtenerLista();
    }

    private void obtenerLista() {
        listaAlumnos = new ArrayList<>();
        for(int i=0; i < alumnosList.size(); i ++) listaAlumnos.add(alumnosList.get(i).getNombre());
    }

    private void updateAdapter() {
        consultarListaAlumnos();
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaAlumnos);
        Alumnos.setAdapter(adapter);
    }
}

