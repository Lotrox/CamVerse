/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */


package camverse;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Audio {
    
    private Clip clip;
    
    /**
     * <b>Dado un fichero, reproduce el sonido dado si es posible.<b>
     * @param file Nombre del sonido a ejecutar (debe estar en la carpeta resources/sounds).
     */
    public synchronized void playSound(String file){    
        stopSound();
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("./resources/sounds/" + file)));
            clip.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
            Logger.getLogger(CamVerseLogic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopSound(){
        if(clip != null) clip.stop();
    }
}
