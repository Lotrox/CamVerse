/**
 * @author José Ángel Pastrana Padilla.
 * @author Daniel Martínez Caballero
 */

package camverse;

import com.github.sarxos.webcam.Webcam;
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
    private final String path;
    private final JComboBox jcb;
    private final Audio sound;
    
    public Record (Webcam activeWebcam, JToggleButton jtb, String path, JComboBox jcb){
        this.wc = activeWebcam;
        this.jtb = jtb;
        this.path = path;
        this.jcb = jcb;
        sound = new Audio();
    }
    
    public void parar(){
        jtb.setText("INICIAR GRABACIÓN");
        this.stop = true;
    }
    @Override
    public void run() {
        
        // Nombre del fichero a guardar en formato mp4.
        new File(path + "/video/").mkdirs();
        File file = new File(path + "/video/Video-" + System.currentTimeMillis() + ".mp4");
        IMediaWriter writer = ToolFactory.makeWriter(file.getAbsolutePath());
       
        //Resolucion y codec de video.
        Dimension size = wc.getViewSize();
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);
        
        long start = System.currentTimeMillis();
        jtb.setText("DETÉN GRABACIÓN");
        sound.playSound("cam2.wav");
        int time = (int) (System.currentTimeMillis()/1000);
        for (int i = 0; !stop && wc.isOpen(); i++) {
            if(((int)(System.currentTimeMillis()/1000)-time) % 2 == 0) jtb.setText("DETÉN GRABACIÓN");
            else jtb.setText(String.valueOf((int)(System.currentTimeMillis()/1000)-time) + " SEGUNDOS");
            
            BufferedImage image = ConverterFactory.convertToType(wc.getImage(), BufferedImage.TYPE_3BYTE_BGR);
            IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

            IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000); // < 1000 cámara rápida. > 1000 cámara lenta.
            frame.setKeyFrame(i == 0);
            frame.setQuality(100);

            writer.encodeVideo(0, frame);

            try {               
                Thread.sleep(1000/(int)jcb.getSelectedItem()); //Espera para generar los FPS dados.
            } catch (InterruptedException ex) {
                Logger.getLogger(CamVerseUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jtb.setText("INICIAR GRABACIÓN");
        jtb.setSelected(false);
        writer.close();
        sound.playSound("cam2.wav");
        JOptionPane.showMessageDialog(null,"Video grabado en: " + path + "/" + file.getName());
    }
    
}
