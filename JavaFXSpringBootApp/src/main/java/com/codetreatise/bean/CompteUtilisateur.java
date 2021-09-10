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
@Table(name="compteutilisateur")
public class CompteUtilisateur implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CompteUtilisateur() {
		// TODO Auto-generated constructor stub
	}
	@Id
	@Column(nullable=false, updatable=false)
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long utilisateur_id;
	
	private String niveau_access;
	private String login;
	private String pass;
	
	@OneToOne
	private Utilisateur utilisateur;

	public String getNiveau_access() {
		return niveau_access;
	}

	public void setNiveau_access(String niveau_access) {
		this.niveau_access = niveau_access;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Long getUtilisateur_id() {
		return utilisateur_id;
	}

	/**
	 * @param utilisateur_id the utilisateur_id to set
	 */
	public void setUtilisateur_id(Long utilisateur_id) {
		this.utilisateur_id = utilisateur_id;
	}
	
	

}
