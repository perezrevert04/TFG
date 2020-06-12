package com.example.proyectonfc.clases;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
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


public class IdentificacionProfesor extends AppCompatActivity {

    Button btnSiguiente;
    Button btnVerDatos;

    private static byte[] identificador = null;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String dni;
    private String nombre;
    private String identificadorBuscado;
    private String identificadorModificado;
    DataBase dataBase;
    private TextView text;
    private TextView textIdentificador;
    private TextView textDni;
    private TextView textNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identificacion_profesor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        text = (TextView) findViewById(R.id.text);

        dataBase = new DataBase(getApplicationContext(), "DB6.db", null, 1);


        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identificador == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(IdentificacionProfesor.this);
                    builder.setTitle("Primero debe de acercar la tarjeta para leer su identificador");
                    builder.setMessage("");
                    builder.setNeutralButton("¡Entendido!", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {

                    try {
                        consultarProfesor();
                        identificadorModificado = String.valueOf(toDec(identificador));
                        if (identificadorBuscado.equals(identificadorModificado)) {
                            Intent intent = new Intent(v.getContext(), AsignaturasProfesor.class);
                            intent.putExtra("IDENTIFICADOR", identificadorBuscado);
                            intent.putExtra("NOMBREPROFESOR", nombre);
                            intent.putExtra("DNIPROFESOR", dni);
                            startActivityForResult(intent, 0);
                        } else {
                        }
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(IdentificacionProfesor.this);
                        builder.setTitle("Esta persona no está dada de alta como profesor");
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
                }
            }

        });

        btnVerDatos = (Button) findViewById(R.id.btnVerDatos);
        btnVerDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identificador == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(IdentificacionProfesor.this);
                    builder.setTitle("Primero debe de acercar la tarjeta para leer su identificador");
                    builder.setMessage("");
                    builder.setNeutralButton("¡Entendido!", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    identificadorModificado = String.valueOf(toDec(identificador));
                    try {
                        consultarProfesor();
                        if (identificadorBuscado.equals(identificadorModificado)) {
                            consultarProfesor();
                            identificadorBuscado = null;
                        } else {
                        }
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(IdentificacionProfesor.this);
                        builder.setTitle("Esta persona no está dada de alta como profesor");
                        builder.setMessage("");
                        builder.setNeutralButton("¡Entendido!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        identificadorBuscado = null;
                    }
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
                new Intent(this, IdentificacionProfesor.class)
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

    //private void showWirelessSettings() {
    //  Toast.makeText(this, "You need enable NFC", Toast.LENGTH_SHORT).show();
    //Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
    //startActivity(intent);
    //}

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("Profesor con Nº de identificación: ").append(toDec(id)).append('\n');

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
private void consultarProfesor() {
    SQLiteDatabase db=dataBase.getReadableDatabase();



    //select * from profesor
    Cursor cursor=db.rawQuery("SELECT * FROM PROFESOR WHERE id LIKE"+"'%"+toDec(identificador)+"'", null);

    while (cursor.moveToNext()){
        identificadorBuscado = cursor.getString(0);
        dni = cursor.getString(1);
        nombre = cursor.getString(2);


    }


    identificadorBuscado = identificadorBuscado.substring(3);

    textIdentificador = (TextView) findViewById(R.id.textIdentificador);
    textIdentificador.setText(identificadorBuscado);
    textDni = (TextView) findViewById(R.id.textDni);
    textDni.setText(dni);
    textNombre = (TextView) findViewById(R.id.textNombre);
    textNombre.setText(nombre);


}

}
