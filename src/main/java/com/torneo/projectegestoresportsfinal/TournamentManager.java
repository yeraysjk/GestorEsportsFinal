package com.torneo.projectegestoresportsfinal;

import java.io.IOException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TournamentManager {
    private static TournamentManager instance;
    private Map<String, Tournament> tournaments;
    private Queue<Participant> globalWaitingList;
    private Stack<String> undoStack;
    private static final String JSON_FILE_PATH = "tournaments.json"; // Ruta del archivo JSON
    private GSON gson;

    private TournamentManager() {
        tournaments = new HashMap<>();
        globalWaitingList = new LinkedList<>();
        undoStack = new Stack<>();
        gson = new GSON();
        loadTournamentsFromJson();
    }

    public static TournamentManager getInstance() {
        if(instance == null) {
            instance = new TournamentManager();
        }
        return instance;
    }

    public void addTournament(Tournament tournament) throws Exception {
        if(tournaments.containsKey(tournament.getName())) {
            throw new Exception("El torneo ya existe.");
        }
        tournaments.put(tournament.getName(), tournament);
        undoStack.push("Agregar torneo: " + tournament.getName());
        saveTournamentsToJson();
    }

    public void removeTournament(String tournamentName) {
        tournaments.remove(tournamentName);
        undoStack.push("Eliminar torneo: " + tournamentName);
        saveTournamentsToJson();
    }


    public ObservableList<Tournament> getAllTournaments() {
        return FXCollections.observableArrayList(tournaments.values());
    }

    public void loadTournamentsFromJson() {
        try {
            Map<String, Tournament> loaded = gson.retornaFitxerJsonAMap(JSON_FILE_PATH);
            if(loaded != null) {
                tournaments.putAll(loaded);
            }
        } catch(IOException e) {
            System.out.println("No se pudo cargar el archivo JSON o está vacío.");
        }
    }

    public void saveTournamentsToJson() {
        try {
            gson.escriuMapAFitxerJson(JSON_FILE_PATH, tournaments);
        } catch(IOException e) {
            System.out.println("Error al guardar el archivo JSON.");
        }
    }

    public void addParticipant(String tournamentName, Participant participant) throws Exception {
        Tournament t = tournaments.get(tournamentName);
        if(t == null) {
            throw new Exception("Torneo no encontrado.");
        }
        t.getParticipants().add(participant);
        undoStack.push("Agregar participante: " + participant.getName() + " al torneo " + tournamentName);
        saveTournamentsToJson();
    }

    public void removeParticipant(String tournamentName, Participant participant) {
        Tournament t = tournaments.get(tournamentName);
        if(t != null) {
            t.getParticipants().remove(participant);
            undoStack.push("Eliminar participante: " + participant.getName() + " del torneo " + tournamentName);
            saveTournamentsToJson();
        }
    }

    public ObservableList<Participant> sortParticipants(String tournamentName) {
        Tournament t = tournaments.get(tournamentName);
        if(t == null) return FXCollections.observableArrayList();
        List<Participant> list = new ArrayList<>(t.getParticipants());
        list.sort(Comparator.comparingInt(Participant::getScore).reversed());
        undoStack.push("Ordenar participantes del torneo " + tournamentName);
        saveTournamentsToJson();
        return FXCollections.observableArrayList(list);
    }

    public String undoLastAction() {
        if(!undoStack.isEmpty()) {
            return undoStack.pop();
        }
        return null;
    }
}
