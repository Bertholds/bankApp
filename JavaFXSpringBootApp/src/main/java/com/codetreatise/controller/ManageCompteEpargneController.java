package com.codetreatise.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.config.StageManager;
import com.codetreatise.repository.AvaliseRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.AdherentServiceImpl;
import com.codetreatise.service.impl.CompteEpargneServiceImplement;
import com.codetreatise.view.FxmlView;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

@Controller
public class ManageCompteEpargneController implements Initializable {
	@FXML
	private TableView<CompteEpargne> compteEpargneTable;
	@FXML
	private TableColumn<CompteEpargne, String> aderentNomTab;
	@FXML
	private TableColumn<CompteEpargne, String> adherentPrenomTab;
	@FXML
	private TextField co├╣pteEpargneFiltre;
	@FXML
	private Label numCompteLabel;
	@FXML
	private Label adherentNomLael;
	@FXML
	private Label adherentPrenomLabel;
	@FXML
	private Button consulterEngagement;
	@FXML
	private Label soldeLabel;
	@FXML
	private Label pretLabel;
	@FXML
	private Label credibiliteLabel;
	@FXML
	private Label statutLabel;
	@FXML
	private Button activeButton;
	@FXML
	private Button btnDel;
	@FXML
	private FontAwesomeIconView activeToggle;

	@Autowired
	private CompteEpargneController compteEpargneController;
	@Autowired
	private AdherentServiceImpl adherentServiceImpl;
	@Autowired
	private CompteEpargneRepository compteEpargneRepository;
	@Autowired
	private AvaliseRepository avaliseRepository;
	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement;
	
	@Autowired
	@Lazy
	private StageManager stageManager;
	
	@Autowired
	private CompteEpargneMoreDetailController dialog;
	
	private CompteEpargne selectedAccount;
	
	List<Avalise> engagementList;

	private ObservableList<CompteEpargne> compteEpargneList = FXCollections.observableArrayList();

	// Event Listener on TextField[#adherentFiltre].onKeyReleased
	@FXML
	public void adherentFiltrePressed(KeyEvent event) {
		filteredTable(event);
	}
	
