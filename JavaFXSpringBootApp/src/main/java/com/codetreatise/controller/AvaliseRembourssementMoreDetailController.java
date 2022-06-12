package com.codetreatise.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Avalise;
import com.codetreatise.repository.CompteEpargneRepository;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

@Controller
public class AvaliseRembourssementMoreDetailController implements Initializable {
	
	@FXML
	private Label idAdherent;
	@FXML
	private Label nom;
	@FXML
	private Label prenom;
	@FXML
	private Label idCompte;
	@FXML
	private Label idTransaction;
	@FXML
	private Label timeTransaction;
	@FXML
	private Label montantTransaction;
	@FXML
	private Label credibilite;
	@FXML
	private Label avance;
	@FXML
	private Label reste;
	
	@FXML
	private Label idAdherentPret;
	@FXML
	private Label nomPret;
	@FXML
	private Label prenomPret;
	@FXML
	private Label idComptePret;
	@FXML
	private Label credibilitePret;
	
	@Autowired
	private CompteEpargneRepository compteEpargneRepository;
	
	Avalise avalise;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("avalise: " + nom.getText());
	
	}
	
	public void  setAvalise(Avalise avalise) {
		this.avalise = avalise;
		System.out.println("epargen: " + this.avalise.getCompteEpargne().getAdherent().getNom());
	}
	
	public void setDetailsValue() {
		
		idAdherent.setText(this.avalise.getCompteEpargne().getAdherent().getIdentifiant().toString());
		nom.setText(this.avalise.getCompteEpargne().getAdherent().getNom());
		prenom.setText(this.avalise.getCompteEpargne().getAdherent().getPrenom());
		idCompte.setText(this.avalise.getCompteEpargne().getEpargneId().toString());
		idTransaction.setText(this.avalise.getTransaction().getTransaction_id().toString());
		timeTransaction.setText(this.avalise.getTransaction().getDate().toString());
		montantTransaction.setText(this.avalise.getTransaction().getMontant().toString());
		credibilite.setText(String.valueOf(this.avalise.getCompteEpargne().getLacarte()));
		avance.setText(String.valueOf(this.avalise.getSolder()));
		reste.setText(String.valueOf((this.avalise.getMontant() - this.avalise.getSolder())));
		
		//pret en cour
		idAdherentPret.setText(this.avalise.getTransaction().getAdherent().getIdentifiant().toString());
		nomPret.setText(this.avalise.getTransaction().getAdherent().getNom().toString());
		prenomPret.setText(this.avalise.getTransaction().getAdherent().getPrenom().toString());
		idComptePret.setText(this.avalise.getCompteTampon().getIdTampon().toString());
		credibilitePret.setText(String.valueOf(compteEpargneRepository.findByAdherent(this.avalise.getTransaction().getAdherent()).getLacarte()));
	}
	
	public void display() {
		setDetailsValue();
	}

}
