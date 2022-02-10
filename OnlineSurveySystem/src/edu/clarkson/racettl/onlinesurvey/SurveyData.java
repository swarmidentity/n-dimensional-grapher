package edu.clarkson.racettl.onlinesurvey;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SurveyData implements Serializable{

	private float[] responseTimes;
	private Integer[] generatedNumber;
	private String[] responses;
	private String participantName;
	private String professorName;
	private int studentID;
	private String gender;
	
	public SurveyData(float[] responseTimes, Integer[] localGeneratedNumber,
			String[] responses, String participantName, String professorName, String gender,
			int studentID) {
		//super();
		this.responseTimes = responseTimes;
		this.generatedNumber = localGeneratedNumber;
		this.responses = responses;
		this.participantName = participantName;
		this.professorName = professorName;
		this.studentID = studentID;
		this.gender = gender;
	}
	
	public float[] getResponseTimes() {
		return responseTimes;
	}
	public void setResponseTimes(float[] responseTimes) {
		this.responseTimes = responseTimes;
	}
	public Integer[] getGeneratedNumber() {
		return generatedNumber;
	}
	public void setGeneratedNumber(Integer[] generatedNumber) {
		this.generatedNumber = generatedNumber;
	}
	public String[] getResponses() {
		return responses;
	}
	public void setResponses(String[] responses) {
		this.responses = responses;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getProfessorName() {
		return professorName;
	}
	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}
	public int getStudentID() {
		return studentID;
	}
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	
	public String[] returnAllInfo()
	{
		String[] tempString = new String[25];
		tempString[0] = participantName;
		tempString[1] = Integer.toString(studentID);
		tempString[2] = professorName;
		tempString[3] = "responses:";
		tempString[4] = responses[0];
		tempString[5] = Float.toString(responseTimes[0]);
		tempString[6] = responses[1];
		tempString[7] = Float.toString(responseTimes[1]);
		tempString[8] = responses[2];
		tempString[9] = Float.toString(responseTimes[2]);
		tempString[10] = responses[3];
		tempString[11] = Float.toString(responseTimes[3]);
		tempString[12] = responses[4];
		tempString[13] = Float.toString(responseTimes[4]);
		tempString[14] = responses[5];
		tempString[15] = Float.toString(responseTimes[5]);
		tempString[16] = generatedNumber[0].toString();
		tempString[17] = generatedNumber[1].toString();
		tempString[18] = generatedNumber[2].toString();
		tempString[19] = generatedNumber[3].toString();
		tempString[20] = generatedNumber[4].toString();
		tempString[21] = generatedNumber[5].toString();
		tempString[22] = generatedNumber[6].toString();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd&HH:mm:ss");
		Date date = new Date();
		tempString[23] = dateFormat.format(date);
		tempString[24] = gender;
		return tempString;
	}
}
