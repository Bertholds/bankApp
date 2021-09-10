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
import javafx.beans.property.SimpleStringProperty;
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
public class ManageCompteEpargneController implements Initializable {
	@FXML
	private TableView<CompteEpargne> compteEpargneTable;
	@FXML
	private TableColumn<CompteEpargne, String> aderentNomTab;
	@FXML
	private TableColumn<CompteEpargne, String> adherentPrenomTab;
	@FXML
	private TextField coùpteEpargneFiltre;
	@FXML
	private Label numCompteLabel;
	@FXML
	private Label adherentNomLael;
	@FXML
	private Label adherentPrenomLabel;
	@FXML
	private Label avaliserLabel;
	@FXML
	private Label soldeLabel;
	@FXML
	private Label fondLabel;
	@FXML
	private Label lacarteLabel;
	@FXML
	private Label statutLabel;
	@FXML
	private Button activeButton;
	@FXML
	private FontAwesomeIconView activeToggle;
	
	@Autowired
	private CompteEpargneController compteEpargneController;
	@Autowired
	private AdherentServiceImpl adherentServiceImpl;
	@Autowired
	private CompteEpargneRepository compteEpargneRepository;
	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement;
	
	private ObservableList<CompteEpargne> compteEpargneList = FXCollections.observableArrayList();

	// Event Listener on TextField[#adherentFiltre].onKeyReleased
	@FXML
	public void adherentFiltrePressed(KeyEvent event) {
		filteredTable(event);
	}
	
	// Event Listener on Button.onAction
	@FXML
	public void handleDeleteClick(ActionEvent event) {
		CompteEpargne compteEpargne = compteEpargneTable.getSelectionModel().getSelectedItem();
		if(compteEpargne != null) {
			if(MethodUtilitaire.confirmationDialog(event, "Suppression de compte", "Vous etes sur le point de supprimer un compte épargne", "Le compte épargne de "+ compteEpargne.getAdherent().getNom()+" "+compteEpargne.getAdherent().getPrenom())) {
				compteEpargne.setStatut("Trash");
				compteEpargneServiceImplement.update(compteEpargne);
				//rafraichissement de la table courante
				loadDataOnTable();
				//rafraichissement de la table sous adjascente
				compteEpargneController.LoadDataOnTable();
				//mise a jour de la comboboxe
				compteEpargneController.setComboboxeAdherent();
			}
		}else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Aucun compte sélectionné", "Aucun compte sélectionné", "Sélectionné un compte epargne dans la table et réessayer");
		}
	}
	// Event Listener on Button[#activeButton].onAction
	@FXML
	public void handleAciveCLICK(ActionEvent event) {
		CompteEpargne compteEpargne = compteEpargneTable.getSelectionModel().getSelectedItem();
		int compteEpargnePosition = compteEpargneTable.getSelectionModel().getFocusedIndex();
		if(compteEpargne !=null) {
			if(MethodUtilitaire.confirmationDialog(event, "Opération recursive", "Opération recursive", "L'activation ou la désactivation d'un compte épargne entraine respectivement l'activation ou la désactivation du titulaire du compte")) {
				if(compteEpargne.getStatut().equals("Actif")) {
					activeButton.setText("OFF");
					compteEpargne.setStatut("Inactif");
					compteEpargneServiceImplement.update(compteEpargne);
					loadDataOnTable();
					focusRow(compteEpargne, compteEpargnePosition);
					//Désactive l'adhérent titulaire du compte
					Adherent adherent = compteEpargne.getAdherent();
					adherent.setSituation("Inactif");
					adherentServiceImpl.update(adherent);
					//Met a jour les adherent dans la liste (comboboxe) et tableView
					compteEpargneController.setComboboxeAdherent();
					compteEpargneController.LoadDataOnTable();
				}else {
					activeButton.setText("ON");
					compteEpargne.setStatut("Actif");
					compteEpargneServiceImplement.update(compteEpargne);
					loadDataOnTable();
					focusRow(compteEpargne, compteEpargnePosition);
					//Active l'adhérent titulaire du compte
					Adherent adherent = compteEpargne.getAdherent();
					adherent.setSituation("Actif");
					adherentServiceImpl.update(adherent);
					//Met a jour les adherent dans la liste (comboboxe) et et tableView
					compteEpargneController.setComboboxeAdherent();
					compteEpargneController.LoadDataOnTable();
				}
			}
		}else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Aucun adhérent sélectionné", "Aucun adhérent sélectionné", "Sélectionné un adhérent dans la table et réessayer");
		}
	}
	
	private void focusRow(CompteEpargne compteEpargne, int position) {
		Platform.runLater(new Runnable()
		{
		    @Override
		    public void run()
		    {
		    	compteEpargneTable.requestFocus();
		    	compteEpargneTable.getSelectionModel().select(compteEpargne);
		    	compteEpargneTable.getFocusModel().focus(position);
		    	compteEpargneTable.scrollTo(compteEpargne);
		    }
		});
	}
	
	private void toggle(CompteEpargne compteEpargne) {
		if(compteEpargne !=null) {
			if(compteEpargne.getStatut().equals("Actif")) {
				activeToggle.setStyle("-fx-fill: green");
			}else {
				activeToggle.setStyle("-fx-fill: red");
			}
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setColumnProperty();
		loadDataOnTable();
		
		compteEpargneTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CompteEpargne>() {
			@Override
			public void changed(ObservableValue<? extends CompteEpargne> observable, CompteEpargne oldValue, CompteEpargne newValue) {
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
			// Fill the labels with info from the person object.
			numCompteLabel.setText(compteEpargne.getEpargneId().toString());
			adherentNomLael.setText(compteEpargne.getAdherent().getNom());
			adherentPrenomLabel.setText(compteEpargne.getAdherent().getPrenom());
			avaliserLabel.setText(String.valueOf(compteEpargne.getAvaliser()));
			soldeLabel.setText(String.valueOf(compteEpargne.getSolde()));
			fondLabel.setText(String.valueOf(compteEpargne.getFond()));
			if(compteEpargne.getStatut().equals("Actif")) {
				statutLabel.setTextFill(Color.web("#00FF00"));
				statutLabel.setText(compteEpargne.getStatut());
			}else {
				statutLabel.setTextFill(Color.RED);
				statutLabel.setText(compteEpargne.getStatut());
			}
			lacarteLabel.setText(String.valueOf(compteEpargne.getLacarte()));
		} else {
			// Person is null, remove all the text.
			numCompteLabel.setText("");
			adherentNomLael.setText("");
			adherentPrenomLabel.setText("");
			avaliserLabel.setText("");
			soldeLabel.setText("");
			fondLabel.setText("");
			statutLabel.setText("");
			lacarteLabel.setText("");
		}
	}
	
	private void filteredTable(KeyEvent event) {
		FilteredList<CompteEpargne> filteredadherent = new FilteredList<CompteEpargne>(compteEpargneList, e -> true);
		coùpteEpargneFiltre.setOnKeyReleased(e -> {
			coùpteEpargneFiltre.textProperty().addListener((observableValue, oldValue, newValue) -> {
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
