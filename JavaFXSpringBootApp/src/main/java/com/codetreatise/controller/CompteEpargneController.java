package com.codetreatise.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.config.StageManager;
import com.codetreatise.repository.AdherentRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.CompteEpargneServiceImplement;
import com.codetreatise.view.FxmlView;

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
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

@Controller
public class CompteEpargneController implements Initializable {
	@FXML
	private ComboBox<String> memberId;
	@FXML
	private TextField id;
	@FXML
	private TextField fond;
	@FXML
	private TableView<CompteEpargne> compteEpargneTable;
	@FXML
	private TableColumn<CompteEpargne, Long> IdTableColumn;
	@FXML
	private TableColumn<CompteEpargne, String> nomTableColumn;
	@FXML
	private TableColumn<CompteEpargne, String> prenomTableColumn;
	@FXML
	private TableColumn<CompteEpargne, Float> soldeTableColumn;
	@FXML
	private TableColumn<CompteEpargne, Float> fondTableColun;
	@FXML
	private TableColumn<CompteEpargne, Boolean> avaliseTableColun;
	@FXML
	private TextField search;
	@FXML
	private Button btnFond;
	@FXML
	private Accordion accordionAccordAvalise;
	
	@Autowired
	private CompteEpargneMoreDetailController dialog;

	@Autowired
	private CompteEpargneRepository compteEpargneRepository;

	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement;

	@Autowired
	private AdherentRepository adherentRepository;

	@Autowired
	private MethodUtilitaire methodUtilitaire;
	
	@Autowired
	@Lazy
	private StageManager stageManager;

	ObservableList<CompteEpargne> compteEpargneList = FXCollections.observableArrayList();

	ObservableList<String> adherentsList = FXCollections.observableArrayList();

	// Event Listener on Button.onAction
	@FXML
	public void handleClearClick(ActionEvent event) {
		clearFields();
	}
	
