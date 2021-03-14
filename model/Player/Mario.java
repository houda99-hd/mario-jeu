package model.Player;



import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import affichage.Animation;
import affichage.ImageLoader;
import gestionJeu.Camera;
import gestionJeu.MoteurJeu;
import model.Personnage;

public class Mario extends Personnage {
    	private int Lives;
    	private int coins;
    	private int points;
    	private double paramTimer;
    	private PlayerForm mario;
    	private boolean right = true;
	public Mario (double x,double y) {
		super(x,y , null);
        	Lives = 3;
        	points = 0;
        	coins = 0;
        	paramTimer = 0;

        	ImageLoader imageLoader = new ImageLoader();
        	BufferedImage[] imageLeft = imageLoader.getLeftImage(mario.SMALL);
        	BufferedImage[] imageRight = imageLoader.getRightImage(mario.SMALL);

        	Animation animation = new Animation(imageLeft, imageRight);
        	mario = new PlayerForm(animation, false, false);
        	setImage(mario.getCurrentStyle(right, false, false));
	}

    @Override
    public void paintComponent(Graphics g) {
        boolean movX = (getdX() != 0);
        boolean movY = (getdY() != 0);

        setImage(mario.getCurrentStyle(right, movX, movY));

        super.paintComponent(g);
    }

    public void jump(MoteurJeu engine) {
        if(!getDeplacementJumping() && !getDeplacementFalling()){
            setDeplacementJumping(true);
            setdY(10);
            engine.playJump();
        }
    }

    public void move(boolean right, Camera camera) {
        if(right){
            setdX(5);
        }
        else if(camera.getX() < getX()){
            setdX(-5);
        }

        this.right = right;
    }

    public boolean onTouchEnemy(MoteurJeu engine){

        if(!mario.isSuper() && !mario.isFire()){
            Lives--;
            engine.playMarioDies();
            return true;
        }
        else{
            engine.shakeCamera();
            mario = mario.onTouchEnemy(engine.getImageLoader());
            return false;
        }
    }

    public MarioVSEnnemi MarioVSEnnemi(){
        return mario.MarioVSEnnemi(right, getX(), getY());
    }

    public void acquireCoin() {
        coins++;
    }

    public void acquirePoints(int point){
        points = points + point;
    }

    public int getLives() {
        return Lives;
    }

    public void setLives(int lives) {
        this.Lives = lives;
    }

    public int getPoints() {
        return points;
    }

    public int getCoins() {
        return coins;
    }

    public PlayerForm getPlayerForm() {
        return mario;
    }

    public void setPlayerForm(PlayerForm marioForm) {
        this.mario = marioForm;
    }

    public boolean isSuper() {
        return mario.isSuper();
    }

    public boolean getRight() {
        return right;
    }

    public void resetLocation() {
        setdX(0);
        setdY(0);
        setX(50);
        setDeplacementJumping(false);
        setDeplacementFalling(true);
    }

}

