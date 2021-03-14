package gestionJeu;


import affichage.ImageLoader;
import affichage.EcranStartSelection;
import affichage.ManagerInterfaceUtilisateur;
import model.Player.Mario;

import javax.swing.*;
import java.awt.*;

public class MoteurJeu implements Runnable {
	
	private final static int LARGEUR = 1190, HAUTEUR = 650;		// dimension de l'écran du jeu
	
	// On fait appel à tous les éléments nécessaire au jeu
	private GestionMap mapManager;
	private ManagerInterfaceUtilisateur managerUI;
	private GestionSon soundManager;
	private StatutsJeu statutJeu;
	private boolean enJeu;
	private Camera camera;
	private ImageLoader imageLoader;
	private Thread thread;
	private EcranStartSelection ecranStartSelection = EcranStartSelection.DEMARRER_JEU;
	private int niveauSelectionne = 0;
	
	private MoteurJeu() {
		init();
	}
	
	// Initialisation du moteur du jeu : on initialise tous les éléments du jeu.
	private void init() {
		imageLoader = new ImageLoader();										// chargeur d'images
		GestionEntrees gestionEntrees = new GestionEntrees(this);				// gère les entrées clavier
		statutJeu = StatutsJeu.ECRAN_START;										// initialise le status du jeu
		camera = new Camera();													// créé une caméra pour suivre Mario lorsq'on joue
		managerUI = new ManagerInterfaceUtilisateur(this, LARGEUR, HAUTEUR);	// initialise un affichage
		soundManager = new GestionSon();										// gère le son
		mapManager = new GestionMap();											// gère la map
		
        JFrame frame = new JFrame("Super Mario Bros.");
        frame.add(managerUI);
        frame.addKeyListener(gestionEntrees);
        frame.addMouseListener(gestionEntrees);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        demarrer();
	}
	
	// Démarrer le jeu
	private synchronized void demarrer() {
		if (enJeu)
			return;
		enJeu = true;
		thread = new Thread(this);
		thread.start();
	}
	
	// Reset le jeu (renvoie au menu principal)
	private void reset() {
		resetCamera();							// réinitialise la caméra
		setStatutJeu(StatutsJeu.ECRAN_START);	// remet le status du jeu à ECRAN_START
	}
	
	// Réinitialiser la caméra
	public void resetCamera() {
		camera = new Camera();				// on remplace la caméra par une nouvelle ("toute neuve")
		soundManager.restartBackground();	// on relance la musique de fond
	}
	
	// Changer le status du jeu
	public void setStatutJeu(StatutsJeu monStatut) {
		this.statutJeu = monStatut;
	}
	
	// Choisir le niveau à jouer avec le clavier
	public void choisirNiveauClavier() {
		String mapSelectionnee = managerUI.choisirNiveauClavier(
				niveauSelectionne);
		if (mapSelectionnee != null) {
			creerNiveau(mapSelectionnee);
		}		
	}
	
	// Choisir le niveau à jouer avec la souris
	public void choisirNiveauSouris() {
		String mapSelectionnee = managerUI.choisirNiveauSouris(
				managerUI.getMousePosition());
		if (mapSelectionnee != null) {
			creerNiveau(mapSelectionnee);
		}
	}
	
	// Initialise le niveau auquel le joueur va joeur
	private void creerNiveau(String mapSelectionnee) {
		boolean loaded = mapManager.createMap(imageLoader, mapSelectionnee);
		if (loaded) {
			setStatutJeu(StatutsJeu.EN_JEU);		// change le status du jeu
			soundManager.restartBackground();		// relance la musique de fond
		} else {
			setStatutJeu(StatutsJeu.ECRAN_START);
		}
	}
	
	// Changer le niveau sélectionné
	public void changerNiveauSelectionne(boolean up) {
		niveauSelectionne = managerUI.changerNiveauSelectionne(
				niveauSelectionne, up);
	};
	
