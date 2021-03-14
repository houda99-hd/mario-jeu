package model.brique;
import java.awt.image.BufferedImage;

import gestionJeu.*;
import model.*;
import model.ObjetCollecte.*;

public abstract class Brique extends Personnage {
	protected boolean breakable;
	protected boolean vide;

	public Brique(double x, double y, BufferedImage sprite) {
		super(x, y, sprite);
		setDimension(48,48);
	}
	
	public boolean GetBreakable() {
        return breakable;
    }
	
	public void SetBreakable(boolean breakable) {
        this.breakable = breakable;
    }
	
    public boolean EstVide() {
        return vide;
    }
    
    public void setVide(boolean vide) {
        this.vide = vide;
    }
    
    public ObjetCollecte reveal(MoteurJeu engine){ return null;}

    public ObjetCollecte getObjetCollecte() {
        return null;
    }
}
