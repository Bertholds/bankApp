package com.codetreatise.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.repository.AdherentRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.AdherentServiceImpl;
import com.codetreatise.service.impl.CompteEpargneServiceImplement;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

@Controller
public class ManageAdherentController implements Initializable {
	@FXML
	private TableView<Adherent> adherentTable;
	@FXML
	private TableColumn<Adherent, String> aderentNomTab;
	@FXML
	private TableColumn<Adherent, String> adherentPrenomTab;
	@FXML
	private TextField adherentFiltre;
	@FXML
	private Label adherentIdLabel;
	@FXML
	private Label adherentNomLael;
	@FXML
	private Label adherentPrenomLabel;
	@FXML
	private Label cniLabel;
	@FXML
	private Label adherentLieuNaissLabel;
	@FXML
	private Label adherentFonctionLabel;
	@FXML
	private Label adherentStatutLabel;
	@FXML
	private Button activeButton;
	@FXML
	private Button btnDelete;
	@FXML
	private FontAwesomeIconView activeToggle;

	@Autowired
	private AdherentsController adherentsController;
	@Autowired
	private CompteEpargneRepository compteEpargneRepository;
	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement;
	@Autowired
	private AdherentRepository adherentRepository;
	@Autowired
	private AdherentServiceImpl adherentServiceImpl;

	@Autowired
	private HomeController homeController;
	
	URL location = SettingController.class.getProtectionDomain().getCodeSource().getLocation();

	private ObservableList<Adherent> adherentList = FXCollections.observableArrayList();

	// Event Listener on TextField[#adherentFiltre].onKeyReleased
	@FXML
	public void adherentFiltrePressed(KeyEvent event) {
		filteredTable(event);
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleDeleteClick(ActionEvent event) {
		Adherent adherent = adherentTable.getSelectionModel().getSelectedItem();
		if (adherent != null) {
			if (MethodUtilitaire.confirmationDialog(event, "Suppression d'un adhérent",
					"Vous etes sur le point de supprimer un adhérent \n sa suppression entrainera la suspenssion de son compte épargne",
					"L'adhérent " + adherent.getNom() + " " + adherent.getPrenom(), "Supprimer", "Annuler")) {
				
				adherent.setSituation("Trash");
				adherentServiceImpl.update(adherent);
				
				CompteEpargne compteEpargne = compteEpargneRepository.findByAdherent(adherent);
				if(compteEpargne != null) {
					compteEpargne.setStatut("Trash");
					compteEpargneServiceImplement.update(compteEpargne);
				}

				clearLabel();

				// rafraichissement de la table courante
				loadDataOnTable();
				// rafraichissement de la table sous adjascente
				adherentsController.loadDataOnTable();

				homeController.initialize(location, ResourceBundle.getBundle("Bundle"));

			}
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Aucun compte sélectionné", "Aucun compte sélectionné",
					"Sélectionné un compte epargne dans la table et réessayer");
		}
	}

	// Lorsque l'on supprime définitivement un adhérent, cela implique que son
	// compte
	// soit désactivé. ainsi le compte désactivé, le réactivé implique que
	// l'adhérent soit
	// de nouveau créer. Un adhérent ne peux avoir qu'un seul compte.
	@FXML
	public void handleActiveClick(ActionEvent event) {
		Adherent adherent = adherentTable.getSelectionModel().getSelectedItem();
		int adherentPosition = adherentTable.getSelectionModel().getFocusedIndex();
		if (adherent != null) {
			if (MethodUtilitaire.confirmationDialog(event, "Opération recursive", "Opération recursive",
					"L'activation ou la désactivation d'un titulaire de compte épargne entraine respectivement l'activation ou la désactivation du compte titulaire",
					"Valider", "Annuler")) {
				if (adherent.getSituation().equals("Actif")) {
					activeButton.setText("OFF");
					adherent.setSituation("Inactif");
					adherentServiceImpl.update(adherent);
					loadDataOnTable();
					focusRow(adherent, adherentPosition);
					// Désactive son compte de l'adhérent
					CompteEpargne compteEpargne = compteEpargneRepository.findByAdherent(adherent);
					if (compteEpargne != null) {
						compteEpargne.setStatut("Inactif");
						compteEpargneServiceImplement.update(compteEpargne);
					}
				} else {
					activeButton.setText("ON");
					adherent.setSituation("Actif");
					adherentServiceImpl.update(adherent);
					loadDataOnTable();
					focusRow(adherent, adherentPosition);
					// Active le compte de l'adhérent
					CompteEpargne compteEpargne = compteEpargneRepository.findByAdherent(adherent);
					if (compteEpargne != null) {
						compteEpargne.setStatut("Actif");
						compteEpargneServiceImplement.update(compteEpargne);
					}
				}
				
				homeController.initialize(location, ResourceBundle.getBundle("Bundle"));
			}
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Aucun adhérent sélectionné", "Aucun adhérent sélectionné",
					"Sélectionné un adhérent dans la table et réessayer");
		}
	}

	private void focusRow(Adherent adherent, int position) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				adherentTable.requestFocus();
				adherentTable.getSelectionModel().select(adherent);
				adherentTable.getFocusModel().focus(position);
				adherentTable.scrollTo(adherent);
			}
		});
	}

	private void toggle(Adherent adherent) {
		if (adherent != null) {
			if (adherent.getSituation().equals("Actif")) {
				activeToggle.setStyle("-fx-fill: green");
			} else {
				activeToggle.setStyle("-fx-fill: red");
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnDelete.setDisable(true);
		activeButton.setDisable(true);
		setColumnProperty();
		loadDataOnTable();

		adherentTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Adherent>() {
			@Override
			public void changed(ObservableValue<? extends Adherent> observable, Adherent oldValue, Adherent newValue) {
				btnDelete.setDisable(false);
				activeButton.setDisable(false);
				showPersonDetails(newValue);
				toggle(newValue);
			}
		});
	}

	private void setColumnProperty() {
		aderentNomTab.setCellValueFactory(new PropertyValueFactory<>("nom"));
		adherentPrenomTab.setCellValueFactory(new PropertyValueFactory<>("prenom"));
	}

	private void loadDataOnTable() {
		adherentList.clear();
		adherentList.addAll(adherentRepository.findBySituation("Actif"));
		adherentList.addAll(adherentRepository.findBySituation("Inactif"));
		adherentTable.setItems(adherentList);
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

	private void showPersonDetails(Adherent adherent) {
		if (adherent != null) {
			// Fill the labels with info from the person object.
			adherentLieuNaissLabel.setText(adherent.getLieuNaiss());
			adherentNomLael.setText(adherent.getNom());
			adherentPrenomLabel.setText(adherent.getPrenom());
			cniLabel.setText(adherent.getCni());
			adherentFonctionLabel.setText(adherent.getFonction());
			adherentStatutLabel.setText(adherent.getSituation());
			if (adherent.getSituation().equals("Actif")) {
				adherentStatutLabel.setTextFill(Color.web("#00FF00"));
			} else {
				adherentStatutLabel.setTextFill(Color.RED);
				System.out.println(adherent.getSituation());
			}
			adherentIdLabel.setText(adherent.getIdentifiant().toString());
		} else {
			// Person is null, remove all the text.
			clearLabel();
		}
	}

	private void clearLabel() {
		adherentNomLael.setText("");
		adherentPrenomLabel.setText("");
		cniLabel.setText("");
		adherentFonctionLabel.setText("");
		adherentStatutLabel.setText("");
		adherentLieuNaissLabel.setText("");
		adherentIdLabel.setText("");

	}

}
