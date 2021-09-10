package com.codetreatise.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.codetreatise.bean.Adherent;
import com.codetreatise.service.GlobalService;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

@Repository
@Transactional
public class AdherentServiceImpl implements GlobalService<Adherent> {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Adherent update(Adherent adherent) {
		Adherent adherentMerge = em.merge(adherent);
		return adherentMerge;
	}

	public boolean isInputValid(String username, String password) {
		String errorMessage = "";

		if (username == null || username.length() == 0) {
			errorMessage += "No valid login!\n";
		}
		if (password == null || password.length() == 0) {
			errorMessage += "No valid password!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}
