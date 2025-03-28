package com.torneo.projectegestoresportsfinal.controladors;

import com.torneo.projectegestoresportsfinal.classes.Participant;
import com.torneo.projectegestoresportsfinal.classes.Tournament;
import com.torneo.projectegestoresportsfinal.classes.TournamentManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

public class MainController {

    @FXML private TextField tournamentNameField;
    @FXML private DatePicker tournamentDatePicker;
    @FXML private TextField gameField;
    @FXML private TextField formatField;
    @FXML private TextField prizesField;
    @FXML private TableView<Tournament> tournamentTable;
    @FXML private TableView<Participant> participantTable;
    @FXML private TextField participantNameField;
    @FXML private TextField participantNicknameField;
    @FXML private TextField participantTeamField;
    @FXML private TextField participantScoreField;

    @FXML private TextField searchTournamentField;
    @FXML private TextField searchParticipantField;

    private TournamentManager tournamentManager;
    private ObservableList<Tournament> observableTournaments;

    @FXML
    public void initialize() {
        tournamentManager = TournamentManager.getInstance();
        observableTournaments = tournamentManager.getAllTournaments();

        // Configuración de columnas para la tabla de torneos
        TableColumn<Tournament, String> colName = new TableColumn<>("Nombre");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Tournament, LocalDate> colDate = new TableColumn<>("Fecha");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Tournament, String> colGame = new TableColumn<>("Juego");
        colGame.setCellValueFactory(new PropertyValueFactory<>("game"));
        TableColumn<Tournament, String> colFormat = new TableColumn<>("Formato");
        colFormat.setCellValueFactory(new PropertyValueFactory<>("format"));
        TableColumn<Tournament, String> colPrizes = new TableColumn<>("Premios");
        colPrizes.setCellValueFactory(new PropertyValueFactory<>("prizes"));
        tournamentTable.getColumns().setAll(colName, colDate, colGame, colFormat, colPrizes);
        tournamentTable.setItems(observableTournaments);

        // Configuración de columnas para la tabla de participantes
        TableColumn<Participant, String> pName = new TableColumn<>("Nombre");
        pName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Participant, String> pNickname = new TableColumn<>("Nickname");
        pNickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        TableColumn<Participant, String> pTeam = new TableColumn<>("Equipo");
        pTeam.setCellValueFactory(new PropertyValueFactory<>("team"));
        TableColumn<Participant, Integer> pScore = new TableColumn<>("Puntuación");
        pScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        participantTable.getColumns().setAll(pName, pNickname, pTeam, pScore);

        // Al seleccionar un torneo se muestran sus participantes
        tournamentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                participantTable.setItems(FXCollections.observableArrayList(newSelection.getParticipants()));
            } else {
                participantTable.setItems(FXCollections.observableArrayList());
            }
        });

        // Añadir listener para cargar datos del participante al seleccionarlo
        participantTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                loadParticipantData();
            }
        });
    }

    @FXML
    private void addTournament() {
        try {
            String name = tournamentNameField.getText();
            LocalDate date = tournamentDatePicker.getValue();
            String game = gameField.getText();
            String format = formatField.getText();
            String prizes = prizesField.getText();
            if(name.isEmpty() || date == null || game.isEmpty() || format.isEmpty() || prizes.isEmpty()){
                showError("Todos los campos del torneo son obligatorios.");
                return;
            }
            Tournament tournament = new Tournament(name, date, game, format, prizes);
            tournamentManager.addTournament(tournament);
            observableTournaments.setAll(tournamentManager.getAllTournaments());
            clearTournamentFields();
        } catch(Exception e) {
            showError("Error al agregar torneo: " + e.getMessage());
        }
    }

    @FXML
    private void deleteTournament() {
        Tournament selectedTournament = tournamentTable.getSelectionModel().getSelectedItem();
        if(selectedTournament != null) {
            tournamentManager.removeTournament(selectedTournament.getName());
            observableTournaments.setAll(tournamentManager.getAllTournaments());
            participantTable.setItems(FXCollections.observableArrayList());
        } else {
            showError("Selecciona un torneo para eliminar.");
        }
    }

    @FXML
    private void addParticipant() {
        try {
            String name = participantNameField.getText();
            String nickname = participantNicknameField.getText();
            String team = participantTeamField.getText();
            int score = Integer.parseInt(participantScoreField.getText());
            if(name.isEmpty() || nickname.isEmpty() || team.isEmpty()){
                showError("Todos los campos del participante son obligatorios.");
                return;
            }
            Participant participant = new Participant(name, nickname, team, score);
            Tournament selectedTournament = tournamentTable.getSelectionModel().getSelectedItem();
            if(selectedTournament != null) {
                tournamentManager.addParticipant(selectedTournament.getName(), participant);
                participantTable.setItems(FXCollections.observableArrayList(selectedTournament.getParticipants()));
                clearParticipantFields();
            } else {
                showError("Selecciona un torneo para agregar participante.");
            }
        } catch(NumberFormatException e) {
            showError("La puntuación debe ser un número.");
        } catch(Exception e) {
            showError("Error al agregar participante: " + e.getMessage());
        }
    }

    @FXML
    private void deleteParticipant() {
        Tournament selectedTournament = tournamentTable.getSelectionModel().getSelectedItem();
        Participant selectedParticipant = participantTable.getSelectionModel().getSelectedItem();
        if(selectedTournament != null && selectedParticipant != null) {
            tournamentManager.removeParticipant(selectedTournament.getName(), selectedParticipant);
            participantTable.setItems(FXCollections.observableArrayList(selectedTournament.getParticipants()));
        } else {
            showError("Selecciona un torneo y un participante para eliminar.");
        }
    }

    @FXML
    private void sortParticipants() {
        Tournament selectedTournament = tournamentTable.getSelectionModel().getSelectedItem();
        if(selectedTournament != null) {
            participantTable.setItems(tournamentManager.sortParticipants(selectedTournament.getName()));
        } else {
            showError("Selecciona un torneo para ordenar participantes.");
        }
    }

    @FXML
    private void undoAction() {
        String action = tournamentManager.undoLastAction();
        if(action != null) {
            showError("Acción deshecha: " + action);
            observableTournaments.setAll(tournamentManager.getAllTournaments());
        } else {
            showError("No hay acciones para deshacer.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearTournamentFields() {
        tournamentNameField.clear();
        tournamentDatePicker.setValue(null);
        gameField.clear();
        formatField.clear();
        prizesField.clear();
    }

    private void clearParticipantFields() {
        participantNameField.clear();
        participantNicknameField.clear();
        participantTeamField.clear();
        participantScoreField.clear();
    }

    // Función de búsqueda para los torneos
    @FXML
    public void searchTournament() {
        String query = searchTournamentField.getText().toLowerCase();
        ObservableList<Tournament> filteredTournaments = FXCollections.observableArrayList();

        for (Tournament tournament : tournamentManager.getAllTournaments()) {
            if (tournament.getName().toLowerCase().contains(query)) {
                filteredTournaments.add(tournament);
            }
        }

        tournamentTable.setItems(filteredTournaments);
    }

    // Función de búsqueda para los participantes
    @FXML
    public void searchParticipant() {
        String query = searchParticipantField.getText().toLowerCase();
        ObservableList<Participant> filteredParticipants = FXCollections.observableArrayList();

        for (Tournament tournament : tournamentManager.getAllTournaments()) {
            for (Participant participant : tournament.getParticipants()) {
                if (participant.getName().toLowerCase().contains(query)) {
                    filteredParticipants.add(participant);
                }
            }
        }

        participantTable.setItems(filteredParticipants);
    }

    @FXML
    private void modifyParticipant() {
        try {
            Tournament selectedTournament = tournamentTable.getSelectionModel().getSelectedItem();
            Participant selectedParticipant = participantTable.getSelectionModel().getSelectedItem();
            
            if(selectedTournament == null || selectedParticipant == null) {
                showError("Selecciona un torneo y un participante para modificar.");
                return;
            }

            String name = participantNameField.getText();
            String nickname = participantNicknameField.getText();
            String team = participantTeamField.getText();
            int score = Integer.parseInt(participantScoreField.getText());

            if(name.isEmpty() || nickname.isEmpty() || team.isEmpty()) {
                showError("Todos los campos del participante son obligatorios.");
                return;
            }

            // Crear nuevo participante con los datos actualizados
            Participant updatedParticipant = new Participant(name, nickname, team, score);
            
            // Actualizar el participante en el torneo
            tournamentManager.removeParticipant(selectedTournament.getName(), selectedParticipant);
            tournamentManager.addParticipant(selectedTournament.getName(), updatedParticipant);
            
            // Actualizar la tabla
            participantTable.setItems(FXCollections.observableArrayList(selectedTournament.getParticipants()));
            clearParticipantFields();
            
        } catch(NumberFormatException e) {
            showError("La puntuación debe ser un número.");
        } catch(Exception e) {
            showError("Error al modificar participante: " + e.getMessage());
        }
    }

    @FXML
    private void loadParticipantData() {
        Participant selectedParticipant = participantTable.getSelectionModel().getSelectedItem();
        if(selectedParticipant != null) {
            participantNameField.setText(selectedParticipant.getName());
            participantNicknameField.setText(selectedParticipant.getNickname());
            participantTeamField.setText(selectedParticipant.getTeam());
            participantScoreField.setText(String.valueOf(selectedParticipant.getScore()));
        }
    }
}
