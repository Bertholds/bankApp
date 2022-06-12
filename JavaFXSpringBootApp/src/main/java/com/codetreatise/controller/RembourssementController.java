package com.codetreatise.controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

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
import com.codetreatise.repository.TransactionRepository;
import com.codetreatise.service.CompteDetailHelper;
import com.codetreatise.service.DateUtil;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.AvaliseServiceImplement;
import com.codetreatise.service.impl.CompteCreanceServiceImplement;
import com.codetreatise.service.impl.CompteEpargneServiceImplement;
import com.codetreatise.service.impl.CompteTamponServiceImplement;
import com.codetreatise.view.FxmlView;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

@Controller
public class RembourssementController implements Initializable {
	@FXML
	private TableView<Avalise> tableAvaliste;
	@FXML
	private TableColumn<Avalise, Boolean> remboursser;
	@FXML
	private TableColumn<Avalise, String> nomAvaliste;
	@FXML
	private TableColumn<Avalise, String> prenomAvalise;
	@FXML
	private TableColumn<Avalise, Long> montantAvaliste;
	@FXML
	private TableColumn<Avalise, Long> avance;
	@FXML
	private TableColumn<Avalise, Long> reste;
	@FXML
	private TableColumn<Avalise, Boolean> detail;
	@FXML
	private TextField search;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CompteEpargneRepository compteEpargneRepository;

	@Autowired
	private CompteEpargneDetailRepository compteEpargneDetailRepository;

	@Autowired
	private HomeController homeController;

	@Autowired
	private AvaliseServiceImplement avaliseServiceImplement;

	@Autowired
	private CompteEpargneServiceImplement compteEpargneServiceImplement;

	@Autowired
	private CompteCreanceServiceImplement compteCreanceServiceImplement;

	@Autowired
	private CompteTamponServiceImplement compteTamponServiceImplement;

	@Autowired
	private MethodUtilitaire methodUtilitaire;

	@Autowired
	@Lazy
	StageManager stageManager;

	@Autowired
	private AvaliseRembourssementMoreDetailController avaliseRembourssementMoreDetailController;

	@Autowired
	private AvaliseRepository avaliseRepository;

	Long montantTransaction;

	DateFormat format = new SimpleDateFormat("yyyy-MM-dd ");

