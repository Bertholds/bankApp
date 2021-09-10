package com.codetreatise.bean;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "operation")
public class Operation implements Serializable {

	private static final long serialVersionUID = 6962688665808728193L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_operation;
	private String name;
	private String cible;
	private Date date;
	private String address;
	@OneToOne()
	private Utilisateur utilisateur;

	public Operation() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCible() {
		return cible;
	}

	public void setCible(String cible) {
		this.cible = cible;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Long getId_operation() {
		return id_operation;
	}

}