	@FXML
	private void handleconsulterEngagement() {
		stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.COMPTEEPARGNEMOREDETAIL);
		dialog.setValue(selectedAccount, engagementList);
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleDeleteClick(ActionEvent event) {
		CompteEpargne compteEpargne = compteEpargneTable.getSelectionModel().getSelectedItem();
		if (compteEpargne != null) {
			if (MethodUtilitaire.confirmationDialog(event, "Suppression de compte",
					"Vous etes sur le point de supprimer un compte ├ępargne \n cette suppression entrainera la suspenssion de son d├ętenteur", "Le compte ├ępargne de "
							+ compteEpargne.getAdherent().getNom() + " " + compteEpargne.getAdherent().getPrenom(),
					"Supprimer", "Annuler")) {
				
				compteEpargne.setStatut("Trash");
				compteEpargneServiceImplement.update(compteEpargne);
				// rafraichissement de la table courante
				loadDataOnTable();
				// rafraichissement de la table sous adjascente
				compteEpargneController.LoadDataOnTable();
				// mise a jour de la comboboxe
				
				Adherent adherent = compteEpargne.getAdherent();
				adherent.setSituation("Trash");
				adherentServiceImpl.update(adherent);
				
				clearLabel();
				

			}
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Aucun compte s├ęlectionn├ę", "Aucun compte s├ęlectionn├ę",
					"S├ęlectionn├ę un compte epargne dans la table et r├ęessayer");
		}
	}

	// Event Listener on Button[#activeButton].onAction
	@FXML
	public void handleAciveCLICK(ActionEvent event) {
		
		CompteEpargne compteEpargne = compteEpargneTable.getSelectionModel().getSelectedItem();
	
		if (compteEpargne != null) {
			if (MethodUtilitaire.confirmationDialog(event, "Op├ęration recursive", "Op├ęration recursive",
					"L'activation ou la d├ęsactivation d'un compte ├ępargne entraine respectivement l'activation ou la d├ęsactivation du titulaire du compte",
					"Valider", "Annuler")) {
				if (compteEpargne.getStatut().equals("Actif")) {
					activeButton.setText("OFF");
					compteEpargne.setStatut("Inactif");
					compteEpargneServiceImplement.update(compteEpargne);
					// D├ęsactive l'adh├ęrent titulaire du compte
					Adherent adherent = compteEpargne.getAdherent();
					adherent.setSituation("Inactif");
					adherentServiceImpl.update(adherent);
					// Met a jour les adherent dans la liste (comboboxe) et tableView
					compteEpargneController.LoadDataOnTable();
				} else {
					activeButton.setText("ON");
					compteEpargne.setStatut("Actif");
					compteEpargneServiceImplement.update(compteEpargne);
					// Active l'adh├ęrent titulaire du compte
					Adherent adherent = compteEpargne.getAdherent();
					adherent.setSituation("Actif");
					adherentServiceImpl.update(adherent);
					// Met a jour les adherent dans la liste (comboboxe) et et tableView
					compteEpargneController.LoadDataOnTable();
				}
				
				loadDataOnTable();
				compteEpargneTable.getSelectionModel().select(compteEpargne);
				int compteEpargnePosition = compteEpargneTable.getSelectionModel().getSelectedIndex();
				focusRow(compteEpargne, compteEpargnePosition);
				
			}
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Aucun adh├ęrent s├ęlectionn├ę", "Aucun adh├ęrent s├ęlectionn├ę",
					"S├ęlectionn├ę un adh├ęrent dans la table et r├ęessayer");
		}
	}

	private void focusRow(CompteEpargne compteEpargne, int position) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				compteEpargneTable.requestFocus();
				compteEpargneTable.getSelectionModel().select(compteEpargne);
				compteEpargneTable.getFocusModel().focus(position);
				compteEpargneTable.scrollTo(compteEpargne);
			}
		});
	}

	private void toggle(CompteEpargne compteEpargne) {
		if (compteEpargne != null) {
			if (compteEpargne.getStatut().equals("Actif")) {
				activeToggle.setStyle("-fx-fill: green");
			} else {
				activeToggle.setStyle("-fx-fill: red");
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		consulterEngagement.setVisible(false);
		btnDel.setDisable(true);
		activeButton.setDisable(true);
		
		setColumnProperty();
		loadDataOnTable();

		compteEpargneTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CompteEpargne>() {
			@Override
			public void changed(ObservableValue<? extends CompteEpargne> observable, CompteEpargne oldValue,
					CompteEpargne newValue) {
				selectedAccount = newValue;
				btnDel.setDisable(false);
				activeButton.setDisable(false);
				showCompteEpargneDetails(newValue);
				toggle(newValue);
			}
		});

	}

	private void setColumnProperty() {
		aderentNomTab.setCellValueFactory(celData -> {
			return new SimpleStringProperty(celData.getValue().getAdherent().getNom());
		});
		adherentPrenomTab.setCellValueFactory(celData -> {
			return new SimpleStringProperty(celData.getValue().getAdherent().getPrenom());
		});
	}

	private void loadDataOnTable() {
		compteEpargneList.clear();
		compteEpargneList.addAll(compteEpargneRepository.findByStatut("Actif"));
		compteEpargneList.addAll(compteEpargneRepository.findByStatut("Inactif"));
		compteEpargneTable.setItems(compteEpargneList);
	}

	private void showCompteEpargneDetails(CompteEpargne compteEpargne) {
		if (compteEpargne != null) {
			
			 engagementList = avaliseRepository.findCompteByIdAndStatut(compteEpargne, false, compteEpargne.getCompteTampon());
			
			// Fill the labels with info from the person object.
			consulterEngagement.setVisible(true);
			numCompteLabel.setText(compteEpargne.getEpargneId().toString());
			adherentNomLael.setText(compteEpargne.getAdherent().getNom());
			adherentPrenomLabel.setText(compteEpargne.getAdherent().getPrenom());
			soldeLabel.setText(String.valueOf(compteEpargne.getSolde()));
			pretLabel.setText(compteEpargne.getCompteTampon().getDette().toString());
			
			if (compteEpargne.getStatut().equals("Actif")) {
				statutLabel.setTextFill(Color.web("#00FF00"));
				statutLabel.setText(compteEpargne.getStatut());
			} else {
				statutLabel.setTextFill(Color.RED);
				statutLabel.setText(compteEpargne.getStatut());
			}
			
			if(engagementList.size() > 0) {
				consulterEngagement.setText("Consulter");
				consulterEngagement.setDisable(false);
				consulterEngagement.setVisible(true);
			}else {
				consulterEngagement.setText("Aucun engagement");
				consulterEngagement.setDisable(true);
			}
			
			credibiliteLabel.setText(String.valueOf(compteEpargne.getLacarte()));
		} else {
			// Person is null, remove all the text.
			clearLabel();
		}
	}
	
	private void clearLabel() {
		numCompteLabel.setText("");
		adherentNomLael.setText("");
		adherentPrenomLabel.setText("");
		consulterEngagement.setVisible(false);
		soldeLabel.setText("");
		pretLabel.setText("");
		statutLabel.setText("");
		credibiliteLabel.setText("");
	}

	private void filteredTable(KeyEvent event) {
		FilteredList<CompteEpargne> filteredadherent = new FilteredList<CompteEpargne>(compteEpargneList, e -> true);
		co├╣pteEpargneFiltre.setOnKeyReleased(e -> {
			co├╣pteEpargneFiltre.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredadherent.setPredicate((Predicate<? super CompteEpargne>) compteEpargne -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String newValueFilter = newValue.toLowerCase();
					if (compteEpargne.getAdherent().getIdentifiant().toString().contains(newValueFilter)) {
						return true;
					} else if (compteEpargne.getAdherent().getNom().toLowerCase().contains(newValueFilter)) {
						return true;
					} else if (compteEpargne.getAdherent().getPrenom().toLowerCase().contains(newValueFilter)) {
						return true;
					}
					return false;
				});
			});
		});

		SortedList<CompteEpargne> sortedList = new SortedList<CompteEpargne>(filteredadherent);
		sortedList.comparatorProperty().bind(compteEpargneTable.comparatorProperty());
		compteEpargneTable.setItems(sortedList);
	}

}
