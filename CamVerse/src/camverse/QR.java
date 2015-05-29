/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */

package camverse;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;

/**
 * <b>Hilo demonio en segundo plano que lee código QR cada cierto tiempo.</b>
 */
public class QR implements Runnable {
    /**
     * Instancia de una interfaz que contiene la webcam a auditar.
     */
    private CamVerseUI instance;

    /**
     * <b>Constructor de un demonio QR al que se le indica la instancia que audita.</b>
     * @param _instance 
     */
    public QR(CamVerseUI _instance) {
        instance = _instance;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Webcam webcam = instance.getWebcam();
            Result result = null;
            BufferedImage image = null;
            
            if (webcam.isOpen()) {
                    if ((image = webcam.getImage()) == null) {
                            continue;
                    }

                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                            result = new MultiFormatReader().decode(bitmap);
                    } catch (NotFoundException e) {
                            // fall thru, it means there is no QR code in image
                    }
            }

            if (result != null) {
                    System.out.println(result.getText());
            }
        }
    }
    
}
