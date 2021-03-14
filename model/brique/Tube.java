/**
 * 
 */
package model.brique;

import java.awt.image.BufferedImage;


public class Tube extends Brique{
	
    public Tube(double x, double y, BufferedImage style){
        super(x, y, style);
        SetBreakable(false);
        setVide(true);
        setDimension(96,96);
    }

}
