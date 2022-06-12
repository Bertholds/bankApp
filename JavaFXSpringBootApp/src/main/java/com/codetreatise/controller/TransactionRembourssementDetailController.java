package com.codetreatise.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.control.Accordion;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.cfg.annotations.IdBagBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.Transaction;
import com.codetreatise.repository.TransactionRepository;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@Controller
public class TransactionRembourssementDetailController implements Initializable {
	@FXML
	private Label idAdherentRembourssement;
	@FXML
	private Label nomRembourssement;
	@FXML
	private Label prenomRembourssement;
	@FXML
	private Label montantRembourssement;
	@FXML
	private Label avanceRembourssement;
	@FXML
	private Label resteRembourssement;
	@FXML
	private Label idTransactionRembourssement;
	@FXML
	private Button btnOtherTransaction;
	@FXML
	private Label idBeneficier;
	@FXML
	private Label nomBeneficier;
	@FXML
	private Label prenomBeneficier;
	@FXML
	private Label idCompteBeneficier;
	@FXML
	private Accordion accordionOtherTransaction;

	@Autowired
	private TransactionRepository transactionRepository;

	// Event Listener on Button[#btnOtherTransaction].onAction
	@FXML
	public void handleOtherTransactionClick(ActionEvent event) {
		accordionOtherTransaction.setVisible(true);
	}

	// Event Listener on Button.onAction
	@FXML
	public void handleCloseClick(ActionEvent event) {
		Node node = (Node) event.getSource();
		Stage windows = (Stage) node.getScene().getWindow();
		windows.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnOtherTransaction.setVisible(false);
		accordionOtherTransaction.setVisible(false);
	}

	public void setValue(Avalise avalise, Transaction transaction) {
		setRembourssementDetail(transaction, avalise);
		setDetailCompteBeneficier(avalise);

	}

	private void setDetailCompteBeneficier(Avalise avalise) {

		idBeneficier.setText(avalise.getCompteEpargne().getAdherent().getIdentifiant().toString());
		nomBeneficier.setText(avalise.getCompteEpargne().getAdherent().getNom());
		prenomBeneficier.setText(avalise.getCompteEpargne().getAdherent().getPrenom());
		idCompteBeneficier.setText(avalise.getCompteEpargne().getEpargneId().toString());

	}

	private void setRembourssementDetail(Transaction transaction, Avalise avalise) {

		idAdherentRembourssement.setText(transaction.getAdherent().getIdentifiant().toString());
		nomRembourssement.setText(transaction.getAdherent().getNom());
		prenomRembourssement.setText(transaction.getAdherent().getPrenom());
		montantRembourssement.setText(avalise.getMontant().toString());
		avanceRembourssement.setText(avalise.getSolder().toString());
		resteRembourssement.setText(String.valueOf(avalise.getMontant() - avalise.getSolder()));
		idTransactionRembourssement.setText(transaction.getTransaction_id().toString());

		List<Transaction> transactions = transactionRepository.findByAvalise(avalise);
		if (transactions.size() > 1) {
			btnOtherTransaction.setVisible(true);

			for (Transaction t : transactions) {

				
				if (!t.getTransaction_id().equals(transaction.getTransaction_id())) {
					Text label = new Text(
							"Montant: " + t.getMontant() + "Fcfa \n \n" + "Datte: " + t.getDate().toString());
					// label.(Color.web("#0076a3"));
					TitledPane titledPane = new TitledPane("ID transaction: " + t.getTransaction_id(), label);
					accordionOtherTransaction.getPanes().add(titledPane);
				}

			}
		}

	}
}
