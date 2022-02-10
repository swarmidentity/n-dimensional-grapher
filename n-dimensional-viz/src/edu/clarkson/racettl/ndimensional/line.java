package edu.clarkson.racettl.ndimensional;
/**
 * Class to hold line information.
 * @author Louis Racette
 *
 */
public class line {
public int endpoint1index;
public int endpoint2index;

public line(int beginpoint, int endpoint)
{
	endpoint1index= beginpoint;
	endpoint2index= endpoint;
}
}
