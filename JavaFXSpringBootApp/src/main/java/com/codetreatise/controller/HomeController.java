package com.codetreatise.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Menu;
import com.codetreatise.config.StageManager;
import com.codetreatise.repository.MenuRepository;
import com.codetreatise.view.FxmlView;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.icons525.Icons525View;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;

import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.MenuButton;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;

@Controller
public class HomeController implements Initializable {
	@FXML
	private MenuButton btnAdherent;
	@FXML
	private MenuItem menuItemAddAdherent;
	@FXML
	private MenuItem menuItemPlusOptionAdherent;
	@FXML
	private MenuButton btnCompte;
	@FXML
	private MenuItem menuItemAjouterCompte;
	@FXML
	private MenuItem menuItemPlusOptionCompte;
	@FXML
	private MenuButton btnTransaction;
	@FXML
	private MenuItem menuItemAjouterTransaction;
	@FXML
	private MenuItem menuItemRapportTransactions;
	@FXML
	private MenuButton btnRapport;
	@FXML
	private MenuItem menuItemRapportMensuel;
	@FXML
	private MenuItem menuItemRapportTrimestriel;
	@FXML
	private MenuItem menuItemRapportAnnuel;
	@FXML
	private Button btnMouchard;
	@FXML
	private Button btnuser;
	@FXML
	private Button btnBackup;
	@FXML
	private Button btnSetting;
	@FXML
	private AnchorPane mainView;
	@FXML
	private Label labelTotalAdherent;
	@FXML
	private Label labelAdherentEnRegle;
	@FXML
	private Label labelAdherentNonEnRegle;
	@FXML
	private Label labelActifBrute;
	@FXML
	private Label labelActifEmprinte;
	@FXML
	private Label associationNameLabel;
	@FXML
	private Label labelActifsRestant;
	@FXML
	private MaterialIconView iconBackup;
	@FXML
	private Icons525View iconMouchard;
	@FXML
	private FontAwesomeIconView iconSetting;
	@FXML
	private MaterialIconView iconUser;
	@FXML
	private MaterialDesignIconView iconRapport;
	@FXML
	private MaterialIconView iconTransaction;
	@FXML
	private OctIconView iconCompte;
	@FXML
	private OctIconView iconAdherent;
	@FXML
	private BarChart<String, String> barchartTransaction;

	private String operation = "any";

	@Lazy
	@Autowired
	private StageManager stageManager;
	
	@Autowired
	private LoginController LoginController;
	
	@Autowired
	private MenuRepository menuRepository;

	private Stage primaryStage;

	// Event Listener on MenuButton[#btnAdherent].onAction
	@FXML
	public void handleDeconexionClick() {
		stageManager.switchScene(FxmlView.LOGIN);
	}
	@FXML
	public void handleBtnAdherentClick(ActionEvent event) {
		System.out.println("click");
	}

