package model.ObjetCollecte;

import java.awt.Graphics;
import model.*;
import model.Player.Mario;

import java.awt.image.BufferedImage;

import gestionJeu.MoteurJeu;


public abstract class BoostItem extends Personnage implements ObjetCollecte{

    private boolean revealed = false;
    private int point;

    public BoostItem(double x, double y, BufferedImage style) {
        super(x, y, style);
        setDimension(48, 48);
    }

    public abstract void onTouch(Mario mario, MoteurJeu engine);

    public int getPoint() {
        return point;
    }


    public void modifierLocation(){
        if(revealed){
            super.modifierLocation();
        }
    }

    public void paintComponent(Graphics g){
        if(revealed){
            g.drawImage(getSprite(), (int)getX(), (int)getY(), null);
        }
    }

    public void reveal(){
        setY(getY()-48);
        revealed = true;
    }

    public void setPoint(int point) {
        this.point = point;
    }

}
