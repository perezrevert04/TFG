package com.example.proyectonfc.presentation.teacher.report;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.proyectonfc.R;
import com.example.proyectonfc.db.DataBase;
import com.example.proyectonfc.model.Group;
import com.example.proyectonfc.model.Subject;
import com.example.proyectonfc.presentation.MainActivity;
import com.example.proyectonfc.util.biometric.Biometry;
import com.example.proyectonfc.util.nearby.Advertise;
import com.example.proyectonfc.util.nearby.NearbyCode;
import com.example.proyectonfc.util.nearby.NearbyCouple;
import com.example.proyectonfc.util.nfc.Nfc;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegistroAlumnos extends AppCompatActivity {

    private Advertise advertise;

    private ArrayList<String> listaIdentificadores = new ArrayList<>();
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView text;

    private Biometry biometry;

    private boolean open = true;

    private List<String> androidIds;

    private Subject subject;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_alumnos);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        androidIds = new ArrayList<>();

        biometry = new Biometry(this, "Autenticación", "Identifíquese para cancelar el parte.");

        text = findViewById(R.id.text);

        subject = (Subject) getIntent().getSerializableExtra("SubjectObject");
        group = (Group) getIntent().getSerializableExtra("GroupObject");

        String nickname = "\n[" + subject.getCode() + "]\n" + subject.getName() + "\n(" + group.getName() + ", " + group.getClassroom() + ")\n";
        advertise = new Advertise(this, nickname, getApplicationContext().getPackageName(), payloadCallback);

        ProgressBar progressBar = findViewById(R.id.progressBar2);
        TextView nearbyStatus = findViewById(R.id.textView16);
        advertise.addObserver( () -> {
            nearbyStatus.setText("Parte abierto correctamente.");
            progressBar.setVisibility(View.INVISIBLE);
        });

        Button btnNext = findViewById(R.id.buttonNext);
        btnNext.setOnClickListener( view -> nextActivity() );

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "No NFC", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1000);
        }

    }

    private void nextActivity() {
        open = false;

        Intent intent = new Intent(this, CreacionParte.class);

        intent.putExtra("listaIdentificadores", listaIdentificadores);

        intent.putExtra("asignatura", subject.getCode());
        intent.putExtra("nombre", subject.getName());
        intent.putExtra("titulacion", subject.getDegree());
        intent.putExtra("grupo", group.getName());
        intent.putExtra("curso", subject.getSchoolYear());
        intent.putExtra("gestoria", subject.getDepartment());
        intent.putExtra("idioma", subject.getLanguage());
        intent.putExtra("duracion", subject.getDuration());
        intent.putExtra("horaInicio", group.getHour());
        intent.putExtra("aula", group.getClassroom());

        startActivityForResult(intent, CreacionParte.REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CreacionParte.REQ_CODE && resultCode == Activity.RESULT_OK) {
            advertise.stop();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

            if (data != null) {
                String filename = data.getStringExtra("filename");
                openFile(filename);
            }
        }
    }

    private void openFile(String filename) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        String path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "ParteFirmasUPV").getPath();
        File fileIn = new File(path + "/" + filename);
        Uri uri = Uri.fromFile(fileIn);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
            biometry.authenticate( () -> {
                advertise.stop();
                open = false;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return null;
            });
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }

        advertise.start();
        open = true;
    }

    /*** INICIO LECTOR NFC ***/

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
            String studentId = Nfc.tagToString(tag);
            dumpTagData(studentId);
        }
    }

    private void dumpTagData(String studentId) {
        final DataBase dataBase = new DataBase(getApplicationContext());

        SQLiteDatabase db=dataBase.getReadableDatabase();

        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM ALUMNO WHERE id = '" + subject.getCode()+studentId + "'", null);

        if (cursor.getCount() == 1) {

            if (listaIdentificadores.contains(studentId)) {
                Toast.makeText(getApplicationContext(), NearbyCode.DUPLICATED.getMsg(), Toast.LENGTH_SHORT).show();
            } else {
                listaIdentificadores.add(studentId);
                text.setText(studentId);
                Toast.makeText(getApplicationContext(), NearbyCode.SUCCESS.getMsg(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), NearbyCode.UNREGISTERED.getMsg(), Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    /*** FIN LECTOR NFC ***/

    /*** INICIO NEARBY ADVERTISE ***/
    private PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
            // This always gets the full data of the payload. Will be null if it's not a BYTES payload. You can check the payload type with payload.getType().
            byte[] receivedBytes = payload.asBytes();
            NearbyCouple couple = NearbyCouple.Companion.getCouple(receivedBytes);
            NearbyCode code = add(couple);
            advertise.sendPayload(endpointId, code.getMsg().getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately after the call to onPayloadReceived().
        }
    };

    private NearbyCode add(NearbyCouple couple) {
        NearbyCode code;
        String identifier = couple.getMsg();
        String androidId = couple.getAndroidId();
        final DataBase dataBase = new DataBase(getApplicationContext());

        SQLiteDatabase db = dataBase.getReadableDatabase();
        //select * from usuarios
        Cursor cursor = db.rawQuery("SELECT * FROM ALUMNO WHERE id = '" + subject.getCode()+identifier + "'", null);

        if (!open) {
            code = NearbyCode.CLOSING;
        } else if (cursor.getCount() < 1 ) {
            code = NearbyCode.UNREGISTERED;
        } else if (listaIdentificadores.contains(identifier)) {
            code = NearbyCode.DUPLICATED;
        } else if (androidIds.contains(androidId)) {
            code = NearbyCode.ANDROID_ID;
        } else {
            androidIds.add(androidId);
            listaIdentificadores.add(identifier);
            code = NearbyCode.SUCCESS;
            text.setText(identifier);
        }

        return code;
    }
    /*** FIN NEARBY ADVERTISE ***/

}
