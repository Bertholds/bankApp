package com.codetreatise.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.CompteCreance;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.CompteEpargneDetail;
import com.codetreatise.bean.CompteTampon;
import com.codetreatise.bean.Transaction;
import com.codetreatise.repository.AvaliseRepository;
import com.codetreatise.repository.CompteEpargneDetailRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.repository.TransactionRepository;
import com.codetreatise.service.CompteDetailHelper;
import com.codetreatise.service.DateUtil;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.CompteCreanceServiceImplement;
import com.codetreatise.service.impl.CompteEpargneServiceImplement;
import com.codetreatise.service.impl.CompteTamponServiceImplement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

@Controller
public class AvaliseEmprintEditDialogController implements Initializable {
	@FXML
	private TableView<CompteEpargne> compteEpargneAvaliseEmprinterTable;
	@FXML
	private TableColumn<CompteEpargne, Long> idCompteEpargneTableColumn;
	@FXML
	private TableColumn<CompteEpargne, String> nomTableColumn;
	@FXML
	private TableColumn<CompteEpargne, String> prenomTableColumn;
	@FXML
	private TableColumn<CompteEpargne, Float> soldeTableColumn;
	@FXML
	private TableColumn<CompteEpargne, Boolean> avaliserTableColumn;
	@FXML
	private TableColumn<CompteEpargne, Float> lacarteTableColumn;
	@FXML
	private TableColumn<CompteEpargne, Boolean> emprinterTableColumn;
	@FXML
	private TableColumn<CompteEpargne, Boolean> actionTableColumn;
	@FXML
	private Label montantTransactionLabel;
	@FXML
	private Label montantEmprinterTransactionLabel;
	@FXML
	private Label nomLabel;
	@FXML
	private Label montantCourantLabel;
	@FXML
	private Label garantLabel;
	@FXML
	private TextField idDestinataire;

	private Adherent adherent;
	private String montantString;
	private String operation;
	private CompteEpargne compteEpargneEmprinteur;

	@Autowired
	private TransactionController t;

	@Autowired
	private CompteEpargneRepository compteEpargneRepository;

	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement;

	@Autowired
	private CompteTamponServiceImplement compteTamponServiceImplement;

	@Autowired
	private CompteCreanceServiceImplement compteCreanceServiceImplement;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AvaliseRepository avaliseRepository;
	
	@Autowired
	private CompteEpargneDetailRepository compteEpargneDetailRepository;

	@Autowired
	private MethodUtilitaire methodUtilitaire;
	@Autowired
	private HomeController homeController;

	ObservableList<CompteEpargne> compteEpargnesList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		compteEpargneAvaliseEmprinterTable.setEditable(true);
		setRequiredValue(t.getAdhEmetteur(), String.valueOf(t.getWithDrawAmontAfterCutLaCarte()), t.getOperation(),
				t.getAccountEpargneEmetteur());
		montantTransactionLabel.setText(String.valueOf((Float.parseFloat(montantString))));
		montantCourantLabel.getStyleClass().add("label-bright");
		montantEmprinterTransactionLabel.setText("0.00");
		nomLabel.setText(adherent.getNom() + " " + adherent.getPrenom());
		montantEmprinterTransactionLabel.getStyleClass().add("label-bright");

