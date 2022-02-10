package edu.clarkson.racettl.onlinesurvey;

import java.io.Serializable;
import java.util.ArrayList;

public class UsageData implements Serializable {

	private int totalNumberParticipants;
	private ArrayList<Integer> timesQuestionAsked;
	
	public int getTotalNumberParticipants() {
		return totalNumberParticipants;
	}
	public void setTotalNumberParticipants(int totalNumberParticipants) {
		this.totalNumberParticipants = totalNumberParticipants;
	}
	
	public ArrayList<Integer> getTimesQuestionAsked() {
		return timesQuestionAsked;
	}
	public void setTimesQuestionAsked(ArrayList<Integer> timesQuestionAsked) {
		this.timesQuestionAsked = timesQuestionAsked;
	}
	//should iterate through the list, asking each question if it has been asked less
	//than the previous one. Otherwise, ask the first one.
	public int whichQuestionToAsk()
	{
		int mostAskedSoFar = timesQuestionAsked.get(0);
		for(int tempInt = 0; tempInt < timesQuestionAsked.size(); tempInt++)
		{
			if (timesQuestionAsked.get(tempInt) < mostAskedSoFar)
			return tempInt;
		}
		return 0;
	}
	public static Integer[] getQuestionArray(Integer sessionNumber) {
		Integer[] tempArray = new Integer[7];
		Integer[] tempFirstQ = new Integer[3];
		Integer[] tempSecondQ = new Integer[3];
			tempArray[0] = sessionNumber % 3; //determines set number
			//determine first block order
			if ((sessionNumber % 36 >= 0)&&(sessionNumber % 36 < 6))
			{
				tempFirstQ[0] = 0;
				tempFirstQ[1] = 1;
				tempFirstQ[2] = 2;
			}
			else if ((sessionNumber % 36 >= 6)&&(sessionNumber % 36 < 12))
			{
				tempFirstQ[0] = 0;
				tempFirstQ[1] = 2;
				tempFirstQ[2] = 1;
			}
			else if ((sessionNumber % 36 >= 12)&&(sessionNumber % 36 < 18))
			{
				tempFirstQ[0] = 1;
				tempFirstQ[1] = 0;
				tempFirstQ[2] = 2;
			}
			else if ((sessionNumber % 36 >= 18)&&(sessionNumber % 36 < 24))
			{
				tempFirstQ[0] = 2;
				tempFirstQ[1] = 0;
				tempFirstQ[2] = 1;
			}
			else if ((sessionNumber % 36 >= 24)&&(sessionNumber % 36 < 30))
			{
				tempFirstQ[0] = 1;
				tempFirstQ[1] = 2;
				tempFirstQ[2] = 0;
			}
			else if ((sessionNumber % 36 >= 30)&&(sessionNumber % 36 < 36))
			{
				tempFirstQ[0] = 2;
				tempFirstQ[1] = 1;
				tempFirstQ[2] = 0;
			}
			
			//determine second block order
			if ((sessionNumber >= 0)&&(sessionNumber< 36))
			{
				tempSecondQ[0] = 3;
				tempSecondQ[1] = 4;
				tempSecondQ[2] = 5;
			}
			else if ((sessionNumber >= 36)&&(sessionNumber < 72))
			{
				tempSecondQ[0] = 3;
				tempSecondQ[1] = 5;
				tempSecondQ[2] = 4;
			}
			else if ((sessionNumber >= 72)&&(sessionNumber < 108))
			{
				tempSecondQ[0] = 4;
				tempSecondQ[1] = 3;
				tempSecondQ[2] = 5;
			}
			else if ((sessionNumber >= 108)&&(sessionNumber < 144))
			{
				tempSecondQ[0] = 5;
				tempSecondQ[1] = 3;
				tempSecondQ[2] = 4;
			}
			else if ((sessionNumber >= 144)&&(sessionNumber < 180))
			{
				tempSecondQ[0] = 4;
				tempSecondQ[1] = 5;
				tempSecondQ[2] = 3;
			}
			else if ((sessionNumber >= 180)&&(sessionNumber < 216))
			{
				tempSecondQ[0] = 5;
				tempSecondQ[1] = 4;
				tempSecondQ[2] = 3;
			}
			
			
			if (sessionNumber %6 < 3)
			{
				tempArray[1] = tempFirstQ[0];
				tempArray[2] = tempFirstQ[1];
				tempArray[3] = tempFirstQ[2];
				tempArray[4] = tempSecondQ[0];
				tempArray[5] = tempSecondQ[1];
				tempArray[6] = tempSecondQ[2];
			}
			else
			{
				tempArray[1] = tempSecondQ[0];
				tempArray[2] = tempSecondQ[1];
				tempArray[3] = tempSecondQ[2];
				tempArray[4] = tempFirstQ[0];
				tempArray[5] = tempFirstQ[1];
				tempArray[6] = tempFirstQ[2];
			}
				
		return tempArray;
	}
		
	
}