	@FXML
	public void handleShowMoreDetailClick() {
		CompteEpargne selectedAccount = compteEpargneTable.getSelectionModel().getSelectedItem();
		if(selectedAccount !=null) {
			stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.COMPTEEPARGNEMOREDETAIL);
			dialog.setValue(selectedAccount);
		}else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Warning no bank account selected",
					"Warning no bank account selected", "Please select one or many bank account and try.");
		}
	}
	
	@FXML
	public void handleAdvancedClick() {
		stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.MANAGECOMPTEEPARGNE);
	}

	@FXML
	public void handleFondClick(ActionEvent event) {
		if (getFond() == null || getFond().trim().isEmpty()) {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Erreur de validation", "La valeur du solde est null", "");
		} else {
			try {
				Float val = Float.parseFloat(fond.getText());
				CompteEpargne compteEpargne = compteEpargneTable.getSelectionModel().getSelectedItem();
				compteEpargne.setFond(val);
				compteEpargneServiceImplement.update(compteEpargne);
				MethodUtilitaire.saveAlert(compteEpargne, "Opération réussi",
						"Le fond de " + compteEpargne.getAdherent().getNom() + " "
								+ compteEpargne.getAdherent().getPrenom() + " a été défini avec succès");
				btnFond.setDisable(true);
				LoadDataOnTable();
				methodUtilitaire.LogFile("Définir le fond du compte bancaire",
						compteEpargne.getAdherent().getNom() + " " + compteEpargne.getAdherent().getPrenom(),
						MethodUtilitaire.deserializationUser());
			} catch (Exception e) {
				e.printStackTrace();
				MethodUtilitaire.deleteNoPersonSelectedAlert("Valeur incorrect", "La valeur du fond est incorrect", "");
			}
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleDeleteClick(ActionEvent event) throws Exception{
		CompteEpargne compteEpargne = compteEpargneTable.getSelectionModel().getSelectedItem();
		if (compteEpargne != null) {
			if (MethodUtilitaire.confirmationDialog(event, "Confirm to delete selected bank account ?",
					"Confirm to delete selected bank account ?", "")) {
				compteEpargneRepository.delete(compteEpargne);
				LoadDataOnTable();
				MethodUtilitaire.saveAlert(event, "Delete operation successful", "Delete operation successful");
				methodUtilitaire.LogFile("Suppression du fond du compte bancaire",
						compteEpargne.getAdherent().getNom() + " " + compteEpargne.getAdherent().getPrenom(),
						MethodUtilitaire.deserializationUser());
			}
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Warning no bank account selected",
					"Warning no bank account selected", "Please select one or many bank account and try.");
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleCreateClick(ActionEvent event) throws Exception{
		if (MethodUtilitaire.emptyValidation("Titulaire du compte", getAdherentNom() == null)) {

			try {
				//peut généré NullPointerExeption
				CompteEpargne oldAccount = compteEpargneRepository.findByAdherent(getAdherent());
				
				if (oldAccount != null) {
					MethodUtilitaire.errorMessageAlert("Warning existing account", "Warning existing account",
							"Attention cet adhérent possède deja un compte épargne");
				} else {
					CompteEpargne compteEpargne = new CompteEpargne();
					compteEpargne.setAdherent(getAdherent());
					compteEpargne.setAvaliser(false);
					compteEpargne.setEpargneId(getAdherent().getIdentifiant());
					compteEpargne.setStatut("Inactif");

					CompteEpargne newCompteEpargne = compteEpargneRepository.save(compteEpargne);
					MethodUtilitaire.saveAlert(newCompteEpargne, "Save compte epargne sucessful",
							"Bank account of " + newCompteEpargne.getAdherent().getNom() + " "
									+ newCompteEpargne.getAdherent().getPrenom() + " created successful");

					clearFields();
					LoadDataOnTable();
					methodUtilitaire.LogFile("Création du fond du compte bancaire",
							compteEpargne.getAdherent().getNom() + " " + compteEpargne.getAdherent().getPrenom(),
							MethodUtilitaire.deserializationUser());
				}
			} catch (Exception e) {
				MethodUtilitaire.deleteNoPersonSelectedAlert(null, "Titulaire du compte introuvable",
						"Le titulaire du compte est introuvable; Sélectionner un adhérent dans la liste déroulante ou vérifier l'orthographe du nom & prenom saisi et réessayer");
			}

		}
	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleSearchReleased(KeyEvent event) {
		filteredTable(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		SetTableColunProperties();
		LoadDataOnTable();
		// compteEpargneTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setComboboxeAdherent();
		FilterValueOnComboboxMemberId();
		compteEpargneTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CompteEpargne>() {
			@Override
			public void changed(ObservableValue<? extends CompteEpargne> observable, CompteEpargne oldValue,
					CompteEpargne newValue) {
				clearFields();
				// setNewValues(newValue);y
				if (newValue != null) {
					if(newValue.getFond() == 0) {
						btnFond.setDisable(false);
					}
				} else
					btnFond.setDisable(true);
			}

		});
	}

	public void setComboboxeAdherent() {
		adherentsList.clear();
		List<Adherent> adherents = adherentRepository.findBySituation("Actif");
		for (Adherent adherent : adherents) {
			adherentsList.add(adherent.getUniqueName());
		}
		memberId.setItems(adherentsList);
	}

	public void LoadDataOnTable() {

		compteEpargneList.clear();
		compteEpargneList.addAll(compteEpargneRepository.findByStatut("Actif"));
		compteEpargneTable.setItems(compteEpargneList);
	}

	private void SetTableColunProperties() {

		IdTableColumn.setCellValueFactory(new PropertyValueFactory<>("epargneId"));
		nomTableColumn.setCellValueFactory(celData -> {
			return new SimpleStringProperty(celData.getValue().getAdherent().getNom());
		});
		prenomTableColumn.setCellValueFactory(celData -> {
			return new SimpleStringProperty(celData.getValue().getAdherent().getPrenom());
		});
		soldeTableColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
		fondTableColun.setCellValueFactory(new PropertyValueFactory<>("fond"));
		avaliseTableColun.setCellValueFactory(new PropertyValueFactory<>("avaliser"));
	}

	private void FilterValueOnComboboxMemberId() {

		// Create a FilteredList wrapping the ObservableList.
		FilteredList<String> filteredItems = new FilteredList<String>(adherentsList, p -> true);

		// Add a listener to the textProperty of the combobox editor. The
		// listener will simply filter the list every time the input is changed
		// as long as the user hasn't selected an item in the list.
		memberId.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			final TextField editor = memberId.getEditor();
			final String selected = memberId.getSelectionModel().getSelectedItem();

			// This needs run on the GUI thread to avoid the error described
			// here: https://bugs.openjdk.java.net/browse/JDK-8081700.
			Platform.runLater(() -> {
				// If the no item in the list is selected or the selected item
				// isn't equal to the current input, we refilter the list.
				if (selected == null || !selected.equals(editor.getText())) {
					filteredItems.setPredicate(item -> {
						// We return true for any items that starts with the
						// same letters as the input. We use toUpperCase to
						// avoid case sensitivity.
						if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
							return true;
						} else {
							return false;
						}
					});
				}
			});
		});

		memberId.setItems(filteredItems);

	}

	private void clearFields() {
		id.clear();
		memberId.getSelectionModel().clearSelection();
		fond.clear();
	}

	private String getAdherentNom() {
		return memberId.getSelectionModel().getSelectedItem();
	}

	private String getFond() {
		return fond.getText();
	}

	private Long getEpargneId() {
		return getAdherent().getIdentifiant();
	}

	private Adherent getAdherent() {
		Adherent adherent = null;
			String uniqueName = memberId.getSelectionModel().getSelectedItem();
		//	Long id = Long.valueOf(memberId.getSelectionModel().getSelectedItem().substring(size - 1, size));
			adherent = adherentRepository.findByUniqueName(uniqueName);
			return adherent;
	}

	/*
	 * private void setNewValues(CompteEpargne compteEpargne) { if (compteEpargne !=
	 * null) { id.setText(compteEpargne.getEpargneId().toString());
	 * memberId.getSelectionModel().select(getMemberNameOnCombobox(compteEpargne));
	 * } }
	 */

	/*
	 * private String getMemberNameOnCombobox(CompteEpargne compteEpargne) { return
	 * compteEpargne.getAdherent().getNom() + " " +
	 * compteEpargne.getAdherent().getPrenom(); }
	 * 
	 */

	private void filteredTable(KeyEvent event) {
		FilteredList<CompteEpargne> filteredCompteEpargne = new FilteredList<CompteEpargne>(compteEpargneList,
				e -> true);
		search.setOnKeyReleased(e -> {
			search.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredCompteEpargne.setPredicate((Predicate<? super CompteEpargne>) compteEpargne -> {
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

		SortedList<CompteEpargne> sortedList = new SortedList<CompteEpargne>(filteredCompteEpargne);
		sortedList.comparatorProperty().bind(compteEpargneTable.comparatorProperty());
		compteEpargneTable.setItems(sortedList);
	}

}
