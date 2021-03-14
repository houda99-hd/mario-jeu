package model.ObjetCollecte;
import java.awt.image.BufferedImage;

import gestionJeu.MoteurJeu;
import model.Player.Mario;

public class Mushroom extends BoostItem{

	

	public Mushroom(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(200);
    }

	
    public void onTouch(Mario mario, MoteurJeu engine) {
        mario.acquirePoints(getPoint());
        //Ajout d'une vie supplementaire
        mario.setLives(mario.getLives() + 1);
        // transformation en marion de grande taille.
        engine.playOneUp();
    }
    
}
