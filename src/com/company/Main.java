package com.company;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private static final String ROOT_PATH = "C:\\Users\\User\\Desktop\\test\\";
    private static final int SIZE = 4;
    private static final float RED_COEFFICIENT = 0.299f;
    private static final float BLUE_COEFFICIENT = 0.114f;
    private static final float GREEN_COEFFICIENT = 0.587f;

    public static void main(String[] args) {
        //convertDirectory(ROOT_PATH);
        try {
            int matrix[][] = getGrayScaleFromImage(imageFromFile(ROOT_PATH + "matrix1.jpg"));
            int pooled[][] = pooling(matrix);
            //printMatrix(pooled);
            System.out.println();
            //printMatrix(matrix);
            createImage(ROOT_PATH + "matrix1_1.jpg", imageFromMatrix(pooled));
            writeMatrixInFile(ROOT_PATH+"matr.txt", pooled);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static int[][] pooling(int[][] raw) {
        int len = raw.length;
        int width = raw[1].length;
        int[][] result = new int[len / SIZE][width / SIZE];
        for (int i = 0; i <= len - SIZE; i += SIZE)
            for (int j = 0; j <= width - SIZE; j += SIZE) {
                int max = 255;
                for (int m = i; m < i + SIZE; m++)
                    for (int n = j; n < j + SIZE; n++)
                        if (raw[m][n] < max)
                            max = raw[m][n];
                result[i / SIZE][j / SIZE] = max;
            }
        return result;
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
                grayScale[i][j] = 255 - Math.round(RED_COEFFICIENT * color.getRed() +
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

    public static BufferedImage imageFromMatrix(int[][] matrix) {
        BufferedImage image = new BufferedImage(matrix.length, matrix[1].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[1].length; j++)
                image.setRGB(i, j, (new Color(matrix[i][j], matrix[i][j], matrix[i][j])).getRGB());
        return image;
    }

    public static void createImage(String path, BufferedImage image){
        File output = new File(path);
        try {
            ImageIO.write(image, "jpg", output);
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
