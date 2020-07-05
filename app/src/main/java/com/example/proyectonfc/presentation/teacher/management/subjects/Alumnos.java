package com.example.proyectonfc.presentation.teacher.management.subjects;

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
import com.example.proyectonfc.model.Alumno;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Alumnos extends AppCompatActivity {

    DataBase dataBase;
    private ListView Alumnos;
    private Button buttonBorrar;
    private Button buttonVer;
    private TextView alumnoSeleccionado;
    private String asignatura;
    private String alumno;
    private String dni;
    ArrayList<String> listaAlumnos;
    ArrayList<Alumno> alumnosList;

    @Override
    public void onResume() {
        super.onResume();

        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dataBase = new DataBase(getApplicationContext());
        Alumnos = findViewById(R.id.listaAlumnos);

        consultarListaAlumnos();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter(this,android.R.layout.simple_list_item_1, (List) listaAlumnos);
        Alumnos.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_nuevo_alumno,menu);
        return true;

    }


    public boolean onOptionsItemSelected(MenuItem menu)
    {
        // gracias a la id, sabemos que item es el que se oprime, en este caso usamos un switch
        switch (menu.getItemId())
        {
            case R.id.MenuAlumno:
                Intent intent1 = new Intent(Alumnos.this, NuevoAlumno.class);
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
        setContentView(R.layout.alumnos);

        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dataBase = new DataBase(getApplicationContext());
        Alumnos = (ListView) findViewById(R.id.listaAlumnos);

        consultarListaAlumnos();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter
                (this,android.R.layout.simple_list_item_1, (List) listaAlumnos);
        Alumnos.setAdapter(adaptador);

        if (alumnosList.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No existe ningún Alumno");
            builder.setMessage("¿Desea añadir un Alumno?");
            builder.setPositiveButton("Añadir Alumno", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Alumnos.this,NuevoAlumno.class);
                    asignatura = getIntent().getStringExtra( "ASIGNATURA");
                    intent.putExtra("ASIGNATURA", asignatura);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cargar Alumnos por defecto", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dataBase.insertarAlumno(asignatura);
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(getApplicationContext(), "ALUMNOS CARGADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(v.getContext(), Alumnos.class);
                intent.putExtra("ASIGNATURA", asignatura );
                if(alumnoSeleccionado != null){
                    consultarDniAlumno();
                    dataBase.borrarAlumno(asignatura, alumno, dni);
                    finish();
                    startActivity(getIntent());
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Alumnos.this);
                    builder.setTitle("No ha seleccionado ningún Alumno");
                    builder.setMessage("¡Debe de seleccionar algún Alumno!");
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
                Intent intent = new Intent(v.getContext(), DatosAlumno.class);
                if(alumnoSeleccionado != null){
                    consultarDniAlumno();
                    intent.putExtra("ASIGNATURA", asignatura );
                    intent.putExtra("DNIALUMNO", dni );
                    startActivityForResult(intent, 0);

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Alumnos.this);
                    builder.setTitle("No ha seleccionado ningún Alumno");
                    builder.setMessage("¡Debe de seleccionar algún Alumno!");
                    builder.setNeutralButton("¡Entendido!", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

        });

        Alumnos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                alumno = listaAlumnos.get(position);
                alumnoSeleccionado = (TextView) findViewById(R.id.alumnoSeleccionado);
                alumnoSeleccionado.setText(alumno);
            }
        });
    }

    private void consultarListaAlumnos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        Alumno alumno=null;
        alumnosList =new ArrayList<Alumno>();
        //select * from usuarios

        Cursor cursor=db.rawQuery("SELECT * FROM ALUMNO WHERE id LIKE"+"'"+asignatura+"%' " +
                "ORDER BY nombre", null);

        while (cursor.moveToNext()){
            alumno =new Alumno();
            alumno.setNombre(cursor.getString(2));



            //Log.i("id",persona.getId().toString());


            alumnosList.add(alumno);

        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaAlumnos=new ArrayList<String>();

        for(int i=0;i<alumnosList.size();i++){
            listaAlumnos.add(alumnosList.get(i).getNombre());
        }

    }

    private void consultarDniAlumno() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        Alumno alumno2=null;

        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM ALUMNO WHERE id LIKE"+"'"+asignatura+"%' AND nombre = '"+alumno+"'", null);

        while (cursor.moveToNext()){
            alumno2 =new Alumno();
            alumno2.setDni(cursor.getString(1));

        }

        dni= alumno2.getDni();

    }
}

