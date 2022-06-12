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
@Table(name="avalise")
public class Avalise implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Avalise() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@Column(name="avaliseId", nullable=false, updatable=false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long avalise_id;
	
	private boolean remboursser;
	private Long solder;
	private Long montant;
	private Long reste;
	
	@OneToOne
	private CompteEpargne compteEpargne;
	@OneToOne
	private Transaction transaction;
	@OneToOne
	private CompteTampon compteTampon;
	@OneToOne
	private CompteCreance compteCreance;
	/**
	 * @return the remboursser
	 */
	public boolean getRemboursser() {
		return remboursser;
	}
	/**
	 * @param remboursser the remboursser to set
	 */
	public void setRemboursser(boolean remboursser) {
		this.remboursser = remboursser;
	}
	/**
	 * @return the rembourssement
	 */
	public Long getSolder() {
		return solder;
	}
	/**
	 * @param rembourssement the rembourssement to set
	 */
	public void setSolder(Long rembourssement) {
		this.solder = rembourssement;
	}
	
	/**
	 * @return the montant
	 */
	public Long getMontant() {
		return montant;
	}
	/**
	 * @param montant the montant to set
	 */
	public void setMontant(Long montant) {
		this.montant = montant;
	}
	
	
	public Long getReste() {
		return reste;
	}
	public void setReste(Long reste) {
		this.reste = reste;
	}
	/**
	 * @return the compteEpargne
	 */
	public CompteEpargne getCompteEpargne() {
		return compteEpargne;
	}
	/**
	 * @param compteEpargne the compteEpargne to set
	 */
	public void setCompteEpargne(CompteEpargne compteEpargne) {
		this.compteEpargne = compteEpargne;
	}
	/**
	 * @return the compteCreance
	 */
	public CompteCreance getCompteCreance() {
		return compteCreance;
	}
	/**
	 * @param compteCreance the compteCreance to set
	 */
	public void setCompteCreance(CompteCreance compteCreance) {
		this.compteCreance = compteCreance;
	}
	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}
	/**
	 * @param transaction the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	/**
	 * @return the compteTampon
	 */
	public CompteTampon getCompteTampon() {
		return compteTampon;
	}
	/**
	 * @param compteTampon the compteTampon to set
	 */
	public void setCompteTampon(CompteTampon compteTampon) {
		this.compteTampon = compteTampon;
	}
	/**
	 * @return the avalise_id
	 */
	public Long getAvalise_id() {
		return avalise_id;
	}
	
}
