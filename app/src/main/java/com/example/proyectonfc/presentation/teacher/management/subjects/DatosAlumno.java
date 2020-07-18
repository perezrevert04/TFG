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
import com.r0adkll.slidr.Slidr;

public class DatosAlumno extends AppCompatActivity {

    private Button buttonEditar;
    DataBase dataBase;
    private String identificador;
    private String alumno;

    private TextView textAlumno;
    private TextView textIdentificador;
    private TextView textDni;

    private String asignatura;
    private String studentId;
    private String dniAlumno;


    @Override
    public void onResume() {
        super.onResume();
        consultarListaAlumnos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_alumno);
        Slidr.attach(this);

        dataBase = new DataBase(getApplicationContext());

        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        studentId = getIntent().getStringExtra( "StudentId");

        setTitle("Alumno de " + asignatura);

        consultarListaAlumnos();

        buttonEditar = findViewById(R.id.buttonEditStudent);
        buttonEditar.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditarAlumno.class);
            intent.putExtra("ASIGNATURA", asignatura );
            intent.putExtra("DNIALUMNO", dniAlumno );
            intent.putExtra("IDENTIFICADORALUMNO", identificador );
            intent.putExtra("StudentId", studentId );
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

    private void consultarListaAlumnos() {
        SQLiteDatabase db = dataBase.getReadableDatabase();

        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM ALUMNO WHERE id = "+"'"+studentId+"'", null);

        while (cursor.moveToNext()){
            identificador = cursor.getString(0);
            dniAlumno = cursor.getString(1);
            alumno = cursor.getString(2);
        }

        textIdentificador = findViewById(R.id.textViewStudentId);
        textIdentificador.setText(identificador);
        textDni = findViewById(R.id.textViewStudentDni);
        textDni.setText(dniAlumno);
        textAlumno = findViewById(R.id.textView60);
        textAlumno.setText(alumno);

    }

}