	// Event Listener on MenuItem[#menuItemAddAdherent].onAction
	@FXML
	public void handleMenuItemAddAdherent(ActionEvent event) {

		if (primaryStage == null) {
			primaryStage = stageManager.switchSceneShowPreviousStage(FxmlView.ADHERENT);
			stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.ADHERENTADD);
			operation = "adherent";
		} else {
			if (operation == "adherent") {
				primaryStage.close();
				primaryStage = stageManager.switchSceneShowPreviousStage(FxmlView.ADHERENT);
				stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.ADHERENTADD);
			}
		}
	}

	// Event Listener on MenuItem[#menuItemPlusOptionAdherent].onAction
	@FXML
	public void handleMenuItemPlusOptionAdherent(ActionEvent event) throws IOException {
		/*
		 * AnchorPane adherentPanel =
		 * FXMLLoader.load(getClass().getResource(FxmlView.ADHERENT.getFxmlFile()));
		 * AnchorPane.setLeftAnchor(adherentPanel, 10.04);
		 * AnchorPane.setRightAnchor(adherentPanel, 10.4);
		 * AnchorPane.setTopAnchor(adherentPanel, 2.01);
		 * AnchorPane.setBottomAnchor(adherentPanel, 2.22);
		 * mainView.getChildren().clear(); mainView.getChildren().add(adherentPanel);
		 * System.out.println(AnchorPane.getTopAnchor(adherentPanel));
		 * System.out.println(AnchorPane.getLeftAnchor(adherentPanel));
		 */
		if (primaryStage == null) {
			// stageManager.switchSceneShowPreviousStage(FxmlView.ADHERENT);
			primaryStage = stageManager.switchSceneShowPreviousStage(FxmlView.ADHERENT);
			operation = "adherent";
		} else {
			primaryStage.close();
			primaryStage = stageManager.switchSceneShowPreviousStage(FxmlView.ADHERENT);
		}
	}

	// Event Listener on MenuButton[#btnCompte].onAction
	@FXML
	public void handleBtnCompteClick(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#menuItemAjouterCompte].onAction
	@FXML
	public void handlemenuItemRapportCompte(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#menuItemPlusOptionCompte].onAction
	@FXML
	public void handleMenuItemPlusOptionCompte(ActionEvent event) {
		if (primaryStage == null) {
			stageManager.switchSceneShowPreviousStage(FxmlView.COMPTEbANCAIRE);
			primaryStage = stageManager.switchSceneShowPreviousStage(FxmlView.COMPTEbANCAIRE);
		} else {
			primaryStage.close();
			primaryStage = stageManager.switchSceneShowPreviousStage(FxmlView.COMPTEbANCAIRE);
		}

	}

	// Event Listener on MenuButton[#btnTransaction].onAction
	@FXML
	public void handleBtnTransactionClick(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#menuItemAjouterTransaction].onAction
	@FXML
	public void handleMenuItemAjouterTransaction(ActionEvent event) {
		stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.TRANSACTION);
	}

	// Event Listener on MenuItem[#menuItemPlusOptionTransaction].onAction
	@FXML
	public void handleMenuItemRapportsTransactions(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuButton[#btnRapport].onAction
	@FXML
	public void handleBtnRapportClick(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#menuItemRapportMensuel].onAction
	@FXML
	public void handleMenuItemRapportMensuel(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#menuItemRapportTrimestriel].onAction
	@FXML
	public void handleMenuItemRapportTrimestriel(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on MenuItem[#menuItemRapportAnnuel].onAction
	@FXML
	public void handleMenuItemRapportAnnuel(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on Button[#btnMouchard].onAction
	@FXML
	public void handleBtnMouchardClick(ActionEvent event) {
		stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.Mouchard);
	}

	// Event Listener on Button[#btnuser].onAction
	@FXML
	public void handleBtnUserClick(ActionEvent event) {
		stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.USER);
	}

	// Event Listener on Button[#btnBackup].onAction
	@FXML
	public void handleBtnBackupClick(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on Button[#btnBackup].onAction
	@FXML
	public void handleBtnSettingClick(ActionEvent event) {
		stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.SETTING);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		for (Button b : getAllButton()) {
			b.setVisible(false);
		}
		
		//deuxieme boucle for por desactiver les menuBouttonItem
		for (MenuButton b : getAllMenuButton()) {
			b.setVisible(false);
		}
		
		//troisieme boucle pour desactiver les icons associer aux boutons
		for(Object o: getAllIconButton()) {
			((Node) o).setVisible(false);
		}
		
		//quatrième boucle pour desactiver les icons associer aux MenuButton
				for(Object o: getAllIconMenuButton()) {
					((Node) o).setVisible(false);
				}
		
		//set accessibleText pour les icons des menu respectif
		SetAccessibleTextForIconForButton();
		SetAccessibleTextForIconForMenuButton();

		try {
			checkAcces();
			setAssociationName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Button> getAllButton() {
		List<Button> buttons = new ArrayList<Button>();
		buttons.add(btnBackup);
		buttons.add(btnMouchard);
		buttons.add(btnSetting);
		buttons.add(btnuser);
		return buttons;
	}
	
	public List<MenuButton> getAllMenuButton() {
		List<MenuButton> menuButtons = new ArrayList<MenuButton>();
		menuButtons.add(btnAdherent);
		menuButtons.add(btnCompte);
		menuButtons.add(btnRapport);
		menuButtons.add(btnTransaction);
		return menuButtons;
	}
	
	//List des icons rattacher aux boutons
	public List<Node> getAllIconButton(){
		List<Node> iconBtn = new ArrayList<Node>();
		
		iconBtn.add(iconSetting);
		iconBtn.add(iconMouchard);
		iconBtn.add(iconUser);
		iconBtn.add(iconBackup);
		
		return iconBtn;
	}
	
	//List des icons rattacher aux MenuBoutons
		public List<Node> getAllIconMenuButton(){
			List<Node> iconMenuBtn = new ArrayList<Node>();
			
			iconMenuBtn.add(iconAdherent);
			iconMenuBtn.add(iconCompte);
			iconMenuBtn.add(iconTransaction);
			iconMenuBtn.add(iconRapport);
			
			return iconMenuBtn;
		}

	//cette methode utilitaire est importante car elle permet de définir  l'affichage ou non des icons
	// des menus respectif car accessible text porte le nom du menu et permet de comparer...
	
	//POUR LES MENUS AVEC BOUTON NODE
	private void SetAccessibleTextForIconForMenuButton(){
		iconAdherent.setAccessibleText("Adhérents");
		iconCompte.setAccessibleText("Comptes bancaire");
		iconTransaction.setAccessibleText("Transactions");
		iconRapport.setAccessibleText("Rapports");
	}
	
	//POUR LES MENUS AVEC MENUBUTTON NODE
	private void SetAccessibleTextForIconForButton(){
		iconSetting.setAccessibleText("Parametre");
		iconMouchard.setAccessibleText("Mochard");
		iconUser.setAccessibleText("Utilisateurs");
		iconBackup.setAccessibleText("Sauvegarder / Restaurer");
	}
	
	private void checkAcces() throws Exception {
		String acces = LoginController.getAcces();

		if (acces.equals("Administrateur")) {
			helpCheckAccess(acces);
		} else if (acces.equals("Manager")) {
			helpCheckAccess(acces);
		} else if (acces.equals("Root")) {
			helpCheckAccess(acces);
		}
	}

	private void helpCheckAccess(String access)   {
		List<Menu> menus = menuRepository.findByDroitAcces(access);
		
		for (Menu menu : menus) {
			for (Button button : getAllButton()) {
				if(button.getText().equals(menu.getNom())) {
					button.setVisible(true);
					for (Node icon : getAllIconButton()) {
						if(icon.getAccessibleText().equals(button.getText())) {
							icon.setVisible(true);
						}
					}
				}
			}
			
			for (MenuButton menuButton : getAllMenuButton()) {
				if(menuButton.getText().equals(menu.getNom())) {
					menuButton.setVisible(true);
					for (Node icon : getAllIconMenuButton()) {
						if(icon.getAccessibleText().equals(menuButton.getText())) {
							icon.setVisible(true);
						}
					}
				}
			}
			
		}

	}

	public String setAssociationName() throws IOException {
		String path = /*HomeController.class.getProtectionDomain().getCodeSource().getLocation().getPath()*/ System.getProperty("user.dir");
		System.out.println(path);
		File file = new File(path+File.separator+"writeLabel.txt");
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String s = bufferedReader.readLine();
		System.out.println("********************  " + s + "  ******************************");
		associationNameLabel.setText(s);
		associationNameLabel.setOnMouseEntered(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				 associationNameLabel.setTextFill(Color.BROWN);
			}
		});
		associationNameLabel.setOnMouseExited(e->{
			 associationNameLabel.setTextFill(Color.BLACK);
		});
		fileReader.close();
		bufferedReader.close();
		System.out.println("Bingoooooooooooooooooooooo!");
		return s;
	}

}
