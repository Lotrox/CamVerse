/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */

package camverse;

import com.github.sarxos.webcam.Webcam;
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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;

/**
 * <b>Implementa las operaciones lógicas de la aplicación y de interacción con la interfaz.</b>
 */
public class CamVerseLogic {
    
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
    
    /**
     * <b>Dada una imagen y un ángulo de rotación en grados, gira la imagen respecto al ángulo.</b>
     * @param img Imagen de entrada.
     * @param rotation Ángulo de rotación.
     * @return Imagen de salida con la rotación aplicada.
     */
    public static BufferedImage rotateBI(BufferedImage img, int rotation) {
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.rotate(Math.toRadians(rotation), w/2, h/2);  
        g2.drawImage(img,null,0,0);
        return newImage;  
    }
    
    /**
     * <b>Dada una imagen y un factor de escala, aumenta o disminuye la imagen (ocupando las mismas dimensiones).</b>
     * @param img Imagen de entrada.
     * @param ss Factor de escala.
     * @return Imagen de salida con la escala aplicada.
     */
    public static BufferedImage zoomBI(BufferedImage img, float ss) {
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.scale(ss,ss);  
        g2.drawImage(img,null,0,0);
        return newImage;  
    }
    
    /**
     * <b>Dada una imagen y el índice+1 de un filtro, lo aplica a la imagen.</b>
     * Si el índice es 0, no aplica filtro.
     * @param image Imagen de entrada.
     * @param filter Índice+1 del filtro a emplear del vector de filtros disponibles {@link #filters}
     * @return Imagen de salida filtrada.
     */
    public static BufferedImage filterBI(BufferedImage image, int filter) {
        if (filter>0) {
            BufferedImage modified = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            filters[filter-1].filter(image, modified);
            modified.flush();
            return modified;
        } else {
            return image;
        }
    }
    
    public static BufferedImage templateBI(BufferedImage image, BufferedImage template) {
        BufferedImage modified = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        Graphics2D g2 = modified.createGraphics();
        g2.drawImage(image, null, 0, 0);
        g2.drawImage(template, null, 0, 0);
        g2.dispose();

        modified.flush();

        return modified;
    }
    
    /**
     * <b>Lee una imagen desde fichero en el directorio resources y hace una conversion en memoria para manejarla.</b>
     * @param image Ruta completa donde se encuentra la imagen.
     * @return Imagen de salida en memoria y como tipo de dato usable en filtros.
     */
    public static BufferedImage getImage(String image) {
        try {
            System.out.println(image);
            return ImageIO.read(new FileInputStream(image));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * <b>Genera una ruta para llegar a un fichero empezando desde el directorio actual, y posteriormente especificando un directorio padre y el nombre de fichero.</b>
     * @param parent Nombre del directorio.
     * @param filename Nombre del fichero.
     * @return Ruta absoluta que empieza desde el directorio actual y recorre el directorio padre hasta llegar al nombre del fichero.
     */
    public static String generateRoute(String parent, String filename) {
        return Paths.get(Paths.get(".").toAbsolutePath().normalize().toString(),parent,filename).toString();
    }
}
