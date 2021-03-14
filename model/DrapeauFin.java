package model;

import java.awt.image.BufferedImage;



public class DrapeauFin extends Personnage {

    private boolean touched = false;

    public DrapeauFin (double x, double y, BufferedImage style) {
	super(x, y, style);
    }

    @Override
    public void modifierLocation() {
	if(touched){
	    if(getY() + getSprite().getHeight() >= 576){
	    setDeplacementFalling(false);
		setdY(0);
		setY(576 - getSprite().getHeight());
	    }
	    super.modifierLocation();
	}
    }

    public boolean isTouched() {
	return touched;
    }

    public void setTouched(boolean touched) {
	this.touched = touched;
    }
}
