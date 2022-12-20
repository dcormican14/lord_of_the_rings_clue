package gui;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundHandler {
	Clip clip;
	URL soundURL[] = new URL[30];
	
	public SoundHandler () {
		// load in the URLs to sounds
		soundURL[0] = getClass().getResource("/sounds/LotrBackground.wav");
		soundURL[1] = getClass().getResource("/sounds/Click.wav");
		soundURL[2] = getClass().getResource("/sounds/Win.wav");
		soundURL[3] = getClass().getResource("/sounds/Error.wav");
	}
	
	// setting the file reader for the mov file
	public void setFile(int i) {
		try {
			AudioInputStream inStream = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(inStream);
			
		} catch (Exception e) {
			System.out.println("There was an error while configuring sound files.");
			e.printStackTrace();
		}
	}
	
	public void play() {
		clip.start();
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		clip.stop();
	}
}
