package edu.clarkson.racettl.onlinesurvey;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

/**
 * Load an image from a remote server- in this case, an apache tomcat server.
 * @author Louis Racette
 *
 */
public class LoadImageServer implements LoadImage{
	private Toolkit imageToolkit = Toolkit.getDefaultToolkit();
	private String pathName; 
	
	/**
	 * Set the server path.
	 */
	
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public String getPathName() {
		return pathName;
	}

	/**
	 * Load a question image from the server.
	 */
	@Override
	public Image loadImageQuestion(int questionNumber, Integer[] surveyNumber, char question) {
		
			Image temp;
			
			try {
				URL readingURL = new URL("http://" + pathName + ":8080/public_html/images/" + getQuestionNumber(questionNumber, surveyNumber, Character.toString(question)));
				temp = java.awt.Toolkit.getDefaultToolkit().createImage(readingURL);
				
				return temp;
			}
	 catch (MalformedURLException e) {
		
		e.printStackTrace();
	}
		
		return null;
		
	}

	/**
	 * Load answer image from remote server.
	 */
	@Override
	public Image loadImageAnswer(int answerNumber, Integer[] surveyNumber) {
		Image temp;
		
			
			URL readingURL;
			try {
				readingURL = new URL("http://" + pathName + ":8080/public_html/images/" + getAnswerNumber(answerNumber, surveyNumber));//"http://people.clarkson.edu/~racettl/images/a00.jpg");
				temp = java.awt.Toolkit.getDefaultToolkit().createImage(readingURL);
				
				return temp;
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
			}
			
			return null;
		
	
	
	}

	/**
	 * Format the question information into an image name.
	 * @param questionNumber
	 * @param surveyNumber
	 * @param character
	 * @return image name
	 */
	private String getQuestionNumber(int questionNumber, Integer[] surveyNumber, String character) {
		
			return new String("q" + surveyNumber[0].toString() + surveyNumber[questionNumber+1].toString()+ character + ".gif");
	}
	
	/**
	 * Format the answer information into an image name.
	 * @param questionNumber
	 * @param surveyNumber
	 * @return image name
	 */
	private String getAnswerNumber(int questionNumber, Integer[] surveyNumber) {
		return new String("a" + surveyNumber[0].toString() + surveyNumber[questionNumber+1].toString()+ ".jpg");
}
	
/**
 * Load the ending image.
 */
	@Override
	public Image loadImageEnding() {
		
		Image temp;
		
		
		URL readingURL;
		try {
			readingURL = new URL("http://" + pathName + ":8080/public_html/images/ending.jpg");
			temp = java.awt.Toolkit.getDefaultToolkit().createImage(readingURL);
			
			return temp;
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/**
	 * Load the IRB informed consent image.
	 */
	@Override
	public Image loadImageIRB() {
		
		Image temp;
		
		URL readingURL;
		try {
			readingURL = new URL("http://" + pathName + ":8080/public_html/images/IRB.jpg");
			temp = java.awt.Toolkit.getDefaultToolkit().createImage(readingURL);
			
			return temp;
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/**
	 * Load the introduction image.
	 */
	public Image loadIntroImage(Integer[] surveyNumbers) {
		URL readingURL;
		try {
			readingURL = new URL("http://" + pathName + ":8080/public_html/images/"+ "i" + surveyNumbers[0].toString() +".jpg");
			java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().createImage(readingURL);
			return image;
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		
					return null;
	}
	
	/**
	 * Load the 3D example image.
	 */
	public Image loadIntroImage3D(Integer[] surveyNumbers) {
		URL readingURL;
		try {
			readingURL = new URL("http://" + pathName + ":8080/public_html/images/"+ "i3d" + surveyNumbers[0].toString() +".jpg");
			java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().createImage(readingURL);
			return image;
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		
					return null;
	}
	
	/**
	 * Load the 4D example image.
	 */
	public Image loadIntroImage4D(Integer[] surveyNumbers) {
		URL readingURL;
		try {
			readingURL = new URL("http://" + pathName + ":8080/public_html/images/"+ "i4d" + surveyNumbers[0].toString() +".jpg");
			java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().createImage(readingURL);
			return image;
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		
					return null;
	}
	
	/**
	 * Load the general introduction image.
	 */
	public Image loadImageGeneral(Integer[] surveyNumbers) {
		URL readingURL;
		try {
			readingURL = new URL("http://" + pathName + ":8080/public_html/images/"+ "general" + surveyNumbers[0].toString() + ".jpg");
			java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().createImage(readingURL);
			return image;
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		
					return null;
	}
	
	/**
	 * Load the reward image.
	 */
	public Image loadRewardImage() {
		URL readingURL;
		try {
			readingURL = new URL("http://" + pathName + ":8080/public_html/images/"+ "reward" +".jpg");
			java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().createImage(readingURL);
			return image;
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
		
					return null;
	}
	
}
