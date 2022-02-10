package unused;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class animationPanel extends JPanel{
	private String pathName = "C:\\Users\\SwarmIdentity\\Documents\\PY493\\OnlineSurveySystem\\images";
	private BufferedImage useThisPic;
	private Color bgColor;
	public void loadIntroImage(Integer[] surveyNumbers) {
		
		try {
			File myFile = new File(pathName, new String("i" + surveyNumbers[0].toString() +".gif")); //+".gif"));
			useThisPic = ImageIO.read(myFile);
			//temp = new ImageIcon(myPicture, "Welcome");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//temp = null;
		}
		
	
	//return temp;
	}
	
	public void paintComponent(Graphics g)
	{
		repaint();
	}
	
	public void paint(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(useThisPic, 0, 0, this);
		
	}
}