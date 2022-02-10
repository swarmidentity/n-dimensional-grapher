package edu.clarkson.racettl.ndimensional;
/**
 * This class contains all information necessary to specify a point.
 * @author Louis Racette
 *
 */
public class point {

	public double[] location;
	
	public point() {
		location = new double[NDimensionalObject.NumberOfDimensions];
	}
	/**
	 * Allow construction with variable number of double inputs.
	 * @param dim
	 */
	public point(double ... dim) {
		location = dim;
	}

	/**
	 * Return the number of dimensions.
	 * @return number
	 */
	public int numDimensions()
	{
		return location.length;
	}
	
	/**
	 * Return the largest measurement in any dimension.
	 * @return largest measurement
	 */
	public double largestDim()
	{
		double largestDimension=0;
		for(double temp:location)
		{
			if (largestDimension < temp)
			largestDimension=temp;
		}
		return largestDimension;
	}
	
	/**
	 * Print this point's information.
	 */
	public void testMe()
	{
		for (double temp : location)
			System.out.println(temp);
		
	}
	
	
}
