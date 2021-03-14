package model.Player;

import java.awt.image.BufferedImage;

import model.Personnage;

public class MarioVSEnnemi extends Personnage {

    public MarioVSEnnemi(double x, double y, BufferedImage img , boolean right) {
        super(x, y, img);
        setDimension(24,24);
        setDeplacementFalling(false);
        setDeplacementJumping(false);
        setdX(10);

        if(!right)
            setdX(-5);
    }
}
