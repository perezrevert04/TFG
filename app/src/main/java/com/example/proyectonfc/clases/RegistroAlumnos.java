package com.example.proyectonfc.clases;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.proyectonfc.CreacionParte;
import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.parser.NdefMessageParser;
import com.example.proyectonfc.record.ParsedNdefRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class RegistroAlumnos extends AppCompatActivity {

    private ArrayList<String> listaIdentificadores = new ArrayList<>();
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView text;
    private static byte[] identificador;
    private String asignatura;
    private String nombre;
    private String titulacion;
    private String grupo;
    private String curso;
    private String gestoria;
    private String idioma;
    private String duracion;
    private String horaInicio;
    private String aula;
    private String identificadorTemporal;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_alumnos);
        text = (TextView) findViewById(R.id.text);

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

        Button btnNext = (Button) findViewById(R.id.buttonNext);
        btnNext.setOnClickListener( view -> nextActivity() );

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

    private void nextActivity() {
        Intent intent = new Intent(this, CreacionParte.class);

        intent.putExtra("listaIdentificadores", listaIdentificadores);

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

        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && listaIdentificadores.size() > 0) {
            backAlert();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancelar parte");
        builder.setMessage("Si vuelves atrás perderás los datos de los alumnos que se hayan identificado. ¿Estás seguro?");

        builder.setNegativeButton("No", (dialog, which) -> {});
        builder.setPositiveButton("Sí", (dialog, which) -> {
            prepareBiometricPrompt( () -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return null;
            });
            biometricPrompt.authenticate(promptInfo);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /******************************************************LECTOR NFC********************************************************************/

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
        sb.append(toDec(id)).append('\n');

        SQLiteDatabase db=dataBase.getReadableDatabase();

        //select * from usuarios


        Cursor cursor=db.rawQuery("SELECT * FROM ALUMNO WHERE id = '"+asignatura+toDec(identificador)+"'", null);

        while (cursor.moveToNext()){
            identificadorTemporal = cursor.getString(0);
        }

        if (identificadorTemporal == null){
            identificadorTemporal = "1234567890123456789012345678901234567890123456789012345678901234567890";
        }else {
            identificadorTemporal = identificadorTemporal.substring(3);
        }

        String identificadorFijo = String.valueOf(toDec(identificador));
        if(identificadorTemporal.equals(identificadorFijo)){

            try {
                dataBase.agregarAlumnoTemporal(String.valueOf(toDec(identificador)));
            }catch (Exception e){
                Log.e("AppLog", "El alumno " + identificador + " ya figura en la base de datos.");
            }
            String idAux = String.valueOf(toDec(identificador));
            if (listaIdentificadores.contains(idAux)) {
                Toast.makeText(getApplicationContext(), "ESTE ALUMNO YA TIENE REGISTRADA LA ASISTENCIA", Toast.LENGTH_SHORT).show();
            } else {
                listaIdentificadores.add(idAux);
                Toast.makeText(getApplicationContext(), "Fichaje realizado.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "EL ALUMNO CON IDENTIFIFCADOR: "+String.valueOf(toDec(identificador))+"NO ESTÁ DADO DE ALTA", Toast.LENGTH_SHORT).show();
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
    /*****************************************************FIN LECTOR NFC*****************************************************************/

    private void prepareBiometricPrompt(Callable<Void> method) {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                try {
                    method.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación")
                .setSubtitle("Identifíquese para cancelar el parte.")
                .setDeviceCredentialAllowed(true)
                .build();
    }
}
