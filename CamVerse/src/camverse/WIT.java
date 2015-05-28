
package camverse;

import static camverse.CamVerseLogic.rotateBI;
import static camverse.CamVerseLogic.zoomBI;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.jhlabs.image.CrystallizeFilter;
import com.jhlabs.image.DitherFilter;
import com.jhlabs.image.ExposureFilter;
import com.jhlabs.image.FBMFilter;
import com.jhlabs.image.GammaFilter;
import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.GlowFilter;
import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.KaleidoscopeFilter;
import com.jhlabs.image.LightFilter;
import com.jhlabs.image.NoiseFilter;
import com.jhlabs.image.SharpenFilter;
import com.jhlabs.image.SolarizeFilter;
import com.jhlabs.image.SphereFilter;
import com.jhlabs.image.ThresholdFilter;
import com.jhlabs.image.WaterFilter;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

    /**
     * <b>Implementa transformaciones a una imagen de la webcam.</b>
     */
    public class WIT implements WebcamImageTransformer {
        
        /**
         * Instancias de filtros disponibles.
         */
        private static final BufferedImageOp[] filters = new BufferedImageOp[] {
            new CrystallizeFilter(),
            new DitherFilter(),
            new ExposureFilter(),
            new FBMFilter(),
            new GammaFilter(),
            new GaussianFilter(10),
            new GlowFilter(),
            new GrayscaleFilter(),
            new InvertFilter(),
            new KaleidoscopeFilter(),
            new LightFilter(),
            new NoiseFilter(),
            new SharpenFilter(),
            new SolarizeFilter(),
            new SphereFilter(),
            new ThresholdFilter(),
            new WaterFilter()
        };
    
        /**
         * Zoom establecido en la transformación.
         */
        private int zoom;
        
        /**
         * Ángulo de rotación establecido en la transformación.
         */
        private int rotate;
        
        /**
         * Id del filtro que representa el filtro activo. -1 cuando no hay filtro activo.
         */
        private int filter;
        
        public WIT() {
            zoom = 100;
            rotate = 0;
            filter = 0;
        }
        
        @Override
        public BufferedImage transform(BufferedImage image) {
            image = rotateBI(image,rotate);
            image = zoomBI(image, zoom/100f);
            
            if (filter>0) {
                BufferedImage modified = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                filters[filter-1].filter(image, modified);
                modified.flush();
                return modified;
            } else {
                return image;
            }
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
    }