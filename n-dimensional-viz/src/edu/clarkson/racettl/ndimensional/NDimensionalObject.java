package edu.clarkson.racettl.ndimensional;
/**
 * Class to hold all points, lines and planes in the current object.
 * @author Louis Racette
 *
 */
public class NDimensionalObject {
static public plane[] myPlanes;
static public point[] myPoints;
static public point[] actualPoints;
public static line[] myLines;
public static int NumberOfDimensions;
public static String[] DimensionLabels;
public NDimensionalObject()
{

	NumberOfDimensions = 3;
	DimensionLabels = new String[NumberOfDimensions]; 
	
	DimensionLabels[0]="X";
	DimensionLabels[1]="Y";
	DimensionLabels[2]="Z";
	actualPoints = new point[0];
	myPlanes = new plane[0];
	myLines = new line[0];
	
	
}

/**
 * Add a point to the current object.
 * @param p
 */
public static void  addpoint(point p)
{
	point[] tempPoints = new point[actualPoints.length+1];
	for(int iii= 0; iii< actualPoints.length; iii++)
	{
		tempPoints[iii] = actualPoints[iii];
	}
	tempPoints[actualPoints.length]= p;
	actualPoints = tempPoints;
	paintMethods3d.paint3Dobject();
}

/**
 * Add a line to the current object.
 * @param l
 */
public static void addline(line l)
{
	line[] tempLines = new line[myLines.length+1];
	for(int iii= 0; iii< myLines.length; iii++)
	{
		tempLines[iii] = myLines[iii];
	}
	tempLines[myLines.length]= l;
	myLines = tempLines;
}

/**
 * Add a plane to the current object.
 * @param pl
 */
public static void addplane(plane pl)
{
	plane[] tempPlanes = new plane[myPlanes.length+1];
	for(int iii= 0; iii< myPlanes.length; iii++)
	{
		tempPlanes[iii] = myPlanes[iii];
	}
	tempPlanes[myPlanes.length]= pl;
	myPlanes = tempPlanes;
}

/**
 * Print out all information about object.
 */
public static void showEverything()
{
	for(point tempPoint: actualPoints)
	{
		tempPoint.testMe();
		System.out.println();
	}
	System.out.println(Integer.toString(NumberOfDimensions));
}

/**
 * Set the number of dimensions.
 * @param parseInt
 */
public static void setNumberOfDimensions(int parseInt) { 
	
	String[] tempDimensionLabels = new String[parseInt]; 
	if(NumberOfDimensions < parseInt)
	{
	for(int jjj=0; jjj < NumberOfDimensions; jjj++)
	{
		
		tempDimensionLabels[jjj]= DimensionLabels[jjj];
	}
	for(int jjj= NumberOfDimensions; jjj < parseInt; jjj++)
		tempDimensionLabels[jjj]=Integer.toString(jjj+1);
	
		for(int jjj=0; jjj<actualPoints.length; jjj++)//If moving up in dimensions
		{
			double[] tempLocations = new double[parseInt];
			for(int iii=0; iii < actualPoints[jjj].location.length; iii++)
				tempLocations[iii] = actualPoints[jjj].location[iii]; 
			for(int iii = actualPoints[jjj].location.length; iii < parseInt; iii++)
				tempLocations[iii] = 0;
			actualPoints[jjj] = new point(tempLocations);
		}
		
	}
	else
	{
		for(int jjj=0; jjj < parseInt; jjj++)
		{
			
			tempDimensionLabels[jjj]= DimensionLabels[jjj];
		}	
		for(int jjj=0; jjj<actualPoints.length; jjj++)
		{
			double[] tempLocations = new double[parseInt];
			for(int iii=0; iii < parseInt; iii++)
				tempLocations[iii] = actualPoints[jjj].location[iii];
			actualPoints[jjj] = new point(tempLocations);
		}
	}
	NumberOfDimensions = parseInt;
	DimensionLabels= tempDimensionLabels;
}
/**
 * Find the largest single measurement for uniform scaling.
 * @return largest measurement
 */
public static double findLargestUniform()
{
	double largest = 0;
	for(point tempPoint:actualPoints)
	{
		if(tempPoint.largestDim()>largest)
			largest=tempPoint.largestDim();
	}
	
	return largest;
}

/**
 * Find the largest measurement along a given dimension for relative scaling.
 * @param dim
 * @return largest measurement
 */
public static double findLargestRelative(int dim)
{
	double largest = actualPoints[0].location[dim];
	for(point tempPoint:actualPoints)//need to implement a view method to test
	{
		if(tempPoint.location[dim]>largest)
			largest=tempPoint.location[dim];
	}
	
	return largest;
}

/**
 * Find the smallest measurement along a given dimension for relative scaling.
 * @param dim
 * @return smallest measurement
 */
public static double findSmallestRelative(int dim)
{
	double smallest = actualPoints[0].location[dim];
	for(point tempPoint:actualPoints)
	{
		if(tempPoint.location[dim]<smallest)
			smallest=tempPoint.location[dim];
	}
	
	return smallest;
}

/**
 * Find the point with the largest measurement for a given plane and dimension.
 * @param dim
 * @param p
 * @return largest
 */
public static int findLargestPlane(int dim, plane p)
{
	double largest = actualPoints[p.pointsIndex[0]].location[dim];
	int largestIndex = 0;
	for(int iii=0; iii<3; iii++)
	{
		if(actualPoints[p.pointsIndex[iii]].location[dim]>largest)
		{
			largest=actualPoints[p.pointsIndex[iii]].location[dim];
			largestIndex = p.pointsIndex[iii];
		}
	}
	return largestIndex;
}

/**
 * Find the point with the smallest measurement for a given plane and dimension.
 * @param dim
 * @param p
 * @return smallest
 */
public static int findSmallestPlane(int dim, plane p)
{
	double smallest = actualPoints[p.pointsIndex[0]].location[dim];
	int smallestIndex = 0;
	for(int iii=0; iii<3; iii++)//need to implement a view method to test
	{
		if(actualPoints[p.pointsIndex[iii]].location[dim]<smallest)
		{
			smallest=actualPoints[p.pointsIndex[iii]].location[dim];
			smallestIndex = p.pointsIndex[iii];
		}
	}
	
	return smallestIndex;
}

/**
 * Interpolate a line as a point in n-space.
 * @param ds
 * @param ds2
 * @param sliceVars
 * @return interpolation point
 * @throws Exception
 */

public static float[] interpolateLine(point ds, point ds2,
		double[] sliceVars) throws Exception {
	//need to find point in visible space that corresponds to nd+1 slice (example: time= 1 second)
	float[] Coordinates = new float[3];
	
	//parametric equations- find t, substitute t into each equation (1-3)
	
	double[] tvar = new double[NumberOfDimensions-3];
	for(int nnn=0; nnn < NumberOfDimensions-3; nnn++)
	{
		if ((ds.location[nnn+3]-ds2.location[nnn+3])!=0)
		tvar[nnn]= (float) ((sliceVars[nnn+3]-ds2.location[nnn+3])/(ds.location[nnn+3]-ds2.location[nnn+3]));
		else if (nnn!=0)//if d1 and d2 on same coordinate, t doesn't matter...
			tvar[nnn] = tvar[nnn-1];
		else 
			tvar[nnn] = 0;
	}
	for(int nnn=0; nnn < NumberOfDimensions-3; nnn++)//make sure that all t's are equal
	{
		Exception exceptionNoDraw = null;
		if (tvar[nnn]!=tvar[0])
			throw exceptionNoDraw;
	}
	for (int iii=0; iii< 3; iii++ )
	{
		if((ds.location[iii]-ds2.location[iii])!=0)
			Coordinates[iii]= (float) ((tvar[0]*(ds.location[iii])+(1-tvar[0])*(ds2.location[iii])));
		else
			Coordinates[iii]=(float) ds.location[iii];
	}
	
	
	
	return Coordinates;
}

/**
 * Interpolate a plane as a line in n-space
 * @param jjj
 * @param i
 * @param j
 * @param k
 * @param sliceVars
 * @return line
 * @throws Exception
 */

public static float[][] interpolatePlane(int jjj, int i, int j, int k, double[] sliceVars) throws Exception {
	float[][] Coordinates = new float[2][3];
	Coordinates[0] = interpolateLine(myPoints[myPlanes[jjj].pointsIndex[i]],myPoints[myPlanes[jjj].pointsIndex[k]],sliceVars);
	Coordinates[1] = interpolateLine(myPoints[myPlanes[jjj].pointsIndex[j]],myPoints[myPlanes[jjj].pointsIndex[k]],sliceVars);

		return Coordinates;
}

/**
 * Delete a given point.
 * @param index
 */
public static void  deletePoint(int index)
{
	if(actualPoints.length!=1)
	{
	point[] tempPoints = new point[actualPoints.length-1];
	for(int iii= 0; iii< index; iii++)
	{
		tempPoints[iii] = actualPoints[iii];
	}
	for(int iii=index; iii< NDimensionalObject.actualPoints.length-1; iii++)
	{
		tempPoints[iii]= actualPoints[iii+1];
	}
	actualPoints = tempPoints;
	}
	else
	{
		actualPoints = new point[0];
	}
	
	
}

/**
 * Delete a given line.
 * @param index
 */
public static void deleteLine(int index)
{
	line[] tempLines = new line[myLines.length-1];
	for(int iii= 0; iii< index; iii++)
	{
		tempLines[iii] = myLines[iii];
	}
	for(int iii=index; iii< NDimensionalObject.myLines.length-1; iii++)
	{
		tempLines[iii]= myLines[iii+1];
	}
	
	myLines = tempLines;

}

/**
 * Delete a given plane.
 * @param index
 */
public static void deletePlane(int index)
{
	plane[] tempPlanes = new plane[myPlanes.length-1];
	for(int iii= 0; iii< index; iii++)
	{
		tempPlanes[iii] = myPlanes[iii];
	}
	for(int iii=index; iii< NDimensionalObject.myPlanes.length-1; iii++)
	{
		tempPlanes[iii]= myPlanes[iii+1];
	}
	
	myPlanes = tempPlanes;

}
/**
 * Compute the distance between two points.
 * @param pointIndex1
 * @param pointIndex2
 * @return distance
 */
public static double getDistance(int pointIndex1, int pointIndex2)
{
	double distanceFromOrigin = 0;
	for(int jjj=0; jjj<NDimensionalObject.NumberOfDimensions; jjj++)
	{
		distanceFromOrigin += ((NDimensionalObject.actualPoints[pointIndex1].location[jjj]-NDimensionalObject.actualPoints[pointIndex2].location[jjj])*(NDimensionalObject.actualPoints[pointIndex1].location[jjj]-NDimensionalObject.actualPoints[pointIndex2].location[jjj]));
	}
	distanceFromOrigin = Math.sqrt(distanceFromOrigin);
	
	return distanceFromOrigin;
}

/**
 * Automatically fill in points with connecting lines.
 * @param parseInt
 * @param userDistance
 */
public static void autoLine(int parseInt, double userDistance) {

	
	for(int iii=0; iii< NDimensionalObject.actualPoints.length-parseInt; iii++)
	{
		int tries=0;
		for(int jjj=1; jjj< parseInt; jjj++)
		{
			tries++;
		if (getDistance(iii,iii+jjj)<=userDistance)
		{
			addline(new line(iii,iii+jjj));
		}
		else if (tries < parseInt)
		{
			jjj--;
		}
	}
	}
}

/**
 * Adjust points to new viewing style.
 */
public static void adjustPoints() {
	
	myPoints = new point[actualPoints.length];
	for(int iii=0; iii< myPoints.length; iii++)
		myPoints[iii]= new point();
	if (subGUIzoom.scaleMethod == "unscaled")
		myPoints = actualPoints;
	else if (subGUIzoom.scaleMethod == "uniform")
		for(int iii=0; iii< actualPoints.length; iii++)
		{
			for(int jjj=0; jjj< NumberOfDimensions; jjj++)
			{
				if(findLargestUniform()!=0)
				myPoints[iii].location[jjj]= (actualPoints[iii].location[jjj]/findLargestUniform());
			}
		}
	else if (subGUIzoom.scaleMethod == "relative")
		for(int iii=0; iii< actualPoints.length; iii++)
		{
			for(int jjj=0; jjj< NumberOfDimensions; jjj++)
			{
				if(findLargestRelative(jjj)!=0)
				myPoints[iii].location[jjj]= (actualPoints[iii].location[jjj]/findLargestRelative(jjj));
			}
		}
	
}
}
