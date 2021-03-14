package gestionJeu;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;


public class GestionSon {

    private Clip contexte;
    private long tempsClip = 0;


    public GestionSon() {
	contexte = getClip(loadAudio("background"));
    }


    private AudioInputStream loadAudio(String url) {
	try {
	    InputStream audioSource = getClass().getResourceAsStream("/media/audio/" + url + ".wav");
	    InputStream tomponneIn = new BufferedInputStream(audioSource);
	    return AudioSystem.getAudioInputStream(tomponneIn);

	} catch (Exception e) {
	    System.err.println(e.getMessage());
	}

	return null;
    }


    private Clip getClip(AudioInputStream stream) {
	try {
	    Clip leClip = AudioSystem.getClip();
	    leClip.open(stream);
	    return leClip;
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return null;
    }


    public void resumeBackground(){
	contexte.setMicrosecondPosition(tempsClip);
	contexte.start();
    }


    public void pauseBackground(){
	tempsClip = contexte.getMicrosecondPosition();
	contexte.stop();
    }


    public void restartBackground() {
	tempsClip = 0;
	resumeBackground();
    }


    public void jouerJump() {
	Clip leClip = getClip(loadAudio("jump"));
	leClip.start();
    }


    public void jouerCoin() {
	Clip leClip = getClip(loadAudio("coin"));
	leClip.start();
    }


    public void jouerFireball() {
	Clip leClip = getClip(loadAudio("fireball"));
	leClip.start();
    }


    public void jouerGameOver() {
	Clip leClip = getClip(loadAudio("gameOver"));
	leClip.start();
    }


    public void jouerStomp() {
	Clip leClip = getClip(loadAudio("stomp"));
	leClip.start();
    }


    public void jouerOneUp() {
	Clip leClip = getClip(loadAudio("oneUp"));
	leClip.start();
    }

    public void jouerSuperMushroom() {
	Clip leClip = getClip(loadAudio("superMushroom"));
	leClip.start();
    }


    public void jouerMarioDies() {
	Clip leClip = getClip(loadAudio("marioDies"));
	leClip.start();
    }


    public void jouerFireFlower() {
    }
}
