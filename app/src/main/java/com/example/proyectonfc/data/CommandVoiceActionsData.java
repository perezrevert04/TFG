package com.example.proyectonfc.data;

import com.example.proyectonfc.presentation.teacher.management.consults.ShowReportsActivity;
import com.example.proyectonfc.presentation.teacher.report.AsignaturasProfesor;

import java.util.HashMap;
import java.util.Map;

public class CommandVoiceActionsData {

    private Map<String, Class> actions;

    public CommandVoiceActionsData() {
        actions = new HashMap<>();

        // Iniciar parte
        actions.put("iniciarparte", AsignaturasProfesor.class);
        actions.put("iniciaparte", AsignaturasProfesor.class);
        actions.put("iniciarparque", AsignaturasProfesor.class);

        actions.put("iniciarpartes", AsignaturasProfesor.class);
        actions.put("iniciapartes", AsignaturasProfesor.class);
        actions.put("iniciarparques", AsignaturasProfesor.class);

        // Consultar parte
        actions.put("consultarparte", ShowReportsActivity.class);
        actions.put("consultaparte", ShowReportsActivity.class);
        actions.put("consultarparque", ShowReportsActivity.class);

        actions.put("consultarpartes", ShowReportsActivity.class);
        actions.put("consultapartes", ShowReportsActivity.class);
        actions.put("consultarparques", ShowReportsActivity.class);
    }

    public Class processData(String data) {
        String key = data.replace(" ", "").toLowerCase();
        Class cl = actions.get(key);

        if (cl == null) {}

        return cl;
    }


}
