package com.example.proyectonfc.clases;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import com.example.proyectonfc.CreacionParte;
import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.parser.NdefMessageParser;
import com.example.proyectonfc.record.ParsedNdefRecord;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import harmony.java.awt.Color;

public class RegistroAlumnos extends AppCompatActivity {


    int sum;
    private int count;
    private int cont;
    private String nombre_documento;
    private String nombre_documentoXml;
    private final static String NOMBRE_DIRECTORIO = "ParteFirmasUPV";
    private final static String NOMBRE_DIRECTORIO_2 = "DatosParteFirmasUPV";

    private final static String ETIQUETA_ERROR = "ERROR";
    private int longui= 0;
    private String[] listIdentificador = new String[1000];
    private String[] listDni = new String[1000];
    private String[] listNombre  = new String[1000];
    private ArrayList<String> listaIdentificadores = new ArrayList<>();
    private String[] listaIdentificadoresNoRepetidos  = new String[1000];
    private ArrayList<String> listaDni = new ArrayList<>();
    private ArrayList<String> listaNombre = new ArrayList<>();
    Button btnGenerar;
    private Spinner spinner2;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView text;
    private static byte[] identificador;
    private String nombreprofesor;
    private String asignatura;
    private String nombre;
    private String titulacion;
    private String grupo;
    private String curso;
    private String gestoria;
    private String idioma;
    private String duracion;
    private String horaInicio;
    private String fecha;
    private String aula;
    private String dniprofesor;
    private int alumnos;
    private CountDownTimer timer;
    private int secondsUntilFinished = 10000;
    private TextView textTimer;
    DataBase dataBase;
    private String identificadorTemporal;
    private String identificadorFijo;

