/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */

package camverse;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class Capture implements Runnable {
    
    private final Webcam wc;
    private final String path;
    private final int delay;
    private JButton jb;
    
    public Capture(int time, Webcam wc, String path, JButton jb){
        this.wc = wc;
        this.path = path;
        this.delay = time;
        this.jb = jb;
    }
 
    @Override
    public void run() {
        
        for(int i=delay;i>0;i--){
            try {
                jb.setText(i + " segundos");
                Thread.sleep(1000); // Tiempo de espera hasta la captura.
            } catch (InterruptedException ex) {
                Logger.getLogger(Capture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            jb.setText("SONRÍE!");
            Thread.sleep(40);
            String nombre = "/img/Image-" + System.currentTimeMillis();
            Audio aud = new Audio();
            aud.init();
            WebcamUtils.capture(wc, path + nombre, ImageUtils.FORMAT_PNG);
            Thread.sleep(500);
            JOptionPane.showMessageDialog(null,"Imagen guardada en: '" + path + nombre + "'\n¡Esperemos que hayas salido guapo! (Siempre que no seas informático)");
            jb.setText("Tomar instantánea");
        } catch (InterruptedException ex) {
            Logger.getLogger(Capture.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
}
