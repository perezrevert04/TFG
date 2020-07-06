package com.example.proyectonfc.presentation.teacher.management.subjects;

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
import com.example.proyectonfc.model.Grupo;

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

public class Grupos extends AppCompatActivity {

    DataBase dataBase;
    private ListView Grupos;
    private String asignatura;

    ArrayList<String> listaGrupos;
    ArrayList<Grupo> gruposList;

    private String[] listaGrupos2 = new String[1000];
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
        setTitle("Grupos de " + asignatura);
        dataBase = new DataBase(getApplicationContext());
        Grupos = (ListView) findViewById(R.id.listaGrupos);

        consultarListaGrupos();
        updateAdapter();

        if (gruposList.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No existe ningún Grupo");
            builder.setMessage("¿Desea añadir grupos?");
            builder.setPositiveButton("Cargar grupos propios", (dialog, which) -> {
                try {
                    while (count < numfinal + 1) {
                        leerXML2();
                        dataBase.agregarGrupo(identificadorGrupo, tituloGrupo, hEntrada, hSalida, aula);
                        count++;
                    }
                    Toast.makeText(getApplicationContext(), "GRUPO CARGADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "GRUPOS ACTUALIZADOS", Toast.LENGTH_SHORT).show();
                }
                finish();
                startActivity(getIntent());
            }).setNeutralButton("Cancelar", (dialog, which) -> onBackPressed());


            AlertDialog dialog = builder.create();
            dialog.show();
        }

        Grupos.setOnItemClickListener( (adapterView, view, position, id) -> {
            Intent intent = new Intent(view.getContext(), DatosGrupo.class);
            intent.putExtra("GRUPO", listaGrupos.get(position) );
            intent.putExtra("ASIGNATURA", asignatura );
            startActivity(intent);
        });

        Grupos.setOnItemLongClickListener( (adapterView, view, position, id) -> {

            String group = listaGrupos.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(Grupos.this);
            builder.setTitle(group);
            builder.setMessage("¿Desea eliminar el grupo de la asignatura " + asignatura + "?");
            builder.setNegativeButton("No", null);

            builder.setPositiveButton("Sí", (dialog, which) -> {
                dataBase.borrarGrupo(asignatura, listaGrupos.get(position));
                listaGrupos.remove(position);
                updateAdapter();

            });

            AlertDialog dialog = builder.create();
            dialog.show();

            return true;
        });
    }

    private void leerXML2() {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dobu;

        try {
            dobu = dbf.newDocumentBuilder();

            String filePath = "/storage/emulated/0/Download/Grupos.xml";
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));

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

        Grupo grupo;
        gruposList =new ArrayList<>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM GRUPO WHERE id LIKE"+"'"+asignatura+"%' ORDER by grupo", null);

        while (cursor.moveToNext()){
            grupo =new Grupo();
            grupo.setGrupo(cursor.getString(1));

            gruposList.add(grupo);
        }

        obtenerLista();
    }

    private void obtenerLista() {
        listaGrupos=new ArrayList<>();

        for(int i=0;i<gruposList.size();i++){
            listaGrupos.add(gruposList.get(i).getGrupo());
        }

    }

    private void updateAdapter() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, (List) listaGrupos);
        Grupos.setAdapter(adapter);
    }

}

