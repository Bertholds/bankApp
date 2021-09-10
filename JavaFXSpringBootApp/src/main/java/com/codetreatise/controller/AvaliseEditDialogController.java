package com.codetreatise.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.CompteCreance;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.CompteTampon;
import com.codetreatise.bean.Transaction;
import com.codetreatise.repository.AvaliseRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.repository.TransactionRepository;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.AvaliseServiceImplement;
import com.codetreatise.service.impl.CompteCreanceServiceImplement;
import com.codetreatise.service.impl.CompteEpargneServiceImplement;
import com.codetreatise.service.impl.CompteTamponServiceImplement;
import com.codetreatise.service.impl.TransactionServiceImplement;

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
public class AvaliseEditDialogController implements Initializable {

	@FXML
	private TextField montant;
	@FXML
	private TableView<Avalise> avaliseRembourssementTable;
	@FXML
	private TableColumn<Avalise, Long> idAvaliseTableColumn;
	@FXML
	private TableColumn<Avalise, String> nomTableColumn;
	@FXML
	private TableColumn<Avalise, String> prenomTableColumn;
	@FXML
	private TableColumn<Avalise, Float> montantTableColumn;
	@FXML
	private TableColumn<Avalise, Float> solderTableColumn;
	@FXML
	private TableColumn<Avalise, Boolean> actionTableColumn;
	@FXML
	private TableColumn<Avalise, Boolean> amortirTableColumn;
	@FXML
	private Label montantTransactionLabel;
	@FXML
	private Label montantTransactionRestantLabel;
	@FXML
	private Label nomLabel;

	@FXML
	private Label montantCourantLabel;

	private Boolean createTransaction = false;
	private String operation;

	@Autowired
	private AvaliseRepository avaliseRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionServiceImplement transactionServiceImplement;

	@Autowired
	private CompteTamponServiceImplement compteTamponServiceImplement;

	@Autowired
	private CompteEpargneRepository compteEpargneRepository;
	
	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement; 

	@Autowired
	private CompteCreanceServiceImplement compteCreanceServiceImplement;

	@Autowired
	private AvaliseServiceImplement avaliseServiceImplement;

	@Autowired
	private TransactionController t;
	
	@Autowired
	private MethodUtilitaire methodUtilitaire;

	private Adherent adherent;
	private String montantString;

	Transaction transaction = null;
	Transaction savedTransaction = null;

	ObservableList<Avalise> avaliseList = FXCollections.observableArrayList();