    private String nombre_directorioPdf;
    private String nombre_directorioXml;
    private String direccion;
    private EditText textDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_alumnos);
        text = (TextView) findViewById(R.id.text);


        nombreprofesor = getIntent().getStringExtra( "NOMBREPROFESOR");
        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        nombre = getIntent().getStringExtra( "NOMBRE");
        titulacion = getIntent().getStringExtra( "TITULACION");
        grupo = getIntent().getStringExtra( "GRUPO");
        curso = getIntent().getStringExtra( "CURSO");
        gestoria = getIntent().getStringExtra( "GESTORA");
        idioma = getIntent().getStringExtra( "IDIOMA");
        duracion = getIntent().getStringExtra( "DURACION");
        horaInicio = getIntent().getStringExtra( "HORAINICIO");
        aula = getIntent().getStringExtra( "AULA");
        dniprofesor = getIntent().getStringExtra( "DNIPROFESOR");



        Button btnSiguiente = (Button) findViewById(R.id.buttonMenu);
        btnSiguiente.setOnClickListener((View.OnClickListener) v -> {
            Log.d("AppLog", "Identificadores size: " + listaIdentificadores.size());

            Intent intent = new Intent(this, CreacionParte.class);

            intent.putExtra("listaIdentificadores", listaIdentificadores);

            intent.putExtra("nombreprofesor", nombreprofesor);
            intent.putExtra("asignatura", asignatura);
            intent.putExtra("nombre", nombre);
            intent.putExtra("titulacion", titulacion);
            intent.putExtra("grupo", grupo);
            intent.putExtra("curso", curso);
            intent.putExtra("gestoria", gestoria);
            intent.putExtra("idioma", idioma);
            intent.putExtra("duracion", duracion);
            intent.putExtra("horaInicio", horaInicio);
            intent.putExtra("aula", aula);
            intent.putExtra("dniprofesor", dniprofesor);

            startActivity(intent);
        });

        btnGenerar=(Button) findViewById(R.id.buttonPdf);
        final DataBase dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);


        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1000);
        } else {
        }


    }



    /******************************************************LECTOR NFC********************************************************************/

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            //showWirelessSettings();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    public void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if(rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }else{
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                identificador = id;
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }
            displayMsgs(msgs);
        }
    }

    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            builder.append(str).append("\n");
        }

        text.setText(builder.toString());
    }

    private String dumpTagData(Tag tag) {
        final DataBase dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("Alumno con dentificador: ").append(toDec(id)).append('\n');

        SQLiteDatabase db=dataBase.getReadableDatabase();

        //select * from usuarios


        Cursor cursor=db.rawQuery("SELECT * FROM ALUMNO WHERE id = '"+asignatura+String.valueOf(toDec(identificador))+"'", null);

        while (cursor.moveToNext()){
            identificadorTemporal = cursor.getString(0);
        }

        if (identificadorTemporal == null){
            identificadorTemporal = "1234567890123456789012345678901234567890123456789012345678901234567890";
        }else {
            identificadorTemporal = identificadorTemporal.substring(3);
        }

        identificadorFijo = String.valueOf(toDec(identificador));
        if(identificadorTemporal.equals(identificadorFijo)){

            try {
                dataBase.agregarAlumnoTemporal(String.valueOf(toDec(identificador)));
            }catch (Exception e){
                Log.d("AppLog", "El alumno " + identificador + " ya figura en la base de datos.");
            }
            String idAux = String.valueOf(toDec(identificador));
            if (listaIdentificadores.contains(idAux)) {
                Toast.makeText(getApplicationContext(), "ESTE ALUMNO YA TIENE REGISTRADA LA ASISTENCIA", Toast.LENGTH_SHORT).show();
            } else {
                listaIdentificadores.add(idAux);
                Toast.makeText(getApplicationContext(), "Fichaje realizado.", Toast.LENGTH_SHORT).show();
            }
            Log.d("AppLog", "Alumno registrado. Total: " + listaIdentificadores.size());
            count++;
            sum++;
        }else{
            Toast.makeText(getApplicationContext(), "EL ALUMNO CON IDENTIFIFCADOR: "+String.valueOf(toDec(identificador))+"NO ESTÁ DADO DE ALTA", Toast.LENGTH_SHORT).show();
        }

        alumnos = count;

        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

/*****************************************************FIN LECTOR NFC*****************************************************************/


    /******************************************************GENERAR XML*****************************************************/

//    public void escribirXml() {
//
//        try {
//
//            XmlSerializer serializer = Xml.newSerializer();
//            String documento = "ParteFirmas-" + asignatura + "-" + grupo + "-" +
//                    fecha.replace('/', '-');
//            nombre_documentoXml = "ParteFirmas-" + asignatura + "-" + grupo + "-" +
//                    fecha.replace('/', '-') + ".xml";
//            File f = crearFichero2(nombre_documentoXml);
//            FileOutputStream ficheroXml = new FileOutputStream(f.getAbsolutePath());
//            OutputStreamWriter fout =
//                    new OutputStreamWriter(ficheroXml);
//            try {
//                serializer.setOutput(fout);
//                serializer.startDocument("UTF-8", true);
//                serializer.startTag("", "ParteFirmas");
//
//                serializer.startTag("", documento);
//
//                serializer.startTag("", "espacio");
//                serializer.text(aula);
//                serializer.endTag("", "espacio");
//
//                serializer.startTag("", "asignatura");
//                serializer.text(asignatura + "-" + nombre);
//                serializer.endTag("", "asignatura");
//
//                serializer.startTag("", "titulacion");
//                serializer.text(titulacion);
//                serializer.endTag("", "titulacion");
//
//                serializer.startTag("", "grupo");
//                serializer.text(grupo);
//                serializer.endTag("", "grupo");
//
//                serializer.startTag("", "nombreProfesor");
//                serializer.text(nombreprofesor);
//                serializer.endTag("", "nombreProfesor");
//
//                serializer.startTag("", "dni");
//                serializer.text(dniprofesor);
//                serializer.endTag("", "dni");
//
//                serializer.startTag("", "curso");
//                serializer.text(curso);
//                serializer.endTag("", "curso");
//
//                serializer.startTag("", "gestora");
//                serializer.text(gestoria);
//                serializer.endTag("", "gestora");
//
//                serializer.startTag("", "idioma");
//                serializer.text(idioma);
//                serializer.endTag("", "idioma");
//
//                serializer.startTag("", "fecha");
//                serializer.text(fecha);
//                serializer.endTag("", "fecha");
//
//                serializer.startTag("", "horaInicio");
//                serializer.text(horaInicio);
//                serializer.endTag("", "horaInicio");
//
//                serializer.startTag("", "duracion");
//                serializer.text(duracion);
//                serializer.endTag("", "duracion");
//
//                serializer.startTag("", "numeroAlumnos");
//                serializer.text(String.valueOf(alumnos));
//                serializer.endTag("", "numeroAlumnos");
//
//                serializer.endTag("", documento);
//
//                serializer.endTag("", "ParteFirmas");
//                serializer.endDocument();
//                fout.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } catch (IOException e) {
//
//            Log.e(ETIQUETA_ERROR, e.getMessage());
//
//        }
//
//    }
//
//    private File crearFichero2(String nombreFichero2) {
//        File ruta2 = getRuta2();
//        File fichero2 = null;
//        if (ruta2!= null)
//            fichero2 = new File(ruta2, nombreFichero2);
//        return fichero2;
//
//    }
//
//    private File getRuta2() {
//        File ruta2 = null;
//        if (Environment.MEDIA_MOUNTED.equals(Environment
//                .getExternalStorageState())) {
//            ruta2 = new File(
//                    Environment
//                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                    NOMBRE_DIRECTORIO_2);
//
//
//            if (ruta2 != null) {
//                if (!ruta2.mkdirs()) {
//                    if (!ruta2.exists()) {
//                        return null;
//                    }
//                }
//            }
//        } else {
//        }
//
//        return ruta2;
//    }


    /******************************************************FIN-GENERAR XML*****************************************************/




}
