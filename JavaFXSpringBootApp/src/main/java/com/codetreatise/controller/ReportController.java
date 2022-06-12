package com.codetreatise.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.jasperreports.JasperReportsUtils;

import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.CompteEpargneDetail;
import com.codetreatise.bean.Report;
import com.codetreatise.config.StageManager;
import com.codetreatise.repository.CompteEpargneDetailRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.repository.ReportRepository;
import com.codetreatise.service.DateUtil;
import com.codetreatise.service.MethodUtilitaire;
import com.codetreatise.view.FxmlView;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

@Controller
public class ReportController implements Initializable {
	@FXML
	private DatePicker soldeAu;
	@FXML
	private DatePicker report;
	@FXML
	private Button btnGenerate;
	@FXML
	private Button btnPrint;
	@Autowired
	private CompteEpargneRepository compteEpargneRepository;

	@Autowired
	private CompteEpargneDetailRepository compteEpargneDetailRepository;

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private ProgressBarController progressBarController;

	@Autowired
	@Lazy
	private StageManager stageManager;

	LocalDate soldeAuDate;
	LocalDate reportDate;

	public Task<Object> taskWorker(int executedQueryCount) {
		return new Task<Object>() {

			@Override
			protected Object call() throws Exception {

				int compteEpargneCount = 1;
				
				// On vide la table report
				reportRepository.deleteAll();

				for (CompteEpargne c : compteEpargneRepository.findByStatut("Actif")) {

					System.out.println("epargneId " + c.getEpargneId());

					// Année et mois du report
					String dateReportLike = getReport().substring(0, 7);

					// Année et mois du solde au
					String dateSoldeAutLike = getSoldeAu().substring(0, 7);

					// Année et mois pour la situation mensuelle
					
					String dateLikeNextMonth = nextMont(reportDate).substring(0, 7);

					System.out.println("Date like: " + dateReportLike);
					System.out.println("Date like next: " + dateLikeNextMonth);

					// Solde a une date choisi
					Long idCompteEpargneDetailSoldeAu = compteEpargneDetailRepository.getSoldeAu(getSoldeAu(), c);
					Long soldeAu;
					if (idCompteEpargneDetailSoldeAu != null) {
						soldeAu = compteEpargneDetailRepository.findOne(idCompteEpargneDetailSoldeAu).getMontant();
					} else {
						// si durant la date selectionné pour solde au il ny'a pas eu de depot
						// il faut recuperer le solde précedent correspondant à la date précédente la
						// plus proche
						soldeAu = getPreviousSolde(soldeAuDate, dateSoldeAutLike, c);
					}

					Long idCompteEpargneDetailReportValue = compteEpargneDetailRepository
							.getReport("%" + dateReportLike + "%", c);
					Long reportValue;
					if (idCompteEpargneDetailReportValue != null) {
						reportValue = compteEpargneDetailRepository.findOne(idCompteEpargneDetailReportValue)
								.getMontant();
					} else {

						reportValue = getPreviousSolde(reportDate, dateReportLike, c);
					}

					System.out.println("Solde au: " + soldeAu);
					System.out.println("Report; " + reportValue);

					// Le montant pour le mois dont on genere le rapport
					Long idCompteEpargneDetailCurrentValue = compteEpargneDetailRepository
							.getReport("%" + dateLikeNextMonth + "%", c);
					Long currentValue;
					if (idCompteEpargneDetailCurrentValue != null) {
						currentValue = compteEpargneDetailRepository.findOne(idCompteEpargneDetailCurrentValue)
								.getMontant();
					} else {
						currentValue = reportValue;
						dateLikeNextMonth = dateReportLike;
						System.out.println("CurrentValue is null; dateLikeNextMonth: " + dateLikeNextMonth);
					}

					Long maxIdPerMont = compteEpargneDetailRepository.getReport("%" + dateLikeNextMonth + "%", c);
					CompteEpargneDetail cd = compteEpargneDetailRepository
							.findByCompteEpargneAndDateAndMaxidPerMont("%" + dateLikeNextMonth + "%", c, maxIdPerMont);

					System.out.println("CurrentValue: " + currentValue);

					Report report = new Report();
					
					Long depot = compteEpargneDetailRepository.getDepotPerMont("%"+ nextMont(reportDate).substring(0, 7) +"%", c);

					report.setSoldeAu(soldeAu);
					report.setReport(reportValue);
					report.setCompteEpargneDetail(cd);
					if(depot != null) {
						report.setDepot(depot);
					}else
						report.setDepot((long)0);

					reportRepository.save(report);

					compteEpargneCount = compteEpargneCount + 1;
					
					updateProgress(compteEpargneCount, executedQueryCount);

				}

				return true;
			}
		};
	}

