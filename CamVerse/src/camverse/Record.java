/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */

package camverse;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JToggleButton;


public class Record implements Runnable{

    private final Webcam wc; 
    private final JToggleButton jtb;
    private boolean stop;
    
    public Record (Webcam activeWebcam, JToggleButton jtb){
        this.wc = activeWebcam;
        this.jtb = jtb;
    }
    
    public void parar(){
        this.stop = true;
    }
    @Override
    public void run() {
        // Nombre del fichero a guardar en formato mp4.
        File file = new File("Video-"+System.currentTimeMillis()+".mp4");
        IMediaWriter writer = ToolFactory.makeWriter(file.getName());
        //Resolucion y codec de video.
        Dimension size = WebcamResolution.VGA.getSize();
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);
        long start = System.currentTimeMillis();
        jtb.setText("DETENER GRABACIÓN");
        
        for (int i = 0; !stop; i++) {

                BufferedImage image = ConverterFactory.convertToType(wc.getImage(), BufferedImage.TYPE_3BYTE_BGR);
                IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

                IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000); // < 1000 cámara rápida. > 1000 cámara lenta.
                frame.setKeyFrame(i == 0);
                frame.setQuality(100);

                writer.encodeVideo(0, frame);

            try {
                //30 FPS
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                Logger.getLogger(CamVerseUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            writer.close();
            jtb.setText("INICIAR GRABACIÓN");
            System.out.println("Video recorded in file: " + file.getAbsolutePath());
            jtb.setSelected(false);
    }
    
}
