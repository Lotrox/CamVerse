package camverse;

import java.applet.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Audio extends Applet
{
    URL codb;
    Image picture;
    AudioClip clip;

    public void init()
    {          
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("cam1.wav")));
            clip.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
            Logger.getLogger(Audio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getAppletInfo()
    {
        return "Hi...";
    }

    @Override
    public void start() { 
        
       // showStatus(getAppletInfo());
    }

    public void paint(Graphics g)
    {
        g.drawImage(picture, 10, 10, this);
    }
}