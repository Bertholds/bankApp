/**
 * 
 */
package com.codetreatise.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Chrispin
 *
 */
@Entity
@Table(name="compteepargne")
public class CompteEpargne implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public CompteEpargne() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@Column(updatable=false, nullable=false)
	private Long epargneId;
	
	private Long solde;
	private Long fond;
	private Long lacarte;
	private boolean avaliser;
	private String statut;
	
	@Temporal(TemporalType.DATE)
	private Date dateCreation;
	
	@OneToOne
	private Adherent adherent;

	@OneToOne
	private CompteTampon compteTampon;
	
	@OneToOne
	private CompteCreance compteCreance;
	
	public Long getSolde() {
		return solde;
	}

	public void setSolde(Long solde) {
		this.solde = solde;
	}

	public boolean getAvaliser() {
		return avaliser;
	}

	public void setAvaliser(boolean avaliser) {
		this.avaliser = avaliser;
	}

	public Adherent getAdherent() {
		return adherent;
	}

	public void setAdherent(Adherent adherent) {
		this.adherent = adherent;
	}

	public Long getEpargneId() {
		return epargneId;
	}
	
	public CompteTampon getCompteTampon() {
		return compteTampon;
	}

	public void setCompteTampon(CompteTampon compteTampon) {
		this.compteTampon = compteTampon;
	}

	public CompteCreance getCompteCreance() {
		return compteCreance;
	}

	public void setCompteCreance(CompteCreance compteCreance) {
		this.compteCreance = compteCreance;
	}

	public void setEpargneId(Long id) {
		this.epargneId = id;
	}
	public void setFond(Long fond) {
		this.fond = fond;
	}
	public Long getFond() {
		return fond;
	}

	/**
	 * @return the lacarte
	 */
	public Long getLacarte() {
		return lacarte;
	}

	/**
	 * @param lacarte the lacarte to set
	 */
	public void setLacarte(Long lacarte) {
		this.lacarte = lacarte;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	

}
