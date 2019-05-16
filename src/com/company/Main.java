package com.company;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private static final String ROOT_PATH = "D:\\Workspace\\Intellij Workspace\\JPGtoMatrix\\res\\";
    private static final float RED_COEFFICIENT = 0.299f;
    private static final float BLUE_COEFFICIENT = 0.114f;
    private static final float GREEN_COEFFICIENT = 0.587f;

    public static void main(String[] args) {
        convertDirectory(ROOT_PATH);
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

    public static BufferedImage imageFromFile(String path) throws IOException {
        File input = new File(path);
        return ImageIO.read(input);
    }

    public static BufferedImage getGrayScaleImage(BufferedImage input) {
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < input.getWidth(); i++)
            for (int j = 0; j < input.getHeight(); j++)
                output.setRGB(i, j, getGrayScale(new Color(input.getRGB(i, j))).getRGB());
        return output;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] vector : matrix) {
            for (int elem : vector)
                System.out.printf("%4d ", elem);
            System.out.println();
        }
    }

    public static void convertJPGToGrayScale(String inputPath, String outputPath) {
        File input = new File(inputPath);
        File output = new File(outputPath);
        try {
            BufferedImage in = ImageIO.read(input);
            if (ImageIO.write(getGrayScaleImage(in), "jpg", output))
                System.out.println("Successfully converted " + input.getName() + " to the " + output.getName());
            else
                System.out.println("Error converting " + inputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isJPG(String name) {
        return (name.contains(".jpg") || name.contains(".jpeg"));
    }

    public static void convertDirectory(String path) {
        System.out.println("Start converting " + path);
        File directory = new File(path);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files)
                if (file.isFile() && isJPG(file.getName())) {
                    String name = file.getName().substring(0, file.getName().indexOf("."));
                    convertJPGToGrayScale(path + name + ".jpg", path + "gs_" + name + ".jpg");
                    try {
                        BufferedImage image = imageFromFile(path + name + ".jpg");
                        writeMatrixInFile(path + name + ".txt", getGrayScaleFromImage(image));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        } else
            System.out.println("Path is not directory");
    }

    public static void writeMatrixInFile(String path, int[][] matrix) {
        try (FileWriter writer = new FileWriter(path, false)) {
            for (int[] vector : matrix) {
                for (int x : vector) {
                    writer.write(String.valueOf(x));
                    writer.write(" ");
                }
                writer.write("\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
