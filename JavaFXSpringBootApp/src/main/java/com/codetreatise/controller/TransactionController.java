package com.codetreatise.controller;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.Transaction;
import com.codetreatise.config.StageManager;
import com.codetreatise.repository.AdherentRepository;
import com.codetreatise.repository.AvaliseRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.repository.TransactionRepository;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.CompteEpargneServiceImplement;
import com.codetreatise.service.impl.TransactionServiceImplement;
import com.codetreatise.view.FxmlView;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

@Controller
public class TransactionController implements Initializable {
	@FXML
	private ComboBox<String> membeEmetteurId;
	@FXML
	private TextField id;
	@FXML
	private Button btnClear;
	@FXML
	private Button btnUndo;
	@FXML
	private Button btnValidate;
	@FXML
	private TextField montant;
	@FXML
	private DatePicker date;
	@FXML
	private ComboBox<String> memberDestinataireId;
	@FXML
	private Button btnDepot;
	@FXML
	private Button btnRetrait;
	@FXML
	private Button btnRembourssement;
	@FXML
	private TableView<Transaction> transactionTable;
	@FXML
	private TableColumn<Transaction, Long> idTransactionTableColumn;
	@FXML
	private TableColumn<Transaction, String> idAdherentTableColumn;
	@FXML
	private TableColumn<Transaction, String> nomTableColumn;
	@FXML
	private TableColumn<Transaction, String> typeTableColumn;
	@FXML
	private TableColumn<Transaction, Float> montantTableColumn;
	@FXML
	private TableColumn<Transaction, Date> dateTableColumn;
	@FXML
	private TextField search;

	private boolean check;
	private String operation = "any";

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CompteEpargneRepository compteEpargneRepository;

	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement;

	@Autowired
	private AdherentRepository adherentRepository;
	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private AvaliseRepository avaliseRepository;

	@Autowired
	private MethodUtilitaire methodUtilitaire;

	ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
	ObservableList<String> adherentsList = FXCollections.observableArrayList();

	// Appeler depuis AvaliseEmprintEditDialogController car clearField supprime les
	// données des textFields
	float withDrawAmont;
	Adherent adherentEmetteur;
	Float laCarte;
	CompteEpargne compteEpargneEmetteur;

	public CompteEpargne getAccountEpargneEmetteur() {
		return compteEpargneEmetteur;
	}
	public Float getLaCarte() {
		return laCarte;
	}

	public Adherent getAdhEmetteur() {
		return adherentEmetteur;
	}

	public Float getWithDrawAmont() {
		return withDrawAmont;
	}

	// Appeler depuis AvaliseEmprintEditDialogController car clearField supprime les
	// données des textFields

	public String getOperation() {
		return operation;
	}

	// Event Listener on Button[#btnClear].onAction
	@FXML
	public void handleClearClick(ActionEvent event) {
		clearField();
	}

