package com.codetreatise.controller;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.CompteUtilisateur;
import com.codetreatise.bean.Utilisateur;
import com.codetreatise.config.StageManager;
import com.codetreatise.repository.UserAccountRepository;
import com.codetreatise.repository.UtilisateurRepository;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.UserAccountServiceImplement;
import com.codetreatise.view.FxmlView;

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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

@Controller
public class UserAccountController implements Initializable {
	@FXML
	private ComboBox<String> utilisateurPseudo;
	@FXML
	private ComboBox<String> droitAccess;
	@FXML
	private TextField login;
	@FXML
	private PasswordField pass;
	@FXML
	private TextField idCompteUtilisateur;
	@FXML
	private TableView<CompteUtilisateur> utilisateurAccountTable;
	@FXML
	private TableColumn<CompteUtilisateur, String> IdTableColumn;
	@FXML
	private TableColumn<CompteUtilisateur, String> pseudoTableColumn;
	@FXML
	private TableColumn<CompteUtilisateur, String> accesTableColumn;
	@FXML
	private TableColumn<CompteUtilisateur, Boolean> editTableColumn;
	@FXML
	private TextField search;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnDel;
	@FXML
	private Button btnCreate;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private UserAccountServiceImplement userAccountServiceImplement;

	@Autowired
	MethodUtilitaire methodUtilitaire;

	@Lazy
	@Autowired
	private StageManager stageManager;

	ObservableList<String> utilisateursList = FXCollections.observableArrayList();
	ObservableList<String> listDroitAcces = FXCollections.observableArrayList("Manager", "Administrateur", "Root");
	ObservableList<CompteUtilisateur> compteUtilisateursList = FXCollections.observableArrayList();

	@FXML
	public void handleBtnCancelClick() {
		clearFields();
		enableBtn();
	}

	private void disableBtn() {
		btnCreate.setDisable(true);
		btnDel.setDisable(true);
		utilisateurPseudo.setDisable(true);
		btnCancel.setVisible(true);
	}

