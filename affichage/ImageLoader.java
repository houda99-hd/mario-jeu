package affichage;

import javax.imageio.ImageIO;



import java.awt.image.BufferedImage;

import java.io.File;

import java.io.IOException;


public class ImageLoader {
    //La forme du joueur
    private BufferedImage playerForm;
    // les briques du jeu
    private BufferedImage animationBrique;

    /** Définir les composantes du jeu.
     */
    public ImageLoader(){
	//définir les forms possible du joueur mario
        playerForm = loadImage("/mario-forms.png");
	//définir les briques du map
        animationBrique = loadImage("/brick-animation.png");
    }
    
    /** Télecharger l'image du jeu.
     * @param le path de l'image voulue.
     * @return l'image recherché.
     */

    public BufferedImage loadImage(String path){
        BufferedImage image = null;
        try {

            image = ImageIO.read(getClass().getResource("/media" + path));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /** Télecharger l'image du jeu à partir du file.
     * @param file contenant l'image voulue.
     * @return l'image recherché.
     */

    public BufferedImage loadImage(File file){
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    /** Extraire une image voulue à partir d'une autre.
     * @param image l'image dont va extraire l'image voulue.
     * @param x la dimension horizontale du rectangle.
     * @param y la dimension verticale du rectangle.
     * @param w le width de l'image initiale.
     * @param w le height de l'image initiale.
     * @return le rectangle image souhaitée.
     */

    public BufferedImage getSubImage(BufferedImage image, int x, int y, int w, int h){

        if((x == 1 || x == 4) && y == 3){ 
            return image.getSubimage((x-1)*48, 128, w, h);
        }
        return image.getSubimage((x-1)*48, (y-1)*48, w, h);
    }

    /** Récupérer l'image à gauche du joueur.
     * @param joueur le numéro de forme mario souhaitée.
     * @requires joueur <= 2.
     * @return l'ensemble d'image de mario de forme à gauche du type donné.
     */
    public BufferedImage[] getLeftImage(int joueur){

        BufferedImage[] imageLeft = new BufferedImage[5];

        int col = 1;

        int width = 52, height = 48;

        if(joueur == 1) { //super mario
            col = 4;
            width = 48;
            height = 96;
        }
        else if(joueur == 2){ //fire mario
            col = 7;
            width = 48;
            height = 96;
        }

        for(int i = 0; i < 5; i++){
            imageLeft [i] = playerForm.getSubimage((col-1)*width, (i)*height, width, height);
        }
        return imageLeft;

    }

    /** Récupérer l'image à droite du joueur.
     * @param joueur le numéro de forme mario souhaitée.
     * @requires joueur <= 2.
     * @return l'ensemble d'image de mario de forme à droite du type donné.
     */

    public BufferedImage[] getRightImage(int marioForm){

        BufferedImage[] imageRight = new BufferedImage[5];
        int col = 2;
        int width = 52, height = 48;

        if(marioForm == 1) { //super mario
            col = 5;
            width = 48;
            height = 96;
        }
        else if(marioForm == 2){ //fire mario
            col = 8;

            width = 48;

            height = 96;
        }

        for(int i = 0; i < 5; i++){
            imageRight[i] = playerForm.getSubimage((col-1)*width, (i)*height, width, height);
        }
        return imageRight;
    }

    /** Récupérer l'image du brique.
     * @return le rectangle image du brique.
     */

    public BufferedImage[] getBrickImage() {
        BufferedImage[] img = new BufferedImage[4];
        for(int i = 0; i < 4; i++){
            img[i] = animationBrique.getSubimage(i*105, 0, 105, 105);
        }
        return img;
    }

}

