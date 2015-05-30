/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */

package camverse;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * <b>Muestra la webcam en pantalla completa.</b>
 * http://stackoverflow.com/questions/2348412/making-a-java-panel-fullscreen
 */
public class Fullscreen extends JFrame {
    /**
     * Instancia de una interfaz que contiene la webcam a auditar.
     */
    private CamVerseUI instance;
    
    /**
     * <b>Constructor que permite crear un JFrame en pantalla completa.</b>
     * @param _instance 
     */
    public Fullscreen(CamVerseUI _instance) {
        super(_instance.getWebcam().getName());
        try {
            Image img = ImageIO.read(new File("resources/icon.png"));
            this.setIconImage(img);
        } catch (IOException ex) {
            Logger.getLogger(Fullscreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        instance = _instance;
        instance.setEnabled(false);
        instance.setVisible(false);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setLayout(new FlowLayout());
        
        Webcam wc = _instance.getWebcam();
        WebcamPanel wcp = new WebcamPanel(wc);
        
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Dimension screenSize = new Dimension(graphicsEnvironment.getMaximumWindowBounds().width - 10,graphicsEnvironment.getMaximumWindowBounds().height - 10);
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double w = wc.getViewSize().getWidth(),
               h = wc.getViewSize().getHeight();

        if (w > h) {
            h = (h/w)*screenSize.width;
            w = screenSize.width;
        } else {
            w = (w/h)*screenSize.height;
            h = screenSize.height;
        }

        if (w > screenSize.width) {
            double diff = screenSize.width - w;
            w -= diff;
            h -= diff;
        }

        if (h > screenSize.height) {
            double diff = h - screenSize.height;
            w -= diff;
            h -= diff;
        }
        
        wcp.setPreferredSize(new Dimension((int)w, (int)h));
        add(wcp);

//        setBounds(0,0,screenSize.width, screenSize.height);
//        setUndecorated(true);
        Rectangle maximumWindowBounds=graphicsEnvironment.getMaximumWindowBounds();
        setBounds(maximumWindowBounds);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if(e.getID() == WindowEvent.WINDOW_CLOSING) {
            instance.setEnabled(true);
            instance.setVisible(true);
            this.setVisible(false);
            this.setEnabled(false);
            this.dispose();
        }
    }
}