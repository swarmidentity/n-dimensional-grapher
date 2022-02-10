package edu.clarkson.racettl.onlinesurvey;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class is used to draw .gif objects (animations).
 * @author Louis Racette
 *
 */
public class ImagePanel extends JPanel 
{

  Image image;
  Image image2;

  /**
   * Constructor for image panel.
   * @param image1input
   * @param image2input
   */
  public ImagePanel(Image image1input, Image image2input) 
  {
	  
	  image = image1input;
	  image2 = image2input;
  		}

  /**
   * Draw the image components separated by a 25 pixel space.
   */
  @Override
  public void paintComponent(Graphics g) 
  {
    super.paintComponent(g);
    if (image != null) 
    {
    	
      g.drawImage(image, 0, 0, this);
      
    }
    if (image2 != null)
    {
    	g.drawImage(image2, image.getWidth(this)+25, 0, this);
    }
  }

}
