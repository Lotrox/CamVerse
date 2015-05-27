/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */

package camverse;

import com.github.sarxos.webcam.Webcam;
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
    public static Webcam getListWebcam(JComboBox jcb) {
        jcb.removeAllItems();
        List<Webcam> l = Webcam.getWebcams();
        for (Webcam w : l) {
            jcb.addItem(w);
        }
        return (l.isEmpty()) ? null : l.get(0);
    }
}
