package edu.clarkson.racettl.ndimensional;

import javax.imageio.ImageIO;

import javax.swing.JComponent;
import javax.swing.JFileChooser;


import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Submenu to save all current points, lines and planes to a text file.
 * @author Louis Racette
 *
 */
public class subGUIsave extends JComponent{
/*
 * Conventions to use:
 * start w/ numDimensions
 * DimensionLabels
 * p: 1st, 2nd, 3rd, ...
 * l: 1st p, 2nd p
 * t: 1st p, 2nd p, 3rd p
 */
	static JFileChooser fc;
	static int returnVal;
	    public subGUIsave() throws IOException
	    {
	    }
	    public void saveText()
	    {
	    	fc = new JFileChooser();
	    	returnVal = fc.showSaveDialog(subGUIsave.this);
	    	
	    	if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                	file =new File(file.toString() +".txt");
					file.createNewFile();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
	        
                PrintWriter outputStream = null;

	        try {
	            
	        	outputStream = new PrintWriter(file);
	            
	            outputStream.println(NDimensionalObject.NumberOfDimensions);
	            for(int iii = 0; iii< NDimensionalObject.NumberOfDimensions; iii++)
	            {
	            	outputStream.println(NDimensionalObject.DimensionLabels[iii]);
	            }
	            for(int ppp = 0; ppp<NDimensionalObject.actualPoints.length; ppp++)
	            {
	            	outputStream.write("p:");
	            	for(int iii = 0; iii< NDimensionalObject.NumberOfDimensions; iii++)
		            {
		            	outputStream.write(","+NDimensionalObject.actualPoints[ppp].location[iii]);
		            }
	            	outputStream.println();
	            }
	            for(int ppp = 0; ppp<NDimensionalObject.myLines.length; ppp++)
	            {
	            	outputStream.write("l:");
	            	outputStream.write(NDimensionalObject.myLines[ppp].endpoint1index +",");
	            	outputStream.write(NDimensionalObject.myLines[ppp].endpoint2index + ",");
	            	outputStream.println();
	            }
	            for(int ppp = 0; ppp<NDimensionalObject.myPlanes.length; ppp++)
	            {
	            	outputStream.write("t:");
	            	outputStream.write(NDimensionalObject.myPlanes[ppp].pointsIndex[0] +",");
	            	outputStream.write(NDimensionalObject.myPlanes[ppp].pointsIndex[1] +",");
	            	outputStream.write(NDimensionalObject.myPlanes[ppp].pointsIndex[2] + ",");
	            	outputStream.println();
	            }
	            
	        }
	        catch(FileNotFoundException g)
	        {
	        	System.out.println("No such file");
	        }
	        finally {
	            if (outputStream != null) {
	                outputStream.close();
	                
	            }
	        }
	    }
	    }
	    
	   /**
	    * Save an image to a designated location.
	    */
	    public void saveImage(){
	    		fc = new JFileChooser();
		    	returnVal = fc.showSaveDialog(subGUIsave.this);
		    	
		    	if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                try {
	                	file =new File(file.toString() +".jpg");
	                	file.createNewFile();
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					try {
	    		FileOutputStream fileOut = new FileOutputStream(file);
	    		
	    			
	    			BufferedImage onimage = new Robot().createScreenCapture( new Rectangle( paintMethods3d.canvas.getLocation().x+GraphicUI3d.newframe.getX()+7,paintMethods3d.canvas.getLocation().y+GraphicUI3d.newframe.getY()+52,paintMethods3d.canvas.getWidth(),paintMethods3d.canvas.getHeight()));
	    			ImageIO.write(onimage, "jpg", fileOut);
	    			fileOut.flush();
	    			fileOut.close();
	    		} catch (FileNotFoundException e) {
	    			
	    			e.printStackTrace();
	    		} catch (AWTException e) {
	    			
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			
	    			e.printStackTrace();
	    		
	    		    	}
	    }
	   
	}
	    
	    /**
	     * Save an animation.
	     */
	    public void saveAnimation(){
    		fc = new JFileChooser();
	    	returnVal = fc.showSaveDialog(subGUIsave.this);
	    	
	    	if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                	file =new File(file.toString() +".gif");
                	file.createNewFile();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
    		paintMethods3d.saveAnimation(file.toString());
    		
    		
    		    	}
    
   
}
}

