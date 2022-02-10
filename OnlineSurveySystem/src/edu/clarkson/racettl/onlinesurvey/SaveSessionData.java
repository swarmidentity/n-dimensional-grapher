package edu.clarkson.racettl.onlinesurvey;

/**
 * Interface to save session information.
 * @author Louis Racette
 *
 */
public interface SaveSessionData {

	/**
	 * Savve the session data.
	 * @param data
	 */
	public void saveSessionData(SurveyData data);
}
