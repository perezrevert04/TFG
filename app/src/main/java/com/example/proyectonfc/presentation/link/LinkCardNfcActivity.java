package com.example.proyectonfc.presentation.link;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonfc.R;
import com.example.proyectonfc.model.Person;
import com.example.proyectonfc.util.nfc.Nfc;

public class LinkCardNfcActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_card_nfc);

        person = (Person) getIntent().getSerializableExtra(Person.CARD_INFO);

        prepareNfc();
    }

    /*** INICIO LECTOR NFC ***/

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    private void prepareNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toast.makeText(this, "No dispone de nfc, no puede completar el proceso...", Toast.LENGTH_SHORT).show();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
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
            String studentId = Nfc.tagToString(tag);
            person.setIdentifier(studentId);

            Intent in = new Intent(this, LinkBiometricPromptActivity.class);
            in.putExtra(Person.CARD_INFO, person);
            startActivity(in);
        }
    }

    /*** FIN LECTOR NFC ***/
}