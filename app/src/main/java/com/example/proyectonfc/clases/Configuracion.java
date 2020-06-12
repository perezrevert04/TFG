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
import android.widget.Toast;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.util.Asignatura;

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

public class Configuracion extends AppCompatActivity {

    DataBase dataBase;
    private ListView Asignaturas;
    private Button buttonBorrar;
    private Button buttonVer;
    private TextView asignaturaSeleccionada;
    private  String asignatura;
    ArrayList<String> listaAsignaturas;
    ArrayList<Asignatura> asignaturasList;
    private int position;


    private String[] listaAsignaturas2 = new String[1000];
    private TextView textAsignatura;
    private Button btnCargarAsignatura;
    private String nombreAsignatura;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_asignatura,menu);
        return true;

    }


    public boolean onOptionsItemSelected(MenuItem menu)
    {
        // gracias a la id, sabemos que item es el que se oprime, en este caso usamos un switch
        switch (menu.getItemId())
        {
            case R.id.MenuBorrarBD:
                Intent intent1 = new Intent(Configuracion.this, Configuracion.class);
                dataBase.borrarTodo();
                startActivity(intent1);
                return true;

            default:
        }
        return super.onOptionsItemSelected(menu);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        Asignaturas = (ListView) findViewById(R.id.listaAsignaturas);


        consultarListaAsignaturas();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter
                (this,android.R.layout.simple_list_item_1, (List) listaAsignaturas);
        Asignaturas.setAdapter(adaptador);

        if (asignaturasList.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No existe ninguna Asignatura");
            builder.setMessage("¿Desea añadir asignaturas?");
            builder.setPositiveButton("Cargar Asignaturas propias", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                       while (count < numfinal+1) {
                            leerXML1();
                            dataBase.agregarAsignatura(identificadorAsignatura, nombre, titulacion, curso, gestora, idioma, duracion);
                            count++;
                        }
                        Toast.makeText(getApplicationContext(), "ASIGNATURAS CARGADA CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        //}

                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(), "ASIGNATURAS ACTUALIZADAS", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    startActivity(getIntent());
                }
            });


            builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Configuracion.this,MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }






        buttonBorrar = (Button) findViewById(R.id.buttonBorrar);
        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(v.getContext(), Configuracion.class);
                    intent.putExtra("ASIGNATURA", asignatura);
                    if (asignaturaSeleccionada != null) {
                        dataBase.borrarAsignatura(asignatura);
                        dataBase.borrarTodoProfesores(asignatura);
                        dataBase.borrarTodoAlumnos(asignatura);
                        dataBase.borrarTodoGrupos(asignatura);
                        finish();
                        startActivity(getIntent());
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Configuracion.this);
                        builder.setTitle("No ha seleccionado ninguna Asignatura");
                        builder.setMessage("¡Debe de seleccionar alguna Asignatura!");
                        builder.setNeutralButton("¡Entendido!", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "LA ASIGNATURA NO HA PODIDO SER BORRADA", Toast.LENGTH_SHORT).show();
                }
            }

        });

        buttonVer = (Button) findViewById(R.id.buttonVer);
        buttonVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), Asignaturas.class);
                intent.putExtra("ASIGNATURA", asignatura);
                if (asignaturaSeleccionada == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Configuracion.this);
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

    private void leerXML1() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dobu;
        try {
            dobu = dbf.newDocumentBuilder();
            Context context = getApplicationContext();
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

        Asignatura asignatura=null;
        asignaturasList =new ArrayList<Asignatura>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT id FROM ASIGNATURA ORDER BY id", null);

        while (cursor.moveToNext()){
            asignatura=new Asignatura();
            asignatura.setNombre(cursor.getString(0));


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

