package model.ObjetCollecte;

import java.awt.image.BufferedImage;

import affichage.Animation;
import affichage.ImageLoader;
import gestionJeu.MoteurJeu;
import model.Player.Mario;
import model.Player.PlayerForm;

public class SuperMushroom extends BoostItem{

    public SuperMushroom(double x, double y, BufferedImage style) {
        super(x, y, style);
        setPoint(125);
    }

    @Override
    public void onTouch(Mario mario, MoteurJeu engine) {
        mario.acquirePoints(getPoint());

        ImageLoader imageLoader = new ImageLoader();

        if(!mario.getPlayerForm().isSuper()){
            BufferedImage[] leftFrames = imageLoader.getLeftImage(PlayerForm.SUPER);
            BufferedImage[] rightFrames = imageLoader.getRightImage(PlayerForm.SUPER);

            Animation animation = new Animation(leftFrames, rightFrames);
            PlayerForm newForm = new PlayerForm(animation, true, false);
            mario.setPlayerForm(newForm);
            mario.setDimension(48, 96);

            engine.playSuperMushroom();
        }
    }
}