	private void enableBtn() {
		btnCancel.setVisible(false);
		btnCreate.setDisable(false);
		btnDel.setDisable(false);
		utilisateurPseudo.setDisable(false);
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleCreateClick(ActionEvent event) throws Exception {

		if (validate("Droit d'acces", getDroit(), "[a-zA-Z]+") && emptyValidation("Login", getLogin().isEmpty())
				&& emptyValidation("Password", getPass().isEmpty()) && emptyValidation("Pseudo", getPseudo() == null)) {

			try {
				CompteUtilisateur compteUtilisateur = new CompteUtilisateur();
				compteUtilisateur.setUtilisateur_id(getUtilisateur().getUserId());
				compteUtilisateur.setUtilisateur(getUtilisateur());
				compteUtilisateur.setNiveau_access(getDroit());
				compteUtilisateur.setLogin(getLogin());
				compteUtilisateur.setPass(getPass());

				CompteUtilisateur newCompteUtilisateur = userAccountRepository.save(compteUtilisateur);
				MethodUtilitaire.saveAlert(newCompteUtilisateur, "Save User account sucessful",
						"User account with pseudo " + newCompteUtilisateur.getUtilisateur().getPseudo()
								+ " create successful");

				clearFields();
				loadDataOnTable();

				methodUtilitaire.LogFile("Creation de compte utilisateur",
						newCompteUtilisateur.getUtilisateur().getPseudo(), MethodUtilitaire.deserializationUser(),
						new Date(System.currentTimeMillis()));
			} catch (Exception e) {
				e.printStackTrace();
				MethodUtilitaire.deleteNoPersonSelectedAlert("Erreur pseudo inconnu", "Erreur pseudo inconnu",
						"Le pseudo ne correspond à aucun utilisateur");
			}

		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleDeleteClick(ActionEvent event) throws Exception {
		List<CompteUtilisateur> usersAccount = utilisateurAccountTable.getSelectionModel().getSelectedItems();
		if (!usersAccount.isEmpty()) {
			if (MethodUtilitaire.confirmationDialog(event, "Confirmer la suppression ?",
					"Confirmer la suppression de compte(s) utilisateur(s) sélectionné(s)",
					"Etes vous sur de vouloir le(s) supprimé(s) ?", "Oui", "Annuler")) {
				userAccountRepository.deleteInBatch(usersAccount);
				loadDataOnTable();
				MethodUtilitaire.saveAlert(event, "Delete operation successful", "Delete operation successful");
				for (CompteUtilisateur compteUtilisateur : usersAccount) {
					methodUtilitaire.LogFile("Suppression de compte utilisateur",
							compteUtilisateur.getUtilisateur().getPseudo(), MethodUtilitaire.deserializationUser(),
							new Date(System.currentTimeMillis()));
				}
			}
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Error no user account selected",
					"Error no user account selected", "Please select one or many user account and try.");
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleEditClick(ActionEvent event) {
		try {
			CompteUtilisateur compteUtilisateur = userAccountRepository.findOne(Long.parseLong(getId()));
			if (validate("Droit d'acces", getDroit(), "[a-zA-Z]+") && emptyValidation("Id", getId() == null)
					&& emptyValidation("Login", getLogin().isEmpty())
					&& emptyValidation("Password", getPass().isEmpty())
					&& emptyValidation("Pseudo", getPseudo().isEmpty())) {

				compteUtilisateur.setUtilisateur(getUtilisateur());
				compteUtilisateur.setNiveau_access(getDroit());
				compteUtilisateur.setLogin(getLogin());
				compteUtilisateur.setPass(getPass());

				CompteUtilisateur updateCompteUtilisateur = userAccountServiceImplement.update(compteUtilisateur);
				MethodUtilitaire.saveAlert(updateCompteUtilisateur, "Update User account sucessful",
						"User account with pseudo " + updateCompteUtilisateur.getUtilisateur().getPseudo()
								+ " updated successful");

				clearFields();
				loadDataOnTable();
				enableBtn();
				methodUtilitaire.LogFile("Edition de compte utilisateur",
						compteUtilisateur.getUtilisateur().getPseudo(), MethodUtilitaire.deserializationUser(),
						new Date(System.currentTimeMillis()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			MethodUtilitaire.deleteNoPersonSelectedAlert("Attention aucune sélection", "Attention aucune sélection",
					"Cliqué sur le bouton éditer correspondant au compte utilisateur dans le tableau et éssayer de nouveau");
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleSwitchUserClick(ActionEvent event) {
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.close();
		stageManager.switchSceneShowPreviousStage(FxmlView.USER);
	}

	// Event Listener on TextField[#search].onKeyReleased
	@FXML
	public void handleSearchReleased(KeyEvent event) {
		filteredTable(event);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setColumProperties();
		loadDataOnTable();
		utilisateurAccountTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setComboboxPseudo();
		setComboboxDroitAcces();
		FilterValueOnComboboxPseudo();
	}

	private void loadDataOnTable() {
		compteUtilisateursList.clear();
		compteUtilisateursList.addAll(userAccountRepository.findAll());
		utilisateurAccountTable.setItems(compteUtilisateursList);
	}

	private void setComboboxPseudo() {
		utilisateursList.clear();
		List<Utilisateur> users = utilisateurRepository.findAll();
		for (Utilisateur utilisateur : users) {
			utilisateursList.add(utilisateur.getPseudo());
		}
		utilisateurPseudo.setItems(utilisateursList);
	}

	private void setComboboxDroitAcces() {
		droitAccess.setItems(listDroitAcces);
	}

	private void FilterValueOnComboboxPseudo() {

		// Create a FilteredList wrapping the ObservableList.
		FilteredList<String> filteredItems = new FilteredList<String>(utilisateursList, p -> true);

		// Add a listener to the textProperty of the combobox editor. The
		// listener will simply filter the list every time the input is changed
		// as long as the user hasn't selected an item in the list.
		utilisateurPseudo.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			final TextField editor = utilisateurPseudo.getEditor();
			final String selected = utilisateurPseudo.getSelectionModel().getSelectedItem();

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

		utilisateurPseudo.setItems(filteredItems);
	}

	private void setColumProperties() {
		accesTableColumn.setCellValueFactory(new PropertyValueFactory<>("niveau_access"));
		IdTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<CompteUtilisateur, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<CompteUtilisateur, String> param) {
						return new SimpleStringProperty(param.getValue().getUtilisateur().getUserId().toString());
					}
				});
		pseudoTableColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<CompteUtilisateur, String>, ObservableValue<String>>() {

					@Override
					public ObservableValue<String> call(CellDataFeatures<CompteUtilisateur, String> param) {
						return new SimpleStringProperty(param.getValue().getUtilisateur().getPseudo());
					}
				});
		editTableColumn.setCellFactory(cellFactory);

	}

	Callback<TableColumn<CompteUtilisateur, Boolean>, TableCell<CompteUtilisateur, Boolean>> cellFactory = new Callback<TableColumn<CompteUtilisateur, Boolean>, TableCell<CompteUtilisateur, Boolean>>() {
		@Override
		public TableCell<CompteUtilisateur, Boolean> call(final TableColumn<CompteUtilisateur, Boolean> param) {
			final TableCell<CompteUtilisateur, Boolean> cell = new TableCell<CompteUtilisateur, Boolean>() {
				Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button();

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						btnEdit.setOnAction(e -> {
							CompteUtilisateur compteUtilisateur = getTableView().getItems().get(getIndex());
							updateUser(compteUtilisateur);
						});

						btnEdit.setStyle("-fx-background-color: transparent;");
						ImageView iv = new ImageView();
						iv.setImage(imgEdit);
						iv.setPreserveRatio(true);
						iv.setSmooth(true);
						iv.setCache(true);
						btnEdit.setGraphic(iv);

						setGraphic(btnEdit);
						setAlignment(Pos.CENTER_LEFT);
						setText(null);
					}
				}

				private void updateUser(CompteUtilisateur compteUtilisateur) {
					disableBtn();
					idCompteUtilisateur.setText(compteUtilisateur.getUtilisateur_id().toString());
					utilisateurPseudo.getSelectionModel().select(compteUtilisateur.getUtilisateur().getPseudo());
					login.setText(compteUtilisateur.getLogin());
					droitAccess.getSelectionModel().select(compteUtilisateur.getNiveau_access());
					pass.setText(compteUtilisateur.getPass());
				}
			};
			return cell;
		}
	};

	private void clearFields() {
		idCompteUtilisateur.clear();
		utilisateurPseudo.getSelectionModel().clearSelection();
		droitAccess.getSelectionModel().clearSelection();
		login.clear();
		pass.clear();
	}

	private String getId() {
		return idCompteUtilisateur.getText();
	}

	private String getDroit() {
		return droitAccess.getSelectionModel().getSelectedItem();
	}

	private String getLogin() {
		System.out.println("loginnnnnnnnnnnnnnnn" + login.getText().trim());
		return login.getText().trim();
	}

	private String getPass() {
		return pass.getText().trim();
	}

	private String getPseudo() {
		return utilisateurPseudo.getSelectionModel().getSelectedItem();
	}

	private Utilisateur getUtilisateur() {
		Utilisateur utilisateur = utilisateurRepository.findByPseudo(getPseudo());
		return utilisateur;
	}

	private void filteredTable(KeyEvent event) {
		FilteredList<CompteUtilisateur> filteredcompteUtilisateurs = new FilteredList<CompteUtilisateur>(
				compteUtilisateursList, e -> true);
		search.setOnKeyReleased(e -> {
			search.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredcompteUtilisateurs.setPredicate((Predicate<? super CompteUtilisateur>) compteUtilisateurs -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String newValueFilter = newValue.toLowerCase();
					if (compteUtilisateurs.getUtilisateur().getPseudo().contains(newValueFilter)) {
						return true;
					} else if (compteUtilisateurs.getNiveau_access().toLowerCase().contains(newValueFilter)) {
						return true;
					}
					return false;
				});
			});
		});

		SortedList<CompteUtilisateur> sortedList = new SortedList<CompteUtilisateur>(filteredcompteUtilisateurs);
		sortedList.comparatorProperty().bind(utilisateurAccountTable.comparatorProperty());
		utilisateurAccountTable.setItems(sortedList);
	}

	/*
	 * Validations
	 */
	// On pouvais appeler la fonction validate depuis La classe MethodUtilitaire
	// mais en la redefinissant
	// car validate ne marche que pour les champs de type textField vu que le
	// selected item du combobox
	// est tester avec !=null et non .isEmpty de la textField
	private boolean validate(String field, String value, String pattern) {
		if (value != null) {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(value);
			if (m.find() && m.group().equals(value)) {
				return true;
			} else {
				validationAlert(field, false);
				return false;
			}
		} else {
			validationAlert(field, true);
			return false;
		}
	}

	private boolean emptyValidation(String field, boolean empty) {
		if (!empty) {
			return true;
		} else {
			validationAlert(field, true);
			return false;
		}
	}

	private void validationAlert(String field, boolean empty) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Validation Error");
		alert.setHeaderText(null);
		if (field.equals("Pseudo") || field.equals("Droit d'acces"))
			alert.setContentText("Please Select " + field);
		else {
			if (empty)
				alert.setContentText("Please Enter " + field);
			else
				alert.setContentText("Please Enter Valid " + field);
		}
		alert.showAndWait();
	}
}
