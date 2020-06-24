package com.example.proyectonfc.clases;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatosAsignaturaNormal extends AppCompatActivity {

    DataBase dataBase;
    private String identificador;
    private String nombreAsignatura;
    private String nombre;
    private String titulacion;
    private String curso;
    private String gestora;
    private String idioma;
    private String duracion;
    private String asignatura;
    private String grupoGrupo;
    private String horaInicioGrupo;
    private String horaFinGrupo;
    private String aulaGrupo;

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //Alternativa 1
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_actualizar,menu);
//        return true;
//
//    }


    public boolean onOptionsItemSelected(MenuItem menu)
    {
        // gracias a la id, sabemos que item es el que se oprime, en este caso usamos un switch
        switch (menu.getItemId())
        {
            case R.id.MenuActualizar:
                Intent intent = new Intent(DatosAsignaturaNormal.this, DatosAsignaturaNormal.class);
                asignatura = getIntent().getStringExtra( "ASIGNATURA");
                intent.putExtra("ASIGNATURA", asignatura );
                startActivity(intent);
                this.finish();
                return true;

            default:
        }
        return super.onOptionsItemSelected(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_asignatura_normal);

        dataBase = new DataBase(getApplicationContext(), "DB5.db", null, 1);

        asignatura = getIntent().getStringExtra( "ASIGNATURA");

        consultarListaAsignaturas();
        consultarHoraMenos();

        Button buttonAsistencia = (Button) findViewById(R.id.buttonAsistencia);
        buttonAsistencia.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), RegistroAlumnos.class);

            intent.putExtra("ASIGNATURA", asignatura );
            intent.putExtra("NOMBRE", nombre );
            intent.putExtra("TITULACION", titulacion );
            intent.putExtra("GRUPO", grupoGrupo );
            intent.putExtra("CURSO", curso );
            intent.putExtra("GESTORA", gestora );
            intent.putExtra("IDIOMA", idioma );
            intent.putExtra("DURACION", duracion );
            intent.putExtra("HORAINICIO", horaInicioGrupo );
            intent.putExtra("HORAFIN", horaFinGrupo );
            intent.putExtra("AULA", aulaGrupo );

            try{
                startActivityForResult(intent, 0);
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "LA ASIGNATURA SELECCIONADA ANTERIORMENTE NO TIENE UN GRUPO CON ESTE HORARIO", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void consultarListaAsignaturas() {
        SQLiteDatabase db=dataBase.getReadableDatabase();
        //select * from usuarios
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

        TextView textAsignatura = (TextView) findViewById(R.id.textAsignatura);
        textAsignatura.setText(nombreAsignatura);
        TextView textIdentificador = (TextView) findViewById(R.id.textIdentificador);
        textIdentificador.setText(identificador);
        TextView textNombre = (TextView) findViewById(R.id.textNombre);
        textNombre.setText(nombre);
        TextView textTitulacion = (TextView) findViewById(R.id.textTitulacion);
        textTitulacion.setText(titulacion);
        TextView textCurso = (TextView) findViewById(R.id.textCurso);
        textCurso.setText(curso);
        TextView textGestora = (TextView) findViewById(R.id.textGestora);
        textGestora.setText(gestora);
        TextView textIdioma = (TextView) findViewById(R.id.textIdioma);
        textIdioma.setText(idioma);
        TextView textDuracion = (TextView) findViewById(R.id.textDuracion);
        textDuracion.setText(duracion);
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
//            identificadorGrupo = cursor.getString(0);
            grupoGrupo = cursor.getString(1);
            horaInicioGrupo = cursor.getString(2);
            horaFinGrupo = cursor.getString(3);
            aulaGrupo = cursor.getString(4);
        }


        TextView textGrupo = (TextView) findViewById(R.id.textGrupo);
        textGrupo.setText(grupoGrupo);
        TextView textHoraEntradaGrupo = (TextView) findViewById(R.id.textHoraEntradaGrupo);
        textHoraEntradaGrupo.setText(horaInicioGrupo);
        TextView textHoraSalidaGrupo = (TextView) findViewById(R.id.textHoraSalidaGrupo);
        textHoraSalidaGrupo.setText(horaFinGrupo);
        TextView textAulaGrupo = (TextView) findViewById(R.id.textAulaGrupo);
        textAulaGrupo.setText(aulaGrupo);
    }

}
