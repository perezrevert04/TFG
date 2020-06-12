package com.example.proyectonfc.clases;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.parser.NdefMessageParser;
import com.example.proyectonfc.record.ParsedNdefRecord;
import com.example.proyectonfc.util.CaptureActivityPortrait;
import com.example.proyectonfc.util.CardInfo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class NuevoProfesor extends AppCompatActivity {

    Button btnAgregar;

    private TextView tvResult = null;
    private CardInfo ci = null;
    private static byte[] identificador;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String asignatura;
    private String dni;
    private String nombre;
    DataBase dataBase;
    private String funcion;
    private TextView text;
    private EditText numeroProfesores;
    private int count = 0;
    private String listaId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_profesor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        text = (TextView) findViewById(R.id.text);
        asignatura = getIntent().getStringExtra( "ASIGNATURA");
        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);


        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NuevoProfesor.class);
                //if (funcion == "PROFESOR/A") {
                    identificador= identificador;
                    dni= dni;
                    nombre= nombre;
                    try {
                        if (identificador==null || nombre == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NuevoProfesor.this);
                            builder.setTitle("No ha realizado todos los pasos");
                            builder.setMessage("¡Debe de escanear el código QR y posteriormente acercar la tarjeta para leer el identificador!");
                            builder.setNeutralButton("¡Entendido!", null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else {
                            dataBase.agregarProfesor(asignatura + toDec(identificador), dni, nombre);
                            Toast.makeText(getApplicationContext(), "PROFESOR AGREGADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            intent.putExtra("ASIGNATURA", asignatura );
                        }
                    }catch (Exception e){
                        AlertDialog.Builder builder = new AlertDialog.Builder(NuevoProfesor.this);
                        builder.setTitle("El profesor con Identificador: "+ toDec(identificador)+" ya está dado de alta.");
                        builder.setMessage("");
                        builder.setNeutralButton("¡Entendido!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                //}else {
                    //Toast.makeText(getApplicationContext(), "LA PERSONA AÑADIDA NO ES UN PROFESOR, ES UN: " + funcion, Toast.LENGTH_SHORT).show();
                //}

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
        } else {
        }

    }



    /*******************************************************ESCANER QR*******************************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelaste el escaneo", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                //wv.setWebViewClient(new MyWebViewClient());
                //wv.loadUrl(result.getContents());
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    org.jsoup.nodes.Document doc = Jsoup.connect(URL).get();
                    String title = doc.title();
                    Elements links = doc.select(CardInfo.css_query);
                    //More patterns at https://try.jsoup.org/~LGB7rk_atM2roavV0d-czMt3J_g
                    //Selector info at https://jsoup.org/apidocs/index.html?org/jsoup/select/Selector.html

                    //builder.append(title).append("\n");


                    ///for (Element link : links) {
                    for (int i=0; i< links.size(); i++){
                       /* builder.append("\n").append("Link : ").append(link.attr("href"))
                                .append("\n").append("Text : ").append(link.text());*/
                        org.jsoup.nodes.Element e = links.get(i);
                        String token = null;
                        if (e!=null) token = e.text();
                        if ((token!=null) && (e.text().length()>0)) {
                            //builder.append(link.text()).append("\n");
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
                                funcion = links.get(i).text();
                            }
                        }
                    }
                    builder.append(ci.toString());


                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(builder.toString());
                    }
                });
            }
        }).start();
    }

/*****************************************************FIN ESCANER QR*****************************************************************/

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
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("Profesor con Nº de identificación: ").append(toDec(id)).append('\n');

        count++;
        //listIdentificador[sum] = new String(di.getListIdentificador());
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


}
