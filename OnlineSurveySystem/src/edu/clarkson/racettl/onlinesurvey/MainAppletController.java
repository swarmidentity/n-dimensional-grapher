package edu.clarkson.racettl.onlinesurvey;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

/**
 * This class is the main applet code for the online survey system- it initializes and runs the survey.
 * This code is meant to be accessed via a web browser as a java applet.
 * @author Louis Racette
 *
 */
public class MainAppletController extends JApplet implements ActionListener{

	private Integer questionIndex = 0;
	private Integer numberOfQuestions = 6;
	private Integer firstAnswer = 0;
	private Integer secondAnswer = 3;
	static Container localContentPane;
	JButton finalButtonAnswer;
	JTextArea studentIDfield = new JTextArea("");
	JTextArea studentNamefield = new JTextArea("");
	JTextArea professorNameField = new JTextArea("");
	LoadImage imageLoader = new LoadImageServer();
	private Integer currentQuestion = 0;
	float[] responseTimes = new float[numberOfQuestions];
	String[] responses = new String[numberOfQuestions];
	String participantName = new String();
	String professorName  = new String();
	String gender = new String();
	Integer studentID = -1;
	String temporaryAnswer = new String();
	String temporaryGender = new String();
	Integer[] localGeneratedNumber;
	long startingSystemTime = 0;	
	//for animation
	Image currentImage;
	Font arialFont = new Font("Arial", Font.PLAIN, 16);

SurveyData thisSessionData;
UsageData totalUseData;
Integer sessionNumber;
private String pathName = "/afs/ad.clarkson.edu/users/r/a/racettl/public_html";
private String pathSeparator = "/";
//this working IP address will change based on the location of the server computer
private String workingIP = "128.153.17.112";

private Boolean alreadyDestroyed = false;

/**
 * Initialize the survey.
 */
	public void init()
	{
		setSize(1000,600);
		//point the image loader to the correct server address
		imageLoader.setPathName(workingIP);
		localContentPane = getContentPane();
		RunFullSurvey();
		
	}

	/**
	 * Destroy the survey- called when the survey is closed. Use to save participant data (only once).
	 */
	public void destroy()
	{
		//save the usage data- make sure that the survey has not already been destroyed.
		if((alreadyDestroyed == false) && !(localGeneratedNumber == null))
		{
		System.out.println("Survey Destroyed.");
		//save the user data
		thisSessionData = new SurveyData(responseTimes, localGeneratedNumber, responses, participantName, professorName, gender, studentID);
		SaveSessionDataServer saveSessionData = new SaveSessionDataServer();
		saveSessionData.setPathName(workingIP);
		saveSessionData.saveSessionData(thisSessionData);
		alreadyDestroyed = true;
		}
		
		
	}
	
	/**
	 * Run the full survey-starting with the IRB slide.
	 */
	public void RunFullSurvey() {

		displayIRBintro();
		
	}
	
	/**
	 * Display the answer to question i. All answers are stored as images with text.
	 * @param i
	 */
	
	private void displayAnswer(int i) {
		localContentPane.removeAll();
		JPanel loadAnAnswer = new JPanel();
		//load the answer image
		Image answer =imageLoader.loadImageAnswer(i, localGeneratedNumber);
		Image question1 = imageLoader.loadImageQuestion(i, localGeneratedNumber, 'a');
		Image question2 = imageLoader.loadImageQuestion(i, localGeneratedNumber, 'b');
		ImageIcon answerIcon = new ImageIcon(answer);
		JLabel answerLabel = new JLabel(answerIcon);
		answerLabel.setSize(500, 500);
		localContentPane.add(new ImagePanel(question1, question2),BorderLayout.CENTER);
		localContentPane.add(answerLabel,BorderLayout.NORTH);
		JButton nextButtonAnswer = new JButton("Next");
		nextButtonAnswer.setFont(arialFont);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(nextButtonAnswer);
		nextButtonAnswer.addActionListener(this);
		nextButtonAnswer.setActionCommand("nextAnswer");
		localContentPane.add(nextButtonAnswer,BorderLayout.SOUTH);
		localContentPane.validate();
	}
	
