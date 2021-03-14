package model;

import model.brique.*;

import model.ennemi.*;
import model.Player.*;
import model.ObjetCollecte.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;


public class MAP {

    private double remainingTime;
    private Mario mario;
    private ArrayList<Brique> briques = new ArrayList<>();
    private ArrayList<Ennemi> Ennemi = new ArrayList<>();
    private ArrayList<Brique> briquesSol = new ArrayList<>();
    private ArrayList<ObjetCollecte> revealedPrizes = new ArrayList<>();
    private ArrayList<Brique> revealedBricks = new ArrayList<>();
    private ArrayList<MarioVSEnnemi> fireballs = new ArrayList<>();
    private DrapeauFin pointFin;
    private BufferedImage backgroundImage;
    private double bottomBorder = 720 - 96;
    private String path;


    public MAP(double remainingTime, BufferedImage backgroundImage) {
	this.backgroundImage = backgroundImage;
	this.remainingTime = remainingTime;
    }


    public Mario getMario() {
	return mario;
    }

    public void setMario(Mario mario) {
	this.mario = mario;
    }

    public ArrayList<Ennemi> getEnemies() {
	return Ennemi;
    }

    public ArrayList<MarioVSEnnemi> getFireballs() {
	return fireballs;
    }

    public ArrayList<ObjetCollecte> getRevealedPrizes() {
	return revealedPrizes;
    }

    public ArrayList<Brique> getAllBricks() {
	ArrayList<Brique> allBricks = new ArrayList<>();

	allBricks.addAll(briques);
	allBricks.addAll(briquesSol);

	return allBricks;
    }

    public void addBrick(Brique brique) {
	this.briques.add(brique);
    }

    public void addGroundBrick(Brique brique) {
	this.briquesSol.add(brique);
    }

    public void addEnemy(Ennemi ennemie) {
	this.Ennemi.add(ennemie);
    }

    public void drawMap(Graphics2D g2){
	drawBackground(g2);
	drawPrizes(g2);
	drawBricks(g2);
	drawEnemies(g2);
	drawFireballs(g2);
	drawMario(g2);
	pointFin.paintComponent(g2);
    }

    private void drawFireballs(Graphics2D g2) {
	for(MarioVSEnnemi fireball : fireballs){
	    fireball.paintComponent(g2);
	}
    }

    private void drawPrizes(Graphics2D g2) {
	for(ObjetCollecte ObjetCollecte : revealedPrizes){
	    if(ObjetCollecte instanceof Coin){
		((Coin) ObjetCollecte).paintComponent(g2);
	    }
	    else if(ObjetCollecte instanceof  BoostItem){
		((BoostItem) ObjetCollecte).paintComponent(g2);
	    }
	}
    }

    private void drawBackground(Graphics2D g2){
	g2.drawImage(backgroundImage, 0, 0, null);
    }

    private void drawBricks(Graphics2D g2) {
	for(Brique brique : briques){
	    if(brique != null)
		brique.paintComponent(g2);
	}

	for(Brique brique : briquesSol){
	    brique.paintComponent(g2);
	}
    }

    private void drawEnemies(Graphics2D g2) {
	for(Ennemi ennemie : Ennemi){
	    if(ennemie != null)
		ennemie.paintComponent(g2);
	}
    }

    private void drawMario(Graphics2D g2) {
	mario.paintComponent(g2);
    }

    public void updateLocations() {
	mario.modifierLocation();
	for(Ennemi Ennemi : Ennemi){
	    Ennemi.modifierLocation();
	}

	for(Iterator<ObjetCollecte> prizeIterator = revealedPrizes.iterator(); prizeIterator.hasNext();){
	    ObjetCollecte ObjetCollecte = prizeIterator.next();
	    if(ObjetCollecte instanceof Coin){
		((Coin) ObjetCollecte).modifierLocation();
		if(((Coin) ObjetCollecte).getRevealBoundary() > ((Coin) ObjetCollecte).getY()){
		    prizeIterator.remove();
		}
	    }
	    else if(ObjetCollecte instanceof BoostItem){
		((BoostItem) ObjetCollecte).modifierLocation();
	    }
	}

	for (MarioVSEnnemi fireball: fireballs) {
	    fireball.modifierLocation();
	}

	for(Iterator<Brique> brickIterator = revealedBricks.iterator(); brickIterator.hasNext();){
	    BriqueOrdinaire brique = (BriqueOrdinaire)brickIterator.next();
	    brique.animate();
	    if(brique.getFrames() < 0){
		briques.remove(brique);
		brickIterator.remove();
	    }
	}

	pointFin.modifierLocation();
    }

    public double getBottomBorder() {
	return bottomBorder;
    }

    public void addRevealedPrize(ObjetCollecte ObjetCollecte) {
	revealedPrizes.add(ObjetCollecte);
    }

    public void addFireball(MarioVSEnnemi fireball) {
	fireballs.add(fireball);
    }

    public void setEndPoint(DrapeauFin pointFin) {
	this.pointFin = pointFin;
    }

    public DrapeauFin getEndPoint() {
	return pointFin;
    }

    public void addRevealedBrick(BriqueOrdinaire briqueOrdinaire) {
	revealedBricks.add(briqueOrdinaire);
    }

    public void removeFireball(MarioVSEnnemi object) {
	fireballs.remove(object);
    }

    public void removeEnemy(Ennemi object) {
    	Ennemi.remove(object);
    }

    public void removePrize(ObjetCollecte object) {
	revealedPrizes.remove(object);
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public void updateTime(double passed){
	remainingTime = remainingTime - passed;
    }

    public boolean isTimeOver(){
	return remainingTime <= 0;
    }

    public double getRemainingTime() {
	return remainingTime;
    }
}
