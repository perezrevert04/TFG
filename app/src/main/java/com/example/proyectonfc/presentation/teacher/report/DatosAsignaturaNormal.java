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

import com.example.proyectonfc.Global;
import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.model.Group;
import com.example.proyectonfc.model.Report;
import com.example.proyectonfc.model.Subject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatosAsignaturaNormal extends AppCompatActivity {

    private DataBase dataBase;
    private Subject subject;
    private Group group;

    private TextView textViewDegree;
    private TextView textViewSubject;
    private TextView textViewDepartment;
    private TextView textViewSchoolYear;
    private EditText editTextLanguage;
    private EditText editTextGroup;
    private EditText editTextClassroom;
    private EditText editTextHour;
    private EditText editTextDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_asignatura_normal);

        dataBase = ((Global) getApplication()).getOldDatabase();

        textViewDegree = findViewById(R.id.textViewDegree);
        textViewSubject = findViewById(R.id.textViewSubject);
        textViewDepartment = findViewById(R.id.textViewDepartment);
        textViewSchoolYear = findViewById(R.id.textViewSchoolYear);
        editTextLanguage = findViewById(R.id.editTextLanguage);
        editTextGroup = findViewById(R.id.editTextGroup);
        editTextClassroom = findViewById(R.id.editTextClassroom);
        editTextHour = findViewById(R.id.editTextHour);
        editTextDuration = findViewById(R.id.editTextDuration);

        String code = getIntent().getStringExtra( "ASIGNATURA");

        getSubject(code);
        consultarHoraMenos();

        Button buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener( v -> {

            Intent intent = new Intent(v.getContext(), RegistroAlumnos.class);

            subject.setLanguage( editTextLanguage.getText().toString() );
            subject.setDuration( editTextDuration.getText().toString() );

            group.setName( editTextGroup.getText().toString() );
            group.setHour( editTextHour.getText().toString() );
            group.setClassroom( editTextClassroom.getText().toString() );

            Report report = new Report(subject, group);

            intent.putExtra("ReportObject", report);

            startActivity(intent);
        });

    }

    private void getSubject(String code) {
        subject = dataBase.getSubjectById(code);

        textViewDegree.setText( subject.getDegree() );
        textViewSchoolYear.setText( subject.getSchoolYear() );
        textViewDepartment.setText( subject.getDepartment() );
        editTextLanguage.setText( subject.getLanguage() );
        editTextDuration.setText( subject.getDuration() );

        String title = code + ": " + subject.getName();
        textViewSubject.setText(title);
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

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "La asignatura no tiene un grupo con este horario", Toast.LENGTH_SHORT).show();

            editTextGroup.setText("");
            editTextHour.setText("");
            editTextClassroom.setText("");
            editTextDuration.setText("");

            group = new Group();
        } else {
            while (cursor.moveToNext()) {
                group = new Group(
                        cursor.getString(0), // code
                        cursor.getString(1), // name
                        cursor.getString(4), // classroom
                        cursor.getString(2), // hour
                        cursor.getString(3)  // end
                );
            }

            editTextGroup.setText( group.getName() );
            editTextHour.setText( group.getHour() );
            editTextClassroom.setText( group.getClassroom() );
        }


    }

}
