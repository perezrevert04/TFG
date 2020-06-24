package com.example.proyectonfc.clases;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

public class DatosAsignaturaRecuperacion extends AppCompatActivity {

    DataBase dataBase;
    private String identificador;
    private String nombreAsignatura;
    private String nombre;
    private String titulacion;
    private String grupo;
    private String curso;
    private String gestora;
    private String idioma;
    private String duracion;
    private String aula;
    private String hEntrada;
    private String asignatura;
    private EditText textGrupo;
    private EditText textEspacio;
    private EditText textHEntrada;

    public boolean onOptionsItemSelected(MenuItem menu) {
        if (menu.getItemId() == R.id.MenuActualizar) {
            Intent intent = new Intent(DatosAsignaturaRecuperacion.this, DatosAsignaturaRecuperacion.class);
            asignatura = getIntent().getStringExtra("ASIGNATURA");
            intent.putExtra("ASIGNATURA", asignatura);
            startActivity(intent);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_asignatura_recuperacion);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);

        asignatura = getIntent().getStringExtra( "ASIGNATURA");

        consultarListaAsignaturas();

        Button buttonAsistencia = (Button) findViewById(R.id.buttonStart);
        buttonAsistencia.setOnClickListener((View.OnClickListener) v -> {

//            textGrupo = (EditText) findViewById(R.id.textGrupo);
//            grupo = textGrupo.getText().toString();
//            textEspacio = (EditText) findViewById(R.id.textEspacio);
//            aula = textEspacio.getText().toString();
//            textHEntrada = (EditText) findViewById(R.id.textHoraEntrada);
//            hEntrada = textHEntrada.getText().toString();

            Intent intent = new Intent(v.getContext(), RegistroAlumnos.class);
            intent.putExtra("ASIGNATURA", asignatura );
            intent.putExtra("NOMBRE", nombre );
            intent.putExtra("TITULACION", titulacion );
            intent.putExtra("GRUPO", grupo );
            intent.putExtra("CURSO", curso );
            intent.putExtra("GESTORA", gestora );
            intent.putExtra("IDIOMA", idioma );
            intent.putExtra("DURACION", duracion );
            intent.putExtra("HORAINICIO", hEntrada );
            intent.putExtra("AULA", aula );
            startActivity(intent);
        });

    }

    private void consultarListaAsignaturas() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM ASIGNATURA WHERE id="+"'"+asignatura+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            nombreAsignatura = cursor.getString(1);
            nombre = cursor.getString(1);
            titulacion = cursor.getString(2);
            curso = cursor.getString(3);
            gestora = cursor.getString(4);
            idioma = cursor.getString(5);
            duracion = cursor.getString(6);

        }

        TextView textViewDegree = (TextView) findViewById(R.id.textViewDegree);
        textViewDegree.setText(titulacion);

        TextView textViewSubject = (TextView) findViewById(R.id.textViewSubject);
        textViewSubject.setText(identificador + ": " + nombreAsignatura);

        TextView textViewDepartment = (TextView) findViewById(R.id.textViewDepartment);
        textViewDepartment.setText(gestora);

        TextView textViewSchoolYear = (TextView) findViewById(R.id.textViewSchoolYear);
        textViewSchoolYear.setText(curso);

        EditText editTextLanguage = (EditText) findViewById(R.id.editTextLanguage);
        editTextLanguage.setText(idioma);

        EditText editTextGroup = (EditText) findViewById(R.id.editTextGroup);
        editTextGroup.setText("");

        EditText editTextClassroom = (EditText) findViewById(R.id.editTextClassroom);
        editTextClassroom.setText("");

        EditText editTextHour = (EditText) findViewById(R.id.editTextHour);
        editTextHour.setText("");

        EditText editTextDuration = (EditText) findViewById(R.id.editTextDuration);
        editTextDuration.setText("");

    }

}
