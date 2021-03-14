package gestionJeu;

import model.DrapeauFin;
import model.brique.*;
import model.ObjetCollecte.*;
import affichage.ImageLoader;
import model.MAP;
import model.ennemi.Ennemi;
import model.ennemi.Goomba;
import model.ennemi.KoopaTroopa;
import model.Player.*;

import java.awt.*;
import java.awt.image.BufferedImage;

class CreationMap {

    private ImageLoader chargeurImage;

    private BufferedImage backgroundImage;
    private BufferedImage superMushroom, oneUpMushroom, fireFlower, coin;
    private BufferedImage briqueOrdinaire, briqueSurprise, briqueSol, tube;
    private BufferedImage goombaLeft, goombaRight, koopaLeft, koopaRight, drapeauFin;


    CreationMap(ImageLoader chargeurImage) {

	this.chargeurImage = chargeurImage;
	BufferedImage lutin = chargeurImage.loadImage("/sprite.png");

	this.backgroundImage = chargeurImage.loadImage("/background.png");
	this.superMushroom = chargeurImage.getSubImage(lutin, 2, 5, 48, 48);
	this.oneUpMushroom= chargeurImage.getSubImage(lutin, 3, 5, 48, 48);
	this.fireFlower= chargeurImage.getSubImage(lutin, 4, 5, 48, 48);
	this.coin = chargeurImage.getSubImage(lutin, 1, 5, 48, 48);
	this.briqueOrdinaire = chargeurImage.getSubImage(lutin, 1, 1, 48, 48);
	this.briqueSurprise = chargeurImage.getSubImage(lutin, 2, 1, 48, 48);
	this.briqueSol = chargeurImage.getSubImage(lutin, 2, 2, 48, 48);
	this.tube = chargeurImage.getSubImage(lutin, 3, 1, 96, 96);
	this.goombaLeft = chargeurImage.getSubImage(lutin, 2, 4, 48, 48);
	this.goombaRight = chargeurImage.getSubImage(lutin, 5, 4, 48, 48);
	this.koopaLeft = chargeurImage.getSubImage(lutin, 1, 3, 48, 64);
	this.koopaRight = chargeurImage.getSubImage(lutin, 4, 3, 48, 64);
	this.drapeauFin = chargeurImage.getSubImage(lutin, 5, 1, 48, 48);

    }

    MAP createMap(String cheminMap, double tempsLimite) {
	BufferedImage imageMap = chargeurImage.loadImage(cheminMap);

	if (imageMap == null) {
	    System.out.println("Given path is invalid...");
	    return null;
	}

	MAP creerMap = new MAP(tempsLimite, backgroundImage);
	String[] paths = cheminMap.split("/");
	creerMap.setPath(paths[paths.length-1]);

	int pixelMultiplier = 48;

	int mario = new Color(160, 160, 160).getRGB();
	int briqueOrdinaire = new Color(0, 0, 255).getRGB();
	int briqueSurprise = new Color(255, 255, 0).getRGB();
	int briqueSol = new Color(255, 0, 0).getRGB();
	int tube = new Color(0, 255, 0).getRGB();
	int goomba = new Color(0, 255, 255).getRGB();
	int koopa = new Color(255, 0, 255).getRGB();
	int end = new Color(160, 0, 160).getRGB();

	for (int x = 0; x < imageMap.getWidth(); x++) {
	    for (int y = 0; y < imageMap.getHeight(); y++) {

		int currentPixel = imageMap.getRGB(x, y);
		int xLocation = x*pixelMultiplier;
		int yLocation = y*pixelMultiplier;

		if (currentPixel == briqueOrdinaire) {
		    Brique brique = new BriqueOrdinaire(xLocation, yLocation, this.briqueOrdinaire);
		    creerMap.addBrick(brique);
		}
		else if (currentPixel == briqueSurprise) {
		    ObjetCollecte prize = generateRandomPrize(xLocation, yLocation);
		    Brique brique = new BriqueSurprise(xLocation, yLocation, this.briqueSurprise, prize);
		    creerMap.addBrick(brique);
		}
		else if (currentPixel == tube) {
		    Brique brique = new Tube(xLocation, yLocation, this.tube);
		    creerMap.addGroundBrick(brique);
		}
		else if (currentPixel == briqueSol) {
		    Brique brique = new BriqueSol(xLocation, yLocation, this.briqueSol);
		    creerMap.addGroundBrick(brique);
		}
		else if (currentPixel == goomba) {
		    Ennemi ennemie = new Goomba(xLocation, yLocation, this.goombaLeft);
		    ((Goomba)ennemie).setRightImage(goombaRight);
		    creerMap.addEnemy(ennemie);
		}
		else if (currentPixel == koopa) {
		    Ennemi ennemie = new KoopaTroopa(xLocation, yLocation, this.koopaLeft);
		    ((KoopaTroopa)ennemie).setRightImage(koopaRight);
		    creerMap.addEnemy(ennemie);
		}
		else if (currentPixel == mario) {
		    Mario marioObject = new Mario(xLocation, yLocation);
		    creerMap.setMario(marioObject);
		}
		else if(currentPixel == end){
		    DrapeauFin pointFin= new DrapeauFin(xLocation+24, yLocation, drapeauFin);
		    creerMap.setEndPoint(pointFin);
		}
	    }
	}

	System.out.println("Map is created..");
	return creerMap;
    }

    private ObjetCollecte generateRandomPrize(double x, double y){
	ObjetCollecte generer;
	int random = (int)(Math.random() * 12);

	
	 if(random == 0){ //super mushroom
	    generer = new SuperMushroom(x, y, this.superMushroom);
	}
	else if(random == 1){ //fire flower
	    generer = new Flower(x, y, this.fireFlower);
	}
	else if(random == 2){ //one up mushroom
	
	    generer = new Mushroom(x, y, this.oneUpMushroom);
	}
	else{ //coin
	    generer = new Coin(x, y, this.coin, 50);
	}

	return generer;
    }


}
