package com.example.proyectonfc.presentation.teacher.report;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.proyectonfc.use_cases.CommandVoiceActivity;
import com.example.proyectonfc.util.biometric.Biometry;
import com.r0adkll.slidr.Slidr;

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

    private Biometry biometry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_asignatura_normal);
        Slidr.attach(this);

        biometry = new Biometry(this, "Autenticación", "Autenticación biométrica para iniciar el parte");
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
        getGroup(code);

        Button buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener( view -> {
            biometry.authenticate( () -> {
                startReport();
                return null;
            });
        });

    }

    private void startReport() {
        Intent intent = new Intent(this, RegistroAlumnos.class);

        subject.setLanguage( editTextLanguage.getText().toString() );
        subject.setDuration( editTextDuration.getText().toString() );

        group.setName( editTextGroup.getText().toString() );
        group.setHour( editTextHour.getText().toString() );
        group.setClassroom( editTextClassroom.getText().toString() );

        Report report = new Report(subject, group);

        intent.putExtra("ReportObject", report);

        startActivity(intent);
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


    private void getGroup(String code) {
        group = dataBase.getCurrentGroup(code);

        if (group.getCode().isEmpty()) {
            Toast.makeText(getApplicationContext(), "La asignatura no tiene un grupo con este horario", Toast.LENGTH_SHORT).show();
            editTextDuration.setText("");
        }

        editTextGroup.setText( group.getName() );
        editTextHour.setText( group.getHour() );
        editTextClassroom.setText( group.getClassroom() );

    }

}
