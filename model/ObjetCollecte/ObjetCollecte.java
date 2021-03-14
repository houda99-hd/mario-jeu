
package model.ObjetCollecte;

import java.awt.*;

import gestionJeu.MoteurJeu;
import model.Player.Mario;


public interface ObjetCollecte {
	
	Rectangle getBounds();
	
	int getPoint();

    void reveal();

    

    void onTouch(Mario mario, MoteurJeu engine);

}
