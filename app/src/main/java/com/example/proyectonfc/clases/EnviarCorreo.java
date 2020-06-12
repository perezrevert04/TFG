package com.example.proyectonfc.clases;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectonfc.R;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.ParserConfigurationException;

import androidx.appcompat.app.AppCompatActivity;

public class EnviarCorreo extends AppCompatActivity {

    private Button btnCorreo;
    private String nombre_directorioPdf;
    private String nombre_directorioXml;
    private EditText textDireccion;
    private String direccion;
    private String nombre_documento;
    private String nombre_documentoXml;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enviar_correo);
        nombre_documento = getIntent().getStringExtra( "NOMBREDOCUMENTO");
        nombre_documentoXml = getIntent().getStringExtra( "NOMBREDOCUMENTO2");
        textDireccion = (EditText) findViewById(R.id.textDireccion);



        btnCorreo = (Button) findViewById(R.id.btnCorreo);
        btnCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                enviarEmail();
                startActivityForResult(emailIntent, 0);
            }

        });

    }

    private void enviarEmail() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Log.e("Test email:", "enviando email");
        nombre_directorioPdf = "ParteFirmasUPV/"+nombre_documento;
        nombre_directorioXml = "DatosParteFirmasUPV/"+nombre_documentoXml;
        direccion = textDireccion.getText().toString();
        String[] TO = {String.valueOf(direccion)};
        String[] CC = {};


        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO );
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Parte de Firmas");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Se adjunta Pdf de Parte de firmas");
        emailIntent.setType("*/*");


        ArrayList<Uri> uris = new ArrayList<Uri>();
        String[] filePaths = new String[] {"/storage/emulated/0/Download/"+nombre_directorioPdf, "/storage/emulated/0/Download/"+nombre_directorioXml};
        for (String file : filePaths)
        {
            File fileIn = new File(file);
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(emailIntent);


        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar correo..."));
            finish();
            Log.e("Test email:", "Fin envio email");

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EnviarCorreo.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }



}
