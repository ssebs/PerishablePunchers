package main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Sound
{
	public static synchronized void play(final String fileName)
	{
		// Note: use .wav files
		new Thread(new Runnable()
		{
			public void run()
			{
				// orig, working, but not in jar
//				try
//				{
//					AudioInputStream inputStream = AudioSystem.getAudioInputStream((Sound.class.getResource(fileName)));
//					Clip clip = AudioSystem.getClip();
//					clip.open(inputStream);
//					clip.start();
//				} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e)
//				{
//					e.printStackTrace();
//					System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
//				}
				
				URL url = Sound.class.getResource(fileName);
				AudioClip clip = Applet.newAudioClip(url);
				clip.play();

			}
		}).start();
	}
}