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

@Entity
@Table(name="transaction")
public class Transaction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Transaction() {
		
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false, updatable=false, name="transactionId")
	private Long transaction_id;
	private String type;
	private Float montant;
	private Date date;
	
	@OneToOne
	private Adherent adherent;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the montant
	 */
	public Float getMontant() {
		return montant;
	}

	/**
	 * @param montant the montant to set
	 */
	public void setMontant(Float montant) {
		this.montant = montant;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the adherent
	 */
	public Adherent getAdherent() {
		return adherent;
	}

	/**
	 * @param adherent the adherent to set
	 */
	public void setAdherent(Adherent adherent) {
		this.adherent = adherent;
	}

	/**
	 * @return the transaction_id
	 */
	public Long getTransaction_id() {
		return transaction_id;
	}
	

}