	/**
	 * Display the last slide shown to the participant.
	 */
	private void displayEnding() {
		localContentPane.removeAll();
		//load the ending image
		Image ending =imageLoader.loadImageEnding();
		ImageIcon endingIcon = new ImageIcon(ending);
		JLabel answerLabel = new JLabel(endingIcon);
		answerLabel.setSize(500, 500);
		localContentPane.add(answerLabel,BorderLayout.NORTH);
		finalButtonAnswer = new JButton("Save Survey Data");
		finalButtonAnswer.setFont(arialFont);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(finalButtonAnswer);
		finalButtonAnswer.addActionListener(this);
		finalButtonAnswer.setActionCommand("nextEnding");
		localContentPane.add(finalButtonAnswer,BorderLayout.SOUTH);
		localContentPane.validate();
		
	}
	
	/**
	 * Display the general introduction (immediately after the IRB slide).
	 */
	private void displayGeneralIntro() {
		localContentPane.removeAll();
		Image irb =imageLoader.loadImageGeneral(localGeneratedNumber);
		ImageIcon endingIcon = new ImageIcon(irb);
		JLabel answerLabel = new JLabel(endingIcon);
		answerLabel.setSize(500, 500);
		localContentPane.add(answerLabel,BorderLayout.NORTH);
		JButton nextButtonAnswer = new JButton("Next");
		nextButtonAnswer.setFont(arialFont);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(nextButtonAnswer);
		nextButtonAnswer.addActionListener(this);
		nextButtonAnswer.setActionCommand("nextGeneral");
		localContentPane.add(nextButtonAnswer,BorderLayout.SOUTH);
		localContentPane.validate();
		
	}
	
	/**
	 * Display a 3-dimensional example set (shown as an image).
	 */
	private void display3DExample() {
		localContentPane.removeAll();
		Image irb = imageLoader.loadIntroImage3D(localGeneratedNumber);
		ImageIcon endingIcon = new ImageIcon(irb);
		JLabel answerLabel = new JLabel(endingIcon);
		answerLabel.setSize(500, 500);
		localContentPane.add(answerLabel,BorderLayout.NORTH);
		JButton nextButton3d = new JButton("Next");
		nextButton3d.setFont(arialFont);
		JButton backButton3d = new JButton("Back");
		backButton3d.setFont(arialFont);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(backButton3d);
		buttonPanel.add(nextButton3d);
		nextButton3d.addActionListener(this);
		nextButton3d.setActionCommand("next3D");
		backButton3d.addActionListener(this);
		backButton3d.setActionCommand("back3D");
		localContentPane.add(buttonPanel,BorderLayout.SOUTH);
		localContentPane.validate();
		
	}
	
	/**
	 * Display a four-dimensional example image.
	 */
	private void display4DExample() {
		localContentPane.removeAll();
		Image irb = imageLoader.loadIntroImage4D(localGeneratedNumber);
		ImageIcon endingIcon = new ImageIcon(irb);
		JLabel answerLabel = new JLabel(endingIcon);
		answerLabel.setSize(500, 500);
		localContentPane.add(answerLabel,BorderLayout.NORTH);
		JButton nextButton4D = new JButton("Next");
		nextButton4D.setFont(arialFont);
		JButton backButton4D = new JButton("Back");
		backButton4D.setFont(arialFont);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(backButton4D);
		buttonPanel.add(nextButton4D);
		nextButton4D.addActionListener(this);
		nextButton4D.setActionCommand("next4D");
		backButton4D.addActionListener(this);
		backButton4D.setActionCommand("back4D");
		localContentPane.add(buttonPanel,BorderLayout.SOUTH);
		localContentPane.validate();
		
	}
	
