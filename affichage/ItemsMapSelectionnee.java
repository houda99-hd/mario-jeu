package affichage;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ItemsMapSelectionnee {
	
	private BufferedImage image;
	private String nom;
	private Point position;
	private Dimension dimension;
	
	// Constructeur des "items" (représentation de chaque niveau dans le menu de 
	// sélection des niveaux / liste des niveaux)
	public ItemsMapSelectionnee(String nomNiveau, Point pointPosition) {
		this.position = pointPosition;
		this.nom = nomNiveau;
		ImageLoader chargeurImage = new ImageLoader();
		this.image = chargeurImage.loadImage("/maps/" + nomNiveau);
		this.dimension = new  Dimension();
	}
	
	// Obtenir le nom du niveau
	public String getNom() {
		return nom;
	}
	
	// Obtenir la position (x, y) du niveau
	public Point getPosition() {
		return position;
	}
	
	// Obtenir les dimensions du niveau
	public Dimension getDimension() {
		return dimension;
	}
	
	// Modifier les dimensions du niveau
	public void setDimension(Dimension newDimension) {
		this.dimension = newDimension;
	}
	
	// Modifier la position du niveau
	public void setPosition(Point newPosition) {
		this.position = newPosition;
	}

}
