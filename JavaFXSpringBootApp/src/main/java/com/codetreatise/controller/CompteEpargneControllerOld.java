package com.codetreatise.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.Utilisateur;
import com.codetreatise.repository.AdherentRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.CompteEpargneServiceImplement;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

@Controller
public class CompteEpargneControllerOld implements Initializable {
	@FXML
	private ComboBox<String> memberId;
	@FXML
	private TextField id;
	@FXML
	private TextField fond;
	@FXML
	private TextField solde;
	@FXML
	private TableView<CompteEpargne> compteEpargneTable;
	@FXML
	private TableColumn<CompteEpargne, Long> IdTableColumn;
	@FXML
	private TableColumn<CompteEpargne, String> nomTableColumn;
	@FXML
	private TableColumn<CompteEpargne, String> prenomTableColumn;
	@FXML
	private TableColumn<CompteEpargne, Long> soldeTableColumn;
	@FXML
	private TableColumn<CompteEpargne, Long> fondTableColun;
	@FXML
	private TableColumn<CompteEpargne, String> avaliseTableColun;
	@FXML
	private TextField search;

	@FXML
	private Button btnAdditionSolde;
	@FXML
	private Button btnSoustractionSolde;
	@FXML
	private Button btnAdditionFond;
	@FXML
	private Button btnSoustractionFond;
	@FXML
	private Button btnAdd;
	@FXML
	private Button btnDel;
	@FXML
	private Button btnClear;
	@FXML
	private Button btnGestionCompte;
	@FXML
	private Button btnGestionSolde;
	@FXML
	private Button btnGestionFond;

	@Autowired
	private CompteEpargneRepository compteEpargneRepository;

	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement;

	@Autowired
	private AdherentRepository adherentRepository;

	ObservableList<CompteEpargne> compteEpargneList = FXCollections.observableArrayList();

	ObservableList<String> adherentsList = FXCollections.observableArrayList();

	private void ActifCompte() {
		solde.setDisable(true);
		btnAdditionSolde.setDisable(true);
		btnSoustractionSolde.setDisable(true);

		fond.setDisable(true);
		btnAdditionFond.setDisable(true);
		btnSoustractionFond.setDisable(true);

		// btnGestionSolde.setDisable(true);
		// btnGestionFond.setDisable(true);
	}

	private void actifSolde() {
		fond.setDisable(true);
		btnAdditionFond.setDisable(true);
		btnSoustractionFond.setDisable(true);

		btnAdd.setDisable(true);
		btnDel.setDisable(true);
		// btnEdit.setDisable(true);

		// btnGestionCompte.setDisable(true);
		// btnGestionFond.setDisable(true);
	}

