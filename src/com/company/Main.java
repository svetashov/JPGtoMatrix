package com.company;

import sun.plugin.dom.css.RGBColor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    private static final String ROOT_PATH = "J:\\Workspace\\IntellijIDEA Workspace\\JPGtoMatrix\\res\\";
    private static final float RED_COEFFICIENT = 0.299f;
    private static final float BLUE_COEFFICIENT = 0.114f;
    private static final float GREEN_COEFFICIENT = 0.587f;

    public static void main(String[] args) {
        convertToBMP(ROOT_PATH + "test.jpg", ROOT_PATH + "test.bmp");
    }

    public static void convertToBMP(String pathJPG, String pathBMP) {
        File input = new File(pathJPG);
        try {
            BufferedImage image = ImageIO.read(input);
            int[][] grayScale = getGrayScaleFromImage(image);
            File output = new File(pathBMP);
            BufferedImage outImage;

            ImageIO.write(image, "bmp", output);
        } catch (IOException e) {
            System.err.println("Error, cannot read to the buffer");
        }

    }

    public static Color getGrayScale(Color input) {
        int gray = Math.round(RED_COEFFICIENT * input.getRed() + BLUE_COEFFICIENT * input.getBlue() + GREEN_COEFFICIENT * input.getGreen());
        return new Color(gray, gray, gray);
    }


    public static int[][] getGrayScaleFromImage(BufferedImage image) {
        int h = image.getHeight();
        int w = image.getWidth();
        int[][] grayScale = new int[w][h];
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++) {
                Color color = new Color(image.getRGB(i, j));
                grayScale[i][j] = Math.round(RED_COEFFICIENT * color.getRed() +
                        BLUE_COEFFICIENT * color.getBlue() +
                        GREEN_COEFFICIENT * color.getGreen());
            }
        return grayScale;
    }

    public static BufferedImage getGrayScaleImage(BufferedImage input) {
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < input.getWidth(); i++)
            for (int j = 0; j < input.getHeight(); j++)
                output.setRGB(i, j, getGrayScale(new Color(input.getRGB(i,j))).getRGB());
        return output;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] vector : matrix)
            for (int elem : vector)
                System.out.println(elem);
    }
}
