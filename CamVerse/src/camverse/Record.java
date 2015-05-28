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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;


public class Record implements Runnable{

    private final Webcam wc; 
    private final JToggleButton jtb;
    private boolean stop;
    private String path;
    private JComboBox jcb;
    
    public Record (Webcam activeWebcam, JToggleButton jtb, String path, JComboBox jcb){
        this.wc = activeWebcam;
        this.jtb = jtb;
        this.path = path;
        this.jcb = jcb;
    }
    
    public void parar(){
        jtb.setText("INICIAR GRABACIÓN");
        this.stop = true;
    }
    @Override
    public void run() {
        // Nombre del fichero a guardar en formato mp4.
        File file = new File(path + "/Video-" + System.currentTimeMillis() + ".mp4");
        IMediaWriter writer = ToolFactory.makeWriter(file.getAbsolutePath());
        
        //Resolucion y codec de video.
        Dimension size = WebcamResolution.VGA.getSize();
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);
        
        long start = System.currentTimeMillis();
        jtb.setText("DETENER GRABACIÓN");
        int time = (int) (System.currentTimeMillis()/1000);
        for (int i = 0; !stop; i++) {
            if(((int)(System.currentTimeMillis()/1000)-time) % 2 == 0) jtb.setText("DETENER GRABACIÓN");
            else jtb.setText(String.valueOf((int)(System.currentTimeMillis()/1000)-time) + " SEGUNDOS");
            BufferedImage image = ConverterFactory.convertToType(wc.getImage(), BufferedImage.TYPE_3BYTE_BGR);
            IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

            IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000); // < 1000 cámara rápida. > 1000 cámara lenta.
            frame.setKeyFrame(i == 0);
            frame.setQuality(100);

            writer.encodeVideo(0, frame);

            try {
                //30 FPS por defecto
                Thread.sleep(1000/(int)jcb.getSelectedItem());
            } catch (InterruptedException ex) {
                Logger.getLogger(CamVerseUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            writer.close();
            JOptionPane.showMessageDialog(null,"Video grabado en: " + path + "/" + file.getName());
    }
    
}