	/**
	 * Display an introduction slide with IRB informed consent information.
	 */
	private void displayIRBintro() {
		localContentPane.removeAll();
		Image irb =imageLoader.loadImageIRB();
		ImageIcon endingIcon = new ImageIcon(irb);
		JLabel answerLabel = new JLabel(endingIcon);
		answerLabel.setSize(500, 500);
		localContentPane.add(answerLabel,BorderLayout.NORTH);
		JButton nextButtonAnswer = new JButton("I have read and accepted this agreement");
		nextButtonAnswer.setFont(arialFont);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(nextButtonAnswer);
		nextButtonAnswer.addActionListener(this);
		nextButtonAnswer.setActionCommand("nextIRB");
		localContentPane.add(nextButtonAnswer,BorderLayout.SOUTH);
		localContentPane.validate();
		
	}
	
	/**
	 * Display question i.
	 * @param i
	 */
	private void displayQuestion(int i) {
		questionIndex++;
		JTextArea questionNumber = new JTextArea("Question " + Integer.toString(questionIndex) + ":");
		questionNumber.setEditable(false);
		questionNumber.setFont(arialFont);
		startingSystemTime = System.currentTimeMillis();
		localContentPane.removeAll();
		Image question1 = imageLoader.loadImageQuestion(i, localGeneratedNumber, 'a');
		Image question2 = imageLoader.loadImageQuestion(i, localGeneratedNumber, 'b');
		JPanel imagePanel = new JPanel();
		JTextArea textQuestion = new JTextArea("Question " + Integer.toString(questionIndex) + ": " + "Do these figures represent the same object?");
		textQuestion.setEditable(false);
		textQuestion.setFont(arialFont);
		JPanel radioButtonPanel = new JPanel();
		ButtonGroup radioButtonGroup = new ButtonGroup();
		JRadioButton yes = new JRadioButton("Yes");
		yes.setFont(arialFont);
		JRadioButton no = new JRadioButton("No");
		no.setFont(arialFont);
		JRadioButton maybe = new JRadioButton("Cannot Tell");
		maybe.setFont(arialFont);
		radioButtonGroup.add(yes);
		radioButtonGroup.add(no);
		radioButtonGroup.add(maybe);
		radioButtonPanel.add(yes, BorderLayout.WEST);
		radioButtonPanel.add(no, BorderLayout.AFTER_LAST_LINE);
		radioButtonPanel.add(maybe, BorderLayout.AFTER_LAST_LINE);
		yes.addActionListener(this);
		yes.setActionCommand("yes");
		no.addActionListener(this);
		no.setActionCommand("no");
		maybe.addActionListener(this);
		maybe.setActionCommand("maybe");
		JPanel topPanel = new JPanel();
		topPanel.add(imagePanel, BorderLayout.NORTH);
		topPanel.add(textQuestion, BorderLayout.CENTER);
		topPanel.add(radioButtonPanel, BorderLayout.SOUTH);
		JButton nextButtonQuestion = new JButton("Next");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(nextButtonQuestion);
		localContentPane.add(new ImagePanel(question1, question2));
		topPanel.add(buttonPanel, BorderLayout.SOUTH);
		localContentPane.add(topPanel,BorderLayout.SOUTH);
		nextButtonQuestion.addActionListener(this);
		nextButtonQuestion.setActionCommand("nextQuestion");
		localContentPane.validate();
		temporaryAnswer = "No Answer";
		
		
	}
	
