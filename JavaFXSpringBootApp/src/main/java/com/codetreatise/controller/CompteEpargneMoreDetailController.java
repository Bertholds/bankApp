package com.codetreatise.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Avalise;
import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.repository.AvaliseRepository;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

@Controller
public class CompteEpargneMoreDetailController implements Initializable {
	
	@FXML
	private TableView<Avalise> avaliseTab;
	
	@FXML
	private TableColumn<Avalise, Long> id;
	
	@FXML
	private TableColumn<Avalise, String> nom;
	
	@FXML
	private TableColumn<Avalise, Long> montant;
	
	@FXML
	private TableColumn<Avalise, Long> avance;
	
	@FXML
	private TableColumn<Avalise, Long> reste;
	
	@FXML
	private Label titulaire;

	List<Avalise> avalises;
	
	
	private ObservableList<Avalise> avaliseList = FXCollections.observableArrayList();

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

	public void setValue(CompteEpargne selectedAccount, List<Avalise> avalises) {
		
		titulaire.setText(selectedAccount.getAdherent().getUniqueName());
		this.avalises = avalises;
		
		setTableProperty();
		
		loadDataTable(avalises);
	}

	private void loadDataTable(List<Avalise> avalises) {
		avaliseList.clear();
		
		//On recupere tous ses engagement en cour sauf les engagement de son propre compte
		avaliseList.addAll(avalises);
		avaliseTab.setItems(avaliseList);
	}

	private void setTableProperty() {
		
		id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Avalise,Long>, ObservableValue<Long>>() {
			
			@Override
			public ObservableValue<Long> call(CellDataFeatures<Avalise, Long> param) {
				
				return new SimpleObjectProperty<Long>(param.getValue().getTransaction().getAdherent().getIdentifiant());
			}
		});
		
		nom.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Avalise,String>, ObservableValue<String>>() {
			
			@Override
			public ObservableValue<String> call(CellDataFeatures<Avalise, String> param) {
				// 
				return new SimpleStringProperty(param.getValue().getTransaction().getAdherent().getNom());
			}
		});
		
		montant.setCellValueFactory(new PropertyValueFactory<>("montant"));
		
		avance.setCellValueFactory(new PropertyValueFactory<>("solder"));
		
		reste.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Avalise,Long>, ObservableValue<Long>>() {
			
			@Override
			public ObservableValue<Long> call(CellDataFeatures<Avalise, Long> param) {
				// TODO Auto-generated method stub
				return new SimpleObjectProperty<Long>(param.getValue().getMontant() - param.getValue().getSolder());
			}
		});
		
	}
}
