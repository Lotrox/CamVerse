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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
    
    /**
     * <b>Traza de ejecución cada 100 milisegundos.</b>
     */
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
                        // QR no era una imagen.
                }
            }

            if (result != null) {
                new AboutDialog(instance, "Código detectado", result.getText());
            }
        }
    }
    private class AboutDialog extends JDialog implements ActionListener {
        public AboutDialog(JFrame parent, String title, String message) {
            super(parent, title, true);
            if (parent != null) {
                Dimension parentSize = parent.getSize(); 
                Point p = parent.getLocation(); 
                setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
            }
            JPanel messagePane = new JPanel();
            messagePane.add(new JLabel(message));
            getContentPane().add(messagePane);
            JPanel buttonPane = new JPanel();
            JButton button = new JButton("OK"); 
            buttonPane.add(button); 
            button.addActionListener(this);
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            pack(); 
            setVisible(true);
        }
        public void actionPerformed(ActionEvent e) {
          setVisible(false); 
          dispose();
        }
    }
}
