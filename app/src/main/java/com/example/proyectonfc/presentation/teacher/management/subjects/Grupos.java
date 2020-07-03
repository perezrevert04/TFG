package com.example.proyectonfc.presentation.teacher.management.subjects;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.util.Grupo;

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

public class Grupos extends AppCompatActivity {

    DataBase dataBase;
    private ListView Grupos;
    private Button buttonBorrar;
    private Button buttonVer;
    private TextView grupoSeleccionado;
    private String asignatura;
    private String grupo;

    ArrayList<String> listaGrupos;
    ArrayList<Grupo> gruposList;


    private String[] listaGrupos2 = new String[1000];
    private TextView textGrupo;
    private Button btnCargarGrupo;
    private String nombreGrupo;

    private String identificadorGrupo;
    private String tituloGrupo;
    private String hEntrada;
    private String hSalida;
    private String aula;

    private int count = 1;
    private int numfinal = 2;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grupos);

        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dataBase = new DataBase(getApplicationContext(), "DB5.db", null, 1);
        Grupos = (ListView) findViewById(R.id.listaGrupos);

        consultarListaGrupos();
        ArrayAdapter<CharSequence> adaptador=new ArrayAdapter
                (this,android.R.layout.simple_list_item_1, (List) listaGrupos);
        Grupos.setAdapter(adaptador);

        if (gruposList.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No existe ningún Grupo");
            builder.setMessage("¿Desea añadir grupos?");
            builder.setPositiveButton("Cargar grupos propios", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        while (count < numfinal + 1) {
                            leerXML2();
                            dataBase.agregarGrupo(identificadorGrupo, tituloGrupo, hEntrada, hSalida, aula);
                            count++;
                        }
                        Toast.makeText(getApplicationContext(), "GRUPO CARGADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        //}

                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(), "GRUPOS ACTUALIZADOS", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    startActivity(getIntent());
                }
            }).setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
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
                Intent intent = new Intent(v.getContext(), Grupos.class);
                intent.putExtra("ASIGNATURA", asignatura );
                if(grupoSeleccionado != null){
                    dataBase.borrarGrupo(asignatura, grupo);
                    finish();
                    startActivity(getIntent());
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Grupos.this);
                    builder.setTitle("No ha seleccionado ningún Grupo");
                    builder.setMessage("¡Debe de seleccionar algún Grupo!");
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
                Intent intent = new Intent(v.getContext(), DatosGrupo.class);
                intent.putExtra("GRUPO", grupo );
                intent.putExtra("ASIGNATURA", asignatura );
                if(grupoSeleccionado != null) {
                    startActivityForResult(intent, 0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Grupos.this);
                    builder.setTitle("No ha seleccionado ningún Grupo");
                    builder.setMessage("¡Debe de seleccionar algún Grupo!");
                    builder.setNeutralButton("¡Entendido!", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

        });

        Grupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                grupo = listaGrupos.get(position);
                grupoSeleccionado = (TextView) findViewById(R.id.grupoSeleccionado);
                grupoSeleccionado.setText(grupo);
            }
        });
    }

    private void leerXML2() {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dobu;

        try {
            dobu = dbf.newDocumentBuilder();

            Context context = getApplicationContext();
            String filePath = "/storage/emulated/0/Download/Grupos.xml";
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));

            //FileInputStream fis = new FileInputStream(new File("raw/grupos.xml"));
            Document doc = dobu.parse(in);
            NodeList num = doc.getElementsByTagName("NumeroGrupos");
            Node valor = num.item(0);
            String numero = valor.getTextContent();
            numfinal = Integer.parseInt(numero);


            NodeList grupos = doc.getElementsByTagName("Grupo" + count);

            for (int i = 0; i < grupos.getLength(); i++) {
                Node grupo = grupos.item(i);

                NodeList grupoInfo = grupo.getChildNodes();
                for (int j = 0; j < grupoInfo.getLength(); j++) {
                    Node info = grupoInfo.item(j);

                    listaGrupos2[j] = info.getTextContent();
                }
                identificadorGrupo = listaGrupos2[1];
                tituloGrupo = listaGrupos2[3];
                hEntrada = listaGrupos2[5];
                hSalida = listaGrupos2[7];
                aula = listaGrupos2[9];

            }


        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void consultarListaGrupos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        Grupo grupo=null;
        gruposList =new ArrayList<Grupo>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM GRUPO WHERE id LIKE"+"'"+asignatura+"%' ORDER by grupo", null);

        while (cursor.moveToNext()){
            grupo =new Grupo();
            grupo.setGrupo(cursor.getString(1));



            //Log.i("id",persona.getId().toString());


            gruposList.add(grupo);

        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaGrupos=new ArrayList<String>();

        for(int i=0;i<gruposList.size();i++){
            listaGrupos.add(gruposList.get(i).getGrupo());
        }

    }

}

