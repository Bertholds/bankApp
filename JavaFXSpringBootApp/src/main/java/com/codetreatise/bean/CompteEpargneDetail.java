package com.codetreatise.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "compteepargnedetail")
public class CompteEpargneDetail implements Serializable {

	private static final long serialVersionUID = -413991778831519831L;

	public CompteEpargneDetail() {
		// TODO Auto-generated constructor stub
	}

	@Id()
	@Column(updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCompteEpargneDetail;

	private Long montant;

	private Long montantTransaction;

	private String date;

	private Long credibilitePerMonth;

	private Long pretPerMonth;

	@OneToOne
	private CompteEpargne compteEpargne;

	public Long getIdCompteEpargneDetail() {
		return idCompteEpargneDetail;
	}

	public Long getCredibilitePerMonth() {
		return credibilitePerMonth;
	}

	public void setCredibilitePerMonth(Long credibilitePerMonth) {
		this.credibilitePerMonth = credibilitePerMonth;
	}

	public Long getPretPerMonth() {
		return pretPerMonth;
	}

	public void setPretPerMonth(Long pretPerMonth) {
		this.pretPerMonth = pretPerMonth;
	}

	public Long getMontant() {
		return montant;
	}

	public void setMontant(Long montant) {
		this.montant = montant;
	}

	public Long getMontantTransaction() {
		return montantTransaction;
	}

	public void setMontantTransaction(Long montantTransaction) {
		this.montantTransaction = montantTransaction;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public CompteEpargne getCompteEpargne() {
		return compteEpargne;
	}

	public void setCompteEpargne(CompteEpargne compteEpargne) {
		this.compteEpargne = compteEpargne;
	}

}
