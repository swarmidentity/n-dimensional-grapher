package edu.clarkson.racettl.onlinesurvey;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.URL;
import java.net.URLConnection;

/**
 * Usage data access class.
 * @author Louis Racette
 *
 */

public class AccessUseDataServer implements AccessUseData {
	private String pathName; 
	String useDataPath = "testingUseData";
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	
	
	@Override
	public String saveUseData(String command) {

		try {
			
			URL writingURL = new URL("http://" + pathName + ":8080/UsageData/UsageServelet");
			
			URLConnection tempConnection = writingURL.openConnection();
			tempConnection.setDoOutput(true);
			
			PrintWriter temp = new PrintWriter(tempConnection.getOutputStream());
			temp.print(command);
			temp.flush();
			temp.close();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
					tempConnection.getInputStream()));
					
		String decodedString;

		while ((decodedString = in.readLine()) != null) {
		    System.out.println(decodedString);
		    return decodedString;
		}
		in.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return "0";
		//error code
	}

}
