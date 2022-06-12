package com.codetreatise.controller;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.CompteCreance;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.CompteEpargneDetail;
import com.codetreatise.bean.CompteTampon;
import com.codetreatise.bean.Transaction;
import com.codetreatise.config.StageManager;
import com.codetreatise.repository.AvaliseRepository;
import com.codetreatise.repository.CompteEpargneDetailRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.repository.CompteTamponRepository;
import com.codetreatise.repository.TransactionRepository;
import com.codetreatise.service.CompteDetailHelper;
import com.codetreatise.service.DateUtil;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.AvaliseServiceImplement;
import com.codetreatise.service.impl.CompteCreanceServiceImplement;
import com.codetreatise.service.impl.CompteEpargneServiceImplement;
import com.codetreatise.service.impl.CompteTamponServiceImplement;
import com.codetreatise.service.impl.TransactionServiceImplement;
import com.codetreatise.view.FxmlView;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

@Controller
public class AvaliseEditDialogController implements Initializable {

	@FXML
	private TextField montant;
	@FXML
	private TableView<Avalise> avaliseRembourssementTable;
	@FXML
	private TableColumn<Avalise, Long> idTableColumn;
	@FXML
	private TableColumn<Avalise, String> nomTableColumn;
	@FXML
	private TableColumn<Avalise, String> prenomTableColumn;
	@FXML
	private TableColumn<Avalise, Long> montantTableColumn;
	@FXML
	private TableColumn<Avalise, Long> solderTableColumn;
	@FXML
	private TableColumn<Avalise, Long> resteTableColumn;
	@FXML
	private TableColumn<Avalise, Boolean> actionTableColumn;
	// @FXML
	// private TableColumn<Avalise, Boolean> amortirTableColumn;
	@FXML
	private TableColumn<Avalise, Boolean> detailTableColumn;
	@FXML
	private Label montantTransactionLabel;
	@FXML
	private Label montantTransactionRestantLabel;
	@FXML
	private Label nomLabel;
	@FXML
	private Button crediteMonCompteBtn;

	@FXML
	private Label montantCourantLabel;

	private String operation;

	@Autowired
	@Lazy
	private StageManager stageManager;

	@Autowired
	private AvaliseRepository avaliseRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CompteTamponServiceImplement compteTamponServiceImplement;

	@Autowired
	private CompteEpargneRepository compteEpargneRepository;

	@Autowired
	private CompteEpargneDetailRepository compteEpargneDetailRepository;

	@Autowired
	private HomeController homeController;

	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement;

	@Autowired
	private CompteCreanceServiceImplement compteCreanceServiceImplement;

	@Autowired
	private AvaliseServiceImplement avaliseServiceImplement;

	@Autowired
	private TransactionController t;

	@Autowired
	private AvaliseRembourssementMoreDetailController avaliseRembourssementMoreDetailController;

	@Autowired
	private MethodUtilitaire methodUtilitaire;

	private Adherent adherent;
	private CompteEpargne compteEpargneEmetteur;
	private String montantString;

	ObservableList<Avalise> avaliseList = FXCollections.observableArrayList();

	private void setStatutCrediteMonCompteBtn() {
		float montantRestant = Float.parseFloat(montantTransactionRestantLabel.getText());

		if (montantRestant > 0) {
			crediteMonCompteBtn.setDisable(false);
		} else {
			crediteMonCompteBtn.setDisable(true);
		}

	}

