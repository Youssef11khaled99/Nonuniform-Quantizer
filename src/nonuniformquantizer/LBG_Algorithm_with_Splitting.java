/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nonuniformquantizer;

import java.io.File;
import java.util.Vector;
import java.lang.Math;
import java.util.Collections;
import java.util.Formatter;
import java.util.Scanner;
/**
 *
 * @author Youssef Khaled
 */
public class LBG_Algorithm_with_Splitting {

    private final int numOfBits;
    
    LBG_Algorithm_with_Splitting(int numOfBits)
    {
        this.numOfBits = numOfBits;
    }
    Vector outter = new Vector();
    Vector <Integer> allAvgs = new Vector<Integer>();
    Vector <Integer> finalAvg = new Vector<Integer>();
    Vector <Integer> finalRange = new Vector<Integer>();
    
    public int getFirstAvg(int [][]matrix)
    {
        int rows = matrix.length;
        int cols = matrix[0].length ;
        int total = 0;
        for (int i = 0; i < rows;i++)
        {
            for (int j = 0; j < cols; j++)
            {
                total += matrix[i][j];
            }
        }
        return (total/(rows*cols))+1;
    }
    
    public int getStandardAvg (Vector <Integer> list)
    {
        int total = 0;
        for (int i = 0; i < list.size(); i++)
        {
            total += list.get(i);
        }
        if(total!=0)return total/list.size();
        else return 0;
    }
    
    public void splitFirstMatrix (int [][]matrix)
    {
        int rows = matrix.length;
        int cols = matrix[0].length ;
        int avg = getFirstAvg(matrix);
        allAvgs.add(avg);
        Vector <Integer> rightVector = new Vector();
        Vector <Integer> leftVector = new Vector();
        for (int i = 0; i < rows;i++)
        {
            for (int j = 0; j < cols; j++)
            {
                if (matrix[i][j] >= avg-1)
                {
                    rightVector.add(matrix[i][j]);
                }
                else
                {
                    leftVector.add(matrix[i][j]);
                }
            }
        }
        if (rightVector.size() != 0)
        outter.add(rightVector);
        if (leftVector.size() != 0)
        outter.add(leftVector);
        
    }
    
    public void standardSplit (Vector <Integer> list)
    {
        int avg = getStandardAvg(list);
        allAvgs.add(avg);
        int right = 0;
        if (avg != 0)
        {
            right = avg - 1;
        }
        Vector <Integer> rightVector = new Vector();
        Vector <Integer> leftVector = new Vector();
        for (int i = 0; i < list.size();i++)
        {
            if (list.get(i) <= right)
            {
                rightVector.add(list.get(i));
            }
            else
            {
                leftVector.add(list.get(i));
            }
            
        }
        if (rightVector.size() != 0)
        outter.add(rightVector);
        if (leftVector.size() != 0)
        outter.add(leftVector);
    }
    
    public void storeFinalAvgs()
    {
        for (int i = 0; i < outter.size(); i++)
        {
            Vector tmp = new Vector();
            tmp = (Vector) outter.get(i);
            int avg = getStandardAvg (tmp);
            finalAvg.add(avg);
        }       
        Collections.sort(finalAvg);
        System.out.println("FinalAvg: ");
        for (int i = 0; i < finalAvg.size(); i++)
        {
            System.out.println(finalAvg.get(i));
        }
        System.out.println("********************************************");
    }
    
    public int getMax(int [][]matrix)
    {
        int rows = matrix.length;
        int cols = matrix[0].length ;
        int max = 0;
        for (int i = 0; i < rows;i++)
        {
            for (int j = 0; j < cols; j++)
            {
                if (matrix[i][j] > max)
                {
                    max = matrix[i][j];
                }
            }
        }
        return max;
    }
    public int getMaxVec (Vector <Integer> list)
    {
        int max = 0;
         for (int j = 0; j < list.size(); j++)
            {
                if (list.get(j) > max)
                {
                    max = list.get(j);
                }
            }
         return max;
    }
    
    public void buildRange (int [][]matrix)
    {
        for (int i = 0; i < finalAvg.size()-1; i++)
        {
            int range = (finalAvg.get(i)+finalAvg.get(i+1))/2;
            finalRange.add(range);
        }
        int max = getMax(matrix) + 1;
        finalRange.add(max);
        Collections.sort(finalRange);
        System.out.println("Ranges: ");
        for (int i = 0; i < finalRange.size(); i++)
        {
            System.out.println(finalRange.get(i));
        }
    }
    
