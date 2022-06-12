package com.codetreatise.controller;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.CompteCreance;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.CompteTampon;
import com.codetreatise.config.StageManager;
import com.codetreatise.repository.AdherentRepository;
import com.codetreatise.repository.AvaliseRepository;
import com.codetreatise.repository.CompteCreanceRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.repository.CompteTamponRepository;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.AdherentServiceImpl;
import com.codetreatise.service.impl.CompteEpargneServiceImplement;
import com.codetreatise.view.FxmlView;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
	private TableColumn<CompteEpargne, Long> soldeTableColumn;
	@FXML
	private TableColumn<CompteEpargne, Long> credibiliteTableColun;
	@FXML
	private TextField search;
	@FXML
	private Button btnFond;
	@FXML
	private Label pretLabel;
	@FXML
	private Label credibiliteLabel;
	@FXML
	private Label soldeLabel;
	@FXML
	private Label prenomLabel;
	@FXML
	private Label nomLabel;
	@FXML
	private Label idLabel;

	@FXML
	private Button consulterEngagement;

	@FXML
	private Button btnCreer;

	@FXML
	private Button btnDesactivate;

	@FXML
	private FontAwesomeIconView desactivateIcon;

	@Autowired
	private CompteEpargneMoreDetailController dialog;

	@Autowired
	private CompteEpargneRepository compteEpargneRepository;

	@Autowired
	private CompteTamponRepository compteTamponRepository;

	@Autowired
	private CompteCreanceRepository compteCreanceRepository;
	
	@Autowired
	private AvaliseRepository avaliseRepository;

	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement;

	@Autowired
	private AdherentServiceImpl adherentServiceImpl;

	@Autowired
	private AdherentRepository adherentRepository;

	@Autowired
	private MethodUtilitaire methodUtilitaire;

	@Autowired
	@Lazy
	private StageManager stageManager;

	private CompteEpargne selectedAccount;

	ObservableList<CompteEpargne> compteEpargneList = FXCollections.observableArrayList();

	List<String> adherentUniqueName;
	
	List<Avalise> engagementList;

	@FXML
	private void handleconsulterEngagement() {
		stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.COMPTEEPARGNEMOREDETAIL);
		dialog.setValue(selectedAccount, engagementList);
	}

	@FXML
	public void handleAdvancedClick() {
		stageManager.switchSceneShowPreviousStage(FxmlView.MANAGECOMPTEEPARGNE);
	}


	// Event Listener on Button.onAction
	@FXML
	public void handleDesactivateClick(ActionEvent event) throws Exception {
		CompteEpargne compteEpargne = compteEpargneTable.getSelectionModel().getSelectedItem();
		if (compteEpargne != null) {

			if (MethodUtilitaire.confirmationDialog(compteEpargne, "Opération recursive", "Opération recursive",
					"La désactivation d'un compte épargne de " + compteEpargne.getAdherent().getUniqueName()
							+ "entrainera respectivement la désactivation de son profil en tant que membre",
					"Valider", "Annuler")) {

				compteEpargne.setStatut("Inactif");
				compteEpargneServiceImplement.update(compteEpargne);
				LoadDataOnTable();
				// Désactive l'adhérent titulaire du compte
				Adherent adherent = compteEpargne.getAdherent();
				adherent.setSituation("Inactif");
				adherentServiceImpl.update(adherent);

				btnDesactivate.setDisable(true);

				MethodUtilitaire.saveAlert(event, "Désactivation réussie", "Désactivation réussie");
				methodUtilitaire.LogFile("Désactivation compte epargne",
						compteEpargne.getAdherent().getNom() + " " + compteEpargne.getAdherent().getPrenom(),
						MethodUtilitaire.deserializationUser(), new Date(System.currentTimeMillis()));
			}

		} else {

			System.out.println("Trim: " + memberId.getEditor().getText().trim().length());
			if (memberId.getEditor().getText().trim().length() > 0) {
				MethodUtilitaire.deleteNoPersonSelectedAlert("Attention titulaire de compte innexistant",
						"Attention titulaire de compte innexistant",
						"SVP vérifier bien l'orthographe du nom et assurer vous de la correspondance parmi les noms figurant dans la liste des titulaire de compte  ");
			} else {
				MethodUtilitaire.deleteNoPersonSelectedAlert("Attention aucun titulaire de compte sélectionné",
						"Attention aucun titulaire de compte sélectionné",
						"SVP sélectionné un titulaire de compte et réessayer.");
			}

		}

	}

	private void checkCreateAccount() {
		adherentUniqueName = new ArrayList<>();
		List<Adherent> adherents = adherentRepository.findBySituation("Actif");

		if (adherents != null) {
			for (Adherent adherent : adherents) {
				// On se rassure qu'il n'as pas de compte épargne
				if (compteEpargneRepository.findByAdherent(adherent) == null) {
					adherentUniqueName.add(adherent.getUniqueName());
				}

			}
			
			if(adherentUniqueName.size() == 0) {
				btnCreer.setDisable(true);
			}
			
		}

	}

	// Event Listener on Button.onAction
	@FXML
	public void handleCreateClick(ActionEvent event) throws Exception {

		ChoiceDialog<String> dialog = new ChoiceDialog<>("Dérouler la liste", adherentUniqueName);
		dialog.setTitle("Création d'un compte épargne");
		dialog.setHeaderText("Création d'un compte épargne");
		dialog.setContentText("Titulaire du compte:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();

		/*
		 * if (result.isPresent()){ System.out.println("Your choice: " + result.get());
		 * }
		 */

		// The Java 8 way to get the response value (with lambda expression).
		result.ifPresent(new Consumer<String>() {

			@Override
			public void accept(String name) {

				try {
					createAccount(name);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private void createAccount(String name) throws UnknownHostException, ClassNotFoundException, IOException {

				Adherent adherent = adherentRepository.findByUniqueName(name);

				if (adherent != null) {
					CompteTampon compteTampon = new CompteTampon();
					compteTampon.setIdTampon(adherent.getIdentifiant());
					compteTampon.setDette((long) 0);

					CompteCreance compteCreance = new CompteCreance();
					compteCreance.setIdCreance(adherent.getIdentifiant());
					compteCreance.setMontant((long) 0);

					CompteEpargne compteEpargne = new CompteEpargne();
					compteEpargne.setAdherent(adherent);
					compteEpargne.setAvaliser(false);
					compteEpargne.setEpargneId(adherent.getIdentifiant());
					compteEpargne.setCompteCreance(compteCreance);
					compteEpargne.setCompteTampon(compteTampon);
					compteEpargne.setStatut("Actif");
					compteEpargne.setDateCreation(new Date(System.currentTimeMillis()));

					compteTamponRepository.save(compteTampon);
					compteCreanceRepository.save(compteCreance);
					CompteEpargne newCompteEpargne = compteEpargneRepository.save(compteEpargne);

					MethodUtilitaire.saveAlert(newCompteEpargne, "Save compte epargne sucessful",
							"Bank account of " + newCompteEpargne.getAdherent().getNom() + " "
									+ newCompteEpargne.getAdherent().getPrenom() + " created successful");

					LoadDataOnTable();
					checkCreateAccount();

					methodUtilitaire.LogFile("Création du fond du compte bancaire",
							compteEpargne.getAdherent().getNom() + " " + compteEpargne.getAdherent().getPrenom(),
							MethodUtilitaire.deserializationUser(), compteEpargne.getDateCreation());
				} else {
					MethodUtilitaire.deleteNoPersonSelectedAlert("Aucun adhérent sélectionné",
							"Attention aucun adhérent sélectionné",
							"Sélectionné un adhérent dans la liste déroulante puis reéssayer");
					try {
						handleCreateClick(event);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleSearchReleased(KeyEvent event) {
		filteredTable(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		checkCreateAccount();
		compteEpargneTable.setCursor(Cursor.HAND);
		consulterEngagement.setVisible(false);
		btnDesactivate.setDisable(true);
		SetTableColunProperties();
		LoadDataOnTable();
		compteEpargneTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CompteEpargne>() {
			@Override
			public void changed(ObservableValue<? extends CompteEpargne> observable, CompteEpargne oldValue,
					CompteEpargne newValue) {
				// utiliser dans autres function
				selectedAccount = newValue;
				btnDesactivate.setDisable(false);
				showCompteEpargneDetail(newValue);

			}
		});
	}

	public void LoadDataOnTable() {

		compteEpargneList.clear();
		compteEpargneList.addAll(compteEpargneRepository.findByStatut("Actif"));
		compteEpargneTable.setItems(compteEpargneList);
	}

	private void showCompteEpargneDetail(CompteEpargne newValue) {

		if (newValue != null) {
			
		    engagementList = avaliseRepository.findCompteByIdAndStatut(newValue, false, newValue.getCompteTampon());
			
			consulterEngagement.setVisible(true);
			idLabel.setText(newValue.getAdherent().getIdentifiant().toString());
			nomLabel.setText(newValue.getAdherent().getNom());
			prenomLabel.setText(newValue.getAdherent().getPrenom());
			soldeLabel.setText(String.valueOf(newValue.getSolde()));
			credibiliteLabel.setText(String.valueOf(newValue.getLacarte()));

			if (engagementList.size() > 0) {
				consulterEngagement.setText("Consulter");
				consulterEngagement.setDisable(false);
				consulterEngagement.setVisible(true);
			} else {
				consulterEngagement.setText("Aucun engagement");
				consulterEngagement.setDisable(true);
			}

			Long pret = newValue.getCompteTampon().getDette();
			pretLabel.setText(String.valueOf(pret));
		} else {
			idLabel.setText(null);
			consulterEngagement.setVisible(false);
			nomLabel.setText(null);
			prenomLabel.setText(null);
			soldeLabel.setText(null);
			credibiliteLabel.setText(null);
			pretLabel.setText(null);
		}

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
		credibiliteTableColun.setCellValueFactory(new PropertyValueFactory<>("lacarte"));
	}

	/*
	 * private void FilterValueOnComboboxMemberIdf() {
	 * 
	 * // Create a FilteredList wrapping the ObservableList. FilteredList<String>
	 * filteredItems = new FilteredList<String>(adherentsList, p -> true);
	 * 
	 * // Add a listener to the textProperty of the combobox editor. The // listener
	 * will simply filter the list every time the input is changed // as long as the
	 * user hasn't selected an item in the list.
	 * memberId.getEditor().textProperty().addListener((obs, oldValue, newValue) ->
	 * { final TextField editor = memberId.getEditor(); final String selected =
	 * memberId.getSelectionModel().getSelectedItem();
	 * 
	 * // This needs run on the GUI thread to avoid the error described // here:
	 * https://bugs.openjdk.java.net/browse/JDK-8081700. Platform.runLater(() -> {
	 * // If the no item in the list is selected or the selected item // isn't equal
	 * to the current input, we refilter the list. if (selected == null ||
	 * !selected.equals(editor.getText())) { filteredItems.setPredicate(item -> { //
	 * We return true for any items that starts with the // same letters as the
	 * input. We use toUpperCase to // avoid case sensitivity. if
	 * (item.toUpperCase().startsWith(newValue.toUpperCase())) { return true; } else
	 * { return false; } }); } }); });
	 * 
	 * memberId.setItems(filteredItems);
	 * 
	 * }
	 */

	private String getFond() {
		return fond.getText();
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

}
