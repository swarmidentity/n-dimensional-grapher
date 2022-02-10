package edu.clarkson.racettl.ndimensional;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.*;
//import sample.nio.file.*; //should work, but doesn't- need JDK 7
import java.util.Scanner;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

/**
 * Submenu to load points, lines and planes from a text file.
 * @author Louis Racette
 *
 */
public class subGUIload extends JComponent{
	JFileChooser fc;
	int returnVal;
	double divisionFactor = 1;
	public subGUIload() throws IOException
	{
		fc = new JFileChooser();
    	returnVal = fc.showOpenDialog(subGUIload.this);
    	
    	if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            
			BufferedReader inputStream = null;

	        try {
	        	GraphicUI3d.myObject = new NDimensionalObject();
	        	paintMethods3d.clearScreen();
	            
	        	inputStream = new BufferedReader(new FileReader(file));
	            
	            NDimensionalObject.setNumberOfDimensions(Integer.parseInt(inputStream.readLine()));
	            
	            for(int iii = 0; iii< NDimensionalObject.NumberOfDimensions; iii++)
	            {
	            	NDimensionalObject.DimensionLabels[iii]=inputStream.readLine();
	            }
	            String read;
	            while ((read = inputStream.readLine())!= null)
	            {
	            if (read.startsWith("p:"))
	            {
	            	double[] tempLocs = new double[NDimensionalObject.NumberOfDimensions];
	            	int[] spaceIndex = new int[NDimensionalObject.NumberOfDimensions+1];
	            	
	            	int index = 0;
	            	for(int ppp = 2; ppp<read.length();ppp++)
	            	{
	            		if (read.charAt(ppp) == ",".charAt(0))
	            		{
	            			spaceIndex[index] = ppp;
	            			index++;
	            		}
	            	
	            	}
	            	spaceIndex[index] = read.length();
	            	for( int nnn = 0; nnn<NDimensionalObject.NumberOfDimensions; nnn++)
	            	{
	            		tempLocs[nnn] = Double.parseDouble(read.substring(spaceIndex[nnn]+1, spaceIndex[nnn+1]))/divisionFactor;
	            	}
	            	NDimensionalObject.addpoint(new point(tempLocs));
	            }
	            if (read.startsWith("l:"))
	            {
	            	int[] tempLocs = new int[2];
	            	int[] spaceIndex = new int[3];
	            	spaceIndex[0] = 1;
	            	int index = 1;
	            	for(int ppp = 2; ppp<read.length();ppp++)
	            	{
	            		if (read.charAt(ppp) == ",".charAt(0))
	            		{
	            			spaceIndex[index] = ppp;
	            			index++;
	            		}
	            	
	            	}
	            	
	            	for( int nnn = 0; nnn<2; nnn++)
	            	{
	            		tempLocs[nnn] = Integer.parseInt(read.substring(spaceIndex[nnn]+1, spaceIndex[nnn+1]));
	            	}
	            	NDimensionalObject.addline(new line(tempLocs[0], tempLocs[1]));
	            }
	            if (read.startsWith("t:"))
	            {
	            	int[] tempLocs = new int[3];
	            	int[] spaceIndex = new int[4];
	            	
	            	int index = 0;
	            	for(int ppp = 1; ppp<read.length();ppp++)
	            	{
	            		if (read.charAt(ppp) == ",".charAt(0))
	            		{
	            			spaceIndex[index] = ppp;
	            			index++;
	            		}
	            	
	            	}
	            	
	            	for( int nnn = 0; nnn<3; nnn++)
	            	{
	            		tempLocs[nnn] = Integer.parseInt(read.substring(spaceIndex[nnn]+1, spaceIndex[nnn+1]));
	            	}
	            	NDimensionalObject.addplane(new plane(tempLocs[0], tempLocs[1], tempLocs[2]));
	            }
	            }
	        } catch (FileNotFoundException n) {
					
					
				} catch (NumberFormatException m) {
					
					m.printStackTrace();
				} catch (IOException f) {
					
					f.printStackTrace();
				}
	            
	         finally {
	        	
	            if (inputStream != null) {
	                try {
						inputStream.close();
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
	            }
	        }
	        }
	    }
    
    
    //public void actionPerformed(ActionEvent e) {
    	
    	
	/*	
	
	final JFileChooser fc = new JFileChooser();
	
	//In response to a button click:
	//int returnVal = fc.showOpenDialog(this);
	int returnVal = fc.showOpenDialog(subGUIload.this);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
          
            try {
            	
           Path input = file.getPath();
          Files.readAllLines();
            //This is where a real application would open the file.
            //System.out.println(file.toString());
           //Files.readAllLines(file);
           
       
    }
	File file = fc.getSelectedFile();

	}*/

}
