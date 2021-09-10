package com.codetreatise.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.CompteCreance;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.CompteTampon;
import com.codetreatise.repository.CompteCreanceRepository;
import com.codetreatise.repository.CompteTamponRepository;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

@Controller
public class CompteEpargneMoreDetailController implements Initializable {
	@FXML
	private Label labelSolde;
	@FXML
	private Label labelLaCarte;
	@FXML
	private Label LabelAvaliser;
	@FXML
	private Label LabelTampon;
	@FXML
	private Label labelCreance;
	@FXML
	private Label titulaire;

	@Autowired
	private CompteTamponRepository compteTamponRepository;

	@Autowired
	private CompteCreanceRepository compteCreanceRepository;

	// Event Listener on Button.onAction
	@FXML
	public void handleCloseClick(ActionEvent event) {
		Node node = (Node) event.getSource();
		Stage windows = (Stage) node.getScene().getWindow();
		windows.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public void setValue(CompteEpargne selectedAccount) {
		CompteTampon tamponMontant = compteTamponRepository.findOne(selectedAccount.getEpargneId());
		CompteCreance creanceMontant = compteCreanceRepository.findOne(selectedAccount.getEpargneId());
		titulaire.setText(selectedAccount.getAdherent().getUniqueName());
		labelSolde.setText(String.valueOf(selectedAccount.getSolde() + " FCFA"));
		labelLaCarte.setText(String.valueOf(selectedAccount.getLacarte() + " FCFA"));
		LabelAvaliser.setText((selectedAccount.getAvaliser() == true ? "OUI" : "NON"));
		LabelTampon.setText(tamponMontant == null ? "0.00 FCFA" : String.valueOf(tamponMontant.getDette()+" FCFA"));
		labelCreance.setText(creanceMontant == null ? "0.00 FCFA" : String.valueOf(creanceMontant.getMontant()+" FCFA"));
	}
}
