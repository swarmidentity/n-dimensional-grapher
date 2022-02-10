package edu.clarkson.racettl.onlinesurvey;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Save the session data to a server.
 * @author Louis Racette
 *
 */
public class SaveSessionDataServer implements SaveSessionData{
	private String pathName;
	public String getPathName() {
		return pathName;
	}
	/**
	 * Set the server name.
	 * @param pathName
	 */
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	/**
	 * Save the session data to an external server.
	 */
	@Override
	public void saveSessionData(SurveyData data) {
	
		BufferedOutputStream temp;
		try {
			URL writingURL = new URL("http://" + pathName +":8080/SessionData/SessionServelet");
			
			URLConnection connection = writingURL.openConnection();
			connection.setDoOutput(true);
			temp = new BufferedOutputStream(connection.getOutputStream());
			String[] dataToWrite = data.returnAllInfo();
			for(int writing = 0; writing < dataToWrite.length; writing++)
			{ 
				if (dataToWrite[writing] != null)
			temp.write((dataToWrite[writing]+ "_").getBytes());
			else
				temp.write("No Data_".getBytes());
			}
			temp.flush();
			temp.close();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
					connection.getInputStream()));
					
		String decodedString;

		while ((decodedString = in.readLine()) != null) {
		    System.out.println(decodedString);
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