	private void actifFond() {
		solde.setDisable(true);
		btnAdditionSolde.setDisable(true);
		btnSoustractionSolde.setDisable(true);

		btnAdd.setDisable(true);
		btnDel.setDisable(true);
		// btnEdit.setDisable(true);

		// btnGestionCompte.setDisable(true);
		// btnGestionSolde.setDisable(true);
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleClearClick(ActionEvent event) {
		clearFields();
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleDeleteClick(ActionEvent event) {

		List<CompteEpargne> compteEpargnes = compteEpargneTable.getSelectionModel().getSelectedItems();
		if (!compteEpargnes.isEmpty()) {
			if (MethodUtilitaire.confirmationDialog(event, "Confirm to delete selected bank account ?",
					"Confirm to delete selected bank account ?", "")) {
				compteEpargneRepository.deleteInBatch(compteEpargnes);
				LoadDataOnTable();
				MethodUtilitaire.saveAlert(event, "Delete operation successful", "Delete operation successful");
			}
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Warning no bank account selected",
					"Warning no bank account selected", "Please select one or many bank account and try.");
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleCreateClick(ActionEvent event) {

		if (MethodUtilitaire.emptyValidation("Id", getId() == null)
				&& MethodUtilitaire.validate("Adhérent", getAdherentNom(), "[a-zA-Z]+")) {

			CompteEpargne oldAccount = compteEpargneRepository.findOne(getEpargneId());
			if (oldAccount.equals(null)) {
				MethodUtilitaire.errorMessageAlert("Warning existing account", "Warning existing account",
						"Attention cet adhérent possède deja un compte épargne");
			} else {
				CompteEpargne compteEpargne = new CompteEpargne();
				compteEpargne.setAdherent(getAdherent());
				compteEpargne.setAvaliser(false);
				compteEpargne.setEpargneId(getEpargneId());
				compteEpargne.setFond(0);
				compteEpargne.setSolde(Long.valueOf(getSolde()));

				CompteEpargne newCompteEpargne = compteEpargneRepository.save(compteEpargne);
				MethodUtilitaire.saveAlert(newCompteEpargne, "Save compte epargne sucessful",
						"Bank account of " + newCompteEpargne.getAdherent().getNom() + " "
								+ newCompteEpargne.getAdherent().getPrenom() + " created successful");

				clearFields();
				LoadDataOnTable();
			}

		}
	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleSearchReleased(KeyEvent event) {

		filteredTable(event);
	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleAdditionFondClick(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleSoustractionFondClick(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleAdditionSlodeClick(ActionEvent event) {
		CompteEpargne compteEpargne = compteEpargneTable.getSelectionModel().getSelectedItem();
		if (compteEpargne != null) {
			if (MethodUtilitaire.confirmationDialog(compteEpargne, "Confirm to credited bank account",
					"Confirm to credited bank account ? ",
					"Le compte bancaire de " + compteEpargne.getAdherent().getNom() + " "
							+ compteEpargne.getAdherent().getPrenom() + " sera crédité de " + Long.valueOf(getSolde())
							+ "Fcfa")) {
				Float solde = compteEpargne.getSolde();
				compteEpargne.setSolde(solde + Long.valueOf(getFond()));
				CompteEpargne updateCompteEpargne = compteEpargneServiceImplement.update(compteEpargne);
				clearFields();
				MethodUtilitaire.saveAlert(updateCompteEpargne, "Updated balance successful",
						"Le solde de " + updateCompteEpargne.getAdherent().getNom() + " "
								+ updateCompteEpargne.getAdherent().getPrenom() + " à été crédité de :"
								+ Long.valueOf(getFond()) + " Fcfa . \n" + " Le nouveau solde est de :"
								+ updateCompteEpargne.getSolde()+" Fcfa");
			}
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Warning no bank account selected",
					"Warning no bank account selected", "Please select one or many bank account and try.");
		}
	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleSoustractionSoldeClick(ActionEvent event) {
		CompteEpargne compteEpargne = compteEpargneTable.getSelectionModel().getSelectedItem();
		if (compteEpargne != null) {
			if (MethodUtilitaire.confirmationDialog(compteEpargne, "Confirm to debited  bank account",
					"Confirm to debited bank account ? ",
					"Le compte bancaire de " + compteEpargne.getAdherent().getNom() + " "
							+ compteEpargne.getAdherent().getPrenom() + " sera débité de " + Long.valueOf(getSolde())
							+ "Fcfa")) {
				Float solde = compteEpargne.getSolde();
				if(solde < Long.valueOf(getSolde())) {
					MethodUtilitaire.errorMessageAlert("Operation failed", "Operation failed", "");
				}
			}
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Warning no bank account selected",
					"Warning no bank account selected", "Please select one or many bank account and try.");
		}
	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleGestionSoldeClick(ActionEvent event) {
		actifSolde();
	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleGestionFondClick(ActionEvent event) {
		actifFond();
	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleGestionCompteClick(ActionEvent event) {
		ActifCompte();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ActifCompte();
		SetTableColunProperties();
		LoadDataOnTable();
		compteEpargneTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		adherentsList.clear();
		List<Adherent> adherents = adherentRepository.findAll();
		for (Adherent adherent : adherents) {
			adherentsList.add(
					adherent.getIdentifiant().toString() + " ---> " + adherent.getNom() + " " + adherent.getPrenom());
		}
		memberId.setItems(adherentsList);
		FilterValueOnComboboxMemberId();
		compteEpargneTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CompteEpargne>() {
			@Override
			public void changed(ObservableValue<? extends CompteEpargne> observable, CompteEpargne oldValue,
					CompteEpargne newValue) {
				clearFields();
				setNewValues(newValue);

			}

		});
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

	private void LoadDataOnTable() {

		compteEpargneList.clear();
		compteEpargneList.addAll(compteEpargneRepository.findAll());
		compteEpargneTable.setItems(compteEpargneList);
	}

	private void clearFields() {
		id.clear();
		memberId.getSelectionModel().clearSelection();
		solde.clear();
		fond.clear();
	}

	private void setNewValues(CompteEpargne compteEpargne) {
		if (compteEpargne != null) {
			id.setText(compteEpargne.getEpargneId().toString());
			memberId.getSelectionModel().select(getMemberNameOnCombobox(compteEpargne));
		}
	}

	private String getId() {
		return id.getText();
	}

	private String getAdherentNom() {
		return memberId.getSelectionModel().getSelectedItem();
	}

	private String getSolde() {
		return solde.getText();
	}

	private String getFond() {
		return fond.getText();
	}

	private Adherent getAdherent() {
		Long id = Long.valueOf(memberId.getSelectionModel().getSelectedItem().substring(0, 1));
		Adherent adherent = adherentRepository.findOne(id);
		return adherent;
	}

	private Long getEpargneId() {
		return getAdherent().getIdentifiant();
	}

	private String getMemberNameOnCombobox(CompteEpargne compteEpargne) {
		return compteEpargne.getAdherent().getIdentifiant().toString() + " ---> " + compteEpargne.getAdherent().getNom()
				+ " " + compteEpargne.getAdherent().getPrenom();
	}

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
}
