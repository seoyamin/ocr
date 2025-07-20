package com.haru.cpn.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

public class ImagePreprocessor {

    public static File preprocessImage(File originalImageFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(originalImageFile);

        BufferedImage grayImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = grayImage.createGraphics();
        g.drawImage(originalImage, 0, 0, null);
        g.dispose();

        RescaleOp rescaleOp = new RescaleOp(1.5f, 0, null);
        rescaleOp.filter(grayImage, grayImage);

        File output = File.createTempFile("preprocessed-", ".png");
        ImageIO.write(grayImage, "png", output);
        return output;
    }
}
