package edu.clarkson.racettl.ndimensional;
/**
 * This class holds all information necessary to draw a plane.
 * @author Louis Racette
 *
 */
public class plane {
public int[] pointsIndex = new int[3];


public plane(int p1, int p2, int p3)
{
	pointsIndex[0] = p1;
	pointsIndex[1] = p2;
	pointsIndex[2] = p3;
}

}
