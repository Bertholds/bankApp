/**
 * 
 */
package com.codetreatise.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Chrispin
 *
 */
@Entity
@Table
public class CompteCreance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@Column(nullable=false, updatable=false)
	private Long idCreance;
	private Long montant;
	
	public CompteCreance() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the idCreance
	 */
	public Long getIdCreance() {
		return idCreance;
	}

	/**
	 * @param idCreance the idCreance to set
	 */
	public void setIdCreance(Long idCreance) {
		this.idCreance = idCreance;
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

	
	
}