	@Transactional
	private void validateActionClick(Avalise avalise) throws Exception{
		// on vérifie que le formulaire est bien rempli
		if (isInputValid(avalise)) {
			Float dette = avalise.getMontant() - avalise.getSolder();

			if (Float.parseFloat(montantCourantLabel.getText()) > dette) {
				MethodUtilitaire.deleteNoPersonSelectedAlert("Montant Faussé",
						"Le montant a remboursser est superieur a la dette",
						"Le montant a remboursser est superieur a la dette; Vérifié le champ amortir");
			} else {
				Float current = Float.parseFloat(montantTransactionRestantLabel.getText());
				if (Float.parseFloat(montantCourantLabel.getText()) > current) {
					MethodUtilitaire.deleteNoPersonSelectedAlert("Attention limite atteind",
							"Vous avaez atteind la limite du montant défini pour cette transaction", "");
				} else {

					// On vérifie si cest la premiere fois qu'il click car pour le rembourssement
					// de plusieur dette avaliser on ne souhaiterai creer plusieurs transaction
					// mais modifier juste le montant de la transaction a chaque fois

					if (createTransaction == false) {
						transaction = new Transaction();
						transaction.setAdherent(adherent);
						transaction.setDate(new Date());
						transaction.setMontant(Float.parseFloat(montantCourantLabel.getText()));
						transaction.setType(operation);

						if (Float.parseFloat(montantCourantLabel.getText()) == dette) {
							avalise.setRemboursser(true);
							System.out.println("Dette = "+dette+" montant courant = "+Float.parseFloat(montantCourantLabel.getText()));
							System.out.println("on est dans le if : remboursser=true");
							avalise.setSolder(avalise.getMontant());
							avaliseServiceImplement.update(avalise);
							montantTransactionRestantLabel
									.setText(String.valueOf(Float.parseFloat(montantTransactionRestantLabel.getText())
											- Float.parseFloat(montantCourantLabel.getText())));
							// LoadDataOnTable();
						} else {
							avalise.setRemboursser(false);
							System.out.println("Dette = "+dette+" montant courant = "+Float.parseFloat(montantCourantLabel.getText()));
							System.out.println("on est dans le if : remboursser=false");
							avalise.setSolder(avalise.getSolder() + Float.parseFloat(montantCourantLabel.getText()));
							montantTransactionRestantLabel
									.setText(String.valueOf(Float.parseFloat(montantTransactionRestantLabel.getText())
											- Float.parseFloat(montantCourantLabel.getText())));
							// LoadDataOnTable();
						}
						
						if (MethodUtilitaire.confirmationDialog(transaction, "VALIDER LA TRANSACTION",
								"VALIDER LA TRANSACTION", "L'adhérent " + adherent.getNom() + " " + adherent.getPrenom()
										+ " effectuera un rembourssement de: " + montantCourantLabel.getText() + "Fcfa")) {
							savedTransaction = transactionRepository.save(transaction);
							
							//On met a jour l'avalise et les comptes tampon et creance
							avaliseServiceImplement.update(avalise);
							updateCompteTampon(avalise);
							updateCompteCreance(avalise);
							
							//on met a jour la carte du compte epargne du remboursseur de dette
							if(operation != "depot") {
								CompteEpargne compteEpargneEmetteur = compteEpargneRepository 
										.findOne(avalise.getTransaction().getAdherent().getIdentifiant());
								compteEpargneEmetteur.setSolde(compteEpargneEmetteur.getSolde() - Float.parseFloat(montantCourantLabel.getText()));
								compteEpargneEmetteur.setLacarte(compteEpargneEmetteur.getLacarte() - Float.parseFloat(montantCourantLabel.getText()));
								compteEpargneServiceImplement.update(compteEpargneEmetteur);
							}
							
							//on met egalement a jour la carte du compte epargne du benefficiare du rembourssement
							CompteEpargne compteEpargneRecepteur = avalise.getCompteEpargne();
							compteEpargneRecepteur.setLacarte(compteEpargneRecepteur.getLacarte() + Float.parseFloat(montantCourantLabel.getText()));
							System.out.println("carte total :"+compteEpargneRecepteur.getLacarte());
							
							//si ce compte epargne ne sert plus d'avalise pour de quelconque pret d'argent, on redéfinit l'attribut avaliser a faux.
							if(compteEpargneRecepteur.getLacarte() == compteEpargneRecepteur.getSolde())
								compteEpargneRecepteur.setAvaliser(false);
							compteEpargneServiceImplement.update(compteEpargneRecepteur);
							
							//permet de mettre a jour la transaction et non d'en creer une autre lors des rembourssement suivant pour le meme adherent.
							createTransaction = true;
							MethodUtilitaire.saveAlert(savedTransaction, "REMBOURSSEMENT EFFECTUE AVEC SUCCES",
									"L'adhérent " + adherent.getNom() + " " + adherent.getPrenom() + " à remboursser "
											+ montantCourantLabel.getText() + "Fcfa");
							montantCourantLabel.setText("");
							LoadDataOnTable();
							methodUtilitaire.LogFile("Opération de " + operation,
									adherent.getNom() + " " + adherent.getPrenom() + " <<remboursse a>> "
											+ compteEpargneRecepteur.getAdherent().getNom() + " "
											+ compteEpargneRecepteur.getAdherent().getPrenom(),
									MethodUtilitaire.deserializationUser());
						}

					} else {
						transaction = savedTransaction;
						transaction.setMontant(transaction.getMontant() + Float.parseFloat(montantCourantLabel.getText()));
						if (Float.parseFloat(montantCourantLabel.getText()) == dette) {
							avalise.setRemboursser(true);
							System.out.println("on est dans le else : remboursser=true");
							avalise.setSolder(avalise.getMontant());
							montantTransactionRestantLabel
									.setText(String.valueOf(Float.parseFloat(montantTransactionRestantLabel.getText())
											- Float.parseFloat(montantCourantLabel.getText())));
							// LoadDataOnTable();
						} else {
							avalise.setRemboursser(false);
							System.out.println("on est dans le else : remboursser=false");
							avalise.setSolder(avalise.getSolder() + Float.parseFloat(montantCourantLabel.getText()));
							montantTransactionRestantLabel
									.setText(String.valueOf(Float.parseFloat(montantTransactionRestantLabel.getText())
											- Float.parseFloat(montantCourantLabel.getText())));
							// LoadDataOnTable();
						}
						
						if (MethodUtilitaire.confirmationDialog(transaction, "VALIDER LA TRANSACTION",
								"VALIDER LA TRANSACTION", "L'adhérent " + adherent.getNom() + " " + adherent.getPrenom()
										+ " effectuera un rembourssement de: " + montantCourantLabel.getText() + "Fcfa")) {
		
									savedTransaction = transactionServiceImplement.update(transaction);
								
									//On met a jour l'avalise et les comptes tampon et creance
									avaliseServiceImplement.update(avalise);
									updateCompteTampon(avalise);
									updateCompteCreance(avalise);
									
									
									//on met a jour la carte du compte epargne du remboursseur de dette
									if(operation != "depot") {
										CompteEpargne compteEpargneEmetteur = compteEpargneRepository 
												.findOne(avalise.getTransaction().getAdherent().getIdentifiant());
										compteEpargneEmetteur.setLacarte(compteEpargneEmetteur.getLacarte() - Float.parseFloat(montantCourantLabel.getText()));
										compteEpargneEmetteur.setSolde(compteEpargneEmetteur.getSolde() - Float.parseFloat(montantCourantLabel.getText()));
										compteEpargneServiceImplement.update(compteEpargneEmetteur);
									}
									
									
									//on met egalement a jour la carte du compte epargne du benefficiare du rembourssement
									CompteEpargne compteEpargneRecepteur = avalise.getCompteEpargne();
									compteEpargneRecepteur.setLacarte(compteEpargneRecepteur.getLacarte() + Float.parseFloat(montantCourantLabel.getText()));
									System.out.println("carte total :"+compteEpargneRecepteur.getLacarte());
									
									//si ce compte epargne ne sert plus d'avalise pour de quelconque pret d'argent, on redéfinit l'attribut avaliser a faux.
									if(compteEpargneRecepteur.getLacarte() == compteEpargneRecepteur.getSolde())
										compteEpargneRecepteur.setAvaliser(false);
									
									compteEpargneServiceImplement.update(compteEpargneRecepteur);
									
									//permet de mettre a jour la transaction et non d'en creer une autre lors des rembourssement suivant pour le meme adherent.
									createTransaction = true;
							MethodUtilitaire.saveAlert(savedTransaction, "REMBOURSSEMENT EFFECTUE AVEC SUCCES",
									"L'adhérent " + adherent.getNom() + " " + adherent.getPrenom() + " à remboursser "
											+ montantCourantLabel.getText() + "Fcfa");
							montantCourantLabel.setText("");
							LoadDataOnTable();
							methodUtilitaire.LogFile("Opération de " + operation,
									adherent.getNom() + " " + adherent.getPrenom() + "-->"
											+ compteEpargneRecepteur.getAdherent().getNom() + " "
											+ compteEpargneRecepteur.getAdherent().getPrenom(),
									MethodUtilitaire.deserializationUser());
						}
					}
				}
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setRequiredValue(t.getAdherentEmetteur(), t.getMontant(), t.getOperation());
		montantTransactionLabel.setText(montantString);
		montantTransactionRestantLabel.setText(montantString);
		nomLabel.setText(adherent.getNom() + " " + adherent.getPrenom());
		setTableColumnProperty();
		LoadDataOnTable();
	}

	public void setRequiredValue(Adherent adherent, String montant, String operation) {
		this.adherent = adherent;
		this.montantString = montant;
		this.operation = operation;
	}

	private CompteTampon updateCompteTampon(Avalise avalise) {
		CompteTampon associatedCompteTampon = avalise.getCompteTampon();
		associatedCompteTampon
				.setDette(associatedCompteTampon.getDette() - Float.parseFloat(montantCourantLabel.getText()));
		CompteTampon updatedCompteTampon = compteTamponServiceImplement.update(associatedCompteTampon);
	    return updatedCompteTampon;
	}

	private CompteCreance updateCompteCreance(Avalise avalise) {
		CompteCreance associatedCompteCreance = avalise.getCompteCreance();
		associatedCompteCreance
				.setMontant(associatedCompteCreance.getMontant() - Float.parseFloat(montantCourantLabel.getText()));
		CompteCreance updatedCompteCreance = compteCreanceServiceImplement.update(associatedCompteCreance);
	     return updatedCompteCreance;
	}

	private void LoadDataOnTable() {
		avaliseList.clear();
		avaliseList.addAll(avaliseRepository.findByIdAndStatutList(adherent.getIdentifiant(), false));
		avaliseRembourssementTable.setItems(avaliseList);
	}

	private void setTableColumnProperty() {
		idAvaliseTableColumn.setCellValueFactory(new PropertyValueFactory<>("avalise_id"));
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
		amortirTableColumn.setCellFactory(cellFactory2);
		actionTableColumn.setCellFactory(cellFactory);
	}

	Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>> cellFactory = new Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>>() {
		@Override
		public TableCell<Avalise, Boolean> call(final TableColumn<Avalise, Boolean> param) {
			final TableCell<Avalise, Boolean> cell = new TableCell<Avalise, Boolean>() {
				Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button("AMORTIR");

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						btnEdit.setOnAction(e -> {
							Avalise avalise = getTableView().getItems().get(getIndex());
							try {
								updaterAvalise(avalise);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						});

						btnEdit.setStyle("-fx-background-color: transparent;");
						ImageView iv = new ImageView();
						iv.setImage(imgEdit);
						iv.setPreserveRatio(true);
						iv.setSmooth(true);
						iv.setCache(true);
						btnEdit.setGraphic(iv);

						setGraphic(btnEdit);
						setAlignment(Pos.CENTER);
						setText(null);
					}
				}

				private void updaterAvalise(Avalise avalise) throws Exception{
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

	Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>> cellFactory2 = new Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>>() {
		@Override
		public TableCell<Avalise, Boolean> call(final TableColumn<Avalise, Boolean> param) {
			final TableCell<Avalise, Boolean> cell = new TableCell<Avalise, Boolean>() {
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
							if (emprinter.getText().trim().length() != 0) {
								System.out.println(emprinter.getText().trim().length());
								Avalise selectedAvalise = avaliseRembourssementTable.getItems().get(getIndex());
								avaliseRembourssementTable.getItems().removeIf(row -> row != selectedAvalise);
								System.out.println("bingo");
							} else {
								// compteEpargneAvaliseEmprinterTable.setItems(compteEpargnesList);
								System.out.println("No bingo");
								LoadDataOnTable();
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
