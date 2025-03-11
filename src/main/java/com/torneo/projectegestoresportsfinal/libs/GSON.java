package com.torneo.projectegestoresportsfinal.libs;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.torneo.projectegestoresportsfinal.classes.Tournament;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Map;

public class GSON {
    private final Gson gson;

    public GSON() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    public void escriuMapAFitxerJson(String ruta, Map<String, Tournament> tournamentsMap) throws IOException {
        try (FileWriter writer = new FileWriter(ruta)) {
            gson.toJson(tournamentsMap, writer);
        }
    }

    public Map<String, Tournament> retornaFitxerJsonAMap(String fitxerJson) throws IOException {
        try (FileReader reader = new FileReader(fitxerJson)) {
            Type mapType = new TypeToken<Map<String, Tournament>>(){}.getType();
            return gson.fromJson(reader, mapType);
        }
    }
}
