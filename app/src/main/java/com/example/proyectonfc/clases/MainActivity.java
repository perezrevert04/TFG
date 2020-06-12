package com.example.proyectonfc.clases;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageButton buttonConfiguracion;
    private ImageButton buttonUso;
    private ImageButton buttonDatos;
    DataBase dataBase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);

        buttonConfiguracion = (ImageButton) findViewById(R.id.buttonConfiguracion);
        buttonConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Configuracion.class);
                startActivityForResult(intent, 0);
            }

        });

        buttonUso = (ImageButton) findViewById(R.id.buttonUso);
        buttonUso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), IdentificacionProfesor.class);
                startActivityForResult(intent, 0);
            }

        });

        buttonDatos = (ImageButton) findViewById(R.id.buttonDatos);
        buttonDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DatosXml.class);
                startActivityForResult(intent, 0);
            }

        });




    }


}
