package edu.clarkson.racettl.ndimensional;

/**
 * Maintains the currently selected points, lines and planes.
 * @author Louis Racette
 *
 */

public class select {

	static int[] selectedPoints= new int[0];
	static int[] selectedLines= new int[0];
	static int[] selectedPlanes= new int[0];
	
	public select()
	{
		
	}
	
	/**
	 * Select some points.
	 * @param P
	 */
	public static void selectPoints(int[] P)
	{
		int[] tempPoints = new int[selectedPoints.length+P.length];
		for(int iii= 0; iii< selectedPoints.length; iii++)
		{
			tempPoints[iii] = selectedPoints[iii];
		}
		for(int iii=selectedPoints.length; iii< tempPoints.length; iii++)
		{
		tempPoints[iii]= P[iii-selectedPoints.length];
		}
		selectedPoints = tempPoints;
	}
	
	/**
	 * Select some lines.
	 * @param P
	 */
	public static void selectLines(int[] P)
	{
		int[] tempLines = new int[selectedLines.length+P.length];
		for(int iii= 0; iii< selectedLines.length; iii++)
		{
			tempLines[iii] = selectedLines[iii];
		}
		for(int iii=selectedLines.length; iii< tempLines.length; iii++)
		{
		tempLines[iii]= P[iii-selectedLines.length];
		}
		selectedLines = tempLines;
	}
	
	/**
	 * Select some planes.
	 * @param P
	 */
	public static void selectPlanes(int[] P)
	{
		int[] tempPlanes = new int[selectedPlanes.length+P.length];
		for(int iii= 0; iii< selectedPlanes.length; iii++)
		{
			tempPlanes[iii] = selectedPlanes[iii];
		}
		for(int iii=selectedPlanes.length; iii< tempPlanes.length; iii++)
		{
		tempPlanes[iii]= P[iii-selectedPlanes.length];
		}
		selectedPlanes = tempPlanes;
	}
	
	/**
	 * Select all points, lines and planes in object.
	 */
	public static void selectEverything() {
		try
		{
			int[] temp = new int[NDimensionalObject.actualPoints.length];
		for(int iii = 0; iii< NDimensionalObject.actualPoints.length; iii++)
			temp[iii]=iii;
		selectPoints(temp);
		}
		catch(NullPointerException f)
		{ System.out.println("null pointer");}
		try
		{
			int[] temp = new int[NDimensionalObject.myLines.length];
			for(int iii = 0; iii< NDimensionalObject.myLines.length; iii++)
				temp[iii]=iii;
			selectLines(temp);
		}
		catch(NullPointerException f)
		{ System.out.println("null pointer");}
		try
		{
			int[] temp = new int[NDimensionalObject.myPlanes.length];
			for(int iii = 0; iii< NDimensionalObject.myPlanes.length; iii++)
				temp[iii]=iii;
			selectPlanes(temp);
		}
		catch(NullPointerException f)
		{ System.out.println("null pointer");}
		
	}

	/**
	 * Remove all selected points, lines and planes.
	 */
	public static void deselect() {
		selectedPoints = new int[0];
		selectedLines = new int[0];
		selectedPlanes = new int[0];
		
	}

}