		setPropertyOnTableColumn();
		LoadDataOnTable();
		System.out.println("initializable");
	}

	private void LoadDataOnTable() {

		try {
			compteEpargnesList.clear();
			List<CompteEpargne> compteEpargnes = compteEpargneRepository.findByLaCartePositive();

			if (compteEpargnes.size() != 0) {
				compteEpargnesList.addAll(compteEpargnes);
				compteEpargneAvaliseEmprinterTable.setItems(compteEpargnesList);
				System.out.println("compteEpargnes !=0 oui");
			} else {
				MethodUtilitaire.deleteNoPersonSelectedAlert("Attention montant indisponible",
						"Attention montant indisponible", "Aucun compte épargne ne dispose de ce montant");
				System.out.println("compteEpargnes = 0 oui");
			}
		} catch (Exception e) {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Attention montant indisponible",
					"Attention montant indisponible", "Aucun compte épargne ne dispose de ce montant");
			e.printStackTrace();
		}
	}

	public void setRequiredValue(Adherent adherent, String montant, String operation,
			CompteEpargne compteEpargneDeCeluiQuiEmprinte) {
		this.adherent = adherent;
		this.montantString = montant;
		this.operation = operation;
		this.compteEpargneEmprinteur = compteEpargneDeCeluiQuiEmprinte;

		System.out.println("montantString: " + montant);
	}

	private void setPropertyOnTableColumn() {
		idCompteEpargneTableColumn.setCellValueFactory(new PropertyValueFactory<>("epargneId"));
		nomTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<CompteEpargne, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<CompteEpargne, String> param) {
						return new SimpleStringProperty(param.getValue().getAdherent().getNom());
					}
				});
		prenomTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<CompteEpargne, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<CompteEpargne, String> param) {
						return new SimpleStringProperty(param.getValue().getAdherent().getPrenom());
					}
				});
		soldeTableColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
		avaliserTableColumn.setCellValueFactory(new PropertyValueFactory<>("avaliser"));
		lacarteTableColumn.setCellValueFactory(new PropertyValueFactory<>("lacarte"));
		emprinterTableColumn.setCellFactory(cellFactory);
		actionTableColumn.setCellFactory(cellFactory2);
	}

	@Transactional
	private void validateActionClick(CompteEpargne compteEpargne, CompteEpargne compteEpargneDeCeluiQuiEmprinte)
			throws Exception {

		// On check si la textField du montant a emprinter est bien rempli
		if (isInputValid()) {
			System.out.println("montant a emprinter disponible " + Float.parseFloat(montantTransactionLabel.getText()));
			System.out.println("Emprinter montant emprinter montant   " + montantEmprinterTransactionLabel.getText());

			if (Float.parseFloat(montantCourantLabel.getText()) > (Float.parseFloat(montantTransactionLabel.getText())
					- Float.parseFloat(montantEmprinterTransactionLabel.getText()))) {
				MethodUtilitaire.deleteNoPersonSelectedAlert(
						"Attention le montant à emprinter est superieur au montant initial",
						"Attention le montant à emprinté ne doit pas etre supérieur au montant initial",
						"Vous avez déja emprinté: " + Float.parseFloat(montantEmprinterTransactionLabel.getText())
								+ "FCFA et il ne vous reste plus que: "
								+ (Float.parseFloat(montantTransactionLabel.getText())
										- Float.parseFloat(montantEmprinterTransactionLabel.getText())));
			} else if (Float.parseFloat(montantCourantLabel.getText()) > compteEpargne.getLacarte()) {
				MethodUtilitaire.deleteNoPersonSelectedAlert("Attention le montant à emprinté est supérieur à la crédibilité de ce compte garant",
						"Attention le montant à emprinté ne doit pas etre supérieur à la crédibilité",
						"Veuillez ajuster le montant du pret au plus a " + compteEpargne.getLacarte() + "FCFA");
			} else {
				Long montant = Long.parseLong(montantCourantLabel.getText());
				if (MethodUtilitaire
						.confirmationDialog(montant, "Valider la transaction", "Valider la transaction",
								"L'adhérent " + adherent.getNom() + " " + adherent.getPrenom()
										+ " effectuera un emprint de: " + montant + "Fcfa",
								"Valider", "Annuler")) {
					
					operation = "pret";
					
					Transaction transaction = new Transaction();
					transaction.setAdherent(adherent);
					transaction.setDate(new Date());
					transaction.setMontant(montant);
					transaction.setType(operation);
					
					CompteTampon compteTampon = compteEpargneDeCeluiQuiEmprinte.getCompteTampon();
					CompteCreance compteCreance = compteEpargne.getCompteCreance();

					compteTampon.setDette(compteTampon.getDette() + Long.parseLong(montantCourantLabel.getText()));
					compteTamponServiceImplement.update(compteTampon);


					compteCreance
							.setMontant(compteCreance.getMontant() + Long.parseLong(montantCourantLabel.getText()));
					compteCreanceServiceImplement.update(compteCreance);

					Transaction savedTransaction = transactionRepository.save(transaction);
					// set la carte de l'avalise
					compteEpargne.setAvaliser(true);
					compteEpargne
							.setLacarte(compteEpargne.getLacarte() - Long.parseLong(montantCourantLabel.getText()));
					CompteEpargne updatedCompteEpargneAvalise = compteEpargneServiceImplement.update(compteEpargne);
					System.out.println("mise a jour réussie");
					
					// Compte epargneDetail

					String date = DateUtil.format(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

					//Pour celui qui met son compte en gage
				
					CompteEpargneDetail cd = CompteDetailHelper.factoryCompteEpargneDetail(updatedCompteEpargneAvalise, date);					
																											
					//Pour celui qui emprinte
					
					CompteEpargneDetail cde = CompteDetailHelper.factoryCompteEpargneDetail(compteEpargneDeCeluiQuiEmprinte, date);																								
																												
				
					compteEpargneDetailRepository.save(cd);
					compteEpargneDetailRepository.save(cde);

					// avalie create
					Avalise avalise = new Avalise();
					avalise.setCompteEpargne(updatedCompteEpargneAvalise);
					avalise.setCompteTampon(compteTampon);
					avalise.setCompteCreance(compteCreance);
					avalise.setMontant(Long.parseLong(montantCourantLabel.getText()));
					avalise.setRemboursser(false);
					avalise.setSolder((long) 0);
					avalise.setTransaction(savedTransaction);
					avaliseRepository.save(avalise);

					montantEmprinterTransactionLabel
							.setText(String.valueOf(Float.parseFloat(montantEmprinterTransactionLabel.getText())
									+ Float.parseFloat(montantCourantLabel.getText())));
					MethodUtilitaire.saveAlert(savedTransaction, "EMPRINT EFFECTUE AVEC SUCCES",
							"L'adhérent " + adherent.getNom() + "" + adherent.getPrenom() + " à emprinter "
									+ savedTransaction.getMontant() + "Fcfa issu du compte de "
									+ compteEpargne.getAdherent().getNom() + " "
									+ compteEpargne.getAdherent().getPrenom());
					LoadDataOnTable();

					// On met a jour la table des transaction
					t.LoadDataOnTable();
					
					//Met a jour les détails des actif sur la home
					homeController.setActifDetail();
					
					methodUtilitaire.LogFile("Opération de " + operation,
							compteEpargneDeCeluiQuiEmprinte.getAdherent().getNom() + " "
									+ compteEpargneDeCeluiQuiEmprinte.getAdherent().getPrenom() + " <<Emprinte a>> "
									+ compteEpargne.getAdherent().getNom() + " "
									+ compteEpargne.getAdherent().getPrenom(),
							MethodUtilitaire.deserializationUser(), savedTransaction.getDate());
				}

			}
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("ATTENTION MONTANT INVALIDE", "ATTENTION MONTANT INVALIDE",
					"Vérifiér le formatage du montant a emprinter et reéssayer");
		}

	}

	private String getEmprinter() {
		return montantCourantLabel.getText();
	}

	private boolean isInputValid() {
		boolean val = false;
		try {
			Float.parseFloat(getEmprinter());
			val = true;
		} catch (Exception e) {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Le format du montant a emprinter est invalide",
					"Le format du montant a emprinter est invalide",
					"Vérifiez le format du montant a emprinter et réessayer");
		}
		return val;
	}

	Callback<TableColumn<CompteEpargne, Boolean>, TableCell<CompteEpargne, Boolean>> cellFactory = new Callback<TableColumn<CompteEpargne, Boolean>, TableCell<CompteEpargne, Boolean>>() {
		@Override
		public TableCell<CompteEpargne, Boolean> call(final TableColumn<CompteEpargne, Boolean> param) {
			final TableCell<CompteEpargne, Boolean> cell = new TableCell<CompteEpargne, Boolean>() {
				final TextField emprinter = new TextField();

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						emprinter.setPromptText("Montant à emprinter");
						emprinter.setTooltip(new Tooltip("Entrez le montant de l'emprint"));
						emprinter.setStyle("-fx-text-box-border: #fff; -fx-prompt-text-fill: #fff;");
						emprinter.textProperty().addListener((observable, oldValue, newValue) -> {
							montantCourantLabel.getStyleClass().add("label-header");
							montantCourantLabel.setText(newValue);
							if (emprinter.getText().trim().length() > 0) {
								System.out.println(emprinter.getText().trim().length());
								CompteEpargne selectedAccount = compteEpargneAvaliseEmprinterTable.getItems()
										.get(getIndex());
								garantLabel.setText(selectedAccount.getAdherent().getUniqueName());
								//compteEpargneAvaliseEmprinterTable.getItems().removeIf(row -> row != selectedAccount);
								System.out.println("bingo");
							} else {
								// compteEpargneAvaliseEmprinterTable.setItems(compteEpargnesList);
								garantLabel.setText(null);
								//LoadDataOnTable();
							}
						});
						setGraphic(emprinter);
						setAlignment(Pos.CENTER);
						setText(null);
					}
				}

			};
			return cell;
		}
	};

	Callback<TableColumn<CompteEpargne, Boolean>, TableCell<CompteEpargne, Boolean>> cellFactory2 = new Callback<TableColumn<CompteEpargne, Boolean>, TableCell<CompteEpargne, Boolean>>() {
		@Override
		public TableCell<CompteEpargne, Boolean> call(final TableColumn<CompteEpargne, Boolean> param) {
			final TableCell<CompteEpargne, Boolean> cell = new TableCell<CompteEpargne, Boolean>() {
				Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button("EMPRINTER");

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						btnEdit.setOnAction(e -> {
							CompteEpargne compteEpargne = getTableView().getItems().get(getIndex());
							try {
								updaterAvalise(compteEpargne, compteEpargneEmprinteur);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						});

						btnEdit.setStyle("-fx-background-color: transparent;");
						ImageView iv = new ImageView();
						//iv.setImage(imgEdit);
						iv.setPreserveRatio(true);
						iv.setSmooth(true);
						iv.setCache(true);
						btnEdit.setGraphic(iv);

						setGraphic(btnEdit);
						setAlignment(Pos.CENTER);
						setText(null);
					}
				}

				private void updaterAvalise(CompteEpargne compteEpargneServantDeAvalise,
						CompteEpargne compteEpargneDeCeluiQuiEmprinte) throws Exception {
					validateActionClick(compteEpargneServantDeAvalise, compteEpargneDeCeluiQuiEmprinte);
				}
			};
			return cell;
		}
	};

}