	// Gère la mise à jour des différentes informations (positions, collisions,
	// affichage) dans le temps.
	@Override
	public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while (enJeu && !thread.isInterrupted()) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                if (statutJeu == StatutsJeu.EN_JEU) {
                    gameLoop();
                }
                delta--;
            }
            render();

            if(statutJeu != StatutsJeu.EN_JEU) {
                timer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                mapManager.updateTime();
            }
        }
	}
	
	private void render() {
		managerUI.repaint();
	}
	
	// Mise à jour des informations du jeu (caméra, positions, collisions).
    private void gameLoop() {
        updatePositions();
        checkCollisions();
        updateCamera();
        
        // Vérifie si on est en Game Over ou pas
        if (isGameOver()) {
            setStatutJeu(StatutsJeu.GAME_OVER);
        }
        
        // Vérifie si on a fini le niveau ou pas
        int missionPassed = passMission();
        if(missionPassed > -1){
            mapManager.acquirePoints(missionPassed);
            //setGameStatus(GameStatus.MISSION_PASSED);
        } else if(mapManager.endLevel())
            setStatutJeu(StatutsJeu.NIVEAU_TERMINE);
    }
	
    // Mise à jour des positions des différents éléments / objets du niveau.
    private void updatePositions() {
    	mapManager.updateLocations();
    }
    
    // Vérifie les collisions dans le niveau.
    private void checkCollisions() {
    	mapManager.checkCollisions(this);
    }
    
    // Mise à jour de la position de la caméra en fonction de la vitesse de Mario.
    public void updateCamera() {
        Mario mario = mapManager.getMario();
        double marioVitesseX = mario.getdX();
        double shiftAmount = 0;

        if (marioVitesseX > 0 && mario.getX() - 600 > camera.getX()) {
            shiftAmount = marioVitesseX;
        }

        camera.bougerCamera(shiftAmount, 0);
    }
    
    // Permet de changer le status du jeu en Game Over.
    private boolean isGameOver() {
    	if (statutJeu == StatutsJeu.EN_JEU)
    		return mapManager.isGameOver();
    	return false;
    }
    
    // Permet de valider la mission.
    private int passMission() {
    	return mapManager.passMission();
    }
    
    // Fait le lien entre la classe GestionEntrees et le moteur du jeu.
    // Fait appel aux fonctions de GestionEntrees selon le status du jeu.
    public void traitementInput(EntreesAction input) {
    	// Commandes dans le menu Start / menu principal.
    	if (statutJeu == StatutsJeu.ECRAN_START) {
    		if (input == EntreesAction.SELECT &&
    				ecranStartSelection == EcranStartSelection.DEMARRER_JEU) {
    			demarrerJeu();
    		} else if (input == EntreesAction.SELECT &&
    				ecranStartSelection == EcranStartSelection.VOIR_AIDE) {
    			setStatutJeu(StatutsJeu.ECRAN_AIDE);
    		} else if (input == EntreesAction.SELECT &&
    				ecranStartSelection == EcranStartSelection.A_PROPOS) {
    			setStatutJeu(StatutsJeu.ECRAN_A_PROPOS);
    		} else if (input == EntreesAction.GO_DOWN) {
    			selectOption(false);
    		} else if (input == EntreesAction.GO_UP) {
    			selectOption(true);
    		}
    	// Commandes dans le menu de sélection des niveaux / maps
    	} else if (statutJeu == StatutsJeu.SELECTION_NIVEAU) {
  			
            if (input == EntreesAction.SELECT){
                choisirNiveauClavier();
            }
            else if (input == EntreesAction.GO_UP){
                changerNiveauSelectionne(true);
            }
            else if (input == EntreesAction.GO_DOWN){
                changerNiveauSelectionne(false);
            }
        // Commandes dans le jeu / in game.
    	} else if (statutJeu == StatutsJeu.EN_JEU) {
            Mario mario = mapManager.getMario();
            if (input == EntreesAction.M_RIGHT) {
            	mario.move(true, camera);
            } else if (input == EntreesAction.M_LEFT) {
            	mario.move(false, camera);
            } else if (input == EntreesAction.JUMP) {
            	mario.jump(this);
            } else if (input == EntreesAction.ACTION_COMPLETED) {
            	mario.setdX(0);
            } else if (input == EntreesAction.FIRE) {
            	mapManager.fire(this);
            } else if (input == EntreesAction.PAUSE_RESUME) {
            	pauseGame();
            }
        // Commandes en pause.
    	} else if (statutJeu == StatutsJeu.EN_PAUSE) {
    		if (input == EntreesAction.PAUSE_RESUME) {
    			pauseGame();
    		}
    	// Commandes lors du Game Over.
    	} else if (statutJeu == StatutsJeu.GAME_OVER &&
    			input == EntreesAction.GO_TO_START_SCREEN) {
    		reset();
    	// Commandes si le niveau est terminé.
    	} else if (statutJeu == StatutsJeu.NIVEAU_TERMINE &&
    			input == EntreesAction.GO_TO_START_SCREEN) {
    		reset();
    	}
    	
    	if (input == EntreesAction.GO_TO_START_SCREEN) {
    		setStatutJeu(StatutsJeu.ECRAN_START);
    	}
    }
    
    // Choisir une option du menu Start / menu principal.
    private void selectOption(boolean selectUp) {
    	ecranStartSelection = ecranStartSelection.select(selectUp);
    }
    
    // Lance le jeu/
    private void demarrerJeu() {
    	if (statutJeu != StatutsJeu.GAME_OVER) {
    		setStatutJeu(StatutsJeu.SELECTION_NIVEAU);
    	}
    }
    
    // Faire pause.
    private void pauseGame() {
    	if (statutJeu == StatutsJeu.EN_JEU) {
    		setStatutJeu(StatutsJeu.EN_PAUSE);
    		soundManager.pauseBackground();
    	} else if (statutJeu == StatutsJeu.EN_PAUSE) {
    		setStatutJeu(StatutsJeu.EN_JEU);
    		soundManager.resumeBackground();
    	}
    }
    
    // Autoriser le mouvement de la caméra.
    public void shakeCamera() {
    	camera.shakeCamera();
    }
    
    public ImageLoader getImageLoader() {
    	return imageLoader;
    }
    
    // Obtenir le status du jeu.
    public StatutsJeu getStatutJeu() {
    	return statutJeu;
    }
    
    public EcranStartSelection getEcranStartSelection() {
    	return ecranStartSelection;
    }
    
    // Obtenir le score du joueur / son nombre de points (en jeu).
    public int getScore() {
    	return mapManager.getScore();
    }
    
    // Obtenir le nombre de vies restantes du joueur.
    public int getViesRestantes() {
    	return mapManager.getRemainingLives();
    }
    
    // Obtenir le nombre de pièces récupérées par le joueur.
    public int getPieces() {
    	return mapManager.getCoins();
    }
    
    public int getNiveauChoisi() {
    	return niveauSelectionne;
    }
    
    // Afficher le niveau.
    public void drawMap(Graphics2D g2) {
    	mapManager.drawMap(g2);
    }
    
    public Point getPositionCamera() {
    	return new Point((int)camera.getX(), (int)camera.getY());
    }
    
    // Joueur le son lorsqu'on récupère une pièce.
    public void playCoin() {
    	soundManager.jouerCoin();
    }
    
    // Joueur le son lorsqu'on récupère une vie.
    public void playOneUp() {
    	soundManager.jouerOneUp();
    }
    
    // Joueur le son lorsqu'on mange un SuperMushroom (qui fait grandir).
    public void playSuperMushroom() {
    	soundManager.jouerSuperMushroom();
    }
    
    // Joueur le son lorsque Mario meurt.
    public void playMarioDies() {
        soundManager.jouerMarioDies();
    }
    
    // Joueur le son lorsque Mario saute/
    public void playJump() {
        soundManager.jouerJump();
    }
    
    // Joueur le son de la fleur qui permet de lancer des boules de feu.
    public void playFireFlower() {
        soundManager.jouerFireFlower();
    }

    // Joueur le son lorsqu'on lance une boule de feu.
    public void playFireball() {
        soundManager.jouerFireball();
    }
    
    // Joueur le son lorsqu'on écrase un ennemi.
    public void playStomp() {
        soundManager.jouerStomp();
    }

    public GestionMap getMapManager() {
        return mapManager;
    }
    
    // main
    public static void main(String... args) {
        new MoteurJeu();
    }

    // Obtenir le temps de jeu restant.
    public int getTempsRestant() {
        return mapManager.getRemainingTime();
    }  
}
