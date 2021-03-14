package affichage;

import java.util.ArrayList;

import affichage.ItemsMapSelectionnee;

import java.awt.*;

public class SelectionMap {
	
	// On enregistre les différents niveaux du jeu dans une liste de
	// chaînes de caractères.
	private ArrayList<String> nosMaps = new ArrayList<>();
	private ItemsMapSelectionnee[] itemsMapSelectionnee;
	
	// Constructeur du menu
	public SelectionMap() {
		getMaps();
		this.itemsMapSelectionnee = creerItems(this.nosMaps);
	}
	
	// Obtenir les différents niveaux jouables
	private void getMaps() {
		nosMaps.add("Map 1.png");	// on ajoute le niveau 1
		nosMaps.add("Map 2.png");	// on ajoute le niveau 2
	}
	
	// Affichage des maps à sélectionner
	private ItemsMapSelectionnee[] creerItems(ArrayList<String> nosMaps) {
		if (nosMaps == null) {
			return null;
		} else {
			int tailleMenu = 100;	// taille de la grille du menu de sélection des niveaux
			ItemsMapSelectionnee[] nosItems = new ItemsMapSelectionnee[nosMaps.size()];
			for (int i = 0 ; i < nosItems.length ; i++) {
				Point position = new Point(0, (i+1)*tailleMenu+200);				// position du niveau dans la grille du menu
				nosItems[i] = new ItemsMapSelectionnee(nosMaps.get(i), position);	// on ajoute le niveau dans la liste des niveaux	
			}
			return nosItems;
		}
	}
	
	// Choisir une des maps proposées avec le clavier
	// Le niveau est alors représenté par un entier qui correspond à sa position dans la liste des niveaux.
	public String choisirMap(int index) {
		if (index < itemsMapSelectionnee.length && index > -1)
			return itemsMapSelectionnee[index].getNom();
		return null;
	}
	
	// Choisir une des maps proposées avec la souris.
    public String choisirMap(Point mouseLocation) {
    	// On va vérifier la position de la souris pour chaque niveau
        for(ItemsMapSelectionnee nosItems : itemsMapSelectionnee) {
        	// On prend les coordonnées de la souris
            Dimension dimension = nosItems.getDimension();
            Point position = nosItems.getPosition();
            // Vérifier que la souris est positionné dans le "rectangle" associé au niveau
            boolean inX = position.x <= mouseLocation.x && position.x + dimension.width >= mouseLocation.x;
            boolean inY = position.y >= mouseLocation.y && position.y - dimension.height <= mouseLocation.y;
            if(inX && inY){
                return nosItems.getNom();
            }
        }
        return null;    
    }
	
	// Changer de niveau sélectionné
	public int changerMapSelectionnee(int index, boolean up) {
		if (up) {
			if (index <= 0) {
				return itemsMapSelectionnee.length - 1;
			} else {
				return index - 1;
			} 	
		} else {
			if (index >= itemsMapSelectionnee.length - 1) {
				return 0;
			} else {
				return index + 1;
			}
		}
	}
	
	// Affichage du choix du menu de sélection du niveau
	public void dessinerAffichage(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, 1280, 720); 			// affichage en 720p
		if (itemsMapSelectionnee == null) {
			System.out.println(1);
			return;
		}
		
		String titre = "Choisissez un niveau";	// affichage de la consigne
		int x_position = (1280 -g.getFontMetrics().stringWidth(titre))/2;	// on centre l'affichage de la consigne
		g.setColor(Color.yellow);
		g.drawString(titre, x_position, 150);
		
		// on affiche chaque niveau 1 par 1
		for (ItemsMapSelectionnee nosItems : itemsMapSelectionnee) {
			g.setColor(Color.white);
			// on prend les dimensions de la chaîne de caractères du niveau
			int largeur = g.getFontMetrics().stringWidth(nosItems.getNom().split("[.]")[0]);
			int hauteur = g.getFontMetrics().getHeight();
			nosItems.setDimension(new Dimension(largeur, hauteur));
			nosItems.setPosition(new Point((1280-largeur)/2, nosItems.getPosition().y));
			// affichage centré de la chaîne de caractères associée au niveau
			g.drawString(nosItems.getNom().split("[.]")[0], nosItems.getPosition().x, nosItems.getPosition().y);
		}
	}
}
