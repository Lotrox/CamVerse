/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */

package camverse;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        instance = _instance;
        instance.setEnabled(false);
        instance.setVisible(false);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                instance.setEnabled(true);
                instance.setVisible(true);
            }  
        });
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
}