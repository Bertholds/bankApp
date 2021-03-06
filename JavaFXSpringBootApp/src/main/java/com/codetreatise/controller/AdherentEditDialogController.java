package com.codetreatise.controller;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.repository.AdherentRepository;
import com.codetreatise.service.DateUtil;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.impl.AdherentServiceImpl;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.StringConverter;

@Controller
public class AdherentEditDialogController implements Initializable {
	@FXML
	private TextField nom;
	@FXML
	private TextField prenom;
	@FXML
	private TextField identifiant;
	@FXML
	private TextField fonction;
	@FXML
	private TextField cni ;
	@FXML
	private ComboBox<String> type;
	@FXML
	private TextField lieuNaissance;
	@FXML
	private ComboBox<String> situation;

	@Autowired
	private AdherentsController adherentsController;
	@Autowired
	private AdherentRepository adherentRepository;
	@Autowired
	private AdherentServiceImpl adherentServiceImpl;
	@Autowired
	private MethodUtilitaire methodUtilitaire;
	
	@Autowired
	private HomeController homeController ;

	String patern = "yyyy-MM-dd";
	
	ObservableList<String> typeList =  FXCollections.observableArrayList("Membre", "Manager", "Administrateur", "Root");
	ObservableList<String> situationList =  FXCollections.observableArrayList("Actif", "Inactif");
	
	// Event Listener on Button.onAction
	@FXML
	public void handleGenerateRegisterClick(ActionEvent event) {
		try {
			identifiant.setText(String.valueOf(adherentRepository.generateId()+1));
		}catch (Exception e) {
			identifiant.setText(String.valueOf(1));
			System.out.println("rien a dire");
		}finally {
		
		}
	}
	
	
	private void  setToolTip() {
		identifiant.setTooltip(new Tooltip("G??n??rer un nombre unique qui identifira un adh??rent"));
		nom.setTooltip(new Tooltip("Nom de l'adh??rent"));
		prenom.setTooltip(new Tooltip("Pr??nom de l'adh??rent"));
		lieuNaissance.setTooltip(new Tooltip("Le lieu de naissance"));
		cni.setTooltip(new Tooltip("Num??ro de la cni"));
		type.setTooltip(new Tooltip("Le type cadre on des droit d'acc??s sur l'application"));
		situation.setTooltip(new Tooltip("La situation de l'adh??rent: En r??gle ou non"));
		fonction.setTooltip(new Tooltip("Quel fonction joue cet adh??rent au sein de l'association ?"));
	}

	// Event Lstener on Button.onAction
	@FXML
	public void handleCreateAdherentClick(ActionEvent event) throws Exception {

		Adherent newAdherent = null;
		if (isInputValid()) {
			Adherent existingAdherent = adherentRepository.findByUniqueName(getNom()+" "+getPrenom());
			if (adherentsController.isEditButtonClick()) {
				if(existingAdherent !=null && existingAdherent.getIdentifiant() != getId()) {
					MethodUtilitaire.deleteNoPersonSelectedAlert("Attention adh??rent existant", "Attention adh??rent existant", "Un adh??rent porte d??ja ce nom veuillez choisir un autre");
				}else {
					Adherent adherent = adherentRepository.findOne(getId());
					adherent.setNom(getNom());
					adherent.setPrenom(getPrenom());
					adherent.setLieuNaiss(getLieuNaiss());
					adherent.setCni(getCni()); 
					adherent.setSituation(getSituation());
					adherent.setType(getType());
					adherent.setFonction(getFonction());
					adherent.setUniqueName(getNom()+" "+getPrenom());
					newAdherent = adherentServiceImpl.update(adherent);
					MethodUtilitaire.saveAlert(newAdherent, "Edition r??ussi", "l'adh??rent "+newAdherent.getNom()+" "+newAdherent.getPrenom()+" ?? ??t?? modifier avec success");
					clearFields();
					adherentsController.setIsEditButtonClick(false);
					adherentsController.loadDataOnTable();
					methodUtilitaire.LogFile("Modification des informations d'un adh??rent",
							newAdherent.getIdentifiant() + "-" + newAdherent.getNom() + " " + newAdherent.getPrenom(),
							MethodUtilitaire.deserializationUser(), new Date(System.currentTimeMillis()));
				}
				
			} else {
				if(existingAdherent !=null) {
					MethodUtilitaire.deleteNoPersonSelectedAlert("Attention adh??rent existant", "Attention adh??rent existant", "Un adh??rent porte d??ja ce nom veuillez choisir un autre");
				}else {
					
					URL location = SettingController.class.getProtectionDomain().getCodeSource().getLocation();
					
					existingAdherent = new Adherent();
					existingAdherent.setNom(getNom());
					existingAdherent.setPrenom(getPrenom());
					existingAdherent.setLieuNaiss(getLieuNaiss());
					existingAdherent.setCni(getCni());
					existingAdherent.setSituation(getSituation());
					existingAdherent.setType(getType());
					existingAdherent.setFonction(getFonction());
					existingAdherent.setUniqueName(getNom()+" "+getPrenom());
					newAdherent = adherentRepository.save(existingAdherent);
					MethodUtilitaire.saveAlert(newAdherent, "Creation de l'adh??rent r??ussi", "l'adh??rent "+newAdherent.getNom()+" "+newAdherent.getPrenom()+" ?? ??t?? creer avec success");
					clearFields();
					adherentsController.loadDataOnTable();
					homeController.initialize(location, ResourceBundle.getBundle("Bundle"));
					methodUtilitaire.LogFile("Enregistrement d'un adh??rent",
							newAdherent.getIdentifiant() + "-" + newAdherent.getNom() + " " + newAdherent.getPrenom(),
							MethodUtilitaire.deserializationUser(), new Date(System.currentTimeMillis()));
				}
			}
		}

	}

