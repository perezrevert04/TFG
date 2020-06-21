package com.example.proyectonfc.clases;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.LinkCardActivity;
import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;

public class MainActivity extends AppCompatActivity {

    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);

        Button buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener( v -> {
            Intent intent = new Intent(v.getContext(), IdentificacionProfesor.class);
            startActivityForResult(intent, 0);
        });

        Button buttonManageSubjects = findViewById(R.id.buttonManageSubjects);
        buttonManageSubjects.setOnClickListener( v -> {
            Intent intent = new Intent(v.getContext(), Configuracion.class);
            startActivityForResult(intent, 0);
        });


        Button buttonConsultParts = findViewById(R.id.buttonConsultParts);
        buttonConsultParts.setOnClickListener( v -> {
            Intent intent = new Intent(v.getContext(), DatosXml.class);
            startActivityForResult(intent, 0);
        });

        TextView link = findViewById(R.id.textViewLinkCard);
        link.setOnClickListener( v -> {
            Intent intent = new Intent(v.getContext(), LinkCardActivity.class);
            startActivityForResult(intent, 0);
        });

    }

}
