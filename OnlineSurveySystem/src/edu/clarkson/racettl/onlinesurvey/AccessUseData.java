package edu.clarkson.racettl.onlinesurvey;

/**
 * Interface for access to usage data.
 * @author Louis Racette
 *
 */

public interface AccessUseData {

	/**
	 * Save the user's data.
	 * @param command
	 * @return user's number
	 */
	public String saveUseData(String command);
	/**
	 * Set the server to access.
	 * @param pathName
	 */
	public void setPathName(String pathName);
}
