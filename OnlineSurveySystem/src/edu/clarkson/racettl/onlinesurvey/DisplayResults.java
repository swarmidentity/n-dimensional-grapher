package edu.clarkson.racettl.onlinesurvey;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class is used to convert generated test files into a single .csv file.
 * @author Louis Racette
 *
 */
public class DisplayResults {

	//This directory is arbitrary.
	private static String directoryName = "C:\\Users\\SwarmIdentity\\Documents\\PY496\\OnlineSurveySystem\\savedData3";
	
	String useDataPath = "testingUseData";
	
	/**
	 * Convert individual files to comma separated values.
	 */
	
	public static void displayUserResults()
	{
		File directory = new File(directoryName);
		File[] tempDir = directory.listFiles();
		ArrayList<String> decodedStrings = new ArrayList<String>();
		
		String readString;
		//iterate through all files in directory, adding their strings to the array list
		for(int iii=0; iii < tempDir.length; iii++)
		{
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(tempDir[iii])));
				
				while ((readString = reader.readLine()) != null) {
					decodedStrings.add(readString);
				    System.out.println(readString);
				    
				}
				
				
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
			}

		String[][] outputStrings = new String[decodedStrings.size()][23];
		LinkedList<String> namesSoFar = new LinkedList<String>();
		//split found strings into their component values and reorganize
		for(int index =0; index < decodedStrings.size(); index++)
		{
			String[] foundStrings = decodedStrings.get(index).split("_");
		
			
			Boolean canAdd = true;
			
			
			for(int iii=0; iii < namesSoFar.size(); iii++)
			{
				if (namesSoFar.get(iii).equals(foundStrings[1]))
				{
					System.out.println("Found Duplicate" + foundStrings[1]);
					//uncomment this line to remove duplicate student IDs from the resulting csv
					//canAdd = false;
					
					break;
				}
			}
			namesSoFar.add(foundStrings[1]);
			
				
				                                       
				                                       
				
			if (canAdd)
			{
				for(int iterator = 0; iterator < 3; iterator ++)
				{
			    outputStrings[index][iterator]= foundStrings[iterator];
				}
				
				//additions for sex/ time stamp	
				outputStrings[index][21]= foundStrings[foundStrings.length - 2];
				outputStrings[index][22]= foundStrings[foundStrings.length -1];
				
			int setNumber = Integer.parseInt(foundStrings[16]);
			outputStrings[index][3] = foundStrings[16];
			int[] questionOrder = new int[6];
			String[] answerKey = new String[6];
			
			for(int iii=0; iii < 6; iii++)
			{
				questionOrder[iii]= Integer.parseInt(foundStrings[iii+17]);
				//make answer key
				if ((questionOrder[iii]==0)||(questionOrder[iii]==2)||(questionOrder[iii]==3))
					answerKey[iii] = "yes";
				else
					answerKey[iii] = "no";
			}
			
			
			for( int iii = 16; iii < 21; iii++)
			{
				outputStrings[index][iii] = "0";
			}
			
				for(int iii=0; iii < 6; iii++)
				{
					if (foundStrings[iii*2 + 4].equals(answerKey[iii]))
					{
						if (answerKey[iii].equals( "yes"))
						{
						outputStrings[index][4 + questionOrder[iii]] = "Correct Yes";
						outputStrings[index][16] =Integer.toString(Integer.parseInt(outputStrings[index][16])+1);
						}
						if (answerKey[iii].equals("no"))
						{
						outputStrings[index][4 + questionOrder[iii]] = "Correct No";
						outputStrings[index][17] = Integer.toString(Integer.parseInt(outputStrings[index][17])+1);
						}
						
					}
					else
					{
						if (foundStrings[iii*2 + 4].equals("no"))
						{
						outputStrings[index][4 + questionOrder[iii]] = "Incorrect No";
						outputStrings[index][18] = Integer.toString(Integer.parseInt(outputStrings[index][18])+1);
						}
						else if (foundStrings[iii*2 + 4].equals("yes"))
						{
							outputStrings[index][4 + questionOrder[iii]] = "Incorrect Yes";
							outputStrings[index][19]= Integer.toString(Integer.parseInt(outputStrings[index][19])+1);
						}
						else if (foundStrings[iii*2 + 4].equals("No Data"))
						{
							outputStrings[index][4 + questionOrder[iii]] = "Didn't answer";
							
						}
						else
						{
							outputStrings[index][4 + questionOrder[iii]] = "Can't Tell";
							outputStrings[index][20]= Integer.toString(Integer.parseInt(outputStrings[index][20])+1);
						}
					}
				}
			
			for(int iii=0; iii < 6; iii++)
			{
					outputStrings[index][10 + questionOrder[iii]] =foundStrings[iii*2 + 5];
				
				
			}
			
		}
			else
			{
				outputStrings[index][1]= "-1";
			
			}
		}
		saveCSV(outputStrings);
	}
	
	
	/**
	 * Save a csv file with the reorganized information.
	 * @param stringsToSave
	 */
	public static void saveCSV( String[][] stringsToSave)
	{
		File saveFile = new File("savedDataFromSecondRun.csv");
		try {
			saveFile.createNewFile();
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(saveFile));
			//write headings
			fileWriter.write("Name, Student ID, Professor, Set, Answer 1, Answer 2, Answer 3, Answer 4, Answer 5, Answer 6, Time 1, Time 2, Time 3, Time 4, Time 5, Time 6, Correct Yes, Correct No, Incorrect No, Incorrect Yes, Can't Tell");
			fileWriter.write("\n");
			//write data
			for(int stringIndex = 0; stringIndex < stringsToSave.length; stringIndex++)
			{
				
				if ((Integer.parseInt(stringsToSave[stringIndex][1]) > -1))
				{
					if (Integer.parseInt(stringsToSave[stringIndex][3]) == 0)
					{
				for(int stringIterator = 0; stringIterator < stringsToSave[stringIndex].length; stringIterator++)
				{
					fileWriter.write(stringsToSave[stringIndex][stringIterator] + ",");
				}
				fileWriter.write("\n");
					}
				}
			}
			for(int stringIndex = 0; stringIndex < stringsToSave.length; stringIndex++)
			{
				
				if ((Integer.parseInt(stringsToSave[stringIndex][1]) > -1))
				{
					if (Integer.parseInt(stringsToSave[stringIndex][3]) == 1)
					{
				for(int stringIterator = 0; stringIterator < stringsToSave[stringIndex].length; stringIterator++)
				{
					fileWriter.write(stringsToSave[stringIndex][stringIterator] + ",");
				}
				fileWriter.write("\n");
					}
				}
			}
			for(int stringIndex = 0; stringIndex < stringsToSave.length; stringIndex++)
			{
				
				if ((Integer.parseInt(stringsToSave[stringIndex][1]) > -1))
				{
					if (Integer.parseInt(stringsToSave[stringIndex][3]) == 2)
					{
				for(int stringIterator = 0; stringIterator < stringsToSave[stringIndex].length; stringIterator++)
				{
					fileWriter.write(stringsToSave[stringIndex][stringIterator] + ",");
				}
				fileWriter.write("\n");
					}
				}
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Main class for just running the csv converter.
	 * @param args
	 */
	public static void main(String[] args)
	{
		DisplayResults.displayUserResults();
	}
}
