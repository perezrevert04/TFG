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

        Button buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener((View.OnClickListener) v -> {
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

        Cursor cursor=db.rawQuery("SELECT * FROM ASIGNATURA WHERE id="+"'"+asignatura+"'", null);

        while (cursor.moveToNext()){
            nombre = cursor.getString(1);
            textViewDegree.setText(cursor.getString(2));
            textViewSchoolYear.setText(cursor.getString(3));
            textViewDepartment.setText(cursor.getString(4));
            editTextLanguage.setText(cursor.getString(5));
        }

        textViewSubject.setText(asignatura + ": " + nombre);

        editTextGroup.setText("");
        editTextClassroom.setText("");
        editTextHour.setText("");
        editTextDuration.setText("");

    }

}
