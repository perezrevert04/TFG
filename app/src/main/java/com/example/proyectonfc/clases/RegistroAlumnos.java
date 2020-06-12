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
    private String[] listaIdentificadores  = new String[1000];
    private String[] listaIdentificadoresNoRepetidos  = new String[1000];
    private String[] listaDni = new String[1000];
    private String[] listaNombre = new String[1000];
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



        Button btn2 = (Button) findViewById(R.id.buttonMenu);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.enviar_correo, null);
                textDireccion = (EditText) dialoglayout.findViewById(R.id.textDireccion);


                Button btnCorreo = (Button) dialoglayout.findViewById(R.id.btnCorreo);
                btnCorreo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        enviarEmail();
                        startActivityForResult(emailIntent, 0);
                    }

                });

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroAlumnos.this);
                builder.setView(dialoglayout);
                builder.show();
            }
        });

        btnGenerar=(Button) findViewById(R.id.buttonPdf);
        final DataBase dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                fecha = dateFormat.format(date);


                try{


                    for (int t = 0; t<count; t++) {


                        dataBase.consultarAlumno(asignatura, String.valueOf(listaIdentificadores[t]));

                        listaDni[t] = dataBase.getListDni();
                        listaNombre[t] = dataBase.getListNombre();

                    }

                    SQLiteDatabase db=dataBase.getReadableDatabase();
                    if(db!=null) {
                        db.execSQL("DELETE FROM ALUMNOTEMPORAL");
                        db.close();
                    }

                    generarPdf(dataBase);
                    escribirXml();




                    Toast.makeText(getApplicationContext(), "Se creo tu archivo pdf", Toast.LENGTH_SHORT).show();

                    String file = "/storage/emulated/0/Download/ParteFirmasUPV/"+nombre_documento;

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());


                    File fileIn = new File(file);
                    Uri u = Uri.fromFile(fileIn);
                    Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                    pdfOpenintent.setDataAndType(u, "application/pdf");
                    pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(pdfOpenintent);

                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No se ha podido crear el archivo pdf", Toast.LENGTH_SHORT).show();
                }

            }



        });
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

    private void enviarEmail() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Log.e("Test email:", "enviando email");
        nombre_directorioPdf = "ParteFirmasUPV/"+nombre_documento;
        nombre_directorioXml = "DatosParteFirmasUPV/"+nombre_documentoXml;
        direccion = textDireccion.getText().toString();
        String[] TO = {String.valueOf(direccion)};
        String[] CC = {};
        /*
        Uri uri = Uri.fromFile(new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                nombre_directorio));
        Uri uri2 = Uri.fromFile(new File(
                Environment.getDownloadCacheDirectory().getAbsolutePath() + nombre_documentoXml));
        */

        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO );
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Parte de Firmas");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Se adjunta:\n    -Pdf de Parte de firmas.\n    -Datos Xml de Parte de Firmas ");
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
        //startActivity(emailIntent);

        //emailIntent.setData(Uri.parse("mailto:"));
        //emailIntent.setType("application/pdf");

        //emailIntent.putExtra(Intent.EXTRA_CC, CC);





        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar correo..."));
            finish();
            Log.e("Test email:", "Fin envio email");

        } catch (android.content.ActivityNotFoundException ex) {

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

    //private void showWirelessSettings() {
    //  Toast.makeText(this, "You need enable NFC", Toast.LENGTH_SHORT).show();
    //Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
    //startActivity(intent);
    //}

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
                listaIdentificadores[sum] = String.valueOf(toDec(identificador));
                count++;
                sum++;
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "ESTE ALUMNO YA TIENE REGISTRADA LA ASISTENCIA", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "EL ALUMNO CON IDENTIFIFCADOR: "+String.valueOf(toDec(identificador))+"NO ESTÁ DADO DE ALTA", Toast.LENGTH_SHORT).show();
        }alumnos = count;




        //listDni[sum] = new String(di.getListDni());
        //listNombre[sum] = new String(di.getListNombre());

        return sb.toString();
    }



    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
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

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

