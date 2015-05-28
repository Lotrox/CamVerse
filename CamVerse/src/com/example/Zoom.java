/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

/**
 *
 * @author acer
 */
public class Zoom {

    public static BufferedImage zoomOut(BufferedImage bi, int scale) {
        try {
            int width = bi.getWidth() / scale;
            int height = bi.getHeight() / scale;

            BufferedImage biScale = new BufferedImage(width, height, bi.getType());

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    biScale.setRGB(i, j, bi.getRGB(i * scale, j * scale));
                }
            }
            return biScale;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Zoom Error", "Unknown Image Type", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static BufferedImage zoomIn(BufferedImage bi, int scale) {
        int width = scale * bi.getWidth();
        int height = scale * bi.getHeight();

        BufferedImage biScale = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Cicla dando un valore medio al pixel corrispondente
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                biScale.setRGB(i, j, bi.getRGB(i / scale, j / scale));
            }
        }

        return biScale;
    }

    public static BufferedImage Zoom(BufferedImage img, int scale) {
        if (scale == 100) {
            return img;
        } else if (scale > 100) {
            return zoomIn(img, scale / 100);
        } else {
            return zoomOut(img, (100 - scale) / 10);
        }
    }
    
    public static BufferedImage ZoomA(BufferedImage img, float scale) {
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int w = img.getWidth();
        int h = img.getHeight();
        double x = (w - scale*w)/2;
        double y = (h - scale*h)/2;
        AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
        g2.translate(x, y);
        g2.drawRenderedImage(img, at);
        return img;
    }
    
}