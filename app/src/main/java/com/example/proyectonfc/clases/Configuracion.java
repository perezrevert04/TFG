package com.example.proyectonfc.clases;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Configuracion extends AppCompatActivity {

    DataBase dataBase;

    private  String asignatura;
    ArrayList<String> listaAsignaturas;

    private String[] listaAsignaturas2 = new String[1000];

    private String identificadorAsignatura;
    private String nombre;
    private String titulacion;
    private String curso;
    private String gestora;
    private String idioma;
    private String duracion;

    private int count = 1;
    private int numfinal = 2;

    @Override
    public void onResume() {
        super.onResume();
        consultarListaAsignaturas();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracion);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        ListView asignaturas = (ListView) findViewById(R.id.listaAsignaturas);


        consultarListaAsignaturas();
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaAsignaturas);
        asignaturas.setAdapter(adaptador);

        if (listaAsignaturas.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No existe ninguna Asignatura");
            builder.setMessage("¿Desea añadir asignaturas?");
            builder.setPositiveButton("Cargar Asignaturas propias", (dialog, which) -> {
                try {
                   while (count < numfinal+1) {
                        leerXML1();
                        dataBase.agregarAsignatura(identificadorAsignatura, nombre, titulacion, curso, gestora, idioma, duracion);
                        count++;
                    }
                    Toast.makeText(getApplicationContext(), "ASIGNATURAS CARGADA CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "ASIGNATURAS ACTUALIZADAS", Toast.LENGTH_SHORT).show();
                }
                finish();
                startActivity(getIntent());
            });


            builder.setNeutralButton("Cancelar", (dialog, which) -> {
                Intent intent = new Intent(Configuracion.this,MainActivity.class);
                finish();
                startActivity(intent);
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        asignaturas.setOnItemClickListener( (adapterView, view, position, id) -> {
            asignatura = listaAsignaturas.get(position);
            Intent intent = new Intent(view.getContext(), Asignaturas.class);
            intent.putExtra("ASIGNATURA", asignatura);
            startActivity(intent);
        });

        asignaturas.setOnItemLongClickListener( (adapterView, view, position, id) -> {
            asignatura = listaAsignaturas.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(asignatura).setMessage("¿Desea eliminar esta asignatura?");
            builder.setNegativeButton("Cancelar", (dialog, id1) -> {});
            builder.setPositiveButton("Eliminar", (dialog, id1) -> {

                try{
                    dataBase.borrarAsignatura(asignatura);
                    dataBase.borrarTodoProfesores(asignatura);
                    dataBase.borrarTodoAlumnos(asignatura);
                    dataBase.borrarTodoGrupos(asignatura);
                    finish();
                    startActivity(getIntent());
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "LA ASIGNATURA NO HA PODIDO SER BORRADA", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        });
    }

    private void leerXML1() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dobu;
        try {
            dobu = dbf.newDocumentBuilder();
            String filePath = "/storage/emulated/0/Download/Asignaturas.xml";
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            Document doc = dobu.parse(in);
            NodeList num = doc.getElementsByTagName("NumeroAsignaturas");
            Node valor = num.item(0);
            String numero = valor.getTextContent();
            numfinal = Integer.parseInt(numero);
            NodeList asignaturas = doc.getElementsByTagName("Asignatura" + count);
            for (int i = 0; i < asignaturas.getLength(); i++) {
                Node asignatura = asignaturas.item(i);
                NodeList asignaturaInfo = asignatura.getChildNodes();
                for (int j = 0; j < asignaturaInfo.getLength(); j++) {
                    Node info = asignaturaInfo.item(j);
                    listaAsignaturas2[j] = info.getTextContent();
                }
                identificadorAsignatura = listaAsignaturas2[1];
                nombre = listaAsignaturas2[3];
                titulacion = listaAsignaturas2[5];
                curso = listaAsignaturas2[7];
                gestora = listaAsignaturas2[9];
                idioma = listaAsignaturas2[11];
                duracion = listaAsignaturas2[13];
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void consultarListaAsignaturas() {
        SQLiteDatabase db=dataBase.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT id FROM ASIGNATURA ORDER BY id", null);

        listaAsignaturas=new ArrayList<>();
        while (cursor.moveToNext()) listaAsignaturas.add(cursor.getString(0));
    }

}

