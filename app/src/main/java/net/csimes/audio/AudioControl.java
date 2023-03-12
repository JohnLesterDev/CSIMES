package net.csimes.audio;

import java.io.*;
import javax.sound.sampled.*;


public class AudioControl {
	
	private static Clip clip;

	public static void play(InputStream is) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(is);
			if (AudioControl.clip == null) {
				AudioControl.clip = AudioSystem.getClip();
				AudioControl.clip.open(ais);
				AudioControl.clip.loop(Clip.LOOP_CONTINUOUSLY);
				AudioControl.clip.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void stop() {
		if (AudioControl.clip != null) {
			AudioControl.clip.stop();
			AudioControl.clip.close();
			AudioControl.clip = null;
		}
	}
	
}
