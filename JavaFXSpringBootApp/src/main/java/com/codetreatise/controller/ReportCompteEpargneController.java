package com.codetreatise.controller;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.CompteEpargneDetail;
import com.codetreatise.repository.CompteEpargneDetailRepository;
import com.codetreatise.repository.CompteEpargneRepository;
import com.codetreatise.service.DateUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;

import javafx.scene.chart.BarChart;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

@Controller
public class ReportCompteEpargneController implements Initializable {
	@FXML
	private BarChart<String, Long> barChart;

	@FXML
	private CategoryAxis xAxe;

	@FXML
	private NumberAxis yAxe;

	@Autowired
	private CompteEpargneDetailRepository compteEpargneDetailRepository;
	@Autowired
	private CompteEpargneRepository compteEpargneRepository;

	private ObservableList<String> monthNames = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		barChart.setTitle("Évolution des actifs en fonction des mois");
		xAxe.setLabel("Les mois de l'année");
		yAxe.setLabel("Total en fcfa");

		// Rotate the label of Tick Marks 90 degrees
		xAxe.setTickLabelRotation(90);

		String[] months = DateFormatSymbols.getInstance(Locale.FRENCH).getMonths();
		// Convert it to a list and add it to our ObservableList of months.
		monthNames.addAll(Arrays.asList(months));

		// Assign the month names as categories for the horizontal axis.
		//xAxe.setCategories(monthNames);

		setAmountCredibiliteData(getCompteEpargneDetailList());

	}

	private List<CompteEpargneDetail> getCompteEpargneDetailList() {
		String months[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		List<CompteEpargneDetail> compteEpargneDetails = new ArrayList<CompteEpargneDetail>();

		for (String month : months) {

			String dateLike = "2022" + "-" + month ;
			System.out.println("dateLike: " + dateLike);
			for (CompteEpargne compteEpargne : compteEpargneRepository.findAll()) {
				CompteEpargneDetail cd = compteEpargneDetailRepository.findByCompteEpargneAndDateAndMaxidPerMont(
						"%" + dateLike + "%", compteEpargne,
						compteEpargneDetailRepository.getReport("%" + dateLike + "%", compteEpargne));
				if (cd != null) {
					compteEpargneDetails.add(cd);
				}
			}
		}

		return compteEpargneDetails;
	}

	public void setAmountCredibiliteData(List<CompteEpargneDetail> compteEpargneDetails) {

		System.out.println("sizeof list: " + compteEpargneDetails.size());
		// Count the number of people having their birthday in a specific month.
		Long[] monthCounter = new Long[12];
		for (CompteEpargneDetail cd : compteEpargneDetails) {
			
			int month = DateUtil.parse(cd.getDate()).getMonthValue() - 1;
			System.out.println("Month: " + month);
			
			if(monthCounter[month] != null) {
				monthCounter[month] += cd.getCredibilitePerMonth();
			}else {
				monthCounter[month] = cd.getCredibilitePerMonth();
			}
		}

		XYChart.Series<String, Long> series = new XYChart.Series<>();
		series.setName("2022");

		for (int i = 0; i < monthCounter.length; i++) {
			series.getData().add(new XYChart.Data<String, Long>(monthNames.get(i), monthCounter[i] == null ? 0 : monthCounter[i]));
		}

		System.out.println("serie: " + series.getData().size() + " mount 2: " + monthCounter[2]);
		barChart.getData().add(series);

	}

}
