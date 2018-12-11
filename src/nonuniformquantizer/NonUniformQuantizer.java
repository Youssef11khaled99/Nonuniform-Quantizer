/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nonuniformquantizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import javax.imageio.ImageIO;


/**
 *
 * @author Youssef Khaled
 */
public class NonUniformQuantizer {

    public static int[][] readImage(String filePath)
    {
        int width=0;
        int height=0;
        File file=new File(filePath);
        BufferedImage image=null;
        try
        {
            image=ImageIO.read(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

          width=image.getWidth();
          height=image.getHeight();
        int[][] pixels=new int[height][width];

        for(int x=0;x<width;x++)
        {
            for(int y=0;y<height;y++)
            {
                int rgb=image.getRGB(x, y);
                int alpha=(rgb >> 24) & 0xff;
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = (rgb >> 0) & 0xff;

                pixels[y][x]=r;
            }
        }

        return pixels;
    }
    
    public static void writeImage(int[][] pixels,String outputFilePath,int width,int height)
    {
        File fileout=new File(outputFilePath);
        BufferedImage image2=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB );

        for(int x=0;x<width ;x++)
        {
            for(int y=0;y<height;y++)
            {
                image2.setRGB(x,y,(pixels[y][x]<<16)|(pixels[y][x]<<8)|(pixels[y][x]));
            }
        }
        try
        {
            ImageIO.write(image2, "jpg", fileout);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    
    
    public static void main(String[] args) 
    {
        System.out.print("Please enter the number of bits: ");
        Scanner n = new Scanner (System.in);
        int numOfBits = n.nextInt();
        
        System.out.print("Please enter the name of the photo: ");
        Scanner w = new Scanner (System.in);
        String name = w.nextLine();
        
        LBG_Algorithm_with_Splitting obj = new LBG_Algorithm_with_Splitting(numOfBits);
        
        int [][]matrix =  readImage("Original_Photo.jpg");
        obj.original_img(matrix);
 
        obj.splitFirstMatrix(matrix);
        while( obj.outter.size() < Math.pow(2, numOfBits))
        {
            Vector tmp = new Vector();
            tmp = (Vector) obj.outter.get(0);
            obj.outter.remove(0);
            obj.standardSplit(tmp);
        }
        obj.storeFinalAvgs();
        obj.buildRange(matrix);
        
        int [][]compressedMatrix = new int[matrix.length][matrix[0].length];
        // I use this matrix if I didn't read from file
        int [][]decompressedMatrix = new int[matrix.length][matrix[0].length];
       
        obj.bulidCompressedMatrix(matrix, compressedMatrix);
        obj.Save (compressedMatrix , "Compress.txt");
        
        // I use this if I read from file
        //obj.open_file("Compress.txt");
        //int [][] img = obj.Read_Image_From_File();
        //obj.close_file();
        //obj.bulidDecompressedMatrix(matrix, img);
        //obj.Save (img , "De-Compress.txt");
        
        // I use this matrix if I didn't read from file
        obj.bulidDecompressedMatrix(matrix, decompressedMatrix);
        obj.Save (decompressedMatrix , "De-Compress.txt");
        
        
        
        
        writeImage(decompressedMatrix, name+".jpg", matrix[0].length , matrix.length);
       
         System.out.println("Done!!");
    }
    

    
}