	@FXML
	private void handleBtnPrintClick(ActionEvent event) throws JRException, SQLException {
		
		try {
			LocalDate reportDateFinal = reportDate.plusMonths(1);
			String soldeInitial = getSoldeAu();
			String report = getReport();

			String path = System.getProperty("user.dir");
			String jrxmPath = path + File.separator  + "reports" + File.separator + "bank.jrxml";
			String logoPath = path + File.separator + "reports" + File.separator;
			String subReportPath =  path + File.separator + "reports" + File.separator;

			System.setProperty("java.awt.headless", "false");
			
			JasperDesign jasperDesign = JRXmlLoader.load(jrxmPath);
			
			String sql = " SELECT a.membre_id AS no, a.unique_name AS nom, r.solde_au, r.report, r.depot, cd.montant AS total, ct.dette, cd.credibilite_per_month AS credibilite \r\n"
					+ "FROM report r \r\n" + "JOIN compteepargnedetail cd\r\n"
					+ "ON cd.id_compte_epargne_detail=r.compte_epargne_detail_id_compte_epargne_detail\r\n"
					+ "JOIN compteepargne ce\r\n" + "ON ce.epargne_id=cd.compte_epargne_epargne_id\r\n"
					+ "JOIN comptetampon ct\r\n" + "ON ct.id_tampon=ce.compte_tampon_id_tampon\r\n" + "JOIN adherent a \r\n"
					+ "ON a.membre_id=ce.adherent_membre_id "
					+ "ORDER BY a.membre_id";
			
			JRDesignQuery designQuery = new JRDesignQuery();
			designQuery.setText(sql);
			jasperDesign.setQuery(designQuery);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			
			 Map<String, Object> param = new HashMap<String, Object>();
			 param.put("logo", logoPath);
			 param.put("subReport", subReportPath);
			 param.put("date", reportDateFinal.getMonth() + " " + reportDateFinal.getYear());
			 param.put("soldeInitial", soldeInitial);
			 param.put("report", report);
			
			JasperPrint print = JasperFillManager.fillReport(jasperReport, param, MethodUtilitaire.getConnection());
			JasperViewer jrviewer = new JasperViewer(print, false);
			// JasperViewer.viewReport(print);
			jrviewer.setVisible(true);
			jrviewer.toFront();
		} catch (Exception e) {
			MethodUtilitaire.deleteNoPersonSelectedAlert("error", "print error", e.getMessage());
		}
	}

	@FXML
	public void handleGenerateClick(ActionEvent event) throws InterruptedException {
		if (isInputValid()) {

			stageManager.switchSceneShowPreviousStageInitOwner(FxmlView.PROGRESSBAR);
			progressBarController.setTask(taskWorker(compteEpargneRepository.findByStatut("Actif").size()));

			btnPrint.setVisible(true);
			System.out.println("start ok");

		}

	}

	// si durant la date selectionné pour solde au il ny'a pas eu de depot
	// il faut recuperer le solde de precedent le plus proche
	@SuppressWarnings("unused")
	private Long getPreviousSolde(LocalDate soldeAuDate, String dateLike, CompteEpargne compteEpargne) {
		List<CompteEpargneDetail> compteEpargneDetails = compteEpargneDetailRepository
				.getCompteEpargneDetailByDateAndCompteEpargne("%" + dateLike + "%", compteEpargne);

		Map<Integer, String> daysMaps = new HashMap<Integer, String>();

		while (true) {
			if (compteEpargneDetails.size() > 0) {

				for (CompteEpargneDetail cd : compteEpargneDetails) {
					String day = cd.getDate().substring(5, 7);
					if (day.contains("0")) {
						day = day.replace("0", "");
					}
					daysMaps.put(Integer.parseInt(day), cd.getDate());
					System.out.println("day: " + day);
				}
				break;
			} else {
				soldeAuDate = soldeAuDate.minusMonths(1);
				String dateLikePreviousMonth = DateUtil.format(soldeAuDate).substring(0, 7);
				compteEpargneDetails = compteEpargneDetailRepository
						.getCompteEpargneDetailByDateAndCompteEpargne("%" + dateLikePreviousMonth + "%", compteEpargne);

				// Si le solde au ou le report ne donne aucun resultat apres décrementation des
				// mois
				// jusqu'a la date de creation des compte epargne on break pour return null
				LocalDate localDateCreationCompteEpargne = DateUtil
						.convertToLocalDateViaInstant(compteEpargne.getDateCreation());
				System.out.println("LocalDateCrea: " + localDateCreationCompteEpargne.toString());
				System.out.println("soldeAuDate: " + soldeAuDate.toString());
				if (soldeAuDate.isBefore(localDateCreationCompteEpargne)) {
					System.out.println("Fin de script");
					break;
				}

			}
		}

		// On recupere le jour du mois le plus grand pour recuperer son solde en
		// trillant la map
		TreeMap<Integer, String> mapTrier = new TreeMap<>(daysMaps);
		if (mapTrier.size() > 0) {
			String date = mapTrier.get(mapTrier.keySet().toArray()[0]);

			Long idChoice = compteEpargneDetailRepository.getSoldeAu(date, compteEpargne);
			return compteEpargneDetailRepository.findOne(idChoice).getMontant();
		}

		return null;

	}

	// Ajoute 1 mois au mois a la date passer en parametre
	private String nextMont(LocalDate date) {
		LocalDate nextMonth = date.plusMonths(1);

		return DateUtil.format(nextMonth);
	}

	private void formatDate() {
		DateUtil.datePickerFormat(soldeAu);
		DateUtil.datePickerFormat(report);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnPrint.setVisible(false);
		formatDate();

	}

	private String getSoldeAu() {
		return soldeAu.getEditor().getText();
	}

	private String getReport() {
		return report.getEditor().getText();
	}

	private boolean isInputValid() {
		String errorMessage = "";
		if (getSoldeAu() == null || getSoldeAu().trim().length() == 0) {
			errorMessage += "Solde est invalide!\n";
		} else {
			if (!DateUtil.validDate(getSoldeAu()))
				errorMessage += "La date du solde ne correspond pas au format valide yyyy-MM-dd";
		}

		if (getReport() == null || getReport().trim().length() == 0) {
			errorMessage += "report est invalide!\n";
		} else {
			if (!DateUtil.validDate(getReport()))
				errorMessage += "La date du report ne correspond pas au format valide yyyy-MM-dd";
		}

		if (errorMessage.length() == 0) {
			reportDate = DateUtil.parse(getReport());
			soldeAuDate = DateUtil.parse(getSoldeAu());
			return true;
		} else {
			// Show the error message.
			MethodUtilitaire.errorMessageAlert("Formulaire invalide", "Vérifier les champs invalide", errorMessage);
			return false;
		}
	}

}
