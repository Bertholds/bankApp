package com.codetreatise.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.CompteUtilisateur;
import com.codetreatise.bean.Operation;
import com.codetreatise.bean.Utilisateur;
import com.codetreatise.controller.LoginController;
import com.codetreatise.repository.OperationRepository;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

@Controller
public class MethodUtilitaire {

	 @Autowired
	 private OperationRepository operationRepository;

	public static void saveAlert(Object object, String title, String content) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public static void deleteNoPersonSelectedAlert(String title, String header, String content) {
		// Nothing selected.
		Alert alert = new Alert(AlertType.WARNING);
		// alert.initOwner( );
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}

	public static boolean confirmationDialog(Object object, String title, String header, String content) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		ButtonType cancel = new ButtonType("Annuler");
		ButtonType yes = new ButtonType("Confirmer");
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(cancel, yes);
		Optional<ButtonType> optional = alert.showAndWait();
		if (optional.get() == yes) {
			return true;
		} else
			return false;
	}

	public static void errorMessageAlert(String title, String header, String content) {
		// Nothing selected.
		Alert alert = new Alert(AlertType.ERROR);
		// alert.initOwner( );
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}

	public static boolean validate(String field, String value, String pattern) {
		if (!value.isEmpty()) {
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

	public static boolean emptyValidation(String field, boolean empty) {
		if (!empty) {
			return true;
		} else {
			validationAlert(field, true);
			return false;
		}
	}

	public static void validationAlert(String field, boolean empty) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Validation Error");
		alert.setHeaderText(null);
		if (field.equals("Pseudo"))
			alert.setContentText("Please Select " + field);
		else {
			if (empty)
				alert.setContentText("Please Enter " + field);
			else
				alert.setContentText("Please Enter Valid " + field);
		}
		alert.showAndWait();
	}

	public static Utilisateur deserializationUser() throws IOException, ClassNotFoundException {
		String path = /*LoginController.class.getProtectionDomain().getCodeSource().getLocation().getPath()*/System.getProperty("user.dir");
		File file = new File(path+File.separator+"serializeUsser.txt");
		FileInputStream fileInputStream = new FileInputStream(file);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
		CompteUtilisateur compteUtilisateur = (CompteUtilisateur) objectInputStream.readObject();
		objectInputStream.close();
		return compteUtilisateur.getUtilisateur();
	}

	public void LogFile(String name, String cible, Utilisateur utilisateur) throws UnknownHostException {
		Operation operation = new Operation();
		String ip = InetAddress.getLocalHost().toString();
		System.out.println(ip);
		operation.setName(name);
		operation.setDate(new Date(System.currentTimeMillis()));
		operation.setUtilisateur(utilisateur);
		operation.setAddress(ip);
		operation.setCible(cible);
		operationRepository.save(operation);
	}

	/*
	 * private static BasicDataSource dataSource; private static final String
	 * JDBC_DRIVER = "com.mysql.jdbc.Driver"; private static final String
	 * CONN_STRING = "jdbc:mysql://localhost:3306/db_jpa";
	 * 
	 * public static Connection dbConnect() throws ClassNotFoundException,
	 * SQLException {
	 * 
	 * Connection connection = null;
	 * 
	 * try { Class.forName(JDBC_DRIVER); } catch (ClassNotFoundException e) {
	 * System.out.println("where is your My sql jdbc driver ?");
	 * e.printStackTrace(); throw e; }
	 * 
	 * System.out.println("Charging drivers successful");
	 * 
	 * try { if(connection==null) { connection =
	 * DriverManager.getConnection(CONN_STRING, "root", "");
	 * System.out.println(" connexion database successful"); }
	 * 
	 * } catch (SQLException e) {
	 * System.out.println("wrong to connect on database"); throw e; } return
	 * connection; }
	 * 
	 * public static Connection getConnection() throws Exception {
	 * 
	 * if(dataSource==null) { dataSource = new BasicDataSource();
	 * dataSource.setUrl("jdbc:mysql://localhost:3306/db_jpa");
	 * dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	 * dataSource.setUrl("root"); dataSource.setPassword(""); } return
	 * dataSource.getConnection(); }
	 */

}
