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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

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
    
    // http://www.java2s.com/Tutorial/Java/0240__Swing/ASimpleModalDialog.htm
    private class AboutDialog extends JDialog implements ActionListener {
        public AboutDialog(JFrame parent, String title, String message) {
            super(parent, title, true);
            if (parent != null) {
                Dimension parentSize = parent.getSize(); 
                Point p = parent.getLocation(); 
                setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
            }
            JPanel messagePane = new JPanel();
            JTextArea jta = new JTextArea(message);
            jta.setCursor(new Cursor(Cursor.TEXT_CURSOR));
            jta.setEditable(false);
            DefaultContextMenu contextMenu = new DefaultContextMenu();
            contextMenu.add(jta);
            messagePane.add(jta);
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
    
    // http://stackoverflow.com/questions/766956/how-do-i-create-a-right-click-context-menu-in-java-swing
    private class DefaultContextMenu extends JPopupMenu
    {
        private Clipboard clipboard;
        private JMenuItem copy;
        private JMenuItem selectAll;
        private JTextComponent jTextComponent;

        public DefaultContextMenu()
        {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            copy = new JMenuItem("Copiar");
            copy.setEnabled(false);
            copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
            copy.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    jTextComponent.copy();
                }
            });

            add(copy);

            add(new JSeparator());

            selectAll = new JMenuItem("Seleccionar todo");
            selectAll.setEnabled(false);
            selectAll.setAccelerator(KeyStroke.getKeyStroke("control A"));
            selectAll.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    jTextComponent.selectAll();
                }
            });

            add(selectAll);
        }

        public void add(JTextComponent jTextComponent) {
            jTextComponent.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent releasedEvent)
                {
                    if (releasedEvent.getButton() == MouseEvent.BUTTON3)
                    {
                        processClick(releasedEvent);
                    }
                }
            });
        }

        private void processClick(MouseEvent event)
        {
            jTextComponent = (JTextComponent) event.getSource();

            boolean enableCopy = false;
            boolean enableSelectAll = false;

            String selectedText = jTextComponent.getSelectedText();
            String text = jTextComponent.getText();

            if (text != null)
            {
                if (text.length() > 0)
                {
                    enableSelectAll = true;
                }
            }

            if (selectedText != null)
            {
                if (selectedText.length() > 0)
                {
                    enableCopy = true;
                }
            }

            copy.setEnabled(enableCopy);
            selectAll.setEnabled(enableSelectAll);

            show(jTextComponent, event.getX(), event.getY());
        }
    }
}