/*****************************************************FIN LECTOR NFC*****************************************************************/

    /******************************************************GENERAR PDF*****************************************************/

    public void generarPdf(DataBase dataBase) {

        // Creamos el documento.
        Document documento = new Document();

        try {


            nombre_documento = "ParteFirmas-"+asignatura+"-"+grupo+"-"+fecha.replace('/','-')+".pdf";

            //Creación archivo pdf
            File f = crearFichero(nombre_documento);
            FileOutputStream ficheroPdf = new FileOutputStream(f.getAbsolutePath());
            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

            // Incluimos el pie de pagina y una cabecera
            HeaderFooter cabecera = new HeaderFooter(new Phrase(
                    "Parte de firmas Universidad Politécnica de Valencia"), false);
            cabecera.setAlignment(Element.ALIGN_CENTER);
            HeaderFooter pie = new HeaderFooter(new Phrase(
                    "Parte de firmas Universidad Politécnica de Valencia"), false);
            pie.setAlignment(Element.ALIGN_CENTER);

            documento.setHeader(cabecera);
            documento.setFooter(pie);

            // Abrimos el documento.
            documento.open();



            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20,
                    Font.BOLD, Color.BLACK);
            documento.add(new Paragraph("     SEGUIMIENTO DE LAS ACTIVIDADES DOCENTES\n\n\n", font));


            // Añadimos un titulo con la fuente por defecto.
            documento.add(new Paragraph(""));



            PdfPTable tablaA = new PdfPTable(1);
            tablaA.setWidthPercentage(100.00f);
            tablaA.addCell("\nEspacio: "+aula+"\n\n");
            documento.add(tablaA);

            PdfPTable tablaB = new PdfPTable(2);
            tablaB.setWidthPercentage(100.00f);
            tablaB.addCell("\nAsignatura: "+asignatura+"-"+nombre+" \nTitulación: "+titulacion+"\nGrupo: "+grupo+"\n\n");
            tablaB.addCell("\nNombre Profesor: "+nombreprofesor+"\nDNI: "+dniprofesor+"\n\n");
            PdfPTable tablaC = new PdfPTable(3);
            tablaC.setWidthPercentage(100.00f);
            tablaC.addCell("\nCurso/Sem.: "+curso+"\nER Gestora: "+gestoria+"\nIdioma: "+idioma+"\n\n");
            tablaC.addCell("\nFecha: "+fecha+"\nHora: "+horaInicio+"\nDuración: "+duracion+"\n\n");
            tablaC.addCell("Firma: \n\n\n");
            PdfPTable tablaD = new PdfPTable(1);
            tablaD.setWidthPercentage(100.00f);
            tablaD.addCell("\nObservaciones: \n\n\n");

            documento.add(tablaB);
            documento.add(tablaC);
            documento.add(tablaD);

            documento.add(new Paragraph("\n\n\n"));




            // Insertamos una tabla.
            PdfPTable tabla1 = new PdfPTable(3);
            tabla1.setWidthPercentage(100.00f);
            tabla1.addCell("IDENTIFICADOR ");
            tabla1.addCell("DNI");
            tabla1.addCell("NOMBRE");




            PdfPTable tabla2 = new PdfPTable(3);
            tabla2.setWidthPercentage(100.00f);
            for (int j = 0; j < sum ;j++){


                tabla2.addCell(""+listaIdentificadores[j]);
                tabla2.addCell(""+listaDni[j]);
                tabla2.addCell(""+listaNombre[j]);

            }



            longui = 0;
            count = 0;
            sum = 0;

            documento.add(tabla1);
            documento.add(tabla2);






        } catch (DocumentException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } catch (IOException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } finally {
            // Cerramos el documento.
            documento.close();
        }
    }


    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }


    /**
     * Obtenemos la ruta donde vamos a almacenar el fichero.
     *
     * @return
     */
    public static File getRuta() {

        // El fichero sera almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);


            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }

    /******************************************************FIN-GENERAR PDF*****************************************************/

    /******************************************************GENERAR XML*****************************************************/

    public void escribirXml() {

        try {

            XmlSerializer serializer = Xml.newSerializer();
            String documento = "ParteFirmas-" + asignatura + "-" + grupo + "-" +
                    fecha.replace('/', '-');
            nombre_documentoXml = "ParteFirmas-" + asignatura + "-" + grupo + "-" +
                    fecha.replace('/', '-') + ".xml";
            File f = crearFichero2(nombre_documentoXml);
            FileOutputStream ficheroXml = new FileOutputStream(f.getAbsolutePath());
            OutputStreamWriter fout =
                    new OutputStreamWriter(ficheroXml);
            try {
                serializer.setOutput(fout);
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "ParteFirmas");

                serializer.startTag("", documento);

                serializer.startTag("", "espacio");
                serializer.text(aula);
                serializer.endTag("", "espacio");

                serializer.startTag("", "asignatura");
                serializer.text(asignatura + "-" + nombre);
                serializer.endTag("", "asignatura");

                serializer.startTag("", "titulacion");
                serializer.text(titulacion);
                serializer.endTag("", "titulacion");

                serializer.startTag("", "grupo");
                serializer.text(grupo);
                serializer.endTag("", "grupo");

                serializer.startTag("", "nombreProfesor");
                serializer.text(nombreprofesor);
                serializer.endTag("", "nombreProfesor");

                serializer.startTag("", "dni");
                serializer.text(dniprofesor);
                serializer.endTag("", "dni");

                serializer.startTag("", "curso");
                serializer.text(curso);
                serializer.endTag("", "curso");

                serializer.startTag("", "gestora");
                serializer.text(gestoria);
                serializer.endTag("", "gestora");

                serializer.startTag("", "idioma");
                serializer.text(idioma);
                serializer.endTag("", "idioma");

                serializer.startTag("", "fecha");
                serializer.text(fecha);
                serializer.endTag("", "fecha");

                serializer.startTag("", "horaInicio");
                serializer.text(horaInicio);
                serializer.endTag("", "horaInicio");

                serializer.startTag("", "duracion");
                serializer.text(duracion);
                serializer.endTag("", "duracion");

                serializer.startTag("", "numeroAlumnos");
                serializer.text(String.valueOf(alumnos));
                serializer.endTag("", "numeroAlumnos");

                serializer.endTag("", documento);

                serializer.endTag("", "ParteFirmas");
                serializer.endDocument();
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        }

    }

    private File crearFichero2(String nombreFichero2) {
        File ruta2 = getRuta2();
        File fichero2 = null;
        if (ruta2!= null)
            fichero2 = new File(ruta2, nombreFichero2);
        return fichero2;

    }

    private File getRuta2() {
        File ruta2 = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta2 = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO_2);


            if (ruta2 != null) {
                if (!ruta2.mkdirs()) {
                    if (!ruta2.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta2;
    }


    /******************************************************FIN-GENERAR XML*****************************************************/




}
