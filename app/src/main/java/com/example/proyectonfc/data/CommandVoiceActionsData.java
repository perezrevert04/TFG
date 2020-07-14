package com.example.proyectonfc.data;

import android.util.Log;

import com.example.proyectonfc.presentation.MainActivity;
import com.example.proyectonfc.presentation.teacher.management.ManagementActivity;
import com.example.proyectonfc.presentation.teacher.management.consults.ShowReportsActivity;
import com.example.proyectonfc.presentation.teacher.management.subjects.Configuracion;
import com.example.proyectonfc.presentation.teacher.report.AsignaturasProfesor;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import java.util.HashMap;
import java.util.Map;

public class CommandVoiceActionsData {

    private static final double MIN_MATCH = 0.75;
    private Map<String, Class<?>> actions;

    public CommandVoiceActionsData() {
        actions = new HashMap<>();

        // Ir a la pantalla principal
        actions.put("pantallaprincipal", MainActivity.class);
        actions.put("inicio", MainActivity.class);

        // Iniciar parte
        actions.put("iniciarparte", AsignaturasProfesor.class);

        // Consultar parte
        actions.put("consultarpartes", ShowReportsActivity.class);

        // Ver mis datos
        actions.put("vermisdatos", ManagementActivity.class);

        // Ver asignaturas
        actions.put("vermisasignaturas", Configuracion.class);
    }

    public Class<?> processData(String data) {
        String key = data.replace(" ", "").toLowerCase();
        Class<?> cl = actions.get(key);

        if (cl == null) cl = getMoreSimilar(key);

        return cl;
    }

    private Class<?> getMoreSimilar(String key) {
        Class<?> cl = null;
        double match = MIN_MATCH;

        double res;

        Log.d("AppLog", "\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        for (Map.Entry<String, Class<?>> entry : actions.entrySet()) {
            res = compareDistance(key, entry.getKey());

            if (res > match) {
                match = res;
                cl = actions.get( entry.getKey() );
            }
        }

        return match > MIN_MATCH ? cl : null;
    }


    private double compareDistance(String s1, String s2) {
        JaroWinklerSimilarity jw = new JaroWinklerSimilarity();
        return jw.apply(s1, s2);
    }

}
