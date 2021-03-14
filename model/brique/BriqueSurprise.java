/**
 * 
 */
package model.brique;

import java.awt.image.BufferedImage;

import gestionJeu.MoteurJeu;
import model.ObjetCollecte.*;


public class BriqueSurprise extends Brique{

	private ObjetCollecte prize;
	
	
	public BriqueSurprise(double x, double y, BufferedImage style, ObjetCollecte prize) {
        super(x, y, style);
        SetBreakable(false);
        setVide(false);
        this.prize = prize;
    }

    public ObjetCollecte reveal(MoteurJeu engine){
        BufferedImage newStyle = engine.getImageLoader().loadImage("/sprite.png");
        newStyle = engine.getImageLoader().getSubImage(newStyle, 1, 2, 48, 48);

        if(prize != null){
            prize.reveal();
        }

        setVide(true);
        setImage(newStyle);

        ObjetCollecte toReturn = this.prize;
        this.prize = null;
        return toReturn;
    }

    public ObjetCollecte getObjetCollecte(){
        return prize;
    }
}
