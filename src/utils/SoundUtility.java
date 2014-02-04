package utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Nate
 */
public class SoundUtility implements Runnable
{
	private boolean running = true;
	private volatile static SoundUtility instance = null;
	private ArrayList fileToPlay = new ArrayList();

	private boolean playSounds = true;

    /**
     *
     * @return
     */
    public static SoundUtility getInstance()
	{
		if (instance == null)
		{
			synchronized (SoundUtility.class)
			{
				if (instance == null) // Double-Check!
					instance = new SoundUtility();
			}
		}
		return instance;
	}

	private SoundUtility()
	{

	}

    /**
     *
     * @param value
     */
    public void setPlaySounds(boolean value)
	{
		playSounds = value;
	}

	private boolean getPlaySounds()
	{
		return playSounds;
	}

	private void playSound()
	{
		if (!getPlaySounds()) return;

		String next = getNextFileToPlay();
		while (next != null)
		{
			File soundFile = new File(next);
			LoggingManager.logInfo("Play: " + next);
			try
			{
				Clip clip = null;                    // The sound clip

				AudioInputStream source = AudioSystem.getAudioInputStream(soundFile);
				DataLine.Info clipInfo = new DataLine.Info(Clip.class, source.getFormat());
				if (AudioSystem.isLineSupported(clipInfo))
				{
					// Create a local clip to avoid discarding the old clip
					Clip newClip = (Clip) AudioSystem.getLine(clipInfo);   // Create the clip
					newClip.open(source);

					// Deal with previous clip
					if (clip != null)
					{
						if (clip.isActive())                 // If it's active
						{
							clip.stop();                      // ...stop it
						}
						if (clip.isOpen())                   // If it's open...
						{
							clip.close();                     // ...close it.
						}
					}
					clip = newClip;                       // We have a clip, so discard old
				}
				else
				{
					LoggingManager.logInfo("Unsupported sound file type - cannot play " + next);
				}

				clip.loop(0);
			}
			catch (UnsupportedAudioFileException e)
			{
				JOptionPane.showMessageDialog(null, "File not supported",
				                              "Unsupported File Type", JOptionPane.WARNING_MESSAGE);
			}
			catch (LineUnavailableException e)
			{
				JOptionPane.showMessageDialog(null, "Clip not available", "Clip Error",
				                              JOptionPane.WARNING_MESSAGE);
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "I/O error creating clip: " + e.getMessage(), "Clip Error",
				                              JOptionPane.WARNING_MESSAGE);
			}
			next = getNextFileToPlay();
		}
	}

    /**
     *
     * @param file
     */
    public synchronized void playSound(String file)
	{
		addFileToPlay("resources\\sounds\\" + file);
		notify();
	}

	public void run()
	{

		while (isRunning())
		{
			synchronized (this)
			{
				try
				{
					wait();
				}
				catch (InterruptedException e)
				{
				}
			}
			playSound();
		}
		LoggingManager.logInfo("Process shut down.");
	}

	private boolean isRunning()
	{
		return running;
	}

    /**
     *
     * @param runningIn
     */
    public void setRunning(boolean runningIn)
	{
		running = runningIn;
	}


	private String getNextFileToPlay()
	{
		if (fileToPlay.size() == 0) return null;
		String next = (String) fileToPlay.get(0);
		fileToPlay.remove(0);
		return next;
	}

	private void addFileToPlay(String newFileToPlay)
	{
            boolean add = fileToPlay.add(newFileToPlay);
            //       LoggingManager.logInfo("SOUNDS: " + fileToPlay.size());
	}

    /**
     *
     */
    public void runBasicTests()
	{
		try
		{
			// Test Code *************************************************************************
			LoggingManager.logInfo("Sound Utility Test: Play one sound");
			SoundUtility.getInstance().playSound("Explosion.wav");
			Thread.sleep(3000);
			LoggingManager.logInfo("Done\n");


			LoggingManager.logInfo("Sound Utility Test: Play 10 individual sounds");
			for (int i = 0; i < 10; i++)
			{
				SoundUtility.getInstance().playSound("Explosion.wav");
				Thread.sleep(1000);
			}
			Thread.sleep(3000);
			LoggingManager.logInfo("Done\n");

			LoggingManager.logInfo("Sound Utility Test: Play 10 sounds asynchronously");
			for (int i = 0; i < 10; i++)
			{
				SoundUtility.getInstance().playSound("Explosion.wav");
			}
			Thread.sleep(3000);
			LoggingManager.logInfo("Done\n");
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}
}
