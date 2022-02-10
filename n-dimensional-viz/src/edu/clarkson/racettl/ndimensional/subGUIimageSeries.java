package edu.clarkson.racettl.ndimensional;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

import java.awt.AWTException;
	import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Robot;
	//import java.awt.Graphics.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Submenu for saving a series of images.
 * @author Louis Racette
 *
 */

public class subGUIimageSeries extends JPanel implements ActionListener{
		protected JFrame rotateFrame = new JFrame("Image Series");
		protected JButton closeButton = new JButton("close");
		protected JTextArea xChange = new JTextArea(Double.toString(4.0));
		protected JTextArea yChange = new JTextArea(Double.toString(0.0));
		protected JTextArea zChange = new JTextArea(Double.toString(0.0));
		protected JTextArea numberImages = new JTextArea(Integer.toString(6));
		protected File file;
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Draw the submenu.
		 */
		public subGUIimageSeries()
		{
			
		
			
			JPanel sliderPanel = new JPanel();
			sliderPanel.add(numberImages, BorderLayout.NORTH);
			sliderPanel.add(xChange,BorderLayout.NORTH);
			sliderPanel.add(yChange,BorderLayout.CENTER);
			sliderPanel.add(zChange,BorderLayout.SOUTH);
			
			rotateFrame.add(sliderPanel,BorderLayout.CENTER);
			rotateFrame.add(closeButton, BorderLayout.SOUTH);
			closeButton.addActionListener(this);
			
			closeButton.setActionCommand("close");
			rotateFrame.pack();
			rotateFrame.setVisible(true);
		}
		
		/**
		 * Monitor the submenu for changes.
		 */
		public void actionPerformed(ActionEvent e)
		{

			if ("close".equals(e.getActionCommand()))
			{
				double xCameraChange;
				double yCameraChange;
				double zCameraChange;
				Integer numberOfImages;
				try
				{
					xCameraChange = Double.parseDouble(xChange.getText());
					yCameraChange = Double.parseDouble(yChange.getText());
					zCameraChange = Double.parseDouble(zChange.getText());
					numberOfImages  = Integer.parseInt(numberImages.getText());
				}
				catch(NumberFormatException f)
				{
					xCameraChange = 4.0;
					yCameraChange = 0.0;
					zCameraChange = 0.0;
					numberOfImages = 6;
				}
				
				for(int iii=0; iii< numberOfImages; iii++)
				{
					
					try {
		                	file =new File((System.currentTimeMillis()) +".jpg");
		                	file.createNewFile();
						} catch (IOException f) {
							
							f.printStackTrace();
						}
						try {
		    		FileOutputStream fileOut = new FileOutputStream(file);
		    			
		    			BufferedImage onimage = new Robot().createScreenCapture( new Rectangle( paintMethods3d.canvas.getLocation().x+GraphicUI3d.newframe.getX()+7,paintMethods3d.canvas.getLocation().y+GraphicUI3d.newframe.getY()+52,paintMethods3d.canvas.getWidth(),paintMethods3d.canvas.getHeight()));
		    			ImageIO.write(onimage, "jpg", fileOut);
		    			
		    			onimage.flush();
		    			fileOut.flush();
		    			fileOut.close();
		    		} catch (FileNotFoundException e1) {
		    			
		    			e1.printStackTrace();
		    		} catch (AWTException e2) {
		    			
		    			e2.printStackTrace();
		    		} catch (IOException e3) {
		    			
		    			e3.printStackTrace();
				}
		    		paintMethods3d.setCamera(paintMethods3d.cameraPosition.x + xCameraChange, paintMethods3d.cameraPosition.y + yCameraChange, paintMethods3d.cameraPosition.z + zCameraChange);
	    			paintMethods3d.paint3Dobject(); 
		    		
			}
			
			
		}

				
				rotateFrame.dispose();
			}
		}





