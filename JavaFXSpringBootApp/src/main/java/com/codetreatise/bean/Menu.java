/**
 * 
 */
package com.codetreatise.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Chrispin
 *
 */
@Entity
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(nullable=false, updatable=false)
	private Long id;
	private String nom;
	private String droitAcces;
	
	public Menu() {
		
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the droitAcces
	 */
	public String getDroitAcces() {
		return droitAcces;
	}

	/**
	 * @param droitAcces the droitAcces to set
	 */
	public void setDroitAcces(String droitAcces) {
		this.droitAcces = droitAcces;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	
	
}
