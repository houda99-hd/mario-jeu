package gestionJeu;

import model.Personnage;
import model.MAP;
import model.brique.Brique;
import model.brique.Brique.*;
import model.brique.BriqueOrdinaire;
import model.ennemi.Ennemi;
import model.Player.*;
import model.ObjetCollecte.*;
import affichage.ImageLoader;

import java.awt.*;
import java.util.ArrayList;

public class GestionMap {

    private MAP carte;


    public GestionMap() {}


    public void updateLocations() {
	if (carte == null)
	    return;
	carte.updateLocations();
    }


    public void resetCurrentMap(MoteurJeu engine) {
	Mario mario = getMario();
	mario.resetLocation();
	engine.resetCamera();
	createMap(engine.getImageLoader(), carte.getPath());
	carte.setMario(mario);
    }


    public boolean createMap(ImageLoader loader, String path) {
	CreationMap mapCreator = new CreationMap(loader);
	carte = mapCreator.createMap("/maps/" + path, 400);
	return carte != null;
    }


    public void acquirePoints(int point) {
	carte.getMario().acquirePoints(point);
    }


    public Mario getMario() {
	return carte.getMario();
    }


    public void fire(MoteurJeu engine) {
	MarioVSEnnemi fireball = getMario().MarioVSEnnemi();
	if (fireball != null) {
	    carte.addFireball(fireball);
	    engine.playFireball();
	}
    }


    public boolean isGameOver() {
	return getMario().getLives() == 0 || carte.isTimeOver();
    }


    public int getScore() {
	return getMario().getPoints();
    }


    public int getRemainingLives() {
	return getMario().getLives();
    }


    public int getCoins() {
	return getMario().getCoins();
    }


    public void drawMap(Graphics2D g2) {
	carte.drawMap(g2);
    }


    public int passMission() {
	if(getMario().getX() >= carte.getEndPoint().getX() && !carte.getEndPoint().isTouched()){
	    carte.getEndPoint().setTouched(true);
	    int height = (int)getMario().getY();
	    return height * 2;
	} else {
	    return -1;
	}
    }


    public boolean endLevel(){
	return getMario().getX() >= carte.getEndPoint().getX() + 320;
    }


    public void checkCollisions(MoteurJeu engine) {
	if (carte == null) {
	    return;
	}

	checkBottomCollisions(engine);
	checkTopCollisions(engine);
	checkMarioHorizontalCollision(engine);
	checkEnemyCollisions();
	checkPrizeCollision();
	checkPrizeContact(engine);
	checkFireballContact();
    }


    private void checkBottomCollisions(MoteurJeu engine) {
	Mario mario = getMario();
	ArrayList<Brique> briques = carte.getAllBricks();
	ArrayList<Ennemi> enemies = carte.getEnemies();
	ArrayList<Personnage> toBeRemoved = new ArrayList<>();

	Rectangle marioBottomBounds = mario.getBottomBounds();

	if (!mario.getDeplacementJumping())
	    mario.setDeplacementFalling(true);

	for (Brique brique : briques) {
	    Rectangle brickTopBounds = brique.getTopBounds();
	    if (marioBottomBounds.intersects(brickTopBounds)) {
		mario.setY(brique.getY() - mario.getSprite().getHeight() + 1);
		mario.setDeplacementFalling(false);
		mario.setdY(0);
	    }
	}

	for (Ennemi ennemi : enemies) {
	    Rectangle enemyTopBounds = ennemi.getTopBounds();
	    if (marioBottomBounds.intersects(enemyTopBounds)) {
		mario.acquirePoints(100);
		toBeRemoved.add(ennemi);
		engine.playStomp();
	    }
	}

	if (mario.getY() + mario.getSprite().getHeight() >= carte.getBottomBorder()) {
	    mario.setY(carte.getBottomBorder() - mario.getSprite().getHeight());
	    mario.setDeplacementFalling(false);
	    mario.setdY(0);
	}

	removeObjects(toBeRemoved);
    }


