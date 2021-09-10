/**
 * 
 */
package com.codetreatise.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
	
	private float solde;
	private float fond;
	private float lacarte;
	private boolean avaliser;
	private String statut;
	
	@OneToOne
	private Adherent adherent;

	public float getSolde() {
		return solde;
	}

	public void setSolde(float solde) {
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
	public void setEpargneId(Long id) {
		this.epargneId = id;
	}
	public void setFond(float fond) {
		this.fond = fond;
	}
	public float getFond() {
		return fond;
	}

	/**
	 * @return the lacarte
	 */
	public float getLacarte() {
		return lacarte;
	}

	/**
	 * @param lacarte the lacarte to set
	 */
	public void setLacarte(float lacarte) {
		this.lacarte = lacarte;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}
	

}
