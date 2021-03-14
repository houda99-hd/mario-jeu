package affichage;

import affichage.SelectionMap;
import gestionJeu.MoteurJeu;
import gestionJeu.StatutsJeu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ManagerInterfaceUtilisateur extends JPanel {
	
	private MoteurJeu moteurJeu;
	private Font gameFont;
	private BufferedImage imageEcranDemarrage, imageEcranAPropos, 
							imageEcranAide, imageEcranGameOver;
    private BufferedImage iconeCoeur;
    private BufferedImage iconePiece;
    private BufferedImage iconeSelect;
    private BufferedImage imageSelectMapMario;
    private SelectionMap selectionMap;
    
    public ManagerInterfaceUtilisateur(MoteurJeu moteurJeu, int largeur, int hauteur) {
    	// On initialise les dimensions de l'affichage
    	setPreferredSize(new Dimension(largeur, hauteur));
        setMaximumSize(new Dimension(largeur, hauteur));
        setMinimumSize(new Dimension(largeur, hauteur));
        
        this.moteurJeu = moteurJeu;
        ImageLoader chargeurImage = moteurJeu.getImageLoader();
        selectionMap = new SelectionMap();
        
        // On charge les images nécessaires à l'affichage
        BufferedImage sprite = chargeurImage.loadImage("/sprite.png");
        this.iconeCoeur = chargeurImage.loadImage("/heart-icon.png");
        this.iconePiece = chargeurImage.getSubImage(sprite, 1, 5, 48, 48);
        this.iconeSelect = chargeurImage.loadImage("/select-icon.png");
        this.imageEcranDemarrage = chargeurImage.loadImage("/start-screen.png");      
        this.imageEcranAPropos = chargeurImage.loadImage("/about-screen.png");
        this.imageEcranAide = chargeurImage.loadImage("/help-screen.png");
        this.imageEcranGameOver = chargeurImage.loadImage("/game-over.png");
        this.imageSelectMapMario = chargeurImage.loadImage("/select-map-mario.png");
        
        try {
        	InputStream in = getClass().getResourceAsStream("/media/font/mario-font.ttf");
        	gameFont = Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (FontFormatException | IOException e) {
        	gameFont = new Font("Verdana", Font.PLAIN, 12);
        	e.printStackTrace();
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2 = (Graphics2D) g.create();
    	StatutsJeu statusJeu = moteurJeu.getStatutJeu();
    	
    	if(statusJeu == StatutsJeu.ECRAN_START) {
            afficherEcranDemarrage(g2);
        } else if (statusJeu == StatutsJeu.SELECTION_NIVEAU) {
            afficherEcranSelectionMap(g2);
        } else if (statusJeu == StatutsJeu.ECRAN_A_PROPOS) {
            afficherEcranAPropos(g2);
        } else if (statusJeu == StatutsJeu.ECRAN_AIDE) {
            afficherEcranAide(g2);
        } else if (statusJeu == StatutsJeu.GAME_OVER) {
            afficherEcranGameOver(g2);
        } else {
        	Point positionCamera = moteurJeu.getPositionCamera();
            g2.translate(-positionCamera.x, -positionCamera.y);
            moteurJeu.drawMap(g2);
            g2.translate(positionCamera.x, positionCamera.y);

            afficherPoints(g2);
            afficherViesRestantes(g2);
            afficherPieces(g2);
            afficherTempsRestant(g2);

            if (statusJeu == StatutsJeu.EN_PAUSE) {
                afficherEcranPause(g2);
            } else if (statusJeu == StatutsJeu.NIVEAU_TERMINE) {
                afficherEcranVictoire(g2);
            }
        }
    	g2.dispose();
    }
    
    // Affichage de l'écran de démarrage
    private void afficherEcranDemarrage(Graphics2D g2) {
        int numeroLigneMenuStart = moteurJeu.getEcranStartSelection().getNumeroLigne();
        // Affichage de l'image de fond du menu start
        g2.drawImage(imageEcranDemarrage, 0, 0, null);
        // Affichage de l'icône de sélection sur la ligne sélectionnée par l'utilisateur
        if (numeroLigneMenuStart == 0) {
        	g2.drawImage(iconeSelect, 375, numeroLigneMenuStart * 60 + 400, null);
        } else if (numeroLigneMenuStart == 1) {
        	g2.drawImage(iconeSelect, 470, numeroLigneMenuStart * 55 + 415, null);
        } else if (numeroLigneMenuStart == 2) {
        	g2.drawImage(iconeSelect, 400, numeroLigneMenuStart * 55 + 420, null);
        }
    }
    
    // Affichage de l'écran de sélection de niveau
    private void afficherEcranSelectionMap(Graphics2D g2) {
    	
        g2.drawImage(imageSelectMapMario, 0, 0, null);
        int row = moteurJeu.getNiveauChoisi();
        int position_y = row*100 + 300 - iconeSelect.getHeight();
        
        // affichage de l'icône Select à une position définie
        g2.drawImage(iconeSelect, 380, position_y+20, null);
    }
    
    // Affichage de l'écran "à propos"
    private void afficherEcranAPropos(Graphics2D g2) {
        g2.drawImage(imageEcranAPropos, 0, 0, null);
    }
    
    // Affichage de l'écran d'aide
    private void afficherEcranAide(Graphics2D g2) {
        g2.drawImage(imageEcranAide, 0, 0, null);
    }
    
    // Affichage de l'écran de Game Over
    private void afficherEcranGameOver(Graphics2D g2) {
        g2.drawImage(imageEcranGameOver, 0, 0, null);	// image à afficher
        g2.setFont(gameFont.deriveFont(50f));			// police d'affichage
        g2.setColor(new Color(130, 48, 48));			// couleur d'affichage
        String chaineScore = "Score: " + moteurJeu.getScore();
        int longueurChaine = g2.getFontMetrics().stringWidth(chaineScore);
        int hauteurChaine = g2.getFontMetrics().getHeight();
        // Affichage du score de manière centrée
        g2.drawString(chaineScore, (getWidth()-longueurChaine)/2, getHeight()-(hauteurChaine*2)-170);
    }
    
    // Affichage des points
    private void afficherPoints(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(25f));	// police d'affichage
        g2.setColor(Color.white);				// couleur d'affichage
        String chaineScore = "Points: " + moteurJeu.getScore();
        //g2.drawImage(coinIcon, 50, 10, null);
        g2.drawString(chaineScore, 300, 50);
    }
    
    // Affichage du nombre de vies restantes
    private void afficherViesRestantes(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(30f));	// police d'affichage
        g2.setColor(Color.white);				// couleur d'affichage
        String chaineViesRestantes = "" + moteurJeu.getViesRestantes();
        // Affichage de l'icône coeur à côté du nombre de vies restantes
        // en haut à gauche de l'écran
        g2.drawImage(iconeCoeur, 50, 10, null);
        g2.drawString(chaineViesRestantes, 100, 50);
    }
    
    // Affichages pièces ramassées
    private void afficherPieces(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(30f));	// police d'affichage
        g2.setColor(Color.white);				// couleur d'affichage
        String chainePieces = "" + moteurJeu.getPieces();
        // Affichage de l'icône pièce à côté du nombre de pièces ramassées
        // en haut à droite de l'écran
        g2.drawImage(iconePiece, getWidth()-115, 10, null);
        g2.drawString(chainePieces, getWidth()-65, 50);
    }
    
    // Affichage du temps de jeu restant
    private void afficherTempsRestant(Graphics2D g2) {
    	g2.setFont(gameFont.deriveFont(30f));	// police d'affichage
    	g2.setColor(Color.white);				// couleur d'affichage
    	String chaineAffichee = "TIME:" + moteurJeu.getTempsRestant(); // chaine affichée
    	//int longueurChaineAffichee = g2.getFontMetrics().stringWidth(chaineAffichee);
    	// On affiche le temps restant à une position définie
    	g2.drawString(chaineAffichee, 650, 50);
    }
    
    // Affichage de l'écran de pause
    private void afficherEcranPause(Graphics2D g2) {
        g2.setFont(gameFont.deriveFont(50f));	// police d'affichage
        g2.setColor(Color.white);				// couleur d'affichage
        String chaineEnPause = "EN PAUSE";
        int longueurChaine = g2.getFontMetrics().stringWidth(chaineEnPause);
        // Affichage de "EN PAUSE" au centre de l'écran
        g2.drawString(chaineEnPause, (getWidth()-longueurChaine)/2, getHeight()/2);
    }
    
    // Affichage de l'écran de victoire
    private void afficherEcranVictoire(Graphics2D g2) {
    	g2.setFont(gameFont.deriveFont(50f));	// police d'affichage
    	g2.setColor(Color.white);				// couleur d'affichage
    	String chaineAffichee = "VICTOIRE !";	// chaine affichée
    	int longueurChaineAffichee = g2.getFontMetrics().stringWidth(chaineAffichee);
    	// On affiche le message de victoire au milieu haut de l'écran
    	g2.drawString(chaineAffichee, (getWidth()-longueurChaineAffichee)/2, getHeight()/2);
    }
    
    // Sélectionner la map / le niveau avec la souris
    public String choisirNiveauSouris(Point positionSouris) {
        return selectionMap.choisirMap(positionSouris);
    }
    
    // Séléctionner la map / le niveau avec le clavier
    public String choisirNiveauClavier(int index){
        return selectionMap.choisirMap(index);
    }
    
    // Changer la map sélectionnée
    public int changerNiveauSelectionne(int index, boolean up){
        return selectionMap.changerMapSelectionnee(index, up);
    }
}