	/**
	 * Display an image and gather the participant's information.
	 */
	private void getParticipantInfoPanel() {
		localContentPane.removeAll();
		JButton nextButtonInfo = new JButton("Next");
		nextButtonInfo.setFont(arialFont);
		JButton backButtonInfo = new JButton("Back");
		backButtonInfo.setFont(arialFont);
		JPanel infoPanel = new JPanel();
		JTextArea namePrompt = new JTextArea("Full Name:");
		namePrompt.setFont(arialFont);
		namePrompt.setEditable(false);
		JTextArea profPrompt = new JTextArea("PY151 Professor's Name:");
		profPrompt.setFont(arialFont);
		profPrompt.setEditable(false);
		JTextArea IDPrompt = new JTextArea("Student ID:");
		IDPrompt.setFont(arialFont);
		IDPrompt.setEditable(false);
		JPanel radioButtonPanel = new JPanel();
		ButtonGroup radioButtonGroup = new ButtonGroup();
		JRadioButton male = new JRadioButton("Male");
		male.setFont(arialFont);
		JRadioButton female = new JRadioButton("Female");
		female.setFont(arialFont);
		JRadioButton other = new JRadioButton("Other");
		other.setFont(arialFont);
		radioButtonGroup.add(male);
		radioButtonGroup.add(female);
		radioButtonPanel.add(male, BorderLayout.WEST);
		radioButtonPanel.add(female, BorderLayout.AFTER_LAST_LINE);
		male.addActionListener(this);
		male.setActionCommand("male");
		female.addActionListener(this);
		female.setActionCommand("female");
		other.addActionListener(this);
		other.setActionCommand("other");
		studentNamefield.setColumns(10);
		studentNamefield.setBorder(BorderFactory.createLineBorder(Color.black));
		professorNameField.setColumns(10);
		professorNameField.setBorder(BorderFactory.createLineBorder(Color.black));
		studentIDfield.setColumns(7);
		studentIDfield.setBorder(BorderFactory.createLineBorder(Color.black));
		
		infoPanel.add(namePrompt,BorderLayout.WEST);
		infoPanel.add(studentNamefield, BorderLayout.AFTER_LAST_LINE);
		infoPanel.add(profPrompt, BorderLayout.AFTER_LAST_LINE);
		infoPanel.add(professorNameField, BorderLayout.AFTER_LAST_LINE);
		infoPanel.add(IDPrompt, BorderLayout.AFTER_LAST_LINE);
		infoPanel.add(studentIDfield, BorderLayout.AFTER_LAST_LINE);
		infoPanel.add(radioButtonPanel, BorderLayout.AFTER_LAST_LINE);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(backButtonInfo, BorderLayout.WEST);
		buttonPanel.add(nextButtonInfo,BorderLayout.AFTER_LAST_LINE);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(infoPanel, BorderLayout.NORTH);
		topPanel.add(buttonPanel, BorderLayout.SOUTH);
		Image intro = imageLoader.loadRewardImage();
		
      localContentPane.add(new ImagePanel(intro,null));
      localContentPane.add(topPanel, BorderLayout.AFTER_LAST_LINE);

		nextButtonInfo.addActionListener(this);
		nextButtonInfo.setActionCommand("nextInfo");
		backButtonInfo.addActionListener(this);
		backButtonInfo.setActionCommand("backInfo");
		localContentPane.validate();
		
			
		
	}
	