    private void checkTopCollisions(MoteurJeu engine) {
	Mario mario = getMario();
	ArrayList<Brique> briques = carte.getAllBricks();

	Rectangle marioTopBounds = mario.getTopBounds();
	for (Brique brique : briques) {
	    Rectangle brickBottomBounds = brique.getBottomBounds();
	    if (marioTopBounds.intersects(brickBottomBounds)) {
		mario.setdY(0);
		mario.setY(brique.getY() + brique.getSprite().getHeight());
		ObjetCollecte prize = brique.reveal(engine);
		if(prize != null)
		    carte.addRevealedPrize(prize);
	    }
	}
    }


    private void checkMarioHorizontalCollision(MoteurJeu engine){
	Mario mario = getMario();
	ArrayList<Brique> briques = carte.getAllBricks();
	ArrayList<Ennemi> enemies = carte.getEnemies();
	ArrayList<Personnage> toBeRemoved = new ArrayList<>();

	boolean marioDies = false;
	boolean toRight = mario.getRight();

	Rectangle marioBounds = toRight ? mario.getRightBounds() : mario.getLeftBounds();

	for (Brique brique : briques) {
	    Rectangle brickBounds = !toRight ? brique.getRightBounds() : brique.getLeftBounds();
	    if (marioBounds.intersects(brickBounds)) {
		mario.setdX(0);
		if(toRight)
		    mario.setX(brique.getX() - mario.getSprite().getWidth());
		else
		    mario.setX(brique.getX() + brique.getSprite().getWidth());
	    }
	}

	for(Ennemi ennemi : enemies){
	    Rectangle enemyBounds = !toRight ? ennemi.getRightBounds() : ennemi.getLeftBounds();
	    if (marioBounds.intersects(enemyBounds)) {
		marioDies = mario.onTouchEnemy(engine);
		toBeRemoved.add(ennemi);
	    }
	}
	removeObjects(toBeRemoved);


	if (mario.getX() <= engine.getPositionCamera().getX() && mario.getdX() < 0) {
	    mario.setdX(0);
	    mario.setX(engine.getPositionCamera().getX());
	}

	if(marioDies) {
	    resetCurrentMap(engine);
	}
    }


    private void checkEnemyCollisions() {
	ArrayList<Brique> briques = carte.getAllBricks();
	ArrayList<Ennemi> enemies = carte.getEnemies();

	for (Ennemi ennemi : enemies) {
	    boolean standsOnBrick = false;

	    for (Brique brique : briques) {
		Rectangle enemyBounds = ennemi.getLeftBounds();
		Rectangle brickBounds = brique.getRightBounds();

		Rectangle enemyBottomBounds = ennemi.getBottomBounds();
		Rectangle brickTopBounds = brique.getTopBounds();

		if (ennemi.getdX() > 0) {
		    enemyBounds = ennemi.getRightBounds();
		    brickBounds = brique.getLeftBounds();
		}

		if (enemyBounds.intersects(brickBounds)) {
		    ennemi.setdX(-ennemi.getdX());
		}

		if (enemyBottomBounds.intersects(brickTopBounds)){
		    ennemi.setDeplacementFalling(false);
		    ennemi.setdY(0);
		    ennemi.setY(brique.getY()-ennemi.getSprite().getHeight());
		    standsOnBrick = true;
		}
	    }

	    if(ennemi.getY() + ennemi.getSprite().getHeight() > carte.getBottomBorder()){
		ennemi.setDeplacementFalling(false);
		ennemi.setdY(0);
		ennemi.setY(carte.getBottomBorder()-ennemi.getSprite().getHeight());
	    }

	    if (!standsOnBrick && ennemi.getY() < carte.getBottomBorder()){
		ennemi.setDeplacementFalling(true);
	    }
	}
    }


