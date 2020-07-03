package com.example.proyectonfc.logic.nfc;

import android.nfc.Tag;

public class Nfc {

    public static String tagToString(Tag tag) {
        byte[] bytes = tag.getId();

        long result = 0;
        long factor = 1;

        for (byte aByte : bytes) {
            long value = aByte & 0xffL;
            result += value * factor;
            factor *= 256L;
        }

        return String.valueOf(result);
    }
}
