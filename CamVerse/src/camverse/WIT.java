/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */


package camverse;

import static camverse.CamVerseLogic.filterBI;
import static camverse.CamVerseLogic.generateRoute;
import static camverse.CamVerseLogic.getImage;
import static camverse.CamVerseLogic.rotateBI;
import static camverse.CamVerseLogic.templateBI;
import static camverse.CamVerseLogic.zoomBI;
import com.github.sarxos.webcam.WebcamImageTransformer;
import java.awt.image.BufferedImage;

/**
 * <b>Implementa transformaciones a una imagen de la webcam.</b>
 */
public class WIT implements WebcamImageTransformer {

    /**
     * Zoom establecido en la transformación.
     */
    private int zoom;

    /**
     * Ángulo de rotación establecido en la transformación.
     */
    private int rotate;

    /**
     * Id del filtro que representa el filtro activo. 0 cuando no hay filtro activo.
     */
    private int filter;
    
    /**
     * Marco cargado para aplicar a la webcam.
     */
    private BufferedImage IMAGE_FRAME = null;

    public WIT() {
        zoom = 100;
        rotate = 0;
        filter = 0;
    }

    @Override
    public BufferedImage transform(BufferedImage image) {
        image = rotateBI(image,rotate);
        image = zoomBI(image, zoom/100f);
        image = filterBI(image,filter);
        image = templateBI(image,IMAGE_FRAME);
        return image;
    }

    public void setZoom(int _zoom) {
        zoom = _zoom;
    }

    public void setRotate(int _rotate) {
        rotate = _rotate;
    }

    public void setFilter(int _filter) {
        filter = _filter;
    }
    
    public void setTemplate(String filename) {
        IMAGE_FRAME = (filename==null) ? null : getImage(generateRoute("templates",filename));
    }
}