package com.codetreatise.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.Transaction;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

@Controller
public class TransactionPretDetailController implements Initializable {
	@FXML
	private Label idPret;
	@FXML
	private Label nomPret;
	@FXML
	private Label prenomPret;
	@FXML
	private Label montantPret;
	@FXML
	private Label datePret;
	@FXML
	private Label idTransactionPret;
	@FXML
	private Label idEngagement;
	@FXML
	private Label nomEngagement;
	@FXML
	private Label prenomEngagement;
	@FXML
	private Label idCompteEngagement;

	// Event Listener on Button.onAction
	@FXML
	public void handleCloseClick(ActionEvent event) {
		Node node = (Node) event.getSource();
		Stage windows = (Stage) node.getScene().getWindow();
		windows.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setValue(Avalise avalise, Transaction transaction) {

		setPretDetail(transaction);
		setEngagementDetail(avalise);
	}

	private void setEngagementDetail(Avalise avalise) {
		idEngagement.setText(avalise.getCompteEpargne().getAdherent().getIdentifiant().toString());
		nomEngagement.setText(avalise.getCompteEpargne().getAdherent().getNom());
		prenomEngagement.setText(avalise.getCompteEpargne().getAdherent().getPrenom());
		idCompteEngagement.setText(avalise.getCompteEpargne().getEpargneId().toString());

	}

	private void setPretDetail(Transaction transaction) {
		idPret.setText(transaction.getAdherent().getIdentifiant().toString());
		nomPret.setText(transaction.getAdherent().getNom());
		prenomPret.setText(transaction.getAdherent().getPrenom());
		montantPret.setText(transaction.getMontant().toString());
		datePret.setText(transaction.getDate().toString());
		idTransactionPret.setText(transaction.getTransaction_id().toString());

	}
}
