package edu.clarkson.racettl.onlinesurvey;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Interface for loading images from a source.
 * @author Louis Racette
 *
 */
public interface LoadImage {

	/**
	 * Load introduction image.
	 * @param surveyNumber
	 * @return
	 */
	public Image loadIntroImage(Integer[] surveyNumber);
	/**
	 * Load question images.
	 * @param questionNumber
	 * @param localGeneratedNumber
	 * @param question
	 * @return question image
	 */
	public Image loadImageQuestion(int questionNumber, Integer[] localGeneratedNumber, char question);
	
	/**
	 * Load answer image.
	 * @param answerNumber
	 * @param localGeneratedNumber
	 * @return answer image
	 */
	public Image loadImageAnswer(int answerNumber, Integer[] localGeneratedNumber);
	
	/**
	 * Load ending image.
	 * @return ending image
	 */
	public Image loadImageEnding();
	
	/**
	 * Load IRB informed consent image.
	 * @return IRB image
	 */
	public Image loadImageIRB();
	
	/**
	 * Set the path to the server.
	 * @param pathName
	 */
	public void setPathName(String pathName);
	
	/**
	 * Load the general introduction image.
	 * @param localGeneratedNumber
	 * @return general introduction
	 */
	public Image loadImageGeneral(Integer[] localGeneratedNumber);
	
	/**
	 * Load the 3D example image.
	 * @param localGeneratedNumber
	 * @return
	 */
	public Image loadIntroImage3D(Integer[] localGeneratedNumber);
	
	/**
	 * Load the 4D example image.
	 * @param localGeneratedNumber
	 * @return
	 */
	public Image loadIntroImage4D(Integer[] localGeneratedNumber);
	
	/**
	 * Load the image with reward details (displayed above the participant info gathering section).
	 * @return
	 */
	public Image loadRewardImage();
}
