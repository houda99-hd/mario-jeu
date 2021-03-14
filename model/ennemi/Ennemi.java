package model.ennemi;

import model.Personnage;

import java.awt.image.BufferedImage;



public abstract class Ennemi extends Personnage{

    public Ennemi(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDeplacementFalling(false);
        setDeplacementJumping(false);
    }
	

}
