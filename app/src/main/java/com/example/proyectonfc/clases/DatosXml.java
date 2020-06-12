package com.example.proyectonfc.clases;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.util.Asignatura;
import com.example.proyectonfc.util.Grupo;
import com.example.proyectonfc.util.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DatosXml extends AppCompatActivity {

    Button btnConsultar;

    private ArrayList<String> listaAsignaturas2 = new ArrayList<>();
    DataBase dataBase;
    private String[] datos = new String[1000];
    private int count = 0;
    private Spinner spinner;
    private Spinner spinner2;
    ArrayList<String> listaAsignaturas;
    ArrayList<Asignatura> asignaturasList;
    ArrayList<String> listaGrupos;
    ArrayList<Grupo> gruposList;
    public String asignaturaSeleccionada;
    private String grupoSeleccionado;
    private String grupoSeleccionado2;
    private EditText textFecha;
    Calendar calendario = Calendar.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_xml);

        textFecha = findViewById(R.id.textFecha);
        textFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DatosXml.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        spinner= (Spinner) findViewById(R.id.spinner);
        consultarListaAsignaturas();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter
                (DatosXml.this,android.R.layout.simple_spinner_item,listaAsignaturas);
        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> spn,
                                               android.view.View v,
                                               int posicion,
                                               long id) {

                        grupoSeleccionado2 = spn.getItemAtPosition(posicion).toString();
                        spinner2= (Spinner) findViewById(R.id.spinner2);
                        consultarListaGrupos();
                        ArrayAdapter<CharSequence> adaptador2 = new ArrayAdapter
                                (DatosXml.this, android.R.layout.simple_spinner_item, listaGrupos);
                        spinner2.setAdapter(adaptador2);
                    }
                    public void onNothingSelected(AdapterView<?> spn) {
                    }
                });





        btnConsultar = (Button) findViewById(R.id.btnConsultar);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DatosParte.class);
                asignaturaSeleccionada = String.valueOf(spinner.getSelectedItem());
                grupoSeleccionado = String.valueOf(spinner2.getSelectedItem());
                String fecha = textFecha.getText().toString();
                if (asignaturaSeleccionada == "" || grupoSeleccionado == "" || fecha == ""){
                    AlertDialog.Builder builder = new AlertDialog.Builder(DatosXml.this);
                    builder.setTitle("No ha rellenado todos los criterios de búsqueda");
                    builder.setMessage("¡Debe de rellenar todos los datos!");
                    builder.setNeutralButton("¡Entendido!", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    intent.putExtra("ASIGNATURASELECCIONADA", asignaturaSeleccionada);
                    intent.putExtra("GRUPOSELECCIONADO", grupoSeleccionado);
                    intent.putExtra("FECHA", fecha);
                    startActivityForResult(intent, 0);
                }
            }
        });





    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        textFecha.setText(sdf.format(calendario.getTime()));
    }

    private void consultarListaAsignaturas() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        Asignatura asignatura =null;
        asignaturasList =new ArrayList<Asignatura>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT id FROM ASIGNATURA ORDER BY id", null);

        while (cursor.moveToNext()){
            asignatura =new Asignatura();
            asignatura.setNombre(cursor.getString(0));


            //Log.i("id",persona.getId().toString());


            asignaturasList.add(asignatura);

        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaAsignaturas=new ArrayList<String>();
        listaAsignaturas.add("");

        for(int i=0;i<asignaturasList.size();i++){
            listaAsignaturas.add(asignaturasList.get(i).getNombre());
        }

    }



    private void consultarListaGrupos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        Grupo grupo =null;
        gruposList =new ArrayList<Grupo>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM GRUPO WHERE ID LIKE '"+grupoSeleccionado2+"%' ORDER BY id", null);

        while (cursor.moveToNext()){
            grupo =new Grupo();
            grupo.setGrupo(cursor.getString(1));


            //Log.i("id",persona.getId().toString());


            gruposList.add(grupo);

        }
        obtenerLista2();
    }

    private void obtenerLista2() {
        listaGrupos=new ArrayList<String>();
        listaGrupos.add("");

        for(int i=0;i<gruposList.size();i++){
            listaGrupos.add(gruposList.get(i).getGrupo());
        }

    }



}


