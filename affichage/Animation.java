package affichage;
import java.awt.image.BufferedImage;


public class Animation {
    /* la position et le compteur de pas du player */
    private int position = 0, counter = 0;
    /* Le sprite du joueur à droite et à gauche */
    private BufferedImage[] imageLeft, imageRight;
    /* le sprite actuel du joueur */
    private BufferedImage currentImage;

    /** Définir les images d'animation du Player et Ennemi .
     * @param imageLeft l'image de l'objet à gauche .
     * @param imageRight l'image de l'objet à droite.
     */

    public Animation(BufferedImage[]imageLeft, BufferedImage[]imageRight){

        this. imageLeft = imageLeft;

        this. imageRight = imageRight;

        currentImage = imageRight [1];

    }

    /** Animer le mouvement du Player et Ennemi.
     * @param speed la vitesse de déplacement du joueur .
     * @param right pour indiquer si le mouvement à gauche ou à droite.
     * @return l'image de l'animation souhaitée.
     */

    public BufferedImage animate(int speed, boolean right){

        counter++;

        BufferedImage[] img  = right ? imageRight: imageLeft;

        if(counter > speed){

            next (img);

            counter = 0;

        }

        return currentImage;
    }

    /** Modifier l' image courante de l'objet.
     * @param img l'image à modifier afin d'animer le changement.
     */

    private void next (BufferedImage[] img) {

        if(position  + 3 > img.length)

            position  = 0;
            currentImage = img[position+2];
	    position++;
    }

    /** Récupérer l' image avec dierction à gauche de l'objet.
     * @return l'image à gauche.
     */

    public BufferedImage[] getLeft () {

        return imageLeft;

    }

    /** Récupérer l' image avec dierction à droite de l'objet.
     * @return l'image à droite.
     */

    public BufferedImage[] getRight () {
        return imageRight;

    }

}

