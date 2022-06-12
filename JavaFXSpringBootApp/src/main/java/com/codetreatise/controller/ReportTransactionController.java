package com.codetreatise.controller;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.Adherent;
import com.codetreatise.bean.Utilisateur;
import com.codetreatise.repository.AdherentRepository;
import com.codetreatise.service.DateUtil;
import com.codetreatise.service.MethodUtilitaire;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

@Controller
public class ReportTransactionController implements Initializable {
	@FXML
	private TableView<Adherent> adherentTable;
	@FXML
	private TableColumn<Adherent, Long> idTableColumn;
	@FXML
	private TableColumn<Adherent, String> nomTableColumn;
	@FXML
	private TableColumn<Adherent, String> prenomTableColumn;
	@FXML
	private TableColumn<Adherent, Boolean> consulterTableColumn;
	@FXML
	private ComboBox<Integer> transactionQuantity;
	@FXML
	private TextField search;
	
	@Autowired
	private AdherentRepository adherentRepository;
	
	private ObservableList<Adherent> adherentList = FXCollections.observableArrayList();
	
	@FXML
	private void handleSearchPressed(KeyEvent event) {
		filteredTable(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		setComboboxeQuantityTransaction();
		setTableProperty();
		loadDataOnTable();

	}

	private void setTableProperty() {

		idTableColumn.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
		nomTableColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
		prenomTableColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		consulterTableColumn.setCellFactory(cellFactory);
	}

	public void loadDataOnTable() {
		
		adherentList.clear();
		adherentList.addAll(adherentRepository.findBySituation("Actif"));
		adherentTable.setItems(adherentList);
	}
	
	private void setComboboxeQuantityTransaction() {
		transactionQuantity.getItems().addAll(10, 15, 25, 40, 50);
	}
	
	private void filteredTable(KeyEvent event) {
		FilteredList<Adherent> filteredadherent = new FilteredList<Adherent>(adherentList, e -> true);
		search.setOnKeyReleased(e -> {
			search.textProperty().addListener((observableValue, oldValue, newValue) -> {
				filteredadherent.setPredicate((Predicate<? super Adherent>) adherent -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String newValueFilter = newValue.toLowerCase();
					if (adherent.getIdentifiant().toString().contains(newValueFilter)) {
						return true;
					} else if (adherent.getNom().toLowerCase().contains(newValueFilter)) {
						return true;
					} else if (adherent.getPrenom().toLowerCase().contains(newValueFilter)) {
						return true;
					}
					return false;
				});
			});
		});

		SortedList<Adherent> sortedList = new SortedList<Adherent>(filteredadherent);
		sortedList.comparatorProperty().bind(adherentTable.comparatorProperty());
		adherentTable.setItems(sortedList);
	}

	Callback<TableColumn<Adherent, Boolean>, TableCell<Adherent, Boolean>> cellFactory = new Callback<TableColumn<Adherent, Boolean>, TableCell<Adherent, Boolean>>() {
		@Override
		public TableCell<Adherent, Boolean> call(final TableColumn<Adherent, Boolean> param) {
			final TableCell<Adherent, Boolean> cell = new TableCell<Adherent, Boolean>() {

				final Button btnEdit = new Button("Imprimer");

				@Override
				public void updateItem(Boolean check, boolean empty) {
					super.updateItem(check, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {
						btnEdit.setOnAction(e -> {
							Adherent adherent = getTableView().getItems().get(getIndex());
							try {
								print(adherent);
							} catch (JRException | SQLException e1) {
								e1.printStackTrace();
							}
						});

						btnEdit.setStyle("-fx-background-color: transparent;");
						setGraphic(btnEdit);
						setAlignment(Pos.CENTER_LEFT);
						setText(null);
					}
				}

				private void print(Adherent adherent) throws JRException, SQLException {
					
					try {
						Integer limit = null;
						String limitString = transactionQuantity.getEditor().getText().trim();
						System.out.println("limitString: " + limitString);
			
						try {
							limit = Integer.parseInt(limitString);
						} catch (Exception e) {
							limit = null;
							//MethodUtilitaire.saveAlert("error", "print error", e.getMessage());
						}finally {
							
							String path = System.getProperty("user.dir");
							String jrxmPath = path + File.separator + "reports" + File.separator + "reportTransaction.jrxml"; 
							String logoPath = path + File.separator + "reports" + File.separator;

							System.setProperty("java.awt.headless", "false");
							
							JasperDesign jasperDesign = JRXmlLoader.load(jrxmPath);
							
							String sql1 = "SELECT t.transaction_id, t.date, t.montant, t.type, a.unique_name  FROM transaction t JOIN adherent a ON a.membre_id = t.adherent_membre_id WHERE a.membre_id = " + adherent.getIdentifiant() + " ORDER BY t.transaction_id DESC LIMIT " + limit;
							
							String sql2 = "SELECT t.transaction_id, t.date, t.montant, t.type, a.unique_name  FROM transaction t JOIN adherent a ON a.membre_id = t.adherent_membre_id WHERE a.membre_id = " + adherent.getIdentifiant() + " ORDER BY t.transaction_id DESC";
							
							JRDesignQuery designQuery = new JRDesignQuery();
							if(limit != null) {
								designQuery.setText(sql1);
							}else {
								designQuery.setText(sql2);
							}
							jasperDesign.setQuery(designQuery);
							JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
							
							 Map<String, Object> param = new HashMap<String, Object>();
							 param.put("logo", logoPath);
							 param.put("id", adherent.getIdentifiant());
							
							JasperPrint print = JasperFillManager.fillReport(jasperReport, param, MethodUtilitaire.getConnection());
							JasperViewer jrviewer = new JasperViewer(print, false);
							// JasperViewer.viewReport(print);
							jrviewer.setVisible(true);
							jrviewer.toFront();
						}
					} catch (Exception e) {
						MethodUtilitaire.deleteNoPersonSelectedAlert("error", "print error", e.getMessage());
					}

				}
			};
			return cell;
		}
	};

}
