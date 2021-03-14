package gestionJeu;

public class Camera {
	
	private double x, y;		// position x et y
	private int nombreFPS;		// nombre d'images par secondes
	private boolean shaking;	// si cette variable prend la valeur true
								// la caméra peut bouger
	
	// Constructeur de la caméra
	public Camera() {
        this.x = 0;
        this.y = 0;
        this.nombreFPS = 25;
        this.shaking = false;
	}
	
	// Obtenir la position X de la caméra
    public double getX() {
        return x;
    }
    
	// Modifier la position X de la caméra
    public void setX(double x) {
        this.x = x;
    }
    
	// Obtenir la position Y de la caméra
    public double getY() {
        return y;
    }
    
	// Modifier la position Y de la caméra
    public void setY(double y) {
        this.y = y;
    }
    
    // Autorise le déplacement ou non de la caméra
    public void shakeCamera() {
        shaking = true;
        nombreFPS = 60;
    }
    
    // Cette méthode s'occupe du déplacement de la caméra
    public void bougerCamera (double deplacementX, double deplacementY) {
        if(shaking && nombreFPS > 0){
            int direction = (nombreFPS%2 == 0)? 1 : -1;
            x = x + 4 * direction;
            nombreFPS--;
        }
        else{
            x = x + deplacementX;
            y = y + deplacementY;
        }

        if(nombreFPS < 0)
            shaking = false;
    }
}
