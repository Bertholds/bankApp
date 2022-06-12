package com.codetreatise.view;

import java.util.ResourceBundle;

public enum FxmlView {

	HOME {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("home.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Home.fxml";
		}
	},
	LOGIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("login.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Login.fxml";
		}
	},
	ADHERENT {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("adherent.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Adherents.fxml";
		}
	},
	ADHERENTADD {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("adherentadd.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/AdherentEditDialog.fxml";
		}
	},
	USER {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("user.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/User.fxml";
		}
	},
	USERACCOUNT {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("userAccount.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/UserAccount.fxml";
		}
	},
	AVALISEEDITDIALOG {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("avaliseEditDialog.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/AvaliseEditDialog.fxml";
		}
	},
	TRANSACTION {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("transaction.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Transaction.fxml";
		}
	},
	COMPTEbANCAIRE {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("compteEpargne.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/CompteEpargne.fxml";
		}
	},
	AVALISEEMPRINTEDITDIALOG {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("avaliseEmprintEditDialog.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/AvaliseEmprintEditDialog.fxml";
		}
	},
	SETTING {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("setting.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Setting.fxml";
		}
	},
	Mouchard {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("mouchard.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Mouchard.fxml";
		}
	},
	COMPTEEPARGNEMOREDETAIL {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("compteepargnedetail.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/CompteEpargneMoreDetail.fxml";
		}
	},
	MANAGEADHERENT {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("manageadherent.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/ManageAdherent.fxml";
		}
	},
	MANAGECOMPTEEPARGNE {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("managecompteepargne.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/ManageCompteEpargne.fxml";
		}
	},
	REMBOURSSEMENT {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("rembourssement.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Rembourssement.fxml";
		}
	},
	AVALISEREMBOURSSEMENTMOREDETAIL {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("avaliserembourssementmoredetail.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/AvaliseRembourssementMoreDetail.fxml";
		}
	},
	TRANSACTIONREMBOURSSEMENTDETAIL {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("transactionrembourssementdetail.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/TransactionRembourssementDetail.fxml";
		}
	},
	TRANSACTIONPRETDETAIL {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("transactionpretdetail.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/TransactionPretDetail.fxml";
		}
	},
	REPORT {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("report.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Report.fxml";
		}
	},
	PROGRESSBAR {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("progressbar.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/ProgressBar.fxml";
		}
	},
	REPORTCOMPTEEPARGNE {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("reportcompteepargne.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/ReportCompteEpargne.fxml";
		}
	},
	BACKUP {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("backup.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Backup.fxml";
		}
	},
	REPORTTRANSACTION {

		@Override
		public String getTitle() {
			return getStringFromResourceBundle("reporttransaction.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/ReportTransaction.fxml";
		}
	};

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}

}
