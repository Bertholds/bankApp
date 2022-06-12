package com.codetreatise.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="report")
public class Report implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id()
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false, updatable=false, name="reportId")
	private Long idReport;
	private Long soldeAu;
	private Long report;
	private Long depot;
	
	@OneToOne
	private CompteEpargneDetail compteEpargneDetail;

	public Report() {
		
	}
	

	public Long getDepot() {
		return depot;
	}


	public void setDepot(Long depot) {
		this.depot = depot;
	}


	public Long getIdReport() {
		return idReport;
	}

	public Long getSoldeAu() {
		return soldeAu;
	}

	public void setSoldeAu(Long soldeAu) {
		this.soldeAu = soldeAu;
	}

	public Long getReport() {
		return report;
	}

	public void setReport(Long report) {
		this.report = report;
	}

	public CompteEpargneDetail getCompteEpargneDetail() {
		return compteEpargneDetail;
	}

	public void setCompteEpargneDetail(CompteEpargneDetail compteEpargneDetail) {
		this.compteEpargneDetail = compteEpargneDetail;
	}
	
	

}
