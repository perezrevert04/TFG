package com.example.proyectonfc.presentation.teacher.management.subjects;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.model.CardInfo;
import com.example.proyectonfc.presentation.CaptureActivityPortrait;
import com.example.proyectonfc.util.nfc.Nfc;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NuevoAlumno extends AppCompatActivity {

    Button buttonAddStudent;

    private TextView tvResult = null;
    private CardInfo ci = null;
    private String studentId;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String asignatura;
    private String dni;
    private String nombre;
    DataBase dataBase;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_alumno);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        text = findViewById(R.id.text);
        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dataBase = new DataBase(getApplicationContext());


        buttonAddStudent = findViewById(R.id.buttonAddStudent);
        buttonAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), Alumnos.class);
            try {
                if (studentId == null || nombre == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NuevoAlumno.this);
                    builder.setTitle("No ha realizado todos los pasos");
                    builder.setMessage("¡Debe de escanear el código QR y posteriormente acercar la tarjeta para leer el identificador!");
                    builder.setNeutralButton("¡Entendido!", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    dataBase.agregarAlumno(asignatura + studentId, dni, nombre);
                    Toast.makeText(getApplicationContext(), "¡Alumno añadido!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }catch (Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(NuevoAlumno.this);
                builder.setTitle("Duplicidad");
                builder.setMessage("El alumno con Identificador " + studentId + " ya está dado de alta.");
                builder.setNeutralButton("¡Entendido!", (dialog, which) -> onBackPressed());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        tvResult = findViewById(R.id.tvResult);
        ci = new CardInfo();

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
        }

    }

    /*** INICIO ESCANER QR ***/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelaste el escaneo", Toast.LENGTH_LONG).show();
            } else {
                String[] strParts = result.getContents().split("/");
                getWebsiteInfo("https://www.upv.es/pls/oalu/sic_tui.inicio?p_tui="+strParts[strParts.length-1]);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onClick(View v) {
        IntentIntegrator intent = new IntentIntegrator(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intent.setPrompt("Escaneando carnet UPV ...");
        intent.setCameraId(0);
        intent.setBeepEnabled(true);
        intent.setCaptureActivity(CaptureActivityPortrait.class);
        intent.setBarcodeImageEnabled(false);
        intent.initiateScan();
    }

    private void getWebsiteInfo(String url) {
        final String URL = url;
        (Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT)).show();
        new Thread(() -> {
            final StringBuilder builder = new StringBuilder();

            try {
                org.jsoup.nodes.Document doc = Jsoup.connect(URL).get();
                Elements links = doc.select(CardInfo.css_query);

                ///for (Element link : links) {
                for (int i=0; i< links.size(); i++){
                    org.jsoup.nodes.Element e = links.get(i);
                    String token = null;
                    if (e!=null) token = e.text();
                    if ((token!=null) && (e.text().length()>0)) {
                        if (ci.isDNI(token)){ ci.setDNI(links.get(++i).text());
                            dni = links.get(i).text();
                        }
                        else if (ci.isEstado(token)) ci.setEstado(links.get(++i).text());
                        else if (ci.isTarjeta(token)) ci.setTarjeta(links.get(++i).text());
                        else if (ci.isVigencia(token)) ci.setVigencia(links.get(++i).text());
                        else if (ci.isNombre(token)){
                            ci.setNombre(links.get(++i).text());
                            nombre = links.get(i).text();
                            ci.setFuncion(links.get(++i).text());
                        }
                    }
                }
                builder.append(ci.toString());


            } catch (IOException e) {
                builder.append("Error : ").append(e.getMessage()).append("\n");

            }

            runOnUiThread(() -> {
                ImageView qrCode = findViewById(R.id.imageView6);
                qrCode.setVisibility(View.INVISIBLE);
                ImageView checkQr = findViewById(R.id.imageViewCheckQR);
                checkQr.setVisibility(View.VISIBLE);
                tvResult.setText(builder.toString());
            });
        }).start();
    }

    /*** INICIO ESCANER QR ***/

    /*** INICIO LECTOR NFC ***/

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
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

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            studentId = Nfc.tagToString(tag);
            text.setText(studentId);

            ImageView qrCode = findViewById(R.id.imageView10);
            qrCode.setVisibility(View.INVISIBLE);
            ImageView checkNfc = findViewById(R.id.imageViewCheckNfc);
            checkNfc.setVisibility(View.VISIBLE);
        }
    }

    /*** FIN LECTOR NFC ***/


}
