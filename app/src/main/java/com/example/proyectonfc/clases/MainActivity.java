package com.example.proyectonfc.clases;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

public class MainActivity extends AppCompatActivity {

    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);

        ImageButton buttonConfiguracion = (ImageButton) findViewById(R.id.buttonConfiguracion);
        buttonConfiguracion.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Configuracion.class);
            startActivityForResult(intent, 0);
        });

        ImageButton buttonUso = (ImageButton) findViewById(R.id.buttonUso);
        buttonUso.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), IdentificacionProfesor.class);
            startActivityForResult(intent, 0);
        });

        ImageButton buttonDatos = (ImageButton) findViewById(R.id.buttonDatos);
        buttonDatos.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DatosXml.class);
            startActivityForResult(intent, 0);
        });

    }

}
