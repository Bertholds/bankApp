package com.codetreatise.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.CompteUtilisateur;
import com.codetreatise.bean.Utilisateur;
import com.codetreatise.config.StageManager;
import com.codetreatise.repository.UserAccountRepository;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.service.UserService;
import com.codetreatise.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Controller
public class LoginController implements Initializable{

	@FXML
    private Button btnLogin;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label lblLogin;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserAccountRepository userAccountRepository; 
    
    @Lazy
    @Autowired
    private StageManager stageManager;
    
    @Autowired
    private MethodUtilitaire methodUtilitaire;
      
    File file;
	CompteUtilisateur compteUtilisateur;
    
	private void serializeUser(CompteUtilisateur compteUtilisateur) throws IOException {
		String path = /*LoginController.class.getProtectionDomain().getCodeSource().getLocation().getPath()*/ System.getProperty("user.dir");
		System.out.println(path);
		file = new File(path+File.separator+"serializeUsser.txt");
		System.out.println("Present Project Directory : "+ System.getProperty("user.dir"));
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(compteUtilisateur);
		oos.close();
		fos.close();
	}
	
	@FXML
    private void login(ActionEvent event) throws IOException{
    	if(userService.authenticate(getUsername(), getPassword())){
    		compteUtilisateur = userAccountRepository.authentification(getUsername(), getPassword());    		
    		serializeUser(compteUtilisateur);
			methodUtilitaire.LogFile("Operation de connection", "Pas de cible", compteUtilisateur.getUtilisateur());
    		stageManager.switchScene(FxmlView.HOME);
    		
    	}else{
    		lblLogin.setText("Login Failed.");
    	}
    }
	
	public String getPassword() {
		return password.getText();
	}

	public String getUsername() {
		return username.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public String getAcces() {
		return compteUtilisateur.getNiveau_access();
	}

}
