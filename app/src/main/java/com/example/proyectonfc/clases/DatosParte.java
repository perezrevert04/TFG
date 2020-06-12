package com.example.proyectonfc.clases;

import android.content.Context;
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

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.util.Alumno;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DatosParte extends AppCompatActivity {

    DataBase dataBase;
    Button btnVer;
    private String asignaturaSeleccionada;
    private String grupoSeleccionado;
    private String fecha;
    private String[] listaDatos = new String[1000];
    private String espacio;
    private String asignatura;
    private String titulacion;
    private String grupo;
    private String nombreProfesor;
    private String dni;
    private String curso;
    private String gestora;
    private String idioma;
    private String fechaParte;
    private String horaInicio;
    private String duracion;
    private String alumnos;

    private TextView textEspacio;
    private TextView textAsignatura;
    private TextView textTitulacion;
    private TextView textGrupo;
    private TextView textNombreProfesor;
    private TextView textDni;
    private TextView textCurso;
    private TextView textGestora;
    private TextView textIdioma;
    private TextView textFecha;
    private TextView textHoraInicio;
    private TextView textDuracion;
    private TextView textAlumnos;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_parte);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        asignaturaSeleccionada = getIntent().getStringExtra( "ASIGNATURASELECCIONADA");
        grupoSeleccionado = getIntent().getStringExtra( "GRUPOSELECCIONADO");
        fecha = getIntent().getStringExtra( "FECHA");
        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        leerXML();

    }

    private void leerXML() {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dobu;

        try {
            dobu = dbf.newDocumentBuilder();

            //Filas añadidas
            Context context = getApplicationContext();
            String filePath = "/storage/emulated/0/Download/DatosParteFirmasUPV/ParteFirmas-"+asignaturaSeleccionada+"-"+grupoSeleccionado+"-"+fecha+".xml";
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));

            //FileInputStream fis = new FileInputStream(new File("raw/asignaturas.xml"));
            Document doc = dobu.parse(in);
            NodeList datos = doc.getElementsByTagName("ParteFirmas-"+asignaturaSeleccionada+"-"+grupoSeleccionado+"-"+fecha);

            for (int i = 0; i < datos.getLength(); i++) {
                Node dato = datos.item(i);

                NodeList datoInfo = dato.getChildNodes();
                for (int j = 0; j < datoInfo.getLength(); j++) {
                    Node info = datoInfo.item(j);

                    listaDatos[j] = info.getTextContent();
                }
                espacio = listaDatos[0];
                asignatura = listaDatos[1];
                titulacion = listaDatos[2];
                grupo = listaDatos[3];
                nombreProfesor = listaDatos[4];
                dni = listaDatos[5];
                curso = listaDatos[6];
                gestora = listaDatos[7];
                idioma = listaDatos[8];
                fechaParte = listaDatos[9];
                horaInicio = listaDatos[10];
                duracion = listaDatos[11];
                alumnos = listaDatos[12];



            }

            textEspacio = (TextView) findViewById(R.id.textEspacio);
            textEspacio.setText(espacio);
            asignatura = asignatura.substring(4);
            textAsignatura = (TextView) findViewById(R.id.textAsignatura);
            textAsignatura.setText(asignatura);
            textTitulacion = (TextView) findViewById(R.id.textTitulacion);
            textTitulacion.setText(titulacion);
            textGrupo = (TextView) findViewById(R.id.textGrupo);
            textGrupo.setText(grupo);
            textCurso = (TextView) findViewById(R.id.textCurso);
            textCurso.setText(curso);
            textNombreProfesor = (TextView) findViewById(R.id.textNombreProfesor);
            textNombreProfesor.setText(nombreProfesor);
            textDni = (TextView) findViewById(R.id.textDni);
            textDni.setText(dni);
            textGestora = (TextView) findViewById(R.id.textGestora);
            textGestora.setText(gestora);
            textIdioma = (TextView) findViewById(R.id.textIdioma);
            textIdioma.setText(idioma);
            textFecha = (TextView) findViewById(R.id.textFecha);
            textFecha.setText(fechaParte);
            textHoraInicio = (TextView) findViewById(R.id.textHoraInicio);
            textHoraInicio.setText(horaInicio);
            textDuracion = (TextView) findViewById(R.id.textDuracion);
            textDuracion.setText(duracion);
            textAlumnos = (TextView) findViewById(R.id.textAlumnos);
            textAlumnos.setText(alumnos);


        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(DatosParte.this);
            builder.setTitle("No se ha encontrado ningún parte de firmas con estos datos");
            builder.setMessage("¿Desea introducir nuevos datos?");
            builder.setNeutralButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}

