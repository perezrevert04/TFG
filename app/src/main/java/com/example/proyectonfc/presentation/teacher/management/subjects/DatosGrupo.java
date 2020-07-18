package com.example.proyectonfc.presentation.teacher.management.subjects;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.use_cases.CommandVoiceActivity;
import com.example.proyectonfc.model.Group;
import com.r0adkll.slidr.Slidr;

public class DatosGrupo extends AppCompatActivity {


    DataBase dataBase;
    Button buttonEditar;
    private String identificador;
    private String nombreGrupo;
    private String grupo;
    private String h_entrada;
    private String h_salida;
    private String aula;

    private TextView textNombreGrupo;
    private TextView textHEntrada;
    private TextView textHSalida;
    private TextView textAula;
    private String Nombregrupo;

    private String asignatura;

    private Group group;

    @Override
    public void onResume() {
        super.onResume();
        consultarListaGrupos();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_grupo);
        Slidr.attach(this);

        dataBase = new DataBase(getApplicationContext());

        Nombregrupo = getIntent().getStringExtra( "GRUPO");
        asignatura = getIntent().getStringExtra( "ASIGNATURA");

        consultarListaGrupos();

        buttonEditar = (Button) findViewById(R.id.buttonEditar);
        buttonEditar.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), EditGroupActivity.class);
            intent.putExtra("Group", group);
            startActivity(intent);
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

    private void consultarListaGrupos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM GRUPO WHERE id LIKE"+"'"+asignatura+"%' AND grupo = '"+Nombregrupo+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            grupo = cursor.getString(1);
            nombreGrupo = cursor.getString(1);
            h_entrada = cursor.getString(2);
            h_salida = cursor.getString(3);
            aula = cursor.getString(4);

            group = new Group(identificador, grupo, aula, h_entrada, h_salida);
        }

        setTitle(identificador);

        textNombreGrupo = (TextView) findViewById(R.id.textNombreGrupo);
        textNombreGrupo.setText(nombreGrupo);

        textHEntrada = (TextView) findViewById(R.id.textViewGroupHour);
        textHEntrada.setText(h_entrada);

        textHSalida = (TextView) findViewById(R.id.textViewGroupHourEnd);
        textHSalida.setText(h_salida);

        textAula = (TextView) findViewById(R.id.textViewGroupClassroom);
        textAula.setText(aula);


    }


}