	/**
	 * This method is called if a GUI object has been interacted with.
	 */
	public void actionPerformed(ActionEvent e)
	{
		//called if next button pushed for participant info slide.
		if ("nextInfo".equals(e.getActionCommand()))
		{
			participantName = studentNamefield.getText();
			professorName = professorNameField.getText();
			gender = temporaryGender;
			try
			{
			studentID = Integer.parseInt(studentIDfield.getText());
			}
			catch(NumberFormatException f)
			{
				
			}
			//make sure all required information is filled in.
			if((studentID != -1) && (!participantName.isEmpty()) && (!professorName.isEmpty()) && (!gender.isEmpty()))
				displayQuestion(0);
			
		}
		
		//if next button pushed on a question slide
		if ("nextQuestion".equals(e.getActionCommand()))
		{
			if (temporaryAnswer != "No Answer")
			{
			responseTimes[currentQuestion] = System.currentTimeMillis() - startingSystemTime; 
			try
			{
			responses[currentQuestion] = temporaryAnswer;
			}
			catch(NumberFormatException f)
			{
				
			}
			temporaryAnswer = "No Answer";
			localContentPane.removeAll();
			
			if (currentQuestion < numberOfQuestions)
			{
				
			displayAnswer(currentQuestion);
			}
			else
			{
				displayEnding();
			}
			}
		}
		
		//if next pushed on answer slide
		if ("nextAnswer".equals(e.getActionCommand()))
		{
			localContentPane.removeAll();
			currentQuestion++;
			if (currentQuestion < numberOfQuestions)
			displayQuestion(currentQuestion);
			else
				displayEnding();
			
		}
		
		//if next pushed on IRB slide
		if ("nextIRB".equals(e.getActionCommand()))
		{
			AccessUseData saveUseData = new AccessUseDataServer();
			saveUseData.setPathName(workingIP);
			String serverResponse = saveUseData.saveUseData("getNext");
			sessionNumber = Integer.decode(serverResponse);
			Integer[] testArray = totalUseData.getQuestionArray(sessionNumber);
			System.out.println(testArray[0].toString()+ testArray[1].toString()+ testArray[2].toString()+ testArray[3].toString()+ testArray[4].toString()+ testArray[5].toString()+ testArray[6].toString());
			localGeneratedNumber = testArray;
			localContentPane.removeAll();
			displayGeneralIntro();
		}
		
		//if next pushed on general introduction slide
		if ("nextGeneral".equals(e.getActionCommand()))
		{
			localContentPane.removeAll();
			display3DExample();
		}
		
		//if next pushed on 3d example slide
		if ("next3D".equals(e.getActionCommand()))
		{
			localContentPane.removeAll();
			display4DExample();
		}
		//if back button pushed on participant info slide
		if ("backInfo".equals(e.getActionCommand()))
		{
			localContentPane.removeAll();
			display4DExample();
		}
		//if back button pushed on 3D example
		if ("back3D".equals(e.getActionCommand()))
		{
			localContentPane.removeAll();
			displayGeneralIntro();
		}
		//if next pushed on 4D example slide
		if ("next4D".equals(e.getActionCommand()))
		{
			localContentPane.removeAll();
			getParticipantInfoPanel();
		}
		//if back pushed on 4D example slide
		if ("back4D".equals(e.getActionCommand()))
		{
			localContentPane.removeAll();
			display3DExample();
		}
		//if data save button on final slide pushed.
		if ("nextEnding".equals(e.getActionCommand()))
		{
			finalButtonAnswer.setText("Data saved.");
			localContentPane.validate();
			System.out.println("Survey ended.");
			destroy();
		}
		//yes pushed on a question slide
		if ("yes".equals(e.getActionCommand()))
		{
			temporaryAnswer = "yes";
		}
		//no pushed on a question slide
		if ("no".equals(e.getActionCommand()))
		{
			temporaryAnswer = "no";
		}
		//maybe pushed on a question slide
		if ("maybe".equals(e.getActionCommand()))
		{
			temporaryAnswer = "maybe";
		}
		//male selected on participant info slide
		if ("male".equals(e.getActionCommand()))
		{
			temporaryGender = "male";
		}
		//female selected on participant info slide
		if ("female".equals(e.getActionCommand()))
		{
			temporaryGender = "female";
		}
		
		//other selected on participant info slide
		if ("other".equals(e.getActionCommand()))
		{
			temporaryGender = "other";
		}
	}

	/**
	 * Administrative tool- read the use data for all surveys so far.
	 * @param x
	 */
	public void displayUseData(UsageData x)
	{
		System.out.println(x.getTotalNumberParticipants());
			for(int iii = 0; iii< x.getTimesQuestionAsked().size(); iii++)
			{
				System.out.println(x.getTimesQuestionAsked().get(iii));
			}
	}
	
			  
	
}
