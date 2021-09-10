package com.codetreatise.controller;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.config.StageManager;
import com.codetreatise.repository.AdherentRepository;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.view.FxmlView;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

@Controller
public class AdherentsController implements Initializable {
	@FXML
	private TableView<Adherent> adherentTable;
	@FXML
	private TableColumn<Adherent, String> aderentNomTab;
	@FXML
	private TableColumn<Adherent, String> adherentPrenomTab;
	@FXML
	private ComboBox<String> adherentComboboxFiltre;
	@FXML
	private TextField adherentFiltre;
	@FXML
	private Label adherentIdLabel;
	@FXML
	private Label adherentNomLael;
	@FXML
	private Label adherentPrenomLabel;
	@FXML
	private Label AdherentDateNaissLabel;
	@FXML
	private Label adherentFonctionLabel;
	@FXML
	private Label adherentStatutLabel;
	@FXML
	private Label adherentLieuNaissLabel;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private AdherentRepository adherentRepository;

	@Autowired
	MethodUtilitaire methodUtilitaire;

	private ObservableList<Adherent> adherentList = FXCollections.observableArrayList();

	@Autowired
	private AdherentEditDialogController dialog;
	private boolean editbuttonclic = false;

	public boolean isEditButtonClick() {
		return editbuttonclic;
	}

	public void setIsEditButtonClick(boolean value) {
		this.editbuttonclic = value;
	}

	// Event Listener on TextField[#adherentFiltre].onKeyReleased
	@FXML
	public void adherentFiltrePressed(KeyEvent event) {
		filteredTable(event);
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleadherentFiltreClick(ActionEvent event) {
		loadDataOnTable();
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleAddClick(ActionEvent event) {
		System.out.println("Hello world");
		stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.ADHERENTADD);
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleEditClick(ActionEvent event) {
		Adherent selectedPerson = adherentTable.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.ADHERENTADD);
			dialog.showPersonDetails(selectedPerson);
			editbuttonclic = true;
		}

		else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Aucun adhérent sélectionné", "Aucun adhérent sélectionné",
					"Sélectionné un adhérent puis essayer de nouveau");
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleDeleteClick(ActionEvent event) throws UnknownHostException, ClassNotFoundException, IOException {
		Adherent selectedAdherent = adherentTable.getSelectionModel().getSelectedItem();
		if (selectedAdherent != null) {
			if (MethodUtilitaire.confirmationDialog(selectedAdherent, "Supprimer l'adhérent",
					"Un adhérent sera supprimer",
					"Supprimer l'adhérent " + selectedAdherent.getNom() + " " + selectedAdherent.getPrenom())) {
				adherentTable.getItems().remove(selectedAdherent);
				adherentRepository.delete(selectedAdherent);
				MethodUtilitaire.saveAlert(selectedAdherent, "Suppression de l'adhérent réussi", "L'adhérent "
						+ selectedAdherent.getNom() + " " + selectedAdherent.getPrenom() + " à été supprimer !");
				methodUtilitaire
						.LogFile(
								"Suppression d'un adherent", selectedAdherent.getIdentifiant() + "-"
										+ selectedAdherent.getNom() + " " + selectedAdherent.getPrenom(),
								MethodUtilitaire.deserializationUser());
			}

		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Aucun adhérent sélectionné", "Aucun adhérent sélectionné",
					"Sélectionné un adhérent puis essayer de nouveau");
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleAdvancedClick(ActionEvent event) {
		stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.MANAGEADHERENT);
	}

	private void filteredTable(KeyEvent event) {
		FilteredList<Adherent> filteredadherent = new FilteredList<Adherent>(adherentList, e -> true);
		adherentFiltre.setOnKeyReleased(e -> {
			adherentFiltre.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredadherent.setPredicate((Predicate<? super Adherent>) adherent -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String newValueFilter = newValue.toLowerCase();
					if (adherent.getIdentifiant().toString().contains(newValueFilter)) {
						return true;
					} else if (adherent.getNom().toLowerCase().contains(newValueFilter)) {
						return true;
					} else if (adherent.getPrenom().toLowerCase().contains(newValueFilter)) {
						return true;
					}
					return false;
				});
			});
		});

		SortedList<Adherent> sortedList = new SortedList<Adherent>(filteredadherent);
		sortedList.comparatorProperty().bind(adherentTable.comparatorProperty());
		adherentTable.setItems(sortedList);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		adherentComboboxFiltre.getItems().addAll("Actif", "Inactif", "All");
		adherentComboboxFiltre.getSelectionModel().selectFirst();
		setColumProperties();
		adherentTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Adherent>() {
			@Override
			public void changed(ObservableValue<? extends Adherent> observable, Adherent oldValue, Adherent newValue) {
				showPersonDetails(newValue);
			}
		});
		loadDataOnTable();
	}

	private void setColumProperties() {
		aderentNomTab.setCellValueFactory(new PropertyValueFactory<>("nom"));
		adherentPrenomTab.setCellValueFactory(new PropertyValueFactory<>("prenom"));
	}

	public void loadDataOnTable() {
		String situation = adherentComboboxFiltre.getSelectionModel().getSelectedItem();
		adherentList.clear();
		if (situation == "All") {
			try {
				adherentList.addAll(adherentRepository.findBySituation("Actif"));
				adherentList.addAll(adherentRepository.findBySituation("Inactif"));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {

			}
		} else {
			try {
				adherentList.addAll(adherentRepository.findBySituation(situation));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		adherentTable.setItems(adherentList);
	}

	private void showPersonDetails(Adherent adherent) {
		if (adherent != null) {
			// Fill the labels with info from the person object.
			adherentLieuNaissLabel.setText(adherent.getLieuNaiss());
			adherentNomLael.setText(adherent.getNom());
			adherentPrenomLabel.setText(adherent.getPrenom());
			AdherentDateNaissLabel.setText(adherent.getDateNaiss().toString());
			adherentFonctionLabel.setText(adherent.getFonction());
			adherentStatutLabel.setText(adherent.getSituation());
			if(adherent.getSituation().equals("Actif")) {
				adherentStatutLabel.setTextFill(Color.web("#00FF00"));
			}else {
				adherentStatutLabel.setTextFill(Color.RED);
				System.out.println(adherent.getSituation());
			}
			adherentIdLabel.setText(adherent.getIdentifiant().toString());
		} else {
			// Person is null, remove all the text.
			adherentNomLael.setText("");
			adherentPrenomLabel.setText("");
			AdherentDateNaissLabel.setText("");
			adherentFonctionLabel.setText("");
			adherentStatutLabel.setText("");
			adherentLieuNaissLabel.setText("");
			adherentIdLabel.setText("");
		}
	}
}