	// Event Listener on Button[#btnUndo].onAction
	@FXML
	public void handleUndoClick(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on Button[#btnValidate].onAction
	@FXML
	public void handleValidateClick(ActionEvent event) throws Exception {
		if (operation != null) {
			switch (operation) {
			case "rembourssement":
				handleRembourssement(event);
				//virement();
				break;

			case "pret":
				pret();
				break;

			case "depot":
				depot();
				break;

			default:
				MethodUtilitaire.deleteNoPersonSelectedAlert("ATTENTION AUCUNE TRANSACTION ACTIVE",
						"ATTENTION AUCUNE TRANSACTION ACTIVE", "Activé soit le dépot, retrait ou virement");
				break;
			}
		}
	}

	// Event Listener on Button[#btnDepot].onAction

	@FXML
	public void handleDepotPressed(MouseEvent event) {
		operation = "depot";
		btnDepot.setStyle("-fx-background-color: red;");
		btnRembourssement.setStyle("-fx-background-color: #1d1d1d;");
		btnRetrait.setStyle("-fx-background-color: #1d1d1d;");
		System.out.println("depot");
		memberDestinataireId.getSelectionModel().clearSelection();
		memberDestinataireId.setDisable(true);
	}

	// Event Listener on Button[#btnRetrait].onAction
	@FXML
	public void handleRetraitPressed(MouseEvent event) {
		operation = "pret";
		btnRetrait.setStyle("-fx-background-color: red;");
		btnRembourssement.setStyle("-fx-background-color: #1d1d1d;");
		btnDepot.setStyle("-fx-background-color: #1d1d1d;");
		System.out.println("retrait");
		memberDestinataireId.getSelectionModel().clearSelection();
		memberDestinataireId.setDisable(true);
	}

	// Event Listener on Button[#btnVirement].onAction
	@FXML
	public void handleRembourssementPressed(MouseEvent event) {
		operation = "rembourssement";
		btnRembourssement.setStyle("-fx-background-color: red;");
		btnDepot.setStyle("-fx-background-color: #1d1d1d;");
		btnRetrait.setStyle("-fx-background-color: #1d1d1d;");
		memberDestinataireId.setDisable(false);
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

		setCurrentDay();
		transactionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		adherentsList.clear();
		List<CompteEpargne> compteEpargnes = compteEpargneRepository.findByStatut("Actif");
		for (CompteEpargne compteEpargne : compteEpargnes) {
			adherentsList.add(compteEpargne.getAdherent().getUniqueName());
		}

		membeEmetteurId.setItems(adherentsList);
		memberDestinataireId.setItems(adherentsList);

		FilterValueOnComboboxEmetteurId(membeEmetteurId);
		FilterValueOnComboboxEmetteurId(memberDestinataireId);
	}

	private void SetTableColunProperties() {
		idTransactionTableColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
		idAdherentTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Transaction, String> param) {

						return new SimpleStringProperty(param.getValue().getAdherent().getIdentifiant().toString());
					}
				});
		nomTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Transaction, String> param) {
						return new SimpleStringProperty(param.getValue().getAdherent().getNom());
					}
				});
		typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		montantTableColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
		dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
	}

	public void LoadDataOnTable() {

		transactionList.clear();
		transactionList.addAll(transactionRepository.findAll());
		transactionTable.setItems(transactionList);
	}

	public void clearField() {
		id.clear();
		membeEmetteurId.getSelectionModel().clearSelection();
		membeEmetteurId.getEditor().clear();
		montant.clear();
		memberDestinataireId.getSelectionModel().clearSelection();
		memberDestinataireId.getEditor().clear();
		btnDepot.setStyle("-fx-background-color: #1d1d1d");
		btnRetrait.setStyle("-fx-background-color: #1d1d1d");
		btnRembourssement.setStyle("-fx-background-color: #1d1d1d");
	}

	private void filteredTable(KeyEvent event) {
		FilteredList<Transaction> filteredTransaction = new FilteredList<Transaction>(transactionList, e -> true);
		search.setOnKeyReleased(e -> {
			search.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredTransaction.setPredicate((Predicate<? super Transaction>) transaction -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String newValueFilter = newValue.toLowerCase();
					if (transaction.getAdherent().getIdentifiant().toString().contains(newValueFilter)) {
						return true;
					} else if (transaction.getAdherent().getNom().toLowerCase().contains(newValueFilter)) {
						return true;
					} else if (transaction.getType().toLowerCase().contains(newValueFilter)) {
						return true;
					}
					return false;
				});
			});
		});

		SortedList<Transaction> sortedList = new SortedList<Transaction>(filteredTransaction);
		sortedList.comparatorProperty().bind(transactionTable.comparatorProperty());
		transactionTable.setItems(sortedList);
	}

	private void FilterValueOnComboboxEmetteurId(ComboBox<String> comboBox) {

		// Create a FilteredList wrapping the ObservableList.
		FilteredList<String> filteredItems = new FilteredList<String>(adherentsList, p -> true);

		// Add a listener to the textProperty of the combobox editor. The
		// listener will simply filter the list every time the input is changed
		// as long as the user hasn't selected an item in the list.
		comboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			final TextField editor = comboBox.getEditor();
			final String selected = comboBox.getSelectionModel().getSelectedItem();

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

		comboBox.setItems(filteredItems);

	}

	private void setCurrentDay() {
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String dat = format.format(new Date());
			date.getEditor().setText(dat);
		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}

	//Cette methode n'est plus utiliser pour le moment car les regles de gestion ne permettent plus 
	//des virements d'un compte épargne à un autre
	@Transactional
	private void virement() throws Exception {
		// On vérifie si l'adhérent doit de l'argent
		 compteEpargneEmetteur = null;
		CompteEpargne compteEpargneDestinataire = null;
		if (MethodUtilitaire.emptyValidation("Emetteur", getEmetteur() == null)
				&& MethodUtilitaire.emptyValidation("Destinataire", getDestinataire() == null)
				&& MethodUtilitaire.validate("Montant", getMontant(), "[0-9]+")
				&& MethodUtilitaire.emptyValidation("Date", getDate() == null)) {
			List<Avalise> avaliseEmetteur = avaliseRepository.findByIdAndStatut(getId(), false);
			System.out.println("aaaaaaaaaaaaaaaaaaa " + avaliseEmetteur.size());
			compteEpargneEmetteur = compteEpargneRepository.findByAdherent(getAdherentEmetteur());
			compteEpargneDestinataire = compteEpargneRepository.findByAdherent(getAdherentDestinataire());
			check = true;
			System.out.println(avaliseEmetteur.size() + "zzzzzzzzzzzzzzzzzzzzzzzzzzz");
			if (avaliseEmetteur.size() == 0) {
				// do action (execute virement)
				System.out.println("dans le if");
				if (MethodUtilitaire.confirmationDialog(null, "Confirmer la transaction", "Confirmer la transaction",
						"Le montant de " + getMontant() + " Fcfa sera transférer du compe de "
								+ compteEpargneEmetteur.getAdherent().getNom() + " "
								+ compteEpargneEmetteur.getAdherent().getPrenom() + " vers le compte de "
								+ compteEpargneDestinataire.getAdherent().getNom() + " "
								+ compteEpargneDestinataire.getAdherent().getPrenom())) {
					if (virementMethod(compteEpargneEmetteur, compteEpargneDestinataire)) {

						Transaction transaction = new Transaction();
						transaction.setAdherent(getAdherentEmetteur());
						transaction.setDate(new Date());
						transaction.setMontant(Float.parseFloat(getMontant()));
						transaction.setType(operation);
						transactionRepository.save(transaction);

						MethodUtilitaire.saveAlert(null, "Virement sucessful",
								"Le montant de " + getMontant() + " à été transférer du compe de "
										+ compteEpargneEmetteur.getAdherent().getNom() + " "
										+ compteEpargneEmetteur.getAdherent().getPrenom() + " vers le compte de "
										+ compteEpargneDestinataire.getAdherent().getNom() + " "
										+ compteEpargneDestinataire.getAdherent().getPrenom());
						check = false;
						operation = "any";
						// on enleve la couleur rouge indiquant l'opération selectionné
						btnRembourssement.setStyle("-fx-background-color:   #1d1d1d;");
						LoadDataOnTable();
						clearField();
						methodUtilitaire.LogFile("Opération de " + operation,
								compteEpargneEmetteur.getAdherent().getNom() + " "
										+ compteEpargneEmetteur.getAdherent().getPrenom() + "-->"
										+ compteEpargneDestinataire.getAdherent().getNom() + " "
										+ compteEpargneDestinataire.getAdherent().getPrenom(),
								MethodUtilitaire.deserializationUser());
					}
				}
			} else if (avaliseEmetteur.size() != 0) {
				System.out.println("dans le else");
				if (MethodUtilitaire.confirmationDialog(avaliseEmetteur, "Rappel emprints non soldé",
						"Mr " + avaliseEmetteur.get(0).getTransaction().getAdherent().getNom() + " "
								+ avaliseEmetteur.get(0).getTransaction().getAdherent().getPrenom()
								+ " à des emprints non soldé",
						"Voulez vous amortir vos dette avec ce virement ?")) {

					// On vérifie que le solde de son du compte epargne contient assez pour cette
					// transaction
					if (compteEpargneEmetteur.getLacarte() >= Float.parseFloat(getMontant())) {
						// do action (switch scene)
						stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.AVALISEEDITDIALOG);
						// editbuttonclic = true;

					} else {
						MethodUtilitaire.deleteNoPersonSelectedAlert("Attention solde de votre plafon insuffisant",
								"Attention solde de votre plafon insuffisant",
								"Le montant autorisé est :" + compteEpargneEmetteur.getSolde() + "Fcfa \n"
										+ " Le montant de la transaction est de : " + Float.parseFloat(getMontant())
										+ "Fcfa");
					}
				} else {
					// do the action ()Execute virement
					if (virementMethod(compteEpargneEmetteur, compteEpargneDestinataire)) {

						Transaction transaction = new Transaction();
						transaction.setAdherent(getAdherentEmetteur());
						transaction.setDate(new Date());
						transaction.setMontant(Float.parseFloat(getMontant()));
						transaction.setType(operation);
						transactionRepository.save(transaction);

						MethodUtilitaire.saveAlert(null, "Virement sucessful",
								"Le montant de " + getMontant() + " à été transférer du compe de "
										+ compteEpargneEmetteur.getAdherent().getNom() + " "
										+ compteEpargneEmetteur.getAdherent().getPrenom() + " vers le compte de "
										+ compteEpargneDestinataire.getAdherent().getNom() + " "
										+ compteEpargneDestinataire.getAdherent().getPrenom());
						check = false;
						operation = "any";
						// on enleve la couleur rouge indiquant l'opération selectionné
						btnRembourssement.setStyle("-fx-background-color:  #1d1d1d;");
						LoadDataOnTable();
						clearField();
						methodUtilitaire.LogFile("Opération de " + operation,
								compteEpargneEmetteur.getAdherent().getNom() + " "
										+ compteEpargneEmetteur.getAdherent().getPrenom() + "-->"
										+ compteEpargneDestinataire.getAdherent().getNom() + " "
										+ compteEpargneDestinataire.getAdherent().getPrenom(),
								MethodUtilitaire.deserializationUser());
					}
				}
			}
		}

	}
	

	@FXML
	private void handleRembourssement(ActionEvent actionEvent) {
		Node node = (Node) actionEvent.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.close();
		stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.REMBOURSSEMENT);
	}

	@Transactional
	private void pret() throws Exception {
		if (MethodUtilitaire.emptyValidation("Emetteur", getEmetteur() == null)
				&& MethodUtilitaire.validate("Montant", getMontant(), "[0-9]+")) {
			 compteEpargneEmetteur = getCompteEpargneEmetteur();
			if (compteEpargneEmetteur != null) {

				// montant de retrait
				withDrawAmont = Float.parseFloat(getMontant());

				if (compteEpargneEmetteur.getLacarte() != 0 && compteEpargneEmetteur.getLacarte() >= withDrawAmont) {

					if (MethodUtilitaire.confirmationDialog(null, "Confirmer la transaction",
							"Confirmer la transaction",
							"Un montant de " + getMontant() + " sera débité dans le compte de "
									+ compteEpargneEmetteur.getAdherent().getNom() + " "
									+ compteEpargneEmetteur.getAdherent().getPrenom())) {
						makeWithDraw(compteEpargneEmetteur, withDrawAmont);
					}
				} else {
					if (compteEpargneEmetteur.getLacarte() == 0.0) {
						if (MethodUtilitaire.confirmationDialog(null, "Solde bancaire insuffisant",
								"Le solde du compte bancaire est insuffisant pour cette transaction",
								"Vous souhaiterez un montant de " + montant.getText()
										+ " Mais votre compte epargne ne vous permet de retirer que "
										+ compteEpargneEmetteur.getLacarte() + "\n Voulez vous emprinter "
										+ withDrawAmont)) {

							stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.AVALISEEMPRINTEDITDIALOG);
							clearField();
						}
					} else {
						if (MethodUtilitaire.confirmationDialog(null, "Solde bancaire insuffisant",
								"Le solde du compte bancaire est insuffisant pour cette transaction",
								"Vous souhaiterez un montant de " + montant.getText()
										+ " Mais votre compte epargne ne vous permet de retirer que "
										+ compteEpargneEmetteur.getLacarte() + "\n Voulez vous rétirer "
										+ compteEpargneEmetteur.getLacarte() + " avant d'emprinter "
										+ (withDrawAmont - compteEpargneEmetteur.getLacarte()))) {

							makeWithDraw(compteEpargneEmetteur, compteEpargneEmetteur.getLacarte());

							clearField();

							stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.AVALISEEMPRINTEDITDIALOG);

						}
					}
				}
			} else {
				MethodUtilitaire.errorMessageAlert("Adhérent émetteur inconnu", "Adhérent émetteur inconnu",
						"Vérifiez que l'hortographe saisi correspond à celle présente dans la liste");
			}

		}
	}

	// Éffectue un retrait
	private void makeWithDraw(CompteEpargne compteEpargneEmetteur, Float montant)
			throws UnknownHostException, ClassNotFoundException, IOException {
		compteEpargneEmetteur.setSolde(compteEpargneEmetteur.getSolde() - montant);
		compteEpargneEmetteur.setLacarte(compteEpargneEmetteur.getLacarte() - montant);

		Transaction transaction = new Transaction();
		transaction.setAdherent(getAdherentEmetteur());
		transaction.setDate(new Date());
		transaction.setMontant(montant);
		transaction.setType(operation);

		transactionRepository.save(transaction);
		compteEpargneServiceImplement.update(compteEpargneEmetteur);
		LoadDataOnTable();
		clearField();
		MethodUtilitaire.saveAlert(compteEpargneEmetteur, "Opération réussi",
				"Retrait de " + montant + " Effectué avec succes");
		operation = "any";
// on enleve la couleur rouge indiquant l'opération selectionné
		btnRetrait.setStyle("-fx-background-color:  #1d1d1d;");
		methodUtilitaire.LogFile("Opération de " + operation,
				compteEpargneEmetteur.getAdherent().getNom() + " " + compteEpargneEmetteur.getAdherent().getPrenom(),
				MethodUtilitaire.deserializationUser());

	}

	@Transactional
	private void depot() throws Exception {
		if (MethodUtilitaire.emptyValidation("Emetteur", getEmetteur() == null)
				&& MethodUtilitaire.validate("Montant", getMontant(), "[0-9]+")) {
			 compteEpargneEmetteur = getCompteEpargneEmetteur();

			if (getId() != null) {
				List<Avalise> avaliseEmetteur = avaliseRepository.findByIdAndStatut(getId(), false);
				if (avaliseEmetteur.size() == 0) {
					if (MethodUtilitaire.confirmationDialog(null, "Confirmer la transaction",
							"Confirmer la transaction",
							"Un montant de " + getMontant() + " sera crédité dans le compte de "
									+ compteEpargneEmetteur.getAdherent().getNom() + " "
									+ compteEpargneEmetteur.getAdherent().getPrenom())) {
						compteEpargneEmetteur
								.setSolde(compteEpargneEmetteur.getSolde() + Float.parseFloat(getMontant()));
						compteEpargneEmetteur
								.setLacarte(compteEpargneEmetteur.getLacarte() + Float.parseFloat(getMontant()));

						Transaction transaction = new Transaction();
						transaction.setAdherent(getAdherentEmetteur());
						transaction.setDate(new Date());
						transaction.setMontant(Float.parseFloat(getMontant()));
						transaction.setType(operation);

						transactionRepository.save(transaction);
						compteEpargneServiceImplement.update(compteEpargneEmetteur);
						LoadDataOnTable();
						clearField();
						MethodUtilitaire.saveAlert(compteEpargneEmetteur, "Opération réussi",
								"Dépot de " + getMontant() + " Effectué avec succes");
						operation = "any";
						// on enleve la couleur rouge indiquant l'opération selectionné
						btnDepot.setStyle("-fx-background-color:  #1d1d1d;");
						methodUtilitaire.LogFile("Opération de " + operation,
								compteEpargneEmetteur.getAdherent().getNom() + " "
										+ compteEpargneEmetteur.getAdherent().getPrenom(),
								MethodUtilitaire.deserializationUser());
					}
				} else {
					if (MethodUtilitaire.confirmationDialog(compteEpargneEmetteur, "Rappel emprints non soldé",
							"Mr " + avaliseEmetteur.get(0).getTransaction().getAdherent().getNom() + " "
									+ avaliseEmetteur.get(0).getTransaction().getAdherent().getPrenom()
									+ " à des emprints non soldé",
							"Voulez vous amortir vos dette avec ce dépot ?")) {
						stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.AVALISEEDITDIALOG);
						
					} else {
						
						//Tant que un adhérent doit de l'argent il ne peux plus crédité son compte
						//donc nous commentons cette fonctionnalité
						
						/**
						
						compteEpargneEmetteur
								.setSolde(compteEpargneEmetteur.getSolde() + Float.parseFloat(getMontant()));
						compteEpargneEmetteur
								.setLacarte(compteEpargneEmetteur.getLacarte() + Float.parseFloat(getMontant()));

						Transaction transaction = new Transaction();
						transaction.setAdherent(getAdherentEmetteur());
						transaction.setDate(new Date());
						transaction.setMontant(Float.parseFloat(getMontant()));
						transaction.setType(operation);

						transactionRepository.save(transaction);
						compteEpargneServiceImplement.update(compteEpargneEmetteur);
						LoadDataOnTable();

						MethodUtilitaire.saveAlert(compteEpargneEmetteur, "Opération réussi",
								"Dépot de " + getMontant() + " Effectué avec successssssssss");
						operation = "any";
						// on enleve la couleur rouge indiquant l'opération selectionné
						btnDepot.setStyle("-fx-background-color:  #1d1d1d;");
						clearField();
						methodUtilitaire.LogFile("Opération de " + operation,
								compteEpargneEmetteur.getAdherent().getNom() + " "
										+ compteEpargneEmetteur.getAdherent().getPrenom(),
								MethodUtilitaire.deserializationUser());
						**/
					}
				}
			} else {
				MethodUtilitaire.errorMessageAlert("Adhérent émetteur inconnu", "Adhérent émetteur inconnu",
						"Vérifiez que l'hortographe saisi correspond à celle présente dans la liste");
			}
		}
	}

	private String getDate() {
		return date.getEditor().getText();
	}

	private String getDestinataire() {
		return memberDestinataireId.getSelectionModel().getSelectedItem();
	}

	public String getMontant() {
		return montant.getText();
	}

	private String getEmetteur() {
		return membeEmetteurId.getSelectionModel().getSelectedItem();
	}

	private Long getId() {
		if (getAdherentEmetteur() != null) {
			return getAdherentEmetteur().getIdentifiant();
		} else {
			return null;
		}

	}

	public Adherent getAdherentEmetteur() {
		adherentEmetteur = adherentRepository.findByUniqueName(getEmetteur());
		if (adherentEmetteur == null) {
			System.out.println(
					"############################################## adhérent null #### unique name " + getEmetteur());
		}
		return adherentEmetteur;
	}

	private Adherent getAdherentDestinataire() {
		Adherent adherent = adherentRepository.findByUniqueName(getDestinataire());
		return adherent;
	}

	public CompteEpargne getCompteEpargneEmetteur() {

		try {
			//On garde ce comte dans une variable globale avant transaction car en cas d'emprint on en aura
			//besoin dans AvaliseEmprintEditDialog
			//si on s'en passe de cette var en pointant directement sur cette fonction, on aura une érreur 
			//nullPointerException vu que clearField() est appeler en fin de transaction 
			//et getAdherentEmetteur() renvera null
			compteEpargneEmetteur = compteEpargneRepository.findByAdherent(getAdherentEmetteur());

			// On recupere la carte avant la transaction; Au cas d'un emprint on pourra
			// déterminer
			// le montant exact de l'emprint dans AvaliseEmprintEditDialog
			// Si on ne recupere pas avant le montant de la carte sera modifier et en cas
			// d'emprint
			// le montant exact de l'emprint sera faussé
			laCarte = compteEpargneEmetteur.getLacarte();
		} catch (Exception e) {
			compteEpargneEmetteur = null;
			e.printStackTrace();
		}
		return compteEpargneEmetteur;
	}

	@javax.transaction.Transactional
	private boolean virementMethod(CompteEpargne compteEmetteur, CompteEpargne compteDestinataire) {

		if (compteEmetteur.getLacarte() < Float.parseFloat(getMontant())) {
			MethodUtilitaire.errorMessageAlert("Solde insuffisant", "Votre solde est insuffisant",
					"Montant de la transaction : " + getMontant() + "\n" + " Montant possible :"
							+ compteEmetteur.getLacarte());
		} else {
			compteEmetteur.setSolde(compteEmetteur.getSolde() - Float.parseFloat(getMontant()));
			compteEmetteur.setLacarte(compteEmetteur.getLacarte() - Float.parseFloat(getMontant()));
			compteEpargneServiceImplement.update(compteEmetteur);

			compteDestinataire.setSolde(compteDestinataire.getSolde() + Float.parseFloat(getMontant()));
			compteDestinataire.setLacarte(compteDestinataire.getLacarte() + Float.parseFloat(getMontant()));
			compteEpargneServiceImplement.update(compteDestinataire);

			return true;
		}

		return false;

	}
}
