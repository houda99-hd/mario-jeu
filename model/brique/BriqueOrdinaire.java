/**
 * 
 */
package model.brique;

import java.awt.image.BufferedImage;

import affichage.*;
import gestionJeu.*;
import model.ObjetCollecte.*;

public class BriqueOrdinaire extends Brique{
	private Animation animation;
	private boolean breaking;
	private int frames;
	
	public BriqueOrdinaire(double x, double y, BufferedImage style){
        super(x, y, style);
        SetBreakable(true);
        setVide(true);

        setAnimation();
        breaking = false;
        frames = animation.getLeft().length;
    }
	
	 private void setAnimation(){
	        ImageLoader imageLoader = new ImageLoader();
	        BufferedImage[] leftFrames = imageLoader.getBrickImage();

	        animation = new Animation(leftFrames, leftFrames);
	    }
	 
	 public ObjetCollecte reveal(MoteurJeu engine){
	        GestionMap manager = engine.getMapManager();
	        if(!manager.getMario().isSuper())
	            return null;

	        breaking = true;
	        manager.addRevealedBrick(this);

	        double newX = getX() - 27, newY = getY() - 27;
	        setX(newX);
	        setY(newY);

	        return null;
	    }
	 
	 public int getFrames(){
	        return frames;
	    }

	    public void animate(){
	        if(breaking){
	            setImage(animation.animate(3, true));
	            frames--;
	        }
	    }


}
