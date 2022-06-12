package com.codetreatise.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.service.MethodUtilitaire;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

@Controller
public class BackupController implements Initializable {
	@FXML
	private TextField cheminSauvegarde;
	@FXML
	private TextField cheminRestauration;
	@FXML
	private TextField nomSauvegarde;
	@FXML
	private AnchorPane anchopane;

	private static String dbName = "bank";
	private static String dbUser = "bk";
	private static String dbPass = "bkbk";
	private static String host = "127.0.0.1";
	String source;
	File directory;
	
	@Autowired
	private HomeController homeController ;

	// Event Listener on Button.onAction
	@FXML
	public void handleParcourtSauvegarde(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Selection du repertoire de sauvegard");
		directoryChooser.setInitialDirectory(new File("C:\\"));
		Stage ownerWindow = (Stage) anchopane.getScene().getWindow();
		directory = directoryChooser.showDialog(ownerWindow);
		cheminSauvegarde.setText(directory.getAbsolutePath());

	}

	// Event Listener on Button.onAction
	@FXML
	public void handkleRestaurationClick(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selection du fichier sql");
		fileChooser.setInitialDirectory(new File("C:\\"));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("fichier sql", "*.sql"));
		Stage ownerWindow = (Stage) anchopane.getScene().getWindow();
		File file = fileChooser.showOpenDialog(ownerWindow);
		source = file.getAbsolutePath().equals(null) ? "" : file.getAbsolutePath();
		cheminRestauration.setText(file.getAbsolutePath());
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleSauvegarderClick(ActionEvent event) throws IOException, InterruptedException {
		if (isBackupInputValid()) {
			backupDB(dbUser, dbPass, dbName, host);
		}
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleRestaurerClick(ActionEvent event) throws IOException, InterruptedException {
		if (source != null) {
			restore(dbUser, dbPass, source);
		} else {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Erreur", "Le fichier de restauration est invalide",
					"Utiliser le bouton parcourir et sélectionné votre fichier");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void backupDB(String userName, String password, String dbName, String host)
			throws IOException, InterruptedException {

		Process p = null;
		try {
			Runtime runtime = Runtime.getRuntime();

			/**
			 * --default-character-set=latin1et --skip-set-charset: Cela empêche MySQL
			 * d'essayer de faire une reconversion réelle des données, ce qui peut casser
			 * les choses. --no-tablespaces: Cela résout une erreur ACCESS-Denied causée par 
			 * un bogue MySQL. --skip-triggers: Ignorez les déclencheurs, mais vous pourriez
			 * en avoir besoin si vous les utilisez.
			 */

			if (directory != null) {
				p = runtime.exec("mysqldump  --user=" + userName + " --password=" + password + "  --host=" + host
						+ " --port=3306  --skip-add-locks --skip-disable-keys --default-character-set=latin1 --skip-set-charset --no-tablespaces --skip-triggers --skip-comments --result-file="
						+ directory.getAbsolutePath() + File.separator + getNomFichierSauvegarde()
						+ ".sql --databases bank");

				int processComplete = p.waitFor();

				if (processComplete == 0) {

					System.out.println("Backup created successfully!");
					MethodUtilitaire.saveAlert(null, "Sauvegarde réussie", "Sauvegarde réussie");

					cheminSauvegarde.clear();
					nomSauvegarde.clear();

				} else {
					MethodUtilitaire.deleteNoPersonSelectedAlert("Error unable to save", "Error unable to save",
							"Verify server and try again");
				}
			} else {
				MethodUtilitaire.deleteNoPersonSelectedAlert("Erreur", "Le chemin de sauvegarde est invalide",
						"Utiliser le bouton parcourir et selectionner un repertoire");
			}

		} catch (Exception e) {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Error unable to save", "Error unable to save",
					e.getMessage());
		}
	}

	private void restore(String userName, String password, String source) throws IOException, InterruptedException {
		String[] restoreCmd = new String[] { "mysql", "--user=" + userName, "--password=" + password, "-e",
				"source " + source };
		try {
			Process runtimeProcess = Runtime.getRuntime().exec(restoreCmd);
			int processComplete = runtimeProcess.waitFor();
			if (processComplete == 0) {
				URL location = SettingController.class.getProtectionDomain().getCodeSource().getLocation();
				System.out.println("Done");
				MethodUtilitaire.saveAlert(null, "Restoration réussie", "Restoration réussie");
				cheminRestauration.clear();
				homeController.initialize(location, ResourceBundle.getBundle("Bundle"));
			} else {
				System.out.println("Failed");
				MethodUtilitaire.deleteNoPersonSelectedAlert("Error unable to restore", "Error unable to restore",
						"Verify server and try again");
			}
		} catch (Exception ex) {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Error unable to save", "Error unable to save",
					ex.getMessage());
		}
	}

	// mysql -u super -p super db_jpa -host=127.0.0.1 --port=3306 <
	// C:/Users/Berthold/Desktop/www/enfin.sql egalement utiliser pour restaurer
	// mysqldump -usuper -psuper db_jpa --host=127.0.0.1 --port=3306 >
	// C:/Users/Berthold/Desktop/www/hello.sql pour un backup

	private String getNomFichierSauvegarde() {
		return nomSauvegarde.getText();
	}

	private boolean isBackupInputValid() {
		String errorMessage = "";

		System.out.println("cheminSauvegarde" + cheminSauvegarde.getText().trim());
		if (cheminSauvegarde.getText().trim().length() == 0) {
			errorMessage += "le repertoire de sauvegarde est invalide \n";
		}
		if (nomSauvegarde.getText().trim().length() == 0) {
			errorMessage += "le nom du fichier de sauvegarde est invalide \n";
		}

		if (errorMessage.length() > 0) {
			MethodUtilitaire.deleteNoPersonSelectedAlert("Sauvegarde invalide",
					"La configuration pour la sauvegarde est invalide", errorMessage);
			return false;
		}

		return true;
	}
}