    private void checkPrizeCollision() {
	ArrayList<ObjetCollecte> prizes = carte.getRevealedPrizes();
	ArrayList<Brique> briques = carte.getAllBricks();

	for (ObjetCollecte prize : prizes) {
	    if (prize instanceof BoostItem) {
		BoostItem boost = (BoostItem) prize;
		Rectangle prizeBottomBounds = boost.getBottomBounds();
		Rectangle prizeRightBounds = boost.getRightBounds();
		Rectangle prizeLeftBounds = boost.getLeftBounds();
		boost.setDeplacementFalling(true);

		for (Brique brique : briques) {
		    Rectangle brickBounds;

		    if (boost.getDeplacementFalling()) {
			brickBounds = brique.getTopBounds();

			if (brickBounds.intersects(prizeBottomBounds)) {
			    boost.setDeplacementFalling(false);
			    boost.setdY(0);
			    boost.setY(brique.getY() - boost.getSprite().getHeight() + 1);
			    if (boost.getdX() == 0)
				boost.setdX(2);
			}
		    }

		    if (boost.getdX() > 0) {
			brickBounds = brique.getLeftBounds();

			if (brickBounds.intersects(prizeRightBounds)) {
			    boost.setdX(-boost.getdX());
			}
		    } else if (boost.getdX() < 0) {
			brickBounds = brique.getRightBounds();

			if (brickBounds.intersects(prizeLeftBounds)) {
			    boost.setdX(-boost.getdX());
			}
		    }
		}

		if (boost.getY() + boost.getSprite().getHeight() > carte.getBottomBorder()) {
		    boost.setDeplacementFalling(false);
		    boost.setdY(0);
		    boost.setY(carte.getBottomBorder() - boost.getSprite().getHeight());
		    if (boost.getdX() == 0)
			boost.setdX(2);
		}

	    }
	}
    }


    private void checkPrizeContact(MoteurJeu engine) {
	ArrayList<ObjetCollecte> prizes = carte.getRevealedPrizes();
	ArrayList<Personnage> toBeRemoved = new ArrayList<>();

	Rectangle marioBounds = getMario().getBounds();
	for(ObjetCollecte prize : prizes){
	    Rectangle prizeBounds = prize.getBounds();
	    if (prizeBounds.intersects(marioBounds)) {
		prize.onTouch(getMario(), engine);
		toBeRemoved.add((Personnage) prize);
	    } else if(prize instanceof Coin){
		prize.onTouch(getMario(), engine);
	    }
	}

	removeObjects(toBeRemoved);
    }


    private void checkFireballContact() {
	ArrayList<MarioVSEnnemi> fireballs = carte.getFireballs();
	ArrayList<Ennemi> enemies = carte.getEnemies();
	ArrayList<Brique> briques = carte.getAllBricks();
	ArrayList<Personnage> toBeRemoved = new ArrayList<>();

	for(MarioVSEnnemi fireball : fireballs){
	    Rectangle fireballBounds = fireball.getBounds();

	    for(Ennemi ennemi : enemies){
		Rectangle enemyBounds = ennemi.getBounds();
		if (fireballBounds.intersects(enemyBounds)) {
		    acquirePoints(100);
		    toBeRemoved.add(ennemi);
		    toBeRemoved.add(fireball);
		}
	    }

	    for(Brique brique : briques){
		Rectangle brickBounds = brique.getBounds();
		if (fireballBounds.intersects(brickBounds)) {
		    toBeRemoved.add(fireball);
		}
	    }
	}

	removeObjects(toBeRemoved);
    }


    private void removeObjects(ArrayList<Personnage> list){
	if(list == null)
	    return;

	for(Personnage object : list){
	    if(object instanceof MarioVSEnnemi){
		carte.removeFireball((MarioVSEnnemi)object);
	    }
	    else if(object instanceof Ennemi){
		carte.removeEnemy((Ennemi)object);
	    }
	    else if(object instanceof Coin || object instanceof BoostItem){
		carte.removePrize((ObjetCollecte)object);
	    }
	}
    }


    public void addRevealedBrick(BriqueOrdinaire ordinaryBrick) {
	carte.addRevealedBrick(ordinaryBrick);
    }


    public void updateTime(){
	if(carte != null)
	    carte.updateTime(1);
    }


    public int getRemainingTime() {
	return (int)carte.getRemainingTime();
    }
}
