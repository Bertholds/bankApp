package com.codetreatise.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

@Controller
public class ProgressBarController implements Initializable {
	@FXML
	private ProgressBar progressBar;
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private Label title;
	
	Task<Object> task;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void setTask(Task<Object> task) throws InterruptedException {
		 this.task = task;
		
		 progressBar.progressProperty().unbind();
		 progressIndicator.progressProperty().unbind();
		 
		progressIndicator.progressProperty().bind(task.progressProperty());
		progressBar.progressProperty().bind(task.progressProperty());
		
		task.setOnSucceeded(e->{
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			getProgressBarStage().close();
		});
		
		task.setOnFailed(e->{
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//getProgressBarStage().close();
			title.setText("Échec");
		});
		
		Thread thread = new Thread(task);
		thread.start();
		
		title.setText("Terminé");

	}
	
	public Stage getProgressBarStage() {
		
		return (Stage) progressIndicator.getScene().getWindow();
	}

}
