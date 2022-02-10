package unused;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;

import edu.clarkson.racettl.onlinesurvey.UsageData;

/**
 * Load usage data from an external server.
 * @author Louis Racette
 *
 */
public class LoadUseDataSurvey implements LoadUseData {
	private String pathName = "C:\\Users\\SwarmIdentity\\Documents\\PY493\\OnlineSurveySystem\\useData";

	String useDataPath = "testingUseData";
	
	/**
	 * Set the path to the server in this object's constructor.
	 * @param pathName
	 */
	public LoadUseDataSurvey(String pathName)
	{
		this.pathName = pathName;
	}
	
	/**
	 * Load the usage data file.
	 */
	@Override
	public UsageData loadUseDataFile() {
		ObjectInputStream temp;
		try {
		URL readingURL = new URL("http://people.clarkson.edu/~racettl/useData/"+ useDataPath);

			temp = new ObjectInputStream((readingURL.openStream()));
			
			return (UsageData) temp.readObject();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return null;
	}

}
