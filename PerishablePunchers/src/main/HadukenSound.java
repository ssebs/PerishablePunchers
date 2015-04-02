package main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class HadukenSound implements Runnable
{
	public HadukenSound()
	{

	}

	public void run()
	{
		URL url = Sound.class.getResource("Haduken.wav");
		AudioClip clip = Applet.newAudioClip(url);
		clip.play();
	}

}
