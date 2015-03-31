package main;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
				try
				{
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(fileName));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e)
				{
					System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
				}

			}
		}).start();
	}
}