/**
 * 
 */
package model.ObjetCollecte;

import java.awt.image.BufferedImage;

import affichage.Animation;
import affichage.ImageLoader;
import gestionJeu.MoteurJeu;
import model.Player.Mario;
import model.Player.PlayerForm;


public class Flower extends BoostItem{

	public Flower(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(150);
    }
	
	 public void onTouch(Mario mario, MoteurJeu engine) {
	        mario.acquirePoints(getPoint());

	        ImageLoader imageLoader = new ImageLoader();

	        if(!mario.getPlayerForm().isFire()){
	            BufferedImage[] leftFrames = imageLoader.getLeftImage(PlayerForm.FIRE);
	            BufferedImage[] rightFrames = imageLoader.getRightImage(PlayerForm.FIRE);

	            Animation animation = new Animation(leftFrames, rightFrames);
	            PlayerForm newForm = new PlayerForm(animation, true, true);
	            mario.setPlayerForm(newForm);
	            mario.setDimension(48, 96);

	            engine.playFireFlower();
	        }
	    }
	    @Override
	    public void modifierLocation(){}
}