	ObservableList<Avalise> avaliseList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTableProperty();
		LoadDataOnTable();
	}

	private void setTableProperty() {
		remboursser.setCellFactory(cellFactory1);
		nomAvaliste.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Avalise, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Avalise, String> param) {

						return new SimpleStringProperty(param.getValue().getTransaction().getAdherent().getNom());
					}
				});
		prenomAvalise.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Avalise, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<Avalise, String> param) {

						return new SimpleStringProperty(param.getValue().getTransaction().getAdherent().getPrenom());
					}
				});
		montantAvaliste.setCellValueFactory(new PropertyValueFactory<>("montant"));
		avance.setCellValueFactory(new PropertyValueFactory<>("solder"));
		reste.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Avalise, Long>, ObservableValue<Long>>() {

			@Override
			public ObservableValue<Long> call(CellDataFeatures<Avalise, Long> param) {

				return new ReadOnlyObjectWrapper<Long>(param.getValue().getMontant() - param.getValue().getSolder());
			}
		});
		detail.setCellFactory(cellFactory);

	}

	private void LoadDataOnTable() {
		avaliseList.clear();
		search.clear();
		avaliseList.addAll(avaliseRepository.findByStatut(false));
		tableAvaliste.setItems(avaliseList);
	}

	@FXML
	private void handleClose(ActionEvent actionEvent) {
		Node node = (Node) actionEvent.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void handlePrecedent(ActionEvent actionEvent) {

		Node node = (Node) actionEvent.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.close();
		stageManager.switchSceneShowPreviousStage(FxmlView.TRANSACTION);
	}
	
	private void reopend() {
		Node node = search.getParent();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.close();
		stageManager.switchSceneShowPreviousStage(FxmlView.REMBOURSSEMENT);
	}

	Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>> cellFactory = new Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>>() {
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

	Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>> cellFactory1 = new Callback<TableColumn<Avalise, Boolean>, TableCell<Avalise, Boolean>>() {
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

							if (methodUtilitaire.validateClock(format.format(new Date()))) {

								Avalise avalise = getTableView().getItems().get(getIndex());

								TextInputDialog dialog = new TextInputDialog("");

								dialog.setTitle("Rembourssement");
								dialog.setHeaderText("Enter le montant");
								dialog.setContentText("Montant:");

								Optional<String> result = dialog.showAndWait();

								if (result.isPresent())

									result.ifPresent(name -> {
										if (name.trim() != null) {
											try {

												Long dette = avalise.getMontant() - avalise.getSolder();

												if (Float.parseFloat(name) > dette) {
													MethodUtilitaire.deleteNoPersonSelectedAlert("Montant Faussé",
															"Le montant du rembourssement saisi est superieur a la dette",
															"Le montant du rembourssement saisi est superieur a la dette. \n la dette est "
																	+ dette + " Vous avez entrer " + name);
												} else {
													montantTransaction = Long.parseLong(name);
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

							}

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
	
	@FXML
	private void handleSearchPressed(KeyEvent event) {
		filteredTable(event);
	}
	
	private void filteredTable(KeyEvent event) {
		FilteredList<Avalise> filteredavalise = new FilteredList<Avalise>(avaliseList, e -> true);
		search.setOnKeyReleased(e -> {
			search.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredavalise.setPredicate((Predicate<? super Avalise>) avalise -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String newValueFilter = newValue.toLowerCase();
					if (avalise.getTransaction().getAdherent().getNom().toString().contains(newValueFilter)) {
						return true;
					} else if (avalise.getTransaction().getAdherent().getPrenom().toLowerCase().contains(newValueFilter)) {
						return true;
					}
					return false;
				});
			});
		});

		SortedList<Avalise> sortedList = new SortedList<Avalise>(filteredavalise);
		sortedList.comparatorProperty().bind(tableAvaliste.comparatorProperty());
		tableAvaliste.setItems(sortedList);
	}

	@Transactional
	private void validateActionClick(Avalise avalise) throws Exception { 

		if (MethodUtilitaire.confirmationDialog(avalise, "Valider la transaction", "Valider la transaction",
				"L'adhérent " + avalise.getTransaction().getAdherent().getNom() + " "
						+ avalise.getTransaction().getAdherent().getPrenom() + " effectuera un rembourssement de: "
						+ montantTransaction + "Fcfa au compte de " + avalise.getCompteEpargne().getAdherent().getNom()
						+ " " + avalise.getCompteEpargne().getAdherent().getPrenom(),
				"Valider", "Annuler")) {

			Long dette = avalise.getMontant() - avalise.getSolder();
			Transaction savedTransaction;

			// Pour chaque rembourssement on creer une transaction
			// comme sa on pourra recuperer les differentes transaction
			// pour une instance d'avalise qui s'est réglé en plusieurs paiement
			// grace à transaction.getAvalise()

			Transaction transaction = new Transaction();
			transaction.setAdherent(avalise.getTransaction().getAdherent());
			transaction.setDate(new Date());
			transaction.setMontant(montantTransaction);
			transaction.setType("rembourssement");
			transaction.setAvalise(avalise);

			System.out.println("Dette: " + dette);
			System.out.println("montant: " + montantTransaction);
			if (montantTransaction.equals(dette)) {
				System.out.println("egal");
				avalise.setRemboursser(true);
				avalise.setSolder(avalise.getMontant());
				avalise.setReste((long) 0);

			} else {
				System.out.println("pas egal");
				avalise.setRemboursser(false);
				avalise.setSolder(avalise.getSolder() + montantTransaction);
				avalise.setReste(avalise.getMontant() - avalise.getSolder());
			}

			savedTransaction = transactionRepository.save(transaction);

			// On met a jour l'avalise et les comptes tampon et creance
			Avalise updatedAvalise = avaliseServiceImplement.update(avalise);
			updateCompteTampon(avalise);
			updateCompteCreance(avalise);

			// on met a jour la carte du compte epargne du benefficiare du
			// rembourssement
			CompteEpargne compteEpargneRecepteur = updatedAvalise.getCompteEpargne();
			compteEpargneRecepteur.setLacarte(compteEpargneRecepteur.getLacarte() + montantTransaction);

			// si ce compte epargne ne sert plus d'avalise pour de quelconque pret d'argent,
			// on redéfinit l'attribut avaliser a faux.
			if (compteEpargneRecepteur.getLacarte() == compteEpargneRecepteur.getSolde())
				compteEpargneRecepteur.setAvaliser(false);
			CompteEpargne savedCompteEpargneRecepteur = compteEpargneServiceImplement.update(compteEpargneRecepteur);

			// Compte epargneDetail
			String date = DateUtil.format(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

			// Pour celui qui recoit le rembourssement
		
			CompteEpargneDetail cd = CompteDetailHelper.factoryCompteEpargneDetail(savedCompteEpargneRecepteur, date);

			// Pour celui qui remboursse
			CompteEpargne compteEpargneDuRemboursseur = compteEpargneRepository
					.findByAdherent(updatedAvalise.getTransaction().getAdherent());
		
			CompteEpargneDetail cde = CompteDetailHelper.factoryCompteEpargneDetail(compteEpargneDuRemboursseur, date);

			// si c'est le membre qui remboursse sont propre pret (il est son propre garant)
			// Dans ce cas on fait une seule sauvegarde de l'un des deux CompteEpargneDetail
			if (cd.getCompteEpargne().getEpargneId().equals(cde.getCompteEpargne().getEpargneId())) {
				compteEpargneDetailRepository.save(cd);
			} else {
				compteEpargneDetailRepository.save(cd);
				compteEpargneDetailRepository.save(cde);
			}

			MethodUtilitaire.saveAlert(savedTransaction, "REMBOURSSEMENT EFFECTUE AVEC SUCCES",
					"L'adhérent " + avalise.getTransaction().getAdherent().getNom() + " "
							+ avalise.getTransaction().getAdherent().getPrenom() + " à remboursser "
							+ montantTransaction + " Fcfa");
			//une fois l'opération effectuer le champ de recherche devient non fonctionnel
			//c'est un burg a résoudre; pour le moment on délègue la tache que fesais 
			//LoadDataOnTable() a la methode reopend() bien que moins performante
			//LoadDataOnTable();
			

			// Met a jour les détails des actif sur la home
			homeController.setActifDetail();

			methodUtilitaire.LogFile("Opération de rembourssement",
					avalise.getTransaction().getAdherent().getNom() + " "
							+ avalise.getTransaction().getAdherent().getPrenom() + " <<remboursse a>> "
							+ compteEpargneRecepteur.getAdherent().getNom() + " "
							+ compteEpargneRecepteur.getAdherent().getPrenom(),
					MethodUtilitaire.deserializationUser(), savedTransaction.getDate());
			
			reopend();
		}

	}

	private CompteTampon updateCompteTampon(Avalise avalise) {
		CompteTampon associatedCompteTampon = avalise.getCompteTampon();
		associatedCompteTampon.setDette(associatedCompteTampon.getDette() - montantTransaction);
		CompteTampon updatedCompteTampon = compteTamponServiceImplement.update(associatedCompteTampon);
		return updatedCompteTampon;
	}

	private CompteCreance updateCompteCreance(Avalise avalise) {
		CompteCreance associatedCompteCreance = avalise.getCompteCreance();
		associatedCompteCreance.setMontant(associatedCompteCreance.getMontant() - montantTransaction);
		CompteCreance updatedCompteCreance = compteCreanceServiceImplement.update(associatedCompteCreance);
		return updatedCompteCreance;
	}
}
