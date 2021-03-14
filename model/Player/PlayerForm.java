package model.Player;



import java.awt.image.BufferedImage;

import affichage.*;


public class PlayerForm {

    public static final int SMALL = 0, SUPER = 1, FIRE = 2;

    private Animation animation;
    private boolean isSuper, isFire;
    private BufferedImage fireballimage;

    public PlayerForm(Animation animation, boolean isSuper, boolean isFire){
        this.animation = animation;
        this.isSuper = isSuper;
        this.isFire = isFire;

        ImageLoader imageLoader = new ImageLoader();
        BufferedImage fireimage = imageLoader.loadImage("/sprite.png");
        fireballimage = imageLoader.getSubImage(fireimage, 3, 4, 24, 24);
    }

    public BufferedImage getCurrentStyle(boolean right, boolean movX, boolean movY){

        BufferedImage style;

        if(movY && right){
            style = animation.getRight()[0];
        }
        else if(movY){
            style = animation.getLeft()[0];
        }
        else if(movX){
            style = animation.animate(5, right);
        }
        else {
            if(right){
                style = animation.getRight()[1];
            }
            else
                style = animation.getLeft()[1];
        }

        return style;
    }

    public PlayerForm onTouchEnemy(ImageLoader imageLoader) {
        BufferedImage[] leftFrames = imageLoader.getLeftImage(0);
        BufferedImage[] rightFrames= imageLoader.getRightImage(0);

        Animation newAnimation = new Animation(leftFrames, rightFrames);

        return new PlayerForm(newAnimation, false, false);
    }

    public MarioVSEnnemi MarioVSEnnemi(boolean toRight, double x, double y) {
        if(isFire){
            return new MarioVSEnnemi(x, y + 48, fireballimage, toRight);
        }
        return null;
    }

    public boolean isSuper() {
        return isSuper;
    }

    public void setSuper(boolean usuper) {
        isSuper = usuper;
    }

    public boolean isFire() {
        return isFire;
    }
}