	// Event Listener on Button.onAction
	@FXML
	public void handleCancelClick(ActionEvent event) {
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		adherentsController.setIsEditButtonClick(false);
		stage.close();
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleClearFieldClick(ActionEvent event) {
		clearFields();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setToolTip();
		handleGenerateRegisterClick(new ActionEvent());
		type.setItems(typeList);
		situation.setItems(situationList);
		type.getSelectionModel().selectFirst();
		FilterValueOnComboboxType();
	}

	private Long getId() {
		return Long.parseLong(identifiant.getText());
	}
	private String getNom() {
		return nom.getText();
	}

	private String getPrenom() {
		return prenom.getText();
	}

	private String getLieuNaiss() {
		return lieuNaissance.getText();
	}

	private String getCni() {
		return cni.getText();
	}
	

	private String getType() {
		return type.getSelectionModel().getSelectedItem();
	}

	private String getSituation() {
		return situation.getSelectionModel().getSelectedItem();
	}

	private String getFonction() {
		return fonction.getText();
	}

	public Adherent showPersonDetails(Adherent adherent) {
		if (adherent != null) {
			// Fill the labels with info from the person object.
			nom.setText(adherent.getNom());
			prenom.setText(adherent.getPrenom());
			cni.setText(adherent.getCni());
			fonction.setText(adherent.getFonction());
			type.getSelectionModel().select(adherent.getType());
			situation.getSelectionModel().select(adherent.getSituation());
			lieuNaissance.setText(adherent.getLieuNaiss());
			identifiant.setText(adherent.getIdentifiant().toString());

		}
		return adherent;
	}
    private void clearFields() {
    	identifiant.clear();
    	nom.clear();
    	prenom.clear();
    	lieuNaissance.clear();
    	cni.clear();
    	fonction.clear();
    	type.getSelectionModel().clearSelection();
    	situation.getSelectionModel().clearSelection();
    }
    
	private void FilterValueOnComboboxType() {

		// Create a FilteredList wrapping the ObservableList.
		FilteredList<String> filteredItems = new FilteredList<String>(typeList, p -> true);

		// Add a listener to the textProperty of the combobox editor. The
		// listener will simply filter the list every time the input is changed
		// as long as the user hasn't selected an item in the list.
		type.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			final TextField editor = type.getEditor();
			final String selected = type.getSelectionModel().getSelectedItem();

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

		type.setItems(filteredItems);
	}
    
	private boolean isInputValid() {
		String errorMessage = "";

		if (getNom() == null || getNom().trim().length() == 0) {
			errorMessage += "Nom invalide!\n";
		}
		if (getPrenom() == null || getPrenom().trim().length() == 0) {
			errorMessage += "Prenom invalide!\n";
		}
		if (getLieuNaiss() == null || getLieuNaiss().trim().length() == 0) {
			errorMessage += "Lieu de naissance invalide!\n";
		}

		if (getCni() == null || getCni().trim().length() == 0) {
			errorMessage += "Date de naissance invalide!\n";
		}

		if (getType() == null || getType().trim().length() == 0) {
			errorMessage += "Type d'adh??rent invalide!\n";
		}

		if (getSituation() == null || getSituation().trim().length() == 0) {
			errorMessage += "Situation de l'adh??rent invalide!\n";
		}

		if (getFonction() == null || getFonction().trim().length() == 0) {
			errorMessage += "La fonction de l'adh??rent est invalide";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			MethodUtilitaire.errorMessageAlert("Formulaire invalide", "V??rifier les champs invalide", errorMessage);
			return false;
		}
	}
}
