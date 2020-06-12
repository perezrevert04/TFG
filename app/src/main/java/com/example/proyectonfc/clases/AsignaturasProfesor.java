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
import com.example.proyectonfc.util.Asignatura;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AsignaturasProfesor extends AppCompatActivity {

    DataBase dataBase;
    private ListView Asignaturas;
    private Button buttonRecuperacion;
    private Button buttonNormal;
    private TextView asignaturaSeleccionada;
    private  String asignatura;
    ArrayList<String> listaAsignaturas;
    ArrayList<Asignatura> asignaturasList;
    private String identificador;
    private String nombreprofesor;
    private String dniprofesor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asignaturas_profesor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        identificador = getIntent().getStringExtra( "IDENTIFICADOR");
        nombreprofesor = getIntent().getStringExtra( "NOMBREPROFESOR");
        dniprofesor = getIntent().getStringExtra( "DNIPROFESOR");
        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        Asignaturas = (ListView) findViewById(R.id.listaAsignaturas);

        consultarListaAsignaturas();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter
                (this,android.R.layout.simple_list_item_1, (List) listaAsignaturas);
        Asignaturas.setAdapter(adaptador);



        buttonRecuperacion = (Button) findViewById(R.id.buttonRecuperacion);
        buttonRecuperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DatosAsignaturaRecuperacion.class);
                intent.putExtra("ASIGNATURA", asignatura );
                intent.putExtra("NOMBREPROFESOR", nombreprofesor );
                intent.putExtra("DNIPROFESOR", dniprofesor );
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
            }

        });

        buttonNormal = (Button) findViewById(R.id.buttonNormal);
        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DatosAsignaturaNormal.class);
                intent.putExtra("ASIGNATURA", asignatura );
                intent.putExtra("NOMBREPROFESOR", nombreprofesor );
                intent.putExtra("DNIPROFESOR", dniprofesor );
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
            }

        });

        Asignaturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                asignatura = listaAsignaturas.get(position);
                asignaturaSeleccionada = (TextView) findViewById(R.id.asignaturaSeleccionada);
                asignaturaSeleccionada.setText(asignatura);
            }
        });
    }

    private void consultarListaAsignaturas() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        Asignatura asignatura=null;
        asignaturasList =new ArrayList<Asignatura>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT id FROM PROFESOR WHERE id LIKE"+"'%"+identificador+"'", null);

        while (cursor.moveToNext()){
            asignatura=new Asignatura();
            String soloIdentificador = (cursor.getString(0));
            soloIdentificador = soloIdentificador.substring(0,3);
            asignatura.setNombre(soloIdentificador);

            //Log.i("id",persona.getId().toString());


            asignaturasList.add(asignatura);

        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaAsignaturas=new ArrayList<String>();

        for(int i=0;i<asignaturasList.size();i++){
            listaAsignaturas.add(asignaturasList.get(i).getNombre());
        }

    }
}

