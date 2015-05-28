/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */

package camverse;

import com.github.sarxos.webcam.Webcam;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.util.List;
import javax.swing.JComboBox;

/**
 * <b>Implementa las operaciones lógicas de la aplicación y de interacción con la interfaz.</b>
 */
public class CamVerseLogic {

    /**
     * <b>Genera la lista de webcams disponibles en el sistema y lo almacena en una lista desplegable de Java.</b>
     * @param jcb Lista desplegable de Java.
     */
    public static Webcam getListWebcam(JComboBox jcb, Webcam active) {
        jcb.removeAllItems();
        if (active!=null) jcb.addItem(active);
        List<Webcam> l = Webcam.getWebcams();
        for (Webcam w : l) {
            if (active!=w) jcb.addItem(w);
        }
        return (l.isEmpty()) ? null : (active!=null) ? active : Webcam.getDefault();
    }
    
    public static BufferedImage rotateBI(BufferedImage img, int rotation) {
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage newImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.rotate(Math.toRadians(rotation), w/2, h/2);  
        g2.drawImage(img,null,0,0);
        return newImage;  
    }
    
    public static BufferedImage zoomBI(BufferedImage img, float ss) {
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage newImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.scale(ss,ss);  
        g2.drawImage(img,null,0,0);
        return newImage;  
    }
}
