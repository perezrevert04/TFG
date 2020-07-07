package com.example.proyectonfc.presentation.teacher.report;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.presentation.teacher.report.RegistroAlumnos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatosAsignaturaNormal extends AppCompatActivity {

    DataBase dataBase;
    private String nombre;
    private String asignatura;

    TextView textViewDegree;
    TextView textViewSubject;
    TextView textViewDepartment;
    TextView textViewSchoolYear;
    EditText editTextLanguage;
    EditText editTextGroup;
    EditText editTextClassroom;
    EditText editTextHour;
    EditText editTextDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_asignatura_normal);

        dataBase = new DataBase(getApplicationContext());

        textViewDegree = (TextView) findViewById(R.id.textViewDegree);
        textViewSubject = (TextView) findViewById(R.id.textViewSubject);
        textViewDepartment = (TextView) findViewById(R.id.textViewDepartment);
        textViewSchoolYear = (TextView) findViewById(R.id.textViewSchoolYear);
        editTextLanguage = (EditText) findViewById(R.id.editTextLanguage);
        editTextGroup = (EditText) findViewById(R.id.editTextGroup);
        editTextClassroom = (EditText) findViewById(R.id.editTextClassroom);
        editTextHour = (EditText) findViewById(R.id.editTextHour);
        editTextDuration = (EditText) findViewById(R.id.editTextDuration);

        asignatura = getIntent().getStringExtra( "ASIGNATURA");

        consultarListaAsignaturas();
        consultarHoraMenos();

        Button buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), RegistroAlumnos.class);
            intent.putExtra("ASIGNATURA", asignatura );
            intent.putExtra("NOMBRE", nombre );
            intent.putExtra("TITULACION", textViewDegree.getText().toString() );
            intent.putExtra("GRUPO", editTextGroup.getText().toString() );
            intent.putExtra("CURSO", textViewSchoolYear.getText().toString() );
            intent.putExtra("GESTORA", textViewDepartment.getText().toString() );
            intent.putExtra("IDIOMA", editTextLanguage.getText().toString() );
            intent.putExtra("DURACION", editTextDuration.getText().toString() );
            intent.putExtra("HORAINICIO", editTextHour.getText().toString() );
            intent.putExtra("AULA", editTextClassroom.getText().toString() );
            startActivity(intent);
        });

    }

    private void consultarListaAsignaturas() {
        SQLiteDatabase db=dataBase.getReadableDatabase();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM ASIGNATURA WHERE id="+"'"+asignatura+"'", null);

        while (cursor.moveToNext()){
            nombre = cursor.getString(1);
            textViewDegree.setText(cursor.getString(2));
            textViewSchoolYear.setText(cursor.getString(3));
            textViewDepartment.setText(cursor.getString(4));
            editTextLanguage.setText(cursor.getString(5));
            editTextDuration.setText(cursor.getString(6));
        }

        textViewSubject.setText(asignatura + ": " + nombre);
    }


    private void consultarHoraMenos() {
        SQLiteDatabase db=dataBase.getReadableDatabase();

        Calendar calendarioIgual = Calendar.getInstance();
        calendarioIgual.add(Calendar.MINUTE, 0);

        String horaIgual =  new SimpleDateFormat("HH:mm").format(calendarioIgual.getTime());

        Calendar calendarioMas = Calendar.getInstance();
        calendarioMas.add(Calendar.MINUTE, 0);

        String horaMas =  new SimpleDateFormat("HH").format(calendarioMas.getTime());

        //select * from asignatura
        Cursor cursor=db.rawQuery("SELECT * FROM GRUPO WHERE h_entrada BETWEEN "+"'"+horaMas+":00' AND "+"'"+horaIgual+"'", null);

        while (cursor.moveToNext()){
            editTextGroup.setText(cursor.getString(1));
            editTextHour.setText(cursor.getString(2));
            editTextClassroom.setText(cursor.getString(4));
        }

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "La asignatura no tiene un grupo con este horario", Toast.LENGTH_SHORT).show();

            editTextGroup.setText("");
            editTextHour.setText("");
            editTextClassroom.setText("");
            editTextDuration.setText("");
        }


    }

}
