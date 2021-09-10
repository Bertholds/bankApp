package com.codetreatise.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="comptetampon")
public class CompteTampon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CompteTampon() {
		// TODO Auto-generated constructor stub
	}

	@Id
	private Long idTampon;
	private Float dette;
	/**
	 * @return the idTampon
	 */
	public Long getIdTampon() {
		return idTampon;
	}
	/**
	 * @param idTampon the idTampon to set
	 */
	public void setIdTampon(Long idTampon) {
		this.idTampon = idTampon;
	}
	/**
	 * @return the dette
	 */
	public Float getDette() {
		return dette;
	}
	/**
	 * @param dette the dette to set
	 */
	public void setDette(Float dette) {
		this.dette = dette;
	}
	
	
}
