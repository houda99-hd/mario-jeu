package model.ennemi;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Goomba extends Ennemi{

    private BufferedImage rightImage;

    public Goomba(double x, double y, BufferedImage style) {
        super(x, y, style);
        setdX(3);
    }

    @Override
    public void paintComponent(Graphics g){
        if(getdX() > 0){
            g.drawImage(rightImage, (int)getX(), (int)getY(), null);
        }
        else
            super.paintComponent(g);
    }

    public void setRightImage(BufferedImage rightImage) {
        this.rightImage = rightImage;
    }
	
	public String toString()
    {
		return "Goomba";
    }
}

