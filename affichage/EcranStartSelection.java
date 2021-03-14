package affichage;


public enum EcranStartSelection {
	DEMARRER_JEU(0), VOIR_AIDE(1), A_PROPOS(2);
	// On représente les lignes du menu par des entiers.
	private final int numeroLigne;
	EcranStartSelection(int newNumeroLigne) {
		this.numeroLigne = newNumeroLigne;
	}
	
	// Associer l'entier entré par l'utilisateur à une action du menu
	public EcranStartSelection getSelection(int entier) {
		if (entier == 0) {
			return DEMARRER_JEU;
		} else if (entier == 1) {
			return VOIR_AIDE;
		} else if (entier == 2) {
			return A_PROPOS;
		} else {
			return null;
		}
	}
	
	// Sélectionner une des lignes du menu 
	public EcranStartSelection select(boolean toUp) {
		int selection;
		
		if (numeroLigne > -1 && numeroLigne < 3) {
			selection = numeroLigne - (toUp ? 1 : -1);
			if (selection == -1) {
				selection = 2;
			} else if (selection == 3) {
				selection = 0;
			}
			return getSelection(selection);
		}
		return null;
	}
	
	// Obtenir la ligne du menu courante
	public int getNumeroLigne() {
		return numeroLigne;
	}
	
}
