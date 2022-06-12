/**
 * 
 */
package com.codetreatise.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Chrispin
 *
 */
@Entity
@Table(name="fond")
public class Fond implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5320235503946775811L;

	/**
	 * 
	 */
	public Fond() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long fondId;
	
	private Long solde;
	
	@OneToOne
	private CompteEpargne compteEpargne;

	/**
	 * @return the solde
	 */
	public Long getSolde() {
		return solde;
	}

	/**
	 * @param solde the solde to set
	 */
	public void setSolde(Long solde) {
		this.solde = solde;
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
	 * @return the fondId
	 */
	public Long getFondId() {
		return fondId;
	}
	
	

}
