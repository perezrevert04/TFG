package com.example.proyectonfc.clases;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.util.Profesor;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Profesores extends AppCompatActivity {

    DataBase dataBase;
    private ListView Profesores;
    private Button buttonBorrar;
    private Button buttonVer;
    private TextView profesorSeleccionado;
    private String asignatura;
    private String profesor;
    private String dni;
    ArrayList<String> listaProfesores;
    ArrayList<Profesor> profesoresList;

    @Override
    public void onResume() {
        super.onResume();

        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        Profesores = (ListView) findViewById(R.id.listaProfesores);

        consultarListaProfesores();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter
                (this,android.R.layout.simple_list_item_1, (List) listaProfesores);
        Profesores.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_nuevo_profesor,menu);
        return true;

    }


    public boolean onOptionsItemSelected(MenuItem menu)
    {
        // gracias a la id, sabemos que item es el que se oprime, en este caso usamos un switch
        switch (menu.getItemId())
        {
            case R.id.MenuProfesor:
                Intent intent1 = new Intent(Profesores.this, NuevoProfesor.class);
                asignatura = getIntent().getStringExtra( "ASIGNATURA");
                intent1.putExtra("ASIGNATURA", asignatura );
                startActivity(intent1);
                return true;

            default:
        }
        return super.onOptionsItemSelected(menu);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profesores);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        Profesores = (ListView) findViewById(R.id.listaProfesores);

        consultarListaProfesores();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter
                (this,android.R.layout.simple_list_item_1, (List) listaProfesores);
        Profesores.setAdapter(adaptador);

        if (profesoresList.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No existe ningún Profesor");
            builder.setMessage("¿Desea añadir un Profesor?");
            builder.setPositiveButton("Añadir Profesor", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Profesores.this,NuevoProfesor.class);
                    asignatura = getIntent().getStringExtra( "ASIGNATURA");
                    intent.putExtra("ASIGNATURA", asignatura);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cargar Profesores por defecto", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dataBase.insertarProfesor(asignatura);
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getApplicationContext(), "PROFESORES CARGADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        buttonBorrar = (Button) findViewById(R.id.buttonBorrar);
        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Profesores.class);
                intent.putExtra("ASIGNATURA", asignatura );
                if(profesorSeleccionado != null){
                    consultarDniProfesor();
                    dataBase.borrarProfesor(asignatura, profesor, dni);
                    finish();
                    startActivity(getIntent());
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Profesores.this);
                    builder.setTitle("No ha seleccionado ningún Profesor");
                    builder.setMessage("¡Debe de seleccionar algún Profesor!");
                    builder.setNeutralButton("¡Entendido!", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

        });

        buttonVer = (Button) findViewById(R.id.buttonVer);
        buttonVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DatosProfesor.class);
                if(profesorSeleccionado != null){
                    consultarDniProfesor();
                    intent.putExtra("ASIGNATURA", asignatura );
                    intent.putExtra("DNIPROFESOR", dni );
                    startActivityForResult(intent, 0);

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Profesores.this);
                    builder.setTitle("No ha seleccionado ningún Profesor");
                    builder.setMessage("¡Debe de seleccionar algún Profesor!");
                    builder.setNeutralButton("¡Entendido!", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

        });

        Profesores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                profesor = listaProfesores.get(position);
                profesorSeleccionado = (TextView) findViewById(R.id.profesorSeleccionado);
                profesorSeleccionado.setText(profesor);
            }
        });
    }

    private void consultarListaProfesores() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        Profesor profesor=null;
        profesoresList =new ArrayList<Profesor>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM PROFESOR WHERE id LIKE"+"'"+asignatura+"%' ORDER BY nombre", null);

        while (cursor.moveToNext()){
            profesor =new Profesor();
            profesor.setNombre(cursor.getString(2));



            //Log.i("id",persona.getId().toString());


            profesoresList.add(profesor);

        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaProfesores=new ArrayList<String>();

        for(int i=0;i<profesoresList.size();i++){
            listaProfesores.add(profesoresList.get(i).getNombre());
        }

    }

    private void consultarDniProfesor() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        Profesor profesor2=null;

        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM PROFESOR WHERE id LIKE"+"'"+asignatura+"%' AND nombre = '"+profesor+"'", null);

        while (cursor.moveToNext()){
            profesor2 =new Profesor();
            profesor2.setDni(cursor.getString(1));

        }

        dni= profesor2.getDni();

    }
}

