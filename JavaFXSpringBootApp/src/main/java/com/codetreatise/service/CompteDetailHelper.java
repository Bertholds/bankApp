package com.codetreatise.service;

import com.codetreatise.bean.CompteEpargne;
import com.codetreatise.bean.CompteEpargneDetail;

public class CompteDetailHelper {

	public static CompteEpargneDetail factoryCompteEpargneDetail(CompteEpargne compteEpargne, String date) {

		CompteEpargneDetail cd = new CompteEpargneDetail();
		cd.setCompteEpargne(compteEpargne);
		cd.setCredibilitePerMonth(compteEpargne.getLacarte());
		cd.setDate(date);
		cd.setMontant(compteEpargne.getSolde());
		cd.setMontantTransaction((long) 0);
		cd.setPretPerMonth(compteEpargne.getCompteTampon().getDette());

		return cd;
	}
}