    public int findRange(int number)
    {
        for (int i = 0; i < finalRange.size(); i++)
        {
            if (number == 0)
            {
                return finalAvg.get(0);
            }
            if (number < finalRange.get(i))
            {
                return finalAvg.get(i);
            }
            
        }
        return 0;
    }
    
    public int compressionMatrix (int number)
    {
        for (int i = 0; i < finalRange.size(); i++)
        {
            if (number < finalRange.get(i))
            {
                return i;
            }
        }
        return 0;
    }
    
    public void bulidCompressedMatrix (int [][]oldMatrix, int [][]newMatrix)
    {
        int rows = oldMatrix.length;
        int cols = oldMatrix[0].length ;
        for (int i = 0; i < rows;i++)
        {
            for (int j = 0; j < cols; j++)
            {
                newMatrix[i][j]=0;
            }
        }
         for (int i = 0; i < rows;i++)
        {
            for (int j = 0; j < cols; j++)
            {
                newMatrix[i][j]=compressionMatrix(oldMatrix[i][j]);
            }
        }
    }
    
    public void bulidDecompressedMatrix(int [][]oldMatrix, int [][]newMatrix)
    {
        int rows = oldMatrix.length;
        int cols = oldMatrix[0].length ;
        for (int i = 0; i < rows;i++)
        {
            for (int j = 0; j < cols; j++)
            {
                newMatrix[i][j]=0;
            }
        }
         for (int i = 0; i < rows;i++)
        {
            for (int j = 0; j < cols; j++)
            {
//                if (findRange(oldMatrix[i][j]) == 0)
//                {
//                    System.out.println("oldMatrix[i][j])"+ oldMatrix[i][j]);
//                    newMatrix[i][j]=finalAvg.get(0);
//                }
//                else
//                {
                    newMatrix[i][j]=findRange(oldMatrix[i][j]);
//                }
            }
        }
        
    }
    
    // Files Functions
     Scanner sc;
    public void open_file(String FileName) {
        try {
            sc = new Scanner(new File(FileName));
        } catch (Exception e) {
            System.out.println("Can not find File ");
        }
    }

    public void close_file() {
        sc.close();
    }
    
      Formatter out; //34an yktb el tag be format el string

    public void openfile(String pass) {
        try {
            out = new Formatter(pass);
        } catch (Exception e) {
            System.out.println("Can not find File ");
        }

    }

    public void closefile() {
        out.close();
    }
    
    void write(String code) {

        out.format("%s", code);
        out.format("%n");
        out.flush(); // 34an yktb 3al file

    }
    
    void Save (int [][] matrix , String path )
    {
        openfile(path);
         
        int rows = matrix.length;
        int cols = matrix[0].length ;
        
        String matrix_dimension = ""+rows+" "+cols;
       
        write(matrix_dimension);
        
        for (int i=0 ; i<rows ; i++)
        {  
            String Row = "";
            
            for (int j=0 ; j<cols ; j++)
            {
                int val = matrix[i][j];
                Row += val + " ";
                
            }
            
            write(Row);
        }
        
        closefile();
        
    }
    
    int [][] Read_Image_From_File () // read comp mn l file
    {
        String matrix_dimension = sc.nextLine() ;
        String [] dimensions = matrix_dimension.split(" ");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);
        int [][] matrix = new int [rows][cols];
        
        for (int i=0 ; i<rows ; i++)
        {  
            String Row = sc.nextLine();
            String [] elements = Row.split(" ");
            
            for (int j=0 ; j<cols ; j++)
            {
                matrix[i][j] = Integer.parseInt(elements[j]);
            }
            
        }
        
        return matrix ;
    }
    
    void original_img (int img[][])
    {
        openfile("Original file.txt");
        int rows = img.length ;
        int cols = img[0].length ;
        
        for (int i=0 ; i<rows ; i++)
        {  
            String Row = "";
            
            for (int j=0 ; j<cols ; j++)
            {
                int val = img[i][j];
                Row += val + " ";
                
            }
            
            write(Row);
        }
        
        closefile();
    }
   
}