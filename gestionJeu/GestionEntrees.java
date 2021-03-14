package gestionJeu;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GestionEntrees implements KeyListener, MouseListener{

    private MoteurJeu engine;

    GestionEntrees(MoteurJeu engine) {
        this.engine = engine; }

    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        StatutsJeu status = engine.getStatutJeu();
        EntreesAction currentAction = EntreesAction.NO_ACTION;
                
        if (keyCode == KeyEvent.VK_UP) {
            if(status == StatutsJeu.SELECTION_NIVEAU || status == StatutsJeu.ECRAN_START)
                currentAction = EntreesAction.GO_UP;
            else
                currentAction = EntreesAction.JUMP;
        }
        else if(keyCode == KeyEvent.VK_DOWN){
            if(status == StatutsJeu.SELECTION_NIVEAU || status == StatutsJeu.ECRAN_START)
                currentAction = EntreesAction.GO_DOWN;
        }
        else if (keyCode == KeyEvent.VK_RIGHT) {
            currentAction = EntreesAction.M_RIGHT;
        }
        else if (keyCode == KeyEvent.VK_LEFT) {
            currentAction = EntreesAction.M_LEFT;
        }
        else if (keyCode == KeyEvent.VK_ENTER) {
            currentAction = EntreesAction.SELECT;
        }
        else if (keyCode == KeyEvent.VK_ESCAPE) {
            if(status == StatutsJeu.EN_JEU || status == StatutsJeu.EN_PAUSE )
                currentAction = EntreesAction.PAUSE_RESUME;
            else
                currentAction = EntreesAction.GO_TO_START_SCREEN;

        }
        else if (keyCode == KeyEvent.VK_SPACE){
            currentAction = EntreesAction.FIRE;
        }


        notifyInput(currentAction);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(engine.getStatutJeu() == StatutsJeu.ECRAN_START){
            engine.choisirNiveauSouris();
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_LEFT)
            notifyInput(EntreesAction.ACTION_COMPLETED);
    }

    private void notifyInput(EntreesAction action) {
        if(action != EntreesAction.NO_ACTION)
            engine.traitementInput(action);
    }

    @Override
    public void keyTyped(KeyEvent arg0) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
