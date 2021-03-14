package model;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.*;

public abstract class Personnage {
    // Les coordonnées de l'objet dans le Map
    private double x,y;
    // Le sprite de l'objet
    private BufferedImage style;
    // Les déplacements élementaires de l'objet
    private double dx,dy;
    // La dimension du rectangle image de l'objet
    private Dimension dimension;
    // Les déplacements particuliers du joueur
    private boolean dep_fall , dep_jump;
   // La gravité
    private double gravity;

    /** Définir l'objet du jeu.
     * @param x la position suivant l'axe X de l'objet.
     * @param y la position suivant l'axe Y de l'objet.
     * @param sprite l'image de l'objet.
     */

    public Personnage( double x, double y, BufferedImage sprite) {
            this.x = x;
            this.y = y ;
            this.dep_fall = false;
	    this.dep_jump = true;
	    this.style = sprite;
	    if(style != null){
	    	setDimension(style.getWidth(),style.getHeight());
	    }
	    this.gravity = 0.36;
	    this.dx = 0;
	    this.dy = 0;    
    }

	/** Tracer l'objet dans le Map.
	 * @param g le graphique du jeu.
	 */
	public void paintComponent(Graphics g) {
	     Graphics g2 = (Graphics2D)g;
             g2.drawImage(this.style,(int)x, (int)y,null);
	}

	/** Récupérer l'abscisse de l'objet.
	 * @return l'abscisse.
	 */

    public double getX() {
        return x;
    }

	/** Récupérer l'ordonnée de l'objet.
	 * @return l'ordonnée.
	 */

    public double getY() {
        return y;
    }

	/** Récupérer un booléen qui indique si l'objet tombe.
	 * @return le déplacement fall du joueur.
	 */

    public boolean getDeplacementFalling() {
        return dep_fall;
    }

	/** Récupérer un booléen qui indique si l'objet saute.
	 * @return le déplacement Jump du joueur.
	 */

    public boolean getDeplacementJumping() {
        return dep_jump;
    }

	/** Récupérer le sprite de l'objet.
	 * @return l'image de l'objet courant.
	 */

    public BufferedImage getSprite() {
	return this.style;
    }

	/** Récupérer le déplacement suivant X de l'objet.
	 * @return le double représentant le déplacement.
	 */

    public double getdX() {
        return dx;
    }

	/** Récupérer le déplacement suivant Y de l'objet.
	 * @return le double représentant le déplacement.
	 */

    public double getdY() {
        return dy;
    }

	/** Récupérer le déplacement suivant Y de l'objet.
	 * @return le double représentant la gravité.
	 */

    public double getGravity() {
        return gravity;
    }

	/** Changer l'abscisse de l'objet.
	 * @param le double représentant le nouveau abscisse.
	 */

    public void setX(double x) {
        this.x = x;
    }

	/** Changer l'ordonnée de l'objet.
	 * @param le double représentant la nouvelle ordonnée.
	 */

    public void setY(double y) {
        this.y = y;
    }

	/** Changer l'image de l'objet.
	 * @param le sprite de l'objet.
	 */

    public void setImage(BufferedImage imag) {
	this.style = imag;
    }

	/** Déplacer l'objet suivant X.
	 * @param le double représentant le déplacement.
	 */

    public void setdX(double dx) {
        this.dx = dx;
    }

	/** Déplacer l'objet suivant Y.
	 * @param le double représentant le déplacement.
	 */

    public void setdY(double dy) {
        this.dy = dy;
    }

	/** Changer la gravité.
	 * @param la nouvelle gravité.
	 */

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

	/** Changer le déplacement Fall du joueur.
	 * @param le booléen représentant le mouvement Fall.
	 */

    public void setDeplacementFalling(boolean fall) {
        this.dep_fall = fall;
    }

	/** Changer le déplacement Jump du joueur.
	 * @param le booléen représentant le mouvement Jump.
	 */

    public void setDeplacementJumping(boolean jump) {
        this.dep_jump = jump;
    }

	/** Récupérer la dimension de l'objet.
	 * @return la dimension du rectangle image du sprite.
	 */

    public Dimension getDimension(){
        return dimension;
    }

	/** Changer la dimension du sprite joueur.
	 * @param la nouvelle dimension.
	 */

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

	/** Changer la dimension du sprite joueur.
	 * @param width de la nouvelle image.
	 * @param heigth de la nouvelle image.
	 */

    public void setDimension(int width, int height){ 
    	this.dimension =  new Dimension(width, height);
    }

	/** Modifier la localisation de l'objet.
	 */

    public void modifierLocation() {
	if (dep_jump && dy <= 0) {
	    dep_jump = false;
	    dep_fall = true;
	} else if (dep_jump) {
	    dy = dy - gravity;
	    y = y - dy;
	} 
	if (dep_fall) {
	    y = y + dy;
	    dy = dy + gravity;
	}
	x = x + dx;
    }

	/** Récupérer le rectangle image.
	 * @return Rectangle image de l'objet.
	 */

	public Rectangle getBounds(){
		return new Rectangle((int)x,(int)y,style.getWidth()/2,style.getHeight()/2);
	}

	/** Récupérer le rectangle image.
	 * @return Rectangle image de l'objet le plus grand.
	 */

    public Rectangle getTopBounds(){
        return new Rectangle((int)x+style.getWidth()/6, (int)y, 2*style.getWidth()/3, style.getHeight()/2);
    }

	/** Récupérer le rectangle image.
	 * @return Rectangle image de l'objet.
	 */

    public Rectangle getBottomBounds(){
        return new Rectangle((int)x+style.getWidth()/6, (int)y + style.getHeight()/2, 2*style.getWidth()/3, style.getHeight()/2);
    }

	/** Récupérer le rectangle image.
	 * @return Rectangle image du sprite à gauche.
	 */

    public Rectangle getLeftBounds(){
        return new Rectangle((int)x, (int)y + style.getHeight()/4, style.getWidth()/4, style.getHeight()/2);
    }

	/** Récupérer le rectangle image.
	 * @return Rectangle image du sprite à droite.
	 */

    public Rectangle getRightBounds(){
        return new Rectangle((int)x + 3*style.getWidth()/4, (int)y + style.getHeight()/4, style.getWidth()/4, style.getHeight()/2);
    }
		
}
