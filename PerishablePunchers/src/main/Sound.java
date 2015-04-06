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
			@Override
			public void run()
			{			
				URL url = Sound.class.getResource(fileName);
				AudioClip clip = Applet.newAudioClip(url);
				clip.play();
			}
		}).start();
	}
}