	@Transactional
	private void validateActionClick(Avalise avalise) throws Exception {
		// on vérifie que le formulaire est bien rempli
		if (isInputValid(avalise)) {

			if (MethodUtilitaire.confirmationDialog(avalise, "Valider la transaction", "Valider la transaction",
					"L'adhérent " + adherent.getNom() + " " + adherent.getPrenom()
							+ " effectuera un rembourssement de: " + montantCourantLabel.getText()
							+ "Fcfa au compte de " + avalise.getCompteEpargne().getAdherent().getNom() + " "
							+ avalise.getCompteEpargne().getAdherent().getPrenom(),
					"Valider", "Annuler")) {

				Long dette = avalise.getMontant() - avalise.getSolder();

				Transaction savedTransaction;

				// Pour chaque rembourssement on creer une transaction
				// comme sa on pourra recuperer les differentes transaction
				// pour une instance d'avalise qui s'est réglé en plusieurs paiement
				// grace à transaction.getAvalise()

				Transaction transaction = new Transaction();
				transaction.setAdherent(adherent);
				transaction.setDate(new Date());
				transaction.setMontant(Long.parseLong(montantCourantLabel.getText()));
				transaction.setType(operation);
				transaction.setAvalise(avalise);

				if (Float.parseFloat(montantCourantLabel.getText()) == dette) {
					avalise.setRemboursser(true);
					avalise.setSolder(avalise.getMontant());
					avalise.setReste((long) 0);
					montantTransactionRestantLabel
							.setText(String.valueOf(Float.parseFloat(montantTransactionRestantLabel.getText())
									- Float.parseFloat(montantCourantLabel.getText())));

				} else {
					avalise.setRemboursser(false);
					avalise.setSolder(avalise.getSolder() + Long.parseLong(montantCourantLabel.getText()));
					avalise.setReste(avalise.getMontant() - avalise.getSolder());
					montantTransactionRestantLabel
							.setText(String.valueOf(Float.parseFloat(montantTransactionRestantLabel.getText())
									- Float.parseFloat(montantCourantLabel.getText())));

				}

				savedTransaction = transactionRepository.save(transaction);

				// On met a jour l'avalise et les comptes tampon et creance
				Avalise updatedAvalise = avaliseServiceImplement.update(avalise);
				updateCompteTampon(avalise);
				updateCompteCreance(avalise);

				// on met a jour la carte du compte epargne du benefficiare du
				// rembourssement
				CompteEpargne compteEpargneRecepteur = updatedAvalise.getCompteEpargne();
				compteEpargneRecepteur.setLacarte(
						compteEpargneRecepteur.getLacarte() + Long.parseLong(montantCourantLabel.getText()));

				// si ce compte epargne ne sert plus d'avalise pour de quelconque pret d'argent,
				// on redéfinit l'attribut avaliser a faux.
				if (compteEpargneRecepteur.getLacarte() == compteEpargneRecepteur.getSolde())
					compteEpargneRecepteur.setAvaliser(false);
				CompteEpargne savedCompteEpargne = compteEpargneServiceImplement.update(compteEpargneRecepteur);

				// Compte epargneDetail
				String date = DateUtil.format(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

				// Pour celui qui recoit le rembourssement
			
				CompteEpargneDetail cd = CompteDetailHelper.factoryCompteEpargneDetail(savedCompteEpargne, date);

				// Pour celui qui remboursse
				CompteEpargne compteEpargneDuRemboursseur = compteEpargneRepository
						.findByAdherent(updatedAvalise.getTransaction().getAdherent());
				
				CompteEpargneDetail cde = CompteDetailHelper.factoryCompteEpargneDetail(compteEpargneDuRemboursseur, date);

				// si c'est le membre qui remboursse sont propre pret (il est son propre garant)
				// Dans ce cas on fait une seule sauvegarde de l'un des deux CompteEpargneDetail
				if (cd.getCompteEpargne().getEpargneId().equals(cde.getCompteEpargne().getEpargneId())) {
					compteEpargneDetailRepository.save(cd);
					System.out.println("pareil");
				} else {
					compteEpargneDetailRepository.save(cd);
					compteEpargneDetailRepository.save(cde);
					System.out.println("différent");
				}

				setStatutCrediteMonCompteBtn();
				MethodUtilitaire.saveAlert(savedTransaction, "Rembourssement éffectué avec succèss",
						"L'adhérent " + adherent.getNom() + " " + adherent.getPrenom() + " à remboursser "
								+ montantCourantLabel.getText() + "Fcfa");
				montantCourantLabel.setText("");
				LoadDataOnTable();

				// On rafraichi la table des transactions
				t.LoadDataOnTable();

				// Met a jour les détails des actif sur la home
				homeController.setActifDetail();

				methodUtilitaire.LogFile("Opération de " + operation,
						adherent.getNom() + " " + adherent.getPrenom() + " <<remboursse a>> "
								+ compteEpargneRecepteur.getAdherent().getNom() + " "
								+ compteEpargneRecepteur.getAdherent().getPrenom(),
						MethodUtilitaire.deserializationUser(), savedTransaction.getDate());
			} else {
				Float montantRestant = (Float.parseFloat(montantTransactionRestantLabel.getText())
						+ Float.parseFloat(montantCourantLabel.getText()));
				montantTransactionRestantLabel.setText(String.valueOf(montantRestant));
				montantCourantLabel.setText(null);
			}

		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setRequiredValue(t.getAdherentEmetteur(), t.getMontant(), t.getOperation(), t.getAccountEpargneEmetteur());
		setInitialValue();
		setStatutCrediteMonCompteBtn();
		setTableColumnProperty();
		LoadDataOnTable();
	}

	public void setRequiredValue(Adherent adherent, String montant, String operation, CompteEpargne compteEpargne) {
		this.adherent = adherent;
		this.montantString = montant;
		this.operation = operation;
		this.compteEpargneEmetteur = compteEpargne;
	}

	private void setInitialValue() {
		montantTransactionLabel.setText(montantString);
		montantTransactionRestantLabel.setText(montantString);
		nomLabel.setText(adherent.getNom() + " " + adherent.getPrenom());
	}

	private CompteTampon updateCompteTampon(Avalise avalise) {
		CompteTampon associatedCompteTampon = avalise.getCompteTampon();
		associatedCompteTampon
				.setDette(associatedCompteTampon.getDette() - Long.parseLong(montantCourantLabel.getText()));
		CompteTampon updatedCompteTampon = compteTamponServiceImplement.update(associatedCompteTampon);
		return updatedCompteTampon;
	}

	private CompteCreance updateCompteCreance(Avalise avalise) {
		CompteCreance associatedCompteCreance = avalise.getCompteCreance();
		associatedCompteCreance
				.setMontant(associatedCompteCreance.getMontant() - Long.parseLong(montantCourantLabel.getText()));
		CompteCreance updatedCompteCreance = compteCreanceServiceImplement.update(associatedCompteCreance);
		return updatedCompteCreance;
	}

	private void LoadDataOnTable() {
		avaliseList.clear();
		avaliseList.addAll(avaliseRepository.findByIdAndStatutList(adherent.getIdentifiant(), false));
		avaliseRembourssementTable.setItems(avaliseList);
	}

	private void setTableColumnProperty() {
		idTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Avalise, Long>, ObservableValue<Long>>() {

					@Override
					public ObservableValue<Long> call(CellDataFeatures<Avalise, Long> param) {

						return new ReadOnlyObjectWrapper<Long>(
								param.getValue().getCompteEpargne().getAdherent().getIdentifiant());
					}
				});
		nomTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Avalise, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Avalise, String> param) {
						return new SimpleStringProperty(param.getValue().getCompteEpargne().getAdherent().getNom());
					}
				});
		prenomTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Avalise, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Avalise, String> param) {
						return new SimpleStringProperty(param.getValue().getCompteEpargne().getAdherent().getPrenom());
					}
				});
		montantTableColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
		solderTableColumn.setCellValueFactory(new PropertyValueFactory<>("solder"));
		resteTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Avalise, Long>, ObservableValue<Long>>() {

					@Override
					public ObservableValue<Long> call(CellDataFeatures<Avalise, Long> param) {

						return new ReadOnlyObjectWrapper<Long>(param.getValue().getReste());
					}
				});
		// amortirTableColumn.setCellFactory(cellFactory2);
		actionTableColumn.setCellFactory(cellFactory);
		detailTableColumn.setCellFactory(cellFactory3);
	}

	@FXML
	private void handleCrediteMonCompte(ActionEvent actionEvent)
			throws UnknownHostException, ClassNotFoundException, IOException {

		operation = "depot";

		// Les différents rembourssements éffectués sur cette scene pourront dans
		// certains cas
		// modifier lacarte(credibilité du remboursseur) contenu dans la var
		// compteEpargneEmetteur
		// qui à reçu sa valeur venant de transactionController depuis la fonction
		// setRequired()
		// donc nous devons l'actualisé pour recuperer la nouvelle carte si elle a
		// changer.
		compteEpargneEmetteur = compteEpargneRepository.findOne(compteEpargneEmetteur.getAdherent().getIdentifiant());

		try {
			t.depot(compteEpargneEmetteur, Long.parseLong(montantTransactionRestantLabel.getText()), true);
		} catch (Exception e) {

			e.printStackTrace();
		}

		montantTransactionRestantLabel.setText("0");

		t.LoadDataOnTable();

		setStatutCrediteMonCompteBtn();
	}

	Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>> cellFactory = new Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>>() {
		@Override
		public TableCell<Avalise, Boolean> call(final TableColumn<Avalise, Boolean> param) {
			final TableCell<Avalise, Boolean> cell = new TableCell<Avalise, Boolean>() {
				// Image imgEdit = new
				// Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button("Remboursser");

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {

						btnEdit.setOnAction(e -> {
							Avalise avalise = getTableView().getItems().get(getIndex());

							TextInputDialog dialog = new TextInputDialog("");

							dialog.setTitle("Rembourssement");
							dialog.setHeaderText("Enter le montant");
							dialog.setContentText("Montant:");

							Optional<String> result = dialog.showAndWait();

							if (result.isPresent())

								result.ifPresent(name -> {
									if (name.trim() != null) {
										Float current = Float.parseFloat(montantTransactionRestantLabel.getText());
										try {

											Long dette = avalise.getMontant() - avalise.getSolder();

											if (Float.parseFloat(name) > dette) {
												MethodUtilitaire.deleteNoPersonSelectedAlert("Montant Faussé",
														"Le montant du rembourssement saisi est superieur a la dette",
														"Le montant du rembourssement saisi est superieur a la dette. \n la dette est "
																+ dette + " Vous avez entrer " + name);
											} else if (Float.parseFloat(name) > current) {
												MethodUtilitaire.deleteNoPersonSelectedAlert("Attention limite atteind",
														"Attention limite atteind",
														"Votre montant initial était de "
																+ montantTransactionLabel.getText() + "Fcfa \n"
																+ " Il ne vous reste plus que "
																+ montantTransactionRestantLabel.getText() + "Fcfa \n"
																+ "Vous avez entrer " + name + "Fcfa");
											} else {
												montantCourantLabel.setText(String.valueOf(Float.parseFloat(name)));
												try {
													updaterAvalise(avalise);
												} catch (Exception e1) {

													e1.printStackTrace();
												}

											}

										} catch (Exception e2) {
											e2.printStackTrace();
											MethodUtilitaire.deleteNoPersonSelectedAlert("Montant invalide",
													"Montant invalide",
													"Le montant entrer ne correspond pas au format numérique");
										}
									}

								});

						});

						// Pour renitialiser le style par défaut des btn indiquer dans le style.css
						btnEdit.setStyle(null);
						btnEdit.setStyle(
								"-fx-background-color: red;-fx-border-radius: 10px; -fx-background-radius: 10px;");
						// ImageView iv = new ImageView();
						// iv.setImage(imgEdit);
						// iv.setPreserveRatio(true);
						// iv.setSmooth(true);
						// iv.setCache(true);
						// btnEdit.setGraphic(iv);

						setGraphic(btnEdit);
						setAlignment(Pos.CENTER_LEFT);
						setText(null);
					}
				}

				private void updaterAvalise(Avalise avalise) throws Exception {
					validateActionClick(avalise);
				}
			};
			return cell;
		}
	};

	/*
	 * 
	 * private Long getIdDestinataire() { return
	 * Long.valueOf(idDestinataire.getText());d }
	 */

	/**
	 * Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>>
	 * cellFactory2 = new Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise,
	 * Boolean>>() {
	 * 
	 * @Override public TableCell<Avalise, Boolean> call(final TableColumn<Avalise,
	 *           Boolean> param) { final TableCell<Avalise, Boolean> cell = new
	 *           TableCell<Avalise, Boolean>() { final TextField emprinter = new
	 *           TextField();
	 * 
	 * @Override public void updateItem(Boolean check, boolean empty) {
	 *           super.updateItem(check, empty); if (empty) { setGraphic(null);
	 *           setText(null); } else { emprinter.setPromptText("Montant à
	 *           remboursser"); emprinter.setTooltip(new Tooltip("Entrez le montant
	 *           à remboursser")); emprinter.setStyle("-fx-text-box-border: #fff;
	 *           -fx-prompt-text-fill: #fff;");
	 *           emprinter.textProperty().addListener((observable, oldValue,
	 *           newValue) -> {
	 *           montantCourantLabel.getStyleClass().add("label-header");
	 *           montantCourantLabel.setText(newValue); if
	 *           (emprinter.getText().trim().length() != 0) {
	 *           System.out.println(emprinter.getText().trim().length()); Avalise
	 *           selectedAvalise =
	 *           avaliseRembourssementTable.getItems().get(getIndex());
	 *           avaliseRembourssementTable.getItems().removeIf(row -> row !=
	 *           selectedAvalise); System.out.println("bingo"); } else { //
	 *           compteEpargneAvaliseEmprinterTable.setItems(compteEpargnesList);
	 *           System.out.println("No bingo"); LoadDataOnTable(); } });
	 *           setGraphic(emprinter); setAlignment(Pos.CENTER); setText(null); } }
	 * 
	 *           }; return cell; } };
	 **/

	Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>> cellFactory3 = new Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>>() {
		@Override
		public TableCell<Avalise, Boolean> call(final TableColumn<Avalise, Boolean> param) {
			final TableCell<Avalise, Boolean> cell = new TableCell<Avalise, Boolean>() {
				// Image imgEdit = new
				// Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button("Plus de détail");

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						btnEdit.setOnAction(e -> {
							Avalise avalise = getTableView().getItems().get(getIndex());
							avaliseRembourssementMoreDetailController.setAvalise(avalise);
							stageManager
									.switchSceneShowPreviousStageInitOwner(FxmlView.AVALISEREMBOURSSEMENTMOREDETAIL);
							avaliseRembourssementMoreDetailController.display();
						});

						btnEdit.setStyle(""
								+ "-fx-background-color: #0007e2; -fx-border-radius: 10px; -fx-background-radius: 10px;");
						// ImageView iv = new ImageView();
						// iv.setImage(imgEdit);
						// iv.setPreserveRatio(true);
						// iv.setSmooth(true);
						// iv.setCache(true);
						// btnEdit.setGraphic(iv);

						setGraphic(btnEdit);
						setAlignment(Pos.CENTER_LEFT);
						setText(null);
					}
				}

			};
			return cell;
		}
	};

	private boolean isInputValid(Avalise avalise) {
		String errorMessage = "";
		try {
			Float amortir = Float.parseFloat(montantCourantLabel.getText());
			if (montantCourantLabel.getText() == null || montantCourantLabel.getText().trim().length() == 0) {
				errorMessage += "Montant invalide!\n";
			} else if (amortir.isNaN() == true) {
				errorMessage += "Le format du montant à amortir est invalide";
			}
		} catch (Exception e) {
			errorMessage += "Montant du rembourssement invalide";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			MethodUtilitaire.errorMessageAlert("Formulaire invalide", "Vérifier les champs invalide", errorMessage);
			return false;
		}
	}

}
