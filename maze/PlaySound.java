package maze;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineEvent.Type;

public class PlaySound extends Thread
{
	/**
	 * @uml.property  name="fileName"
	 */
	String fileName;

	public PlaySound (String s)
	{
		fileName = s;
		this.start();
	}

	public void run ()
	{
		playClip(new File(fileName));
	}

	public static void playClip(File clipFile) 
	{
		class AudioListener implements LineListener 
		{
			private boolean done = false;

			public synchronized void update(LineEvent event) 
			{
				Type eventType = event.getType();
				if (eventType == Type.STOP || eventType == Type.CLOSE) 
				{
					done = true;
					notifyAll();
				}
			}
			public synchronized void waitUntilDone() throws InterruptedException 
			{
				while (!done) { wait(); }
			}
		}
		AudioListener listener = new AudioListener();
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile);
			try 
			{
				Clip clip = AudioSystem.getClip();
				clip.addLineListener(listener);
				clip.open(audioInputStream);
				try 
				{
					clip.start();
					listener.waitUntilDone();
				} 
				finally 
				{
					clip.close();
				}
			} 
			finally 
			{
				audioInputStream.close();
			}
		}
		catch (IOException e) {} 
		catch (UnsupportedAudioFileException e) {}
		catch (LineUnavailableException e) {} 
		catch (InterruptedException e) {}
	}
}
