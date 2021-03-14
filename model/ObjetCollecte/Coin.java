package model.ObjetCollecte;


import model.*;
import model.Player.Mario;

import java.awt.*;
import java.awt.image.BufferedImage;

import gestionJeu.MoteurJeu;


public class Coin extends Personnage implements ObjetCollecte{

    private int point;
    private boolean revealed, acquired = false;
    private int revealBoundary;

    public Coin(double x, double y, BufferedImage style, int point){
        super(x, y, style);
        this.point = point;
        revealed = false;
        setDimension(30, 42);
        revealBoundary = (int)getY() - getDimension().height;
    }

    @Override
    public int getPoint() {
        return point;
    }

    @Override
    public void reveal() {
        revealed = true;
    }

    @Override
    public void onTouch(Mario mario, MoteurJeu engine) {
        if(!acquired){
            acquired = true;
            mario.acquirePoints(point);
            mario.acquireCoin();
            engine.playCoin();
        }
    }

    @Override
    public void modifierLocation(){
        if(revealed){
            setY(getY() - 5);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        if(revealed){
            g.drawImage(getSprite(), (int)getX(), (int)getY(), null);
        }
    }

    public int getRevealBoundary() {
        return revealBoundary;
    }
}
