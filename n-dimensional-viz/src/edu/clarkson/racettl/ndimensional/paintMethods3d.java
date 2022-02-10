package edu.clarkson.racettl.ndimensional;
/*
 * This software was written by Louis Racette while at Clarkson University.
 * 
 */
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.management.monitor.Monitor;
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.LineArray;
import javax.media.j3d.OrderedGroup;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Screen3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Switch;
import javax.media.j3d.SwitchValueInterpolator;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.swing.JFileChooser;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
/**
 * This class contains the methods used to draw each different visualization method.
 * @author Louis Racette
 *
 */
public class paintMethods3d{
static public Boolean axisView = true;
static public Boolean pointView = true;
static public Boolean lineView = true;
static public Boolean planeView = true;
static public Color axesColor = Color.GRAY;
static public Color lineColor = Color.GREEN;
static public Color pointColor = Color.YELLOW;
static public Color planeColor = Color.BLUE;
static public Color backgroundColor = Color.BLACK;
static protected int[] activeAxes = new int[3];
static GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
static Canvas3D canvas = new Canvas3D(config);
static SimpleUniverse universe = new SimpleUniverse(canvas);
public static double rotationFactor[] = new double[3];
public static Vector3d cameraPosition = new Vector3d(0,0,5);
public static Boolean slice = false;
public static double[] sliceVars;
public static BranchGroup topGroup = new BranchGroup();
public static Vector3d[] felderVectors;
public static String[] felderStrings;
public static int[] felderSlices;
public static double[][] felderSliceVars;
public static String animView = "ortho";
private static Alpha rotationAlpha;
private static Alpha switchAlpha;
public static Transform3D rotatecam;
public static double distanceBetweenAxes;
/**
 * Set up the virtual universe before it is made live.
 */
	public paintMethods3d()
	{
		
		topGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		topGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		topGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		topGroup.setCapability(BranchGroup.ALLOW_DETACH);
		TransformGroup viewPlat = universe.getViewingPlatform().getViewPlatformTransform();
		rotatecam = new Transform3D();
		Transform3D rotatecam1 = new Transform3D();
		   Transform3D rotatecam2 = new Transform3D();
		   Transform3D rotatecam3 = new Transform3D();
		   rotatecam1.rotX(paintMethods3d.rotationFactor[0]); 
		   rotatecam2.rotY(paintMethods3d.rotationFactor[1]);
		   rotatecam3.rotZ(paintMethods3d.rotationFactor[2]);
		   rotatecam1.mul(rotatecam2);
		   rotatecam1.mul(rotatecam3);
		  rotatecam.set(cameraPosition);
		   rotatecam.mul(rotatecam1);
		viewPlat.setTransform(rotatecam);
		   KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(viewPlat);
		   keyNavBeh.setSchedulingBounds(new BoundingSphere(new Point3d(),1000.0));
		   topGroup.addChild(keyNavBeh);
		   Background coloredBackground = new Background();
			float[] tempColor = new float[3];
			backgroundColor.getColorComponents(tempColor);
			coloredBackground.setColor(new Color3f(tempColor));
			coloredBackground.setApplicationBounds(new BoundingSphere());

			topGroup.addChild(coloredBackground);
		universe.addBranchGraph(topGroup);
		setActive(0,1,2);
	}
/**
 * Set up the background and choose a viewing method.
 */
public static void paint3Dobject() 
{
	rotatecam = new Transform3D();
	Transform3D rotatecam1 = new Transform3D();
	   Transform3D rotatecam2 = new Transform3D();
	   Transform3D rotatecam3 = new Transform3D();
	   rotatecam1.rotX(paintMethods3d.rotationFactor[0]); 
	   rotatecam2.rotY(paintMethods3d.rotationFactor[1]);
	   rotatecam3.rotZ(paintMethods3d.rotationFactor[2]);
	   rotatecam1.mul(rotatecam2);
	   rotatecam1.mul(rotatecam3);
	  rotatecam.set(cameraPosition);
	   rotatecam.mul(rotatecam1);
	universe.getViewer().getView().setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
	

	   universe.getViewingPlatform().getViewPlatformTransform().setTransform(rotatecam);
	NDimensionalObject.adjustPoints();
	clearScreen();
	if (subGUIzoom.scaleMethod== "unscaled")
		if (NDimensionalObject.findLargestUniform()>1)
	distanceBetweenAxes = NDimensionalObject.findLargestUniform()+3;
		else
			distanceBetweenAxes = 4;
	else
		distanceBetweenAxes = 3;
	if ((GraphicUI3d.drawMethod == "sing3d")&&(subGUIsing3d.rotating == false))
		sing3d(universe);
	else if ((GraphicUI3d.drawMethod == "sing3d")&&(subGUIsing3d.rotating == true))
		sing3drotate(universe);
	else if (GraphicUI3d.drawMethod == "parallel")
		parallel();
	else if (GraphicUI3d.drawMethod == "mult3d")
		mult3d();
	else if (GraphicUI3d.drawMethod == "mult2d")
		mult2d(universe);
	else if (GraphicUI3d.drawMethod == "felder")
		felder(universe);
	else if (GraphicUI3d.drawMethod == "spiral")
		spiral();
	else if (GraphicUI3d.drawMethod == "animated")
		animated(universe);
	else if (GraphicUI3d.drawMethod == "timeAxis")
		timeAxis(universe);
	else if (GraphicUI3d.drawMethod == "mult4d")
		mult4d(universe);
	else if (GraphicUI3d.drawMethod == "felder4d")
		felder4d(universe);
	else if (GraphicUI3d.drawMethod == "sing2d")
		sing2d(universe);
	else
		test();
}

/**
 * Draw a single two-dimensional axis.
 * @param universe2
 */
private static void sing2d(SimpleUniverse universe2) 
{

	   BranchGroup contents = new BranchGroup();
	   contents.setCapability(BranchGroup.ALLOW_DETACH);
	   contents.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
	   
	   if (axisView)
	   {
		   float[] tempColor = new float[3];
			axesColor.getColorComponents(tempColor);
		  LineArray xAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  xAxis.setCoordinate(0, new Point3d(-1,0,0));
		  xAxis.setCoordinate(1, new Point3d(1,0,0));
		  xAxis.setColor(0, new Color3f(tempColor));
		  xAxis.setColor(1, new Color3f(tempColor));
		  Font3D font3d = new Font3D(new Font("Helvetica", Font.PLAIN, 1), new FontExtrusion());
		  Text3D textGeom = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[0]]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText1 = new Shape3D(textGeom);
		  
		  TransformGroup temp1 = new TransformGroup();
		  Transform3D move1 = new Transform3D();
		  move1.set(new Vector3d(1,0,0));
		  move1.setScale(0.1);
		  temp1.setTransform(move1);
		  temp1.addChild(tempText1);
		  LineArray yAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  yAxis.setCoordinate(0, new Point3d(0,-1,0));
		  yAxis.setCoordinate(1, new Point3d(0,1,0));
		  yAxis.setColor(0, new Color3f(tempColor));
		  yAxis.setColor(1, new Color3f(tempColor));
		  Text3D textGeom2 = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[1]]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText2 = new Shape3D(textGeom2);
		  TransformGroup temp2 = new TransformGroup();
		  Transform3D move2 = new Transform3D();
		  move2.set(new Vector3d(0,1,0));
		  move2.setScale(0.1);
		  temp2.setTransform(move2);
		  temp2.addChild(tempText2);
		  contents.addChild(new Shape3D(xAxis));
		  contents.addChild(new Shape3D(yAxis));
		  contents.addChild(temp1);
		  contents.addChild(temp2);
		 
	   }
	 //draw planes
	   if (slice == false)
	   {
	   if (planeView)
	   {

		   try
		   {
			   
		    
			   double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
			for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
			{
			
			TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
				   
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					
					{
						drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii];
						drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii];
						drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii];
					}
				}
			}
			
			myplanes.setCoordinate(0, drawPoints[jjj][0]);
			myplanes.setCoordinate(1, drawPoints[jjj][1]);
			myplanes.setCoordinate(2, drawPoints[jjj][2]);
			float[] tempColor = new float[3];
			planeColor.getColorComponents(tempColor);
			myplanes.setColor(0, new Color3f(tempColor)); 
			myplanes.setColor(1, new Color3f(tempColor));
			myplanes.setColor(2, new Color3f(tempColor));
			PolygonAttributes pgonAttrs = new PolygonAttributes();
	        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
	        Appearance tempApp = new Appearance();
	        tempApp.setPolygonAttributes(pgonAttrs);
	        Shape3D temporary = new Shape3D(myplanes);
	        temporary.setAppearance(tempApp);
			contents.addChild(temporary);
			}
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No Planes");
		   }
		   finally
		   {
			   
		   }  
	   }
	   //draw lines
	   if (lineView)
	   {
		  
		   try
		   {
			
			for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
			{
			double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
			LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					
					{
						drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii];
						drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii];
					}
				}
			}
			mylines.setCoordinate(0, drawPoints[jjj][0]);
			mylines.setCoordinate(1, drawPoints[jjj][1]);
			float[] tempColor = new float[3];
			lineColor.getColorComponents(tempColor);
			mylines.setColor(0, new Color3f(tempColor)); 
			mylines.setColor(1, new Color3f(tempColor));
			contents.addChild(new Shape3D(mylines));
			}
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No lines");
		   }
		   finally
		   {
			   
		   }
			
	   }
	 

	   if (pointView)
	   {
		   
		  
		   try
		   {
			   
		   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
		   PointArray mypoints = new PointArray(NDimensionalObject.myPoints.length, PointArray.COORDINATES|PointArray.COLOR_3);
		   
			for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
			{
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					{
						drawPoints[jjj][mmm]= NDimensionalObject.myPoints[jjj].location[iii];
					}
				}
			}
			
			mypoints.setCoordinate(jjj, drawPoints[jjj]);
			float[] tempColor = new float[3];
			pointColor.getColorComponents(tempColor);
			mypoints.setColor(jjj, new Color3f(tempColor));
			}
			contents.addChild(new Shape3D(mypoints));
			
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No points");
		   }
		   finally
		   {
			   
		   }
			
	   }
	   }
	   if (slice == true) 
	   {
		   if (planeView)
		   {
			   	//4 cases:
			   //plane doesn't intersect slice
			   //plane intersects slice at point
			   //plane intersects slice at line
			   //plane within slice
			   try 
			   {
				String[] drawMethod = new String[NDimensionalObject.myPlanes.length];
				double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
			   
				for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
				{
					
					   TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
					    
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
					{
					if (((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))
							||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))
						drawMethod[jjj]= "none";
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
						drawMethod[jjj]= "point1";//if all in same plane, setting as just point
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
						drawMethod[jjj]= "point2";
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
						drawMethod[jjj]= "point3";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="line")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
						drawMethod[jjj]= "triangle";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))
									||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
							drawMethod[jjj]= "line12";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
							drawMethod[jjj]= "line13";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
							drawMethod[jjj]= "line23";
					}
						for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (iii==activeAxes[mmm])
						
						{
							drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii];
							drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii];
							drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii];
						}
					}
				}
				if (drawMethod[jjj]=="triangle")
				{
				myplanes.setCoordinate(0, drawPoints[jjj][0]);
				myplanes.setCoordinate(1, drawPoints[jjj][1]);
				myplanes.setCoordinate(2, drawPoints[jjj][2]);
				float[] tempColor = new float[3];
				planeColor.getColorComponents(tempColor);
				myplanes.setColor(0, new Color3f(tempColor)); 
				myplanes.setColor(1, new Color3f(tempColor));
				myplanes.setColor(2, new Color3f(tempColor));
				PolygonAttributes pgonAttrs = new PolygonAttributes();
		        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
		        Appearance tempApp = new Appearance();
		        tempApp.setPolygonAttributes(pgonAttrs);
		        Shape3D temporary = new Shape3D(myplanes);
		        temporary.setAppearance(tempApp);
				contents.addChild(temporary);
				}
				else if (drawMethod[jjj]== "point1")
				{
					PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp.setCoordinate(0, drawPoints[jjj][0]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					contents.addChild(new Shape3D(temp));
				}
				else if (drawMethod[jjj]== "point2")
				{
					PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp.setCoordinate(0, drawPoints[jjj][1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					contents.addChild(new Shape3D(temp));
				}
				else if (drawMethod[jjj]== "point3")
				{
					PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp.setCoordinate(0, drawPoints[jjj][2]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					contents.addChild(new Shape3D(temp));
				}
				else if (drawMethod[jjj]=="line12")
				{
					LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					try
					{
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,1,2,sliceVars);
					temp.setCoordinate(0, lineCoords[0]);
					temp.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					temp.setColor(1, tempColor);
					contents.addChild(new Shape3D(temp));
					}
					catch(Exception g)
					{
						
					}
				}
				else if (drawMethod[jjj]=="line13")
				{
					try
					{
					LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,2,1,sliceVars);
					temp.setCoordinate(0, lineCoords[0]);
					temp.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					temp.setColor(1, tempColor);
					contents.addChild(new Shape3D(temp));
					}
					catch(Exception g)
					{
						
					}
				}
				else if (drawMethod[jjj]=="line23")
				{
					try
					{
					LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,1,2,0,sliceVars);
					temp.setCoordinate(0, lineCoords[0]);
					temp.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					temp.setColor(1, tempColor);
					contents.addChild(new Shape3D(temp));
					}
					catch(Exception g)
					{
						
					}
				}
				}
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No Planes");
			   }
			   finally
			   {
				   
			   }  
		   }
		   //draw lines
		   if (lineView)//need to draw points (could be lines if in plane)
		   {
			   //3 cases to deal with
			 //line doesn't intersect plane
				
				//lines are entirely within viewing plane
			//line intersects plane at 1 point
			  
			   try
			   {
				   String[] drawMethod = new String[NDimensionalObject.myLines.length];
			   double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
			   LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
			  
				for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
				{
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					
					if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
					{
					if (((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]> sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]< sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]< sliceVars[iii])))
						drawMethod[jjj]= "none";
					if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="point")&&(((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]== sliceVars[iii]))))
						drawMethod[jjj]= "inplane";
					if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="inplane"))
						drawMethod[jjj]= "point";
					}
						for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (iii==activeAxes[mmm])
						{   
							drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii];
							drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii];
						}
					}
				}
				
				if (drawMethod[jjj]=="inplane")
				{
				mylines.setCoordinate(0, drawPoints[jjj][0]);
				mylines.setCoordinate(1, drawPoints[jjj][1]);
				float[] tempColor = new float[3];
				lineColor.getColorComponents(tempColor);
				mylines.setColor(0, new Color3f(tempColor)); 
				mylines.setColor(1, new Color3f(tempColor));
				contents.addChild(new Shape3D(mylines));
				}
				else if (drawMethod[jjj]=="point")
				{
					try
					{
					PointArray tempPoint = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					tempPoint.setCoordinate(0, NDimensionalObject.interpolateLine(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index],NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index],sliceVars));
					float[] tempColor = new float[3];
					lineColor.getColorComponents(tempColor);
					tempPoint.setColor(0, tempColor);
					contents.addChild(new Shape3D(tempPoint));
					}
					catch(Exception g)
					{
						
					}
				}
				
				
				}
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No lines");
			   }
			   catch(NullPointerException f)
			   {
				   System.out.println("No slicing variables- 3d or less");
			   }
			   finally
			   {
				   
			   }
				
		   }
		  

		   if (pointView)
		   {
			   try
			   {
				
				Boolean[] canDraw = new Boolean[NDimensionalObject.myPoints.length];
				int canDrawLength = NDimensionalObject.myPoints.length;
				for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
					{
					canDraw[jjj]=true;//assume can draw initially
					for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
						{
						//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
						if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
							{
								if(NDimensionalObject.myPoints[jjj].location[iii] != sliceVars[iii])
								{
								canDraw[jjj]=false;
								canDrawLength -= 1;
								}
							}
						
						
						}
					
					}
			   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
			   PointArray mypoints = new PointArray(canDrawLength, PointArray.COORDINATES|PointArray.COLOR_3);
				int index = 0;
			   for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
				{
					
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					for(int mmm = 0; mmm < 3; mmm++ )//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
					{
						if (iii==activeAxes[mmm])
						{
							drawPoints[jjj][mmm]= NDimensionalObject.myPoints[jjj].location[iii];
						}
					}
				}
				if (canDraw[jjj])
				{
				mypoints.setCoordinate(index, drawPoints[jjj]);
				float[] tempColor = new float[3];
				pointColor.getColorComponents(tempColor);
				mypoints.setColor(index, new Color3f(tempColor));
				index++;
				}
				}
				
				contents.addChild(new Shape3D(mypoints));
				
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No points");
			   }
			   catch(NullPointerException g)
			   {
				   System.out.println("No points");
			   }
			   finally
			   {
				   
			   }
				
		   }
	   
	   }
	 
	   
	   topGroup.addChild(contents);
	   
	
}
/**
 * Draw a single 4d axis.
 * @param universe2
 */
private static void timeAxis(SimpleUniverse universe2) 
{

	BranchGroup objRoot = new BranchGroup();
	objRoot.setCapability(BranchGroup.ALLOW_DETACH);
	TransformGroup objSpin = new TransformGroup();
	objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objRoot.addChild(objSpin);

	BoundingSphere bounds = new BoundingSphere();
	
		universe.getViewer().getView().setProjectionPolicy(View.PERSPECTIVE_PROJECTION);

	//set up transform groups/ objects
	
	Switch mainSwitch = new Switch(Switch.CHILD_MASK);
	mainSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
	mainSwitch.setCapability(Switch.ALLOW_CHILDREN_EXTEND);
	mainSwitch.setCapability(Switch.ALLOW_CHILDREN_READ);
	for( int qqq=0; qqq < subGUIsing4d.timeSlices.length; qqq++)
	{
		sliceVars[subGUIsing4d.AxisOfTime] = subGUIsing4d.timeSlices[qqq];
	OrderedGroup temp = new OrderedGroup();
	
		//draw axis
			   if (axisView)
			   {
				   float[] tempColor = new float[3];
					axesColor.getColorComponents(tempColor);
				  LineArray xAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
				  xAxis.setCoordinate(0, new Point3d(-1,0,0));
				  xAxis.setCoordinate(1, new Point3d(1,0,0));
				  xAxis.setColor(0, new Color3f(tempColor));
				  xAxis.setColor(1, new Color3f(tempColor));
				  Font3D font3d = new Font3D(new Font("Helvetica", Font.PLAIN, 1), new FontExtrusion());
				  Text3D textGeom = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[0]]),new Point3f(0.0f, 0.0f, 0.0f));
				  Shape3D tempText1 = new Shape3D(textGeom);
				  TransformGroup temp1 = new TransformGroup();
				  Transform3D move1 = new Transform3D();
				  move1.set(new Vector3d(1,0,0));
				  move1.setScale(0.1);
				  temp1.setTransform(move1);
				  temp1.addChild(tempText1);
				  LineArray yAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
				  yAxis.setCoordinate(0, new Point3d(0,-1,0));
				  yAxis.setCoordinate(1, new Point3d(0,1,0));
				  yAxis.setColor(0, new Color3f(tempColor));
				  yAxis.setColor(1, new Color3f(tempColor));
				  Text3D textGeom2 = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[1]]),new Point3f(0.0f, 0.0f, 0.0f));
				  Shape3D tempText2 = new Shape3D(textGeom2);
				  TransformGroup temp2 = new TransformGroup();
				  Transform3D move2 = new Transform3D();
				  move2.set(new Vector3d(0,1,0));
				  move2.setScale(0.1);
				  
				  temp2.setTransform(move2);
				  temp2.addChild(tempText2);
				  LineArray zAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
				  zAxis.setCoordinate(0, new Point3d(0,0,-1));
				  zAxis.setCoordinate(1, new Point3d(0,0,1));
				  zAxis.setColor(0, new Color3f(tempColor));
				  zAxis.setColor(1, new Color3f(tempColor));
				  TransformGroup temp3 = new TransformGroup();
				  Transform3D move3 = new Transform3D();
				  Text3D textGeom3 = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[2]]),new Point3f(0.0f, 0.0f, 0.0f));
				  Shape3D tempText3 = new Shape3D(textGeom3);
				  move3.set(new Vector3d(0,0,1));
				  move3.setScale(0.1);
				  
				  temp3.setTransform(move3);
				  temp3.addChild(tempText3);
				  TransformGroup temp4 = new TransformGroup();
				  Transform3D move4 = new Transform3D();
				  Text3D textGeom4 = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[subGUIsing4d.AxisOfTime]) + "=" + Float.toString((float)subGUIsing4d.timeSlices[qqq]),new Point3f(0.0f, 0.0f, 0.0f));
				  Shape3D tempText4 = new Shape3D(textGeom4);
				  move4.set(new Vector3d(0,-1,0));
				  move4.setScale(0.1);
				  temp4.setTransform(move4);
				  temp4.addChild(tempText4);
				  temp.addChild(new Shape3D(xAxis));
				  temp.addChild(new Shape3D(yAxis));
				  temp.addChild(new Shape3D(zAxis));
				  temp.addChild(temp1);
				  temp.addChild(temp2);
				  temp.addChild(temp3);
				  temp.addChild(temp4);
			   }
		
			   if (planeView)
			   {
				   	//4 cases:
				   //plane doesn't intersect slice
				   //plane intersects slice at point
				   //plane intersects slice at line
				   //plane within slice
				   try 
				   {
					String[] drawMethod = new String[NDimensionalObject.myPlanes.length];
					double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];

					for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
					{
						
						   TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
						    
					for(int bbb=0;bbb<NDimensionalObject.NumberOfDimensions; bbb++)
					{
						if ((bbb!=activeAxes[0])&&(bbb!=activeAxes[1])&&(bbb!=activeAxes[2]))
						{
						if (((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> sliceVars[bbb]))
								||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< sliceVars[bbb])))
							drawMethod[jjj]= "none";
						if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]!= sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]!= sliceVars[bbb])))
							drawMethod[jjj]= "point1";//if all in same plane, setting as just point
						if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]!= sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]!= sliceVars[bbb])))
							drawMethod[jjj]= "point2";
						if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]!= sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]!= sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== sliceVars[bbb])))
							drawMethod[jjj]= "point3";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="line")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== sliceVars[bbb])))
							drawMethod[jjj]= "triangle";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> sliceVars[bbb]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< sliceVars[bbb])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== sliceVars[bbb])&&
												(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== sliceVars[bbb])
												&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]!= sliceVars[bbb])))
								drawMethod[jjj]= "line12";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< sliceVars[bbb]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> sliceVars[bbb])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== sliceVars[bbb])&&
												(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]!= sliceVars[bbb])
												&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== sliceVars[bbb])))
								drawMethod[jjj]= "line13";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> sliceVars[bbb]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< sliceVars[bbb])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]!= sliceVars[bbb])&&
												(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== sliceVars[bbb])
												&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== sliceVars[bbb])))
								drawMethod[jjj]= "line23";
						}
							for(int mmm = 0; mmm < 3; mmm++ )
						{
							if (bbb==activeAxes[mmm])
							
							{
								drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb];
								drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb];
								drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb];
							}
						}
					}
					if (drawMethod[jjj]=="triangle")
					{
					myplanes.setCoordinate(0, drawPoints[jjj][0]);
					myplanes.setCoordinate(1, drawPoints[jjj][1]);
					myplanes.setCoordinate(2, drawPoints[jjj][2]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					myplanes.setColor(0, new Color3f(tempColor)); 
					myplanes.setColor(1, new Color3f(tempColor));
					myplanes.setColor(2, new Color3f(tempColor));
					PolygonAttributes pgonAttrs = new PolygonAttributes();
			        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
			        Appearance tempApp = new Appearance();
			        tempApp.setPolygonAttributes(pgonAttrs);
			        Shape3D temporary = new Shape3D(myplanes);
			        temporary.setAppearance(tempApp);
					temp.addChild(temporary);
					}
					else if (drawMethod[jjj]== "point1")
					{
						PointArray temp2 = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						temp2.setCoordinate(0, drawPoints[jjj][0]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp.addChild(new Shape3D(temp2));
					}
					else if (drawMethod[jjj]== "point2")
					{
						PointArray temp2 = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						temp2.setCoordinate(0, drawPoints[jjj][1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp.addChild(new Shape3D(temp2));
					}
					else if (drawMethod[jjj]== "point3")
					{
						PointArray temp2 = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						temp2.setCoordinate(0, drawPoints[jjj][2]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp.addChild(new Shape3D(temp2));
					}
					else if (drawMethod[jjj]=="line12")
					{
						LineArray temp2 = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
						try
						{
						float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,1,2,sliceVars);
						temp2.setCoordinate(0, lineCoords[0]);
						temp2.setCoordinate(1, lineCoords[1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp2.setColor(1, tempColor);
						temp.addChild(new Shape3D(temp2));
						}
						catch(Exception g)
						{
							
						}
					}
					else if (drawMethod[jjj]=="line13")
					{
						try
						{
						LineArray temp2 = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
						float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,2,1,sliceVars);
						temp2.setCoordinate(0, lineCoords[0]);
						temp2.setCoordinate(1, lineCoords[1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp2.setColor(1, tempColor);
						temp.addChild(new Shape3D(temp2));
						}
						catch(Exception g)
						{
							
						}
					}
					else if (drawMethod[jjj]=="line23")
					{
						try
						{
						LineArray temp2 = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
						float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,1,2,0,sliceVars);
						temp2.setCoordinate(0, lineCoords[0]);
						temp2.setCoordinate(1, lineCoords[1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp2.setColor(1, tempColor);
						temp.addChild(new Shape3D(temp2));
						}
						catch(Exception g)
						{
							
						}
					}
					}
					
				   }
				   catch(IllegalArgumentException f)
				   {
					   System.out.println("No Planes");
				   }
				   finally
				   {
					   
				   }  
			   }
			   if (lineView)
			   {
				   //3 cases to deal with
				 //line doesn't intersect plane
					
					//lines are entirely within viewing plane
				//line intersects plane at 1 point
				  
				   try
				   {
					   String[] drawMethod = new String[NDimensionalObject.myLines.length];
				   double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
				   
				    
					for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
					{
						LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
					for(int aaa=0;aaa<NDimensionalObject.NumberOfDimensions; aaa++)
					{
						
						if ((aaa!=activeAxes[0])&&(aaa!=activeAxes[1])&&(aaa!=activeAxes[2]))
						{
						if (((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]> sliceVars[aaa])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]> sliceVars[aaa]))||((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]< sliceVars[aaa])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]< sliceVars[aaa])))
							drawMethod[jjj]= "none";
						if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="point")&&(((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]== sliceVars[aaa])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]== sliceVars[aaa]))))
							drawMethod[jjj]= "inplane";
						if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="inplane"))
							drawMethod[jjj]= "point";
						}
							for(int mmm = 0; mmm < 3; mmm++ )
						{
							if (aaa==activeAxes[mmm])
							{   
								drawPoints[jjj][0][mmm]= (NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]);
								drawPoints[jjj][1][mmm]= (NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]);
							}
						}
					}
					
					if (drawMethod[jjj]=="inplane")
					{
					mylines.setCoordinate(0, drawPoints[jjj][0]);
					mylines.setCoordinate(1, drawPoints[jjj][1]);
					float[] tempColor = new float[3];
					lineColor.getColorComponents(tempColor);
					mylines.setColor(0, new Color3f(tempColor)); 
					mylines.setColor(1, new Color3f(tempColor));
					temp.addChild(new Shape3D(mylines));
					}
					else if (drawMethod[jjj]=="point")
					{
						try
						{
						PointArray tempPoint = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						tempPoint.setCoordinate(0, NDimensionalObject.interpolateLine(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index],NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index],sliceVars));
						float[] tempColor = new float[3];
						lineColor.getColorComponents(tempColor);
						tempPoint.setColor(0, tempColor);
						temp.addChild(new Shape3D(tempPoint));
						}
						catch(Exception g)
						{}
					}
					
					
					}
					
				   }
				   catch(IllegalArgumentException f)
				   {
					   System.out.println("No lines");
				   }
				   catch(NullPointerException f)
				   {
					   System.out.println("No slicing variables- 3d or less");
				   }
				   finally
				   {
					   
				   }
					
			   }
			   if (pointView)
			   {
				   try
				   {
					
					Boolean[] canDraw = new Boolean[NDimensionalObject.myPoints.length];
					int canDrawLength = NDimensionalObject.myPoints.length;
					for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
						{
						canDraw[jjj]=true;//assume can draw initially
						for(int ccc=0;ccc<NDimensionalObject.NumberOfDimensions; ccc++)
							{
							//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
							if ((ccc!=activeAxes[0])&&(ccc!=activeAxes[1])&&(ccc!=activeAxes[2]))
								{
									if(NDimensionalObject.myPoints[jjj].location[ccc] != sliceVars[ccc])
									{
									canDraw[jjj]=false;
									canDrawLength -= 1;
									}
								}
							
							
							}
						
						}
				   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
				   PointArray mypoints = new PointArray(canDrawLength, PointArray.COORDINATES|PointArray.COLOR_3);
					int index = 0;
				   for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
					{
						
					for(int bbb=0;bbb<NDimensionalObject.NumberOfDimensions; bbb++)
					{
						for(int mmm = 0; mmm < 3; mmm++ )//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
						{
							if (bbb==activeAxes[mmm])
							{
								drawPoints[jjj][mmm]= (NDimensionalObject.myPoints[jjj].location[bbb]);
							}
						}
					}
					if (canDraw[jjj])
					{
					mypoints.setCoordinate(index, drawPoints[jjj]);
					
					float[] tempColor = new float[3];
					pointColor.getColorComponents(tempColor);
					mypoints.setColor(index, new Color3f(tempColor));
					index++;
					}
					}
					
					temp.addChild(new Shape3D(mypoints));
					
					
				   }
				   catch(IllegalArgumentException f)
				   {
					   System.out.println("No points (illegal)");
				   }
				   catch(NullPointerException g)
				   {
					   System.out.println("No points (null)");
				   }
				   finally
				   {
					   
				   }
					
			   }
			   
	
	   mainSwitch.addChild(temp);
	  
	}
	//set up alphas to control camera movement, rotation, and object switch
	
	mainSwitch.setWhichChild(0);
	switchAlpha= new Alpha(-1, 6000*(NDimensionalObject.NumberOfDimensions-2));
	SwitchValueInterpolator mySwitch = new SwitchValueInterpolator(switchAlpha,mainSwitch);
	mySwitch.setSchedulingBounds(bounds);
	mySwitch.setEnable(true);
	objSpin.addChild(mySwitch);
	objSpin.addChild(mainSwitch);
	//add all to universe
	topGroup.addChild(objRoot);
	

	
}

/**
 * Draw multiple four-dimensional axes.
 * @param universe2
 */
private static void mult4d(SimpleUniverse universe2) {
	
	int n=NDimensionalObject.NumberOfDimensions;
	int k = 4;
	int numberAxes = 1;
	for (float iii=1; iii<=k; iii++)
	{
		numberAxes= (int) (numberAxes*((n-(k-iii))/iii));
	}
	//get dimension labeling
	String[][] myDimensions = new String[numberAxes][4];
	int[][] dimIndex = new int[numberAxes][4];
	int index2 = 0;
	for(int iii = 0;iii<NDimensionalObject.NumberOfDimensions -3;iii++)
	{
		for( int jjj=iii+1; jjj< NDimensionalObject.NumberOfDimensions-2; jjj++)
		{
			for( int nnn=jjj+1; nnn< NDimensionalObject.NumberOfDimensions-1; nnn++)
			{
				for( int mmm= nnn+1; mmm< NDimensionalObject.NumberOfDimensions; mmm++)
				{
			myDimensions[index2][0]= NDimensionalObject.DimensionLabels[iii];
			myDimensions[index2][1]= NDimensionalObject.DimensionLabels[jjj];
			myDimensions[index2][2]= NDimensionalObject.DimensionLabels[nnn];
			myDimensions[index2][3]= NDimensionalObject.DimensionLabels[mmm];
			dimIndex[index2][0]= iii;
			dimIndex[index2][1]= jjj;
			dimIndex[index2][2]= nnn;
			dimIndex[index2][3]= mmm;
			index2++;
				}
				}
		}
		
	}
	
	BranchGroup objRoot = new BranchGroup();
	objRoot.setCapability(BranchGroup.ALLOW_DETACH);
	TransformGroup objSpin = new TransformGroup();
	objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objSpin.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
	objRoot.addChild(objSpin);

	BoundingSphere bounds = new BoundingSphere();
	
		
	//set up transform groups/ objects
for(int xxx=0; xxx< numberAxes;xxx++)
	{
			   setActive(dimIndex[xxx][0],dimIndex[xxx][1],dimIndex[xxx][2]);
		   //create transformgroup temp
			   TransformGroup contents = new TransformGroup();
			   contents.setCapability(TransformGroup.ALLOW_PARENT_READ);
			   BranchGroup tempBranch = new BranchGroup();
			   tempBranch.setCapability(BranchGroup.ALLOW_DETACH);
			   Transform3D move = new Transform3D();
			   move.set(new Vector3d(distanceBetweenAxes*xxx,0,0));
			   contents.setTransform(move);
	Switch mainSwitch = new Switch(Switch.CHILD_MASK);
	mainSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
	mainSwitch.setCapability(Switch.ALLOW_CHILDREN_EXTEND);
	mainSwitch.setCapability(Switch.ALLOW_CHILDREN_READ);
	for( int qqq=0; qqq < subGUImult4d.timeSlices.length; qqq++)
	{
		sliceVars[dimIndex[xxx][3]] = subGUImult4d.timeSlices[qqq];
	OrderedGroup temp = new OrderedGroup();
	
		//draw axis
			   if (axisView)
			   {
				   float[] tempColor = new float[3];
					axesColor.getColorComponents(tempColor);
				  LineArray xAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
				  xAxis.setCoordinate(0, new Point3d(-1,0,0));
				  xAxis.setCoordinate(1, new Point3d(1,0,0));
				  xAxis.setColor(0, new Color3f(tempColor));
				  xAxis.setColor(1, new Color3f(tempColor));
				  Font3D font3d = new Font3D(new Font("Helvetica", Font.PLAIN, 1), new FontExtrusion());
				  Text3D textGeom = new Text3D(font3d, new String(myDimensions[xxx][0]),new Point3f(0.0f, 0.0f, 0.0f));
				  Shape3D tempText1 = new Shape3D(textGeom);
				  TransformGroup temp1 = new TransformGroup();
				  Transform3D move1 = new Transform3D();
				  move1.set(new Vector3d(1,0,0));
				  move1.setScale(0.1);
				  temp1.setTransform(move1);
				  temp1.addChild(tempText1);
				  LineArray yAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
				  yAxis.setCoordinate(0, new Point3d(0,-1,0));
				  yAxis.setCoordinate(1, new Point3d(0,1,0));
				  yAxis.setColor(0, new Color3f(tempColor));
				  yAxis.setColor(1, new Color3f(tempColor));
				  Text3D textGeom2 = new Text3D(font3d, new String(myDimensions[xxx][1]),new Point3f(0.0f, 0.0f, 0.0f));
				  Shape3D tempText2 = new Shape3D(textGeom2);
				  TransformGroup temp2 = new TransformGroup();
				  Transform3D move2 = new Transform3D();
				  move2.set(new Vector3d(0,1,0));
				  move2.setScale(0.1);
				  
				  temp2.setTransform(move2);
				  temp2.addChild(tempText2);
				  LineArray zAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
				  zAxis.setCoordinate(0, new Point3d(0,0,-1));
				  zAxis.setCoordinate(1, new Point3d(0,0,1));
				  zAxis.setColor(0, new Color3f(tempColor));
				  zAxis.setColor(1, new Color3f(tempColor));
				  TransformGroup temp3 = new TransformGroup();
				  Transform3D move3 = new Transform3D();
				  Text3D textGeom3 = new Text3D(font3d, new String(myDimensions[xxx][2]),new Point3f(0.0f, 0.0f, 0.0f));
				  Shape3D tempText3 = new Shape3D(textGeom3);
				  move3.set(new Vector3d(0,0,1));
				  move3.setScale(0.1);
				  temp3.setTransform(move3);
				  temp3.addChild(tempText3);
				  TransformGroup temp4 = new TransformGroup();
				  Transform3D move4 = new Transform3D();
				  Text3D textGeom4 = new Text3D(font3d, new String(myDimensions[xxx][3]) + "=" + Float.toString((float)subGUImult4d.timeSlices[qqq]),new Point3f(0.0f, 0.0f, 0.0f));
				  Shape3D tempText4 = new Shape3D(textGeom4);
				  move4.set(new Vector3d(0,-1,0));
				  move4.setScale(0.1);
				  temp4.setTransform(move4);
				  temp4.addChild(tempText4);
				  temp.addChild(new Shape3D(xAxis));
				  temp.addChild(new Shape3D(yAxis));
				  temp.addChild(new Shape3D(zAxis));
				  temp.addChild(temp1);
				  temp.addChild(temp2);
				  temp.addChild(temp3);
				  temp.addChild(temp4);
			   }
		
			   if (planeView)
			   {
				   	//4 cases:
				   //plane doesn't intersect slice
				   //plane intersects slice at point
				   //plane intersects slice at line
				   //plane within slice
				   try 
				   {
					String[] drawMethod = new String[NDimensionalObject.myPlanes.length];
					double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
					for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
					{
						
						   TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
						    
					for(int bbb=0;bbb<NDimensionalObject.NumberOfDimensions; bbb++)
					{
						if ((bbb!=activeAxes[0])&&(bbb!=activeAxes[1])&&(bbb!=activeAxes[2]))
						{
						if (((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> sliceVars[bbb]))
								||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< sliceVars[bbb])))
							drawMethod[jjj]= "none";
						if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]!= sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]!= sliceVars[bbb])))
							drawMethod[jjj]= "point1";//if all in same plane, setting as just point
						if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]!= sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]!= sliceVars[bbb])))
							drawMethod[jjj]= "point2";
						if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]!= sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]!= sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== sliceVars[bbb])))
							drawMethod[jjj]= "point3";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="line")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== sliceVars[bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== sliceVars[bbb])))
							drawMethod[jjj]= "triangle";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> sliceVars[bbb]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< sliceVars[bbb])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== sliceVars[bbb])&&
												(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== sliceVars[bbb])
												&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]!= sliceVars[bbb])))
								drawMethod[jjj]= "line12";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< sliceVars[bbb]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> sliceVars[bbb])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== sliceVars[bbb])&&
												(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]!= sliceVars[bbb])
												&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== sliceVars[bbb])))
								drawMethod[jjj]= "line13";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> sliceVars[bbb])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> sliceVars[bbb]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< sliceVars[bbb])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< sliceVars[bbb])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]!= sliceVars[bbb])&&
												(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== sliceVars[bbb])
												&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== sliceVars[bbb])))
								drawMethod[jjj]= "line23";
						}
							for(int mmm = 0; mmm < 3; mmm++ )
						{
							if (bbb==activeAxes[mmm])
							
							{
								drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb];
								drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb];
								drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb];
							}
						}
					}
					if (drawMethod[jjj]=="triangle")
					{
					myplanes.setCoordinate(0, drawPoints[jjj][0]);
					myplanes.setCoordinate(1, drawPoints[jjj][1]);
					myplanes.setCoordinate(2, drawPoints[jjj][2]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					myplanes.setColor(0, new Color3f(tempColor)); 
					myplanes.setColor(1, new Color3f(tempColor));
					myplanes.setColor(2, new Color3f(tempColor));
					PolygonAttributes pgonAttrs = new PolygonAttributes();
			        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
			        Appearance tempApp = new Appearance();
			        tempApp.setPolygonAttributes(pgonAttrs);
			        Shape3D temporary = new Shape3D(myplanes);
			        temporary.setAppearance(tempApp);
					temp.addChild(temporary);
					}
					else if (drawMethod[jjj]== "point1")
					{
						PointArray temp2 = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						temp2.setCoordinate(0, drawPoints[jjj][0]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp.addChild(new Shape3D(temp2));
					}
					else if (drawMethod[jjj]== "point2")
					{
						PointArray temp2 = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						temp2.setCoordinate(0, drawPoints[jjj][1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp.addChild(new Shape3D(temp2));
					}
					else if (drawMethod[jjj]== "point3")
					{
						PointArray temp2 = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						temp2.setCoordinate(0, drawPoints[jjj][2]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp.addChild(new Shape3D(temp2));
					}
					else if (drawMethod[jjj]=="line12")
					{
						LineArray temp2 = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
						try
						{
						float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,1,2,sliceVars);
						temp2.setCoordinate(0, lineCoords[0]);
						temp2.setCoordinate(1, lineCoords[1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp2.setColor(1, tempColor);
						temp.addChild(new Shape3D(temp2));
						}
						catch(Exception g)
						{
							
						}
					}
					else if (drawMethod[jjj]=="line13")
					{
						try
						{
						LineArray temp2 = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
						float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,2,1,sliceVars);
						temp2.setCoordinate(0, lineCoords[0]);
						temp2.setCoordinate(1, lineCoords[1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp2.setColor(1, tempColor);
						temp.addChild(new Shape3D(temp2));
						}
						catch(Exception g)
						{
							
						}
					}
					else if (drawMethod[jjj]=="line23")
					{
						try
						{
						LineArray temp2 = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
						float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,1,2,0,sliceVars);
						temp2.setCoordinate(0, lineCoords[0]);
						temp2.setCoordinate(1, lineCoords[1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp2.setColor(0, tempColor);
						temp2.setColor(1, tempColor);
						temp.addChild(new Shape3D(temp2));
						}
						catch(Exception g)
						{
							
						}
					}
					}
					
				   }
				   catch(IllegalArgumentException f)
				   {
					   System.out.println("No Planes");
				   }
				   finally
				   {
					   
				   }  
			   }
			   if (lineView)//need to draw points (could be lines if in plane)
			   {
				   //3 cases to deal with
				 //line doesn't intersect plane
					
					//lines are entirely within viewing plane
				//line intersects plane at 1 point
				  
				   try
				   {
					   String[] drawMethod = new String[NDimensionalObject.myLines.length];
				   double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
				   
				    
					for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
					{
						LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
					for(int aaa=0;aaa<NDimensionalObject.NumberOfDimensions; aaa++)
					{
						
						if ((aaa!=activeAxes[0])&&(aaa!=activeAxes[1])&&(aaa!=activeAxes[2]))
						{
						if (((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]> sliceVars[aaa])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]> sliceVars[aaa]))||((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]< sliceVars[aaa])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]< sliceVars[aaa])))
							drawMethod[jjj]= "none";
						if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="point")&&(((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]== sliceVars[aaa])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]== sliceVars[aaa]))))
							drawMethod[jjj]= "inplane";
						if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="inplane"))
							drawMethod[jjj]= "point";
						}
							for(int mmm = 0; mmm < 3; mmm++ )
						{
							if (aaa==activeAxes[mmm])
							{   
								drawPoints[jjj][0][mmm]= (NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]);
								drawPoints[jjj][1][mmm]= (NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]);
							}
						}
					}
					
					if (drawMethod[jjj]=="inplane")
					{
					mylines.setCoordinate(0, drawPoints[jjj][0]);
					mylines.setCoordinate(1, drawPoints[jjj][1]);
					float[] tempColor = new float[3];
					lineColor.getColorComponents(tempColor);
					mylines.setColor(0, new Color3f(tempColor)); 
					mylines.setColor(1, new Color3f(tempColor));
					temp.addChild(new Shape3D(mylines));
					}
					else if (drawMethod[jjj]=="point")
					{
						try
						{
						PointArray tempPoint = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						tempPoint.setCoordinate(0, NDimensionalObject.interpolateLine(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index],NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index],sliceVars));
						float[] tempColor = new float[3];
						lineColor.getColorComponents(tempColor);
						tempPoint.setColor(0, tempColor);
						temp.addChild(new Shape3D(tempPoint));
						}
						catch(Exception g)
						{}
					}
					
					
					}
					
				   }
				   catch(IllegalArgumentException f)
				   {
					   System.out.println("No lines");
				   }
				   catch(NullPointerException f)
				   {
					   System.out.println("No slicing variables- 3d or less");
				   }
				   finally
				   {
					   
				   }
					
			   }
			   if (pointView)
			   {
				   try
				   {
					
					Boolean[] canDraw = new Boolean[NDimensionalObject.myPoints.length];
					int canDrawLength = NDimensionalObject.myPoints.length;
					for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
						{
						canDraw[jjj]=true;//assume can draw initially
						for(int ccc=0;ccc<NDimensionalObject.NumberOfDimensions; ccc++)
							{
							//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
							if ((ccc!=activeAxes[0])&&(ccc!=activeAxes[1])&&(ccc!=activeAxes[2]))
								{
									if(NDimensionalObject.myPoints[jjj].location[ccc] != sliceVars[ccc])
									{
									canDraw[jjj]=false;
									canDrawLength -= 1;
									}
								}
							
							
							}
						
						}
				   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
				   PointArray mypoints = new PointArray(canDrawLength, PointArray.COORDINATES|PointArray.COLOR_3);
					int index = 0;
				   for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
					{
						
					for(int bbb=0;bbb<NDimensionalObject.NumberOfDimensions; bbb++)
					{
						for(int mmm = 0; mmm < 3; mmm++ )//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
						{
							if (bbb==activeAxes[mmm])
							{
								drawPoints[jjj][mmm]= (NDimensionalObject.myPoints[jjj].location[bbb]);
							}
						}
					}
					if (canDraw[jjj])
					{
					mypoints.setCoordinate(index, drawPoints[jjj]);
					
					float[] tempColor = new float[3];
					pointColor.getColorComponents(tempColor);
					mypoints.setColor(index, new Color3f(tempColor));
					index++;
					}
					}
					
					temp.addChild(new Shape3D(mypoints));
					
					
				   }
				   catch(IllegalArgumentException f)
				   {
					   System.out.println("No points (illegal)");
				   }
				   catch(NullPointerException g)
				   {
					   System.out.println("No points (null)");
				   }
				   finally
				   {
					   
				   }
					
			   }
			   
	
	   mainSwitch.addChild(temp);
	   
	}
	mainSwitch.setWhichChild(0);
	switchAlpha= new Alpha(-1, 6000*(NDimensionalObject.NumberOfDimensions-2));;
	SwitchValueInterpolator mySwitch = new SwitchValueInterpolator(switchAlpha,mainSwitch);
	mySwitch.setSchedulingBounds(bounds);
	mySwitch.setEnable(true);
	
	contents.addChild(mySwitch);
	contents.addChild(mainSwitch);
	objSpin.addChild(contents);
	//set up alphas to control camera movement, rotation, and object switch
	//add all to universe

	}
	
topGroup.addChild(objRoot);

	
	
}

/**
 * Four-dimensional felder method- unimplemented in this version.
 * @param universe2
 */
private static void felder4d(SimpleUniverse universe2) {
	// TODO Auto-generated method stub
	
}

/**
 * Draw single rotating three-dimensional axis.
 * @param universe2
 */
private static void sing3drotate(SimpleUniverse universe2) {
	
	BranchGroup objRoot = new BranchGroup();
	objRoot.setCapability(BranchGroup.ALLOW_DETACH);
	TransformGroup objSpin = new TransformGroup();
	objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objRoot.addChild(objSpin);

	rotationAlpha  = new Alpha(-1, Alpha.DECREASING_ENABLE|Alpha.INCREASING_ENABLE, 0,0,1000,250,2000,1000,250,2000);//set rotation to be 0 to pi/2
	RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objSpin, new Transform3D(), 0, (float) (Math.PI/2));
	BoundingSphere bounds = new BoundingSphere();
	rotator.setSchedulingBounds(bounds);
	objSpin.addChild(rotator);
	
	   
	
	
	OrderedGroup contents = new OrderedGroup();
	if (axisView)
	   {
		   float[] tempColor = new float[3];
			axesColor.getColorComponents(tempColor);
		  LineArray xAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  xAxis.setCoordinate(0, new Point3d(-1,0,0));
		  xAxis.setCoordinate(1, new Point3d(1,0,0));
		  xAxis.setColor(0, new Color3f(tempColor));
		  xAxis.setColor(1, new Color3f(tempColor));
		  Font3D font3d = new Font3D(new Font("Helvetica", Font.PLAIN, 1), new FontExtrusion());
		  Text3D textGeom = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[0]]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText1 = new Shape3D(textGeom);
		  TransformGroup temp1 = new TransformGroup();
		  Transform3D move1 = new Transform3D();
		  move1.set(new Vector3d(1,0,0));
		  move1.setScale(0.1);
		  temp1.setTransform(move1);
		  temp1.addChild(tempText1);
		  LineArray yAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  yAxis.setCoordinate(0, new Point3d(0,-1,0));
		  yAxis.setCoordinate(1, new Point3d(0,1,0));
		  yAxis.setColor(0, new Color3f(tempColor));
		  yAxis.setColor(1, new Color3f(tempColor));
		  Text3D textGeom2 = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[1]]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText2 = new Shape3D(textGeom2);
		  TransformGroup temp2 = new TransformGroup();
		  Transform3D move2 = new Transform3D();
		  move2.set(new Vector3d(0,1,0));
		  move2.setScale(0.1);
		  
		  temp2.setTransform(move2);
		  temp2.addChild(tempText2);
		  LineArray zAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  zAxis.setCoordinate(0, new Point3d(0,0,-1));
		  zAxis.setCoordinate(1, new Point3d(0,0,1));
		  zAxis.setColor(0, new Color3f(tempColor));
		  zAxis.setColor(1, new Color3f(tempColor));
		  TransformGroup temp3 = new TransformGroup();
		  Transform3D move3 = new Transform3D();
		  Text3D textGeom3 = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[2]]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText3 = new Shape3D(textGeom3);
		  move3.set(new Vector3d(0,0,1));
		  move3.setScale(0.1);
		  
		  temp3.setTransform(move3);
		  temp3.addChild(tempText3);
		  contents.addChild(new Shape3D(xAxis));
		  contents.addChild(new Shape3D(yAxis));
		  contents.addChild(new Shape3D(zAxis));
		  contents.addChild(temp1);
		  contents.addChild(temp2);
		  contents.addChild(temp3);
	   }
	   if (planeView)
	   {

		   try
		   {
			   
		    
			for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
			{
			double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
			TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
				   
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					
					{
						drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii];
						drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii];
						drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii];
					}
				}
			}
			myplanes.setCoordinate(0, drawPoints[jjj][0]);
			myplanes.setCoordinate(1, drawPoints[jjj][1]);
			myplanes.setCoordinate(2, drawPoints[jjj][2]);
			float[] tempColor = new float[3];
			planeColor.getColorComponents(tempColor);
			myplanes.setColor(0, new Color3f(tempColor)); 
			myplanes.setColor(1, new Color3f(tempColor));
			myplanes.setColor(2, new Color3f(tempColor));
			PolygonAttributes pgonAttrs = new PolygonAttributes();
	        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
	        Appearance tempApp = new Appearance();
	        tempApp.setPolygonAttributes(pgonAttrs);
	        Shape3D temporary = new Shape3D(myplanes);
	        temporary.setAppearance(tempApp);
			contents.addChild(temporary);
			}
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No Planes");
		   }
		   finally
		   {
			   
		   }  
	   }
	   //draw lines
	   if (lineView)
	   {
		  
		   try
		   {
			   
		   
		   
			for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
			{
			double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
			LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					
					{
						drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii];
						drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii];
					}
				}
			}
			mylines.setCoordinate(0, drawPoints[jjj][0]);
			mylines.setCoordinate(1, drawPoints[jjj][1]);
			float[] tempColor = new float[3];
			lineColor.getColorComponents(tempColor);
			mylines.setColor(0, new Color3f(tempColor)); 
			mylines.setColor(1, new Color3f(tempColor));
			contents.addChild(new Shape3D(mylines));
			}
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No lines");
		   }
		   finally
		   {
			   
		   }
			
	   }
	  
	   if (pointView)
	   {
		   
		  
		   try
		   {
			   
		   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
		   PointArray mypoints = new PointArray(NDimensionalObject.myPoints.length, PointArray.COORDINATES|PointArray.COLOR_3);
		    
			for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
			{
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					{
						drawPoints[jjj][mmm]= NDimensionalObject.myPoints[jjj].location[iii];
					}
				}
			}
			
			mypoints.setCoordinate(jjj, drawPoints[jjj]);
			
			float[] tempColor = new float[3];
			pointColor.getColorComponents(tempColor);
			mypoints.setColor(jjj, new Color3f(tempColor));
			}
			
			
			contents.addChild(new Shape3D(mypoints));
			
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No points");
		   }
		   finally
		   {
			   
		   }
			
	   }
	   objSpin.addChild(contents);

	
	
	topGroup.addChild(objRoot);


	
}

/**
 * Draw single static three-dimensional axes set.
 * @param universe
 */

private static void sing3d(SimpleUniverse universe) {
	   //create universe/groups
	 
	   BranchGroup contents = new BranchGroup();
	   contents.setCapability(BranchGroup.ALLOW_DETACH);
	   contents.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
	   //draw axes
	   //draw points
	  
	   if (axisView)
	   {
		   float[] tempColor = new float[3];
			axesColor.getColorComponents(tempColor);
		  LineArray xAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  xAxis.setCoordinate(0, new Point3d(-1,0,0));
		  xAxis.setCoordinate(1, new Point3d(1,0,0));
		  xAxis.setColor(0, new Color3f(tempColor));
		  xAxis.setColor(1, new Color3f(tempColor));
		  Font3D font3d = new Font3D(new Font("Helvetica", Font.PLAIN, 1), new FontExtrusion());
		  Text3D textGeom = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[0]]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText1 = new Shape3D(textGeom);
		  TransformGroup temp1 = new TransformGroup();
		  Transform3D move1 = new Transform3D();
		  move1.set(new Vector3d(1,0,0));
		  move1.setScale(0.1);
		  temp1.setTransform(move1);
		  temp1.addChild(tempText1);
		  LineArray yAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  yAxis.setCoordinate(0, new Point3d(0,-1,0));
		  yAxis.setCoordinate(1, new Point3d(0,1,0));
		  yAxis.setColor(0, new Color3f(tempColor));
		  yAxis.setColor(1, new Color3f(tempColor));
		  Text3D textGeom2 = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[1]]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText2 = new Shape3D(textGeom2);
		  TransformGroup temp2 = new TransformGroup();
		  Transform3D move2 = new Transform3D();
		  move2.set(new Vector3d(0,1,0));
		  move2.setScale(0.1);
		  
		  temp2.setTransform(move2);
		  temp2.addChild(tempText2);
		  LineArray zAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  zAxis.setCoordinate(0, new Point3d(0,0,-1));
		  zAxis.setCoordinate(1, new Point3d(0,0,1));
		  zAxis.setColor(0, new Color3f(tempColor));
		  zAxis.setColor(1, new Color3f(tempColor));
		  TransformGroup temp3 = new TransformGroup();
		  Transform3D move3 = new Transform3D();
		  Text3D textGeom3 = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[activeAxes[2]]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText3 = new Shape3D(textGeom3);
		  move3.set(new Vector3d(0,0,1));
		  move3.setScale(0.1);
		  
		  temp3.setTransform(move3);
		  temp3.addChild(tempText3);
		  contents.addChild(new Shape3D(xAxis));
		  contents.addChild(new Shape3D(yAxis));
		  contents.addChild(new Shape3D(zAxis));
		  contents.addChild(temp1);
		  contents.addChild(temp2);
		  contents.addChild(temp3);
	   }
	 //draw planes
	   if (slice == false)
	   {
	   if (planeView)
	   {

		   try
		   {
			   
		    
			   double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
			for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
			{
			
			TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
				  
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					
					{
						drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii];
						drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii];
						drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii];
					}
				}
			}
			
			myplanes.setCoordinate(0, drawPoints[jjj][0]);
			myplanes.setCoordinate(1, drawPoints[jjj][1]);
			myplanes.setCoordinate(2, drawPoints[jjj][2]);
			
			float[] tempColor = new float[3];
			planeColor.getColorComponents(tempColor);
			myplanes.setColor(0, new Color3f(tempColor)); 
			myplanes.setColor(1, new Color3f(tempColor));
			myplanes.setColor(2, new Color3f(tempColor));
			
			PolygonAttributes pgonAttrs = new PolygonAttributes();
	        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
	        Appearance tempApp = new Appearance();
	        tempApp.setPolygonAttributes(pgonAttrs);
	        Shape3D temporary = new Shape3D(myplanes);
	        temporary.setAppearance(tempApp);
			contents.addChild(temporary);
			}
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No Planes");
		   }
		   finally
		   {
			   
		   }  
	   }
	   //draw lines
	   if (lineView)
	   {
		  
		   try
		   {
			   
		   
		    for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
			{
			double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
			LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					
					{
						drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii];
						drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii];
					}
				}
			}
			mylines.setCoordinate(0, drawPoints[jjj][0]);
			mylines.setCoordinate(1, drawPoints[jjj][1]);
			float[] tempColor = new float[3];
			lineColor.getColorComponents(tempColor);
			mylines.setColor(0, new Color3f(tempColor)); 
			mylines.setColor(1, new Color3f(tempColor));
			contents.addChild(new Shape3D(mylines));
			}
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No lines");
		   }
		   finally
		   {
			   
		   }
			
	   }
	   
	   if (pointView)
	   {
		   
		  
		   try
		   {
			  double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
		   PointArray mypoints = new PointArray(NDimensionalObject.myPoints.length, PointArray.COORDINATES|PointArray.COLOR_3);
		    for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
			{
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					{
						drawPoints[jjj][mmm]= NDimensionalObject.myPoints[jjj].location[iii];
					}
				}
			}
			
			mypoints.setCoordinate(jjj, drawPoints[jjj]);
			float[] tempColor = new float[3];
			pointColor.getColorComponents(tempColor);
			mypoints.setColor(jjj, new Color3f(tempColor));
			}
			
			contents.addChild(new Shape3D(mypoints));
			
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No points");
		   }
		   finally
		   {
			   
		   }
			
	   }
	   }
	   if (slice == true) 
	   {
		   if (planeView)
		   {
			   	//4 cases:
			   //plane doesn't intersect slice
			   //plane intersects slice at point
			   //plane intersects slice at line
			   //plane within slice
			   try 
			   {
				String[] drawMethod = new String[NDimensionalObject.myPlanes.length];
				double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
			   for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
				{
					
					   TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
					    
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
					{
					if (((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))
							||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))
						drawMethod[jjj]= "none";
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
						drawMethod[jjj]= "point1";//if all in same plane, setting as just point
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
						drawMethod[jjj]= "point2";
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
						drawMethod[jjj]= "point3";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="line")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
						drawMethod[jjj]= "triangle";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))
									||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
							drawMethod[jjj]= "line12";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
							drawMethod[jjj]= "line13";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
							drawMethod[jjj]= "line23";
					}
						for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (iii==activeAxes[mmm])
						
						{
							drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii];
							drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii];
							drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii];
						}
					}
				}
				if (drawMethod[jjj]=="triangle")
				{
				myplanes.setCoordinate(0, drawPoints[jjj][0]);
				myplanes.setCoordinate(1, drawPoints[jjj][1]);
				myplanes.setCoordinate(2, drawPoints[jjj][2]);
				float[] tempColor = new float[3];
				planeColor.getColorComponents(tempColor);
				myplanes.setColor(0, new Color3f(tempColor)); 
				myplanes.setColor(1, new Color3f(tempColor));
				myplanes.setColor(2, new Color3f(tempColor));
				PolygonAttributes pgonAttrs = new PolygonAttributes();
		        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
		        Appearance tempApp = new Appearance();
		        tempApp.setPolygonAttributes(pgonAttrs);
		        Shape3D temporary = new Shape3D(myplanes);
		        temporary.setAppearance(tempApp);
				contents.addChild(temporary);
				}
				else if (drawMethod[jjj]== "point1")
				{
					PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp.setCoordinate(0, drawPoints[jjj][0]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					contents.addChild(new Shape3D(temp));
				}
				else if (drawMethod[jjj]== "point2")
				{
					PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp.setCoordinate(0, drawPoints[jjj][1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					contents.addChild(new Shape3D(temp));
				}
				else if (drawMethod[jjj]== "point3")
				{
					PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp.setCoordinate(0, drawPoints[jjj][2]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					contents.addChild(new Shape3D(temp));
				}
				else if (drawMethod[jjj]=="line12")
				{
					LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					try
					{
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,1,2,sliceVars);
					temp.setCoordinate(0, lineCoords[0]);
					temp.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					temp.setColor(1, tempColor);
					contents.addChild(new Shape3D(temp));
					}
					catch(Exception g)
					{
						
					}
				}
				else if (drawMethod[jjj]=="line13")
				{
					try
					{
					LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,2,1,sliceVars);
					temp.setCoordinate(0, lineCoords[0]);
					temp.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					temp.setColor(1, tempColor);
					contents.addChild(new Shape3D(temp));
					}
					catch(Exception g)
					{
						
					}
				}
				else if (drawMethod[jjj]=="line23")
				{
					try
					{
					LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,1,2,0,sliceVars);
					temp.setCoordinate(0, lineCoords[0]);
					temp.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					temp.setColor(1, tempColor);
					contents.addChild(new Shape3D(temp));
					}
					catch(Exception g)
					{
						
					}
				}
				}
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No Planes");
			   }
			   finally
			   {
				   
			   }  
		   }
		   //draw lines
		   if (lineView)
		   {
			   //3 cases to deal with
			 //line doesn't intersect plane
				
				//lines are entirely within viewing plane
			//line intersects plane at 1 point
			  
			   try
			   {
				   String[] drawMethod = new String[NDimensionalObject.myLines.length];
			   double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
			   LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
			   
				for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
				{
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					
					if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
					{
					if (((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]> sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]< sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]< sliceVars[iii])))
						drawMethod[jjj]= "none";
					if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="point")&&(((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]== sliceVars[iii]))))
						drawMethod[jjj]= "inplane";
					if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="inplane"))
						drawMethod[jjj]= "point";
					}
						for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (iii==activeAxes[mmm])
						{   
							drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii];
							drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii];
						}
					}
				}
				
				if (drawMethod[jjj]=="inplane")
				{
				mylines.setCoordinate(0, drawPoints[jjj][0]);
				mylines.setCoordinate(1, drawPoints[jjj][1]);
				float[] tempColor = new float[3];
				lineColor.getColorComponents(tempColor);
				mylines.setColor(0, new Color3f(tempColor)); 
				mylines.setColor(1, new Color3f(tempColor));
				contents.addChild(new Shape3D(mylines));
				}
				else if (drawMethod[jjj]=="point")
				{
					try
					{
					PointArray tempPoint = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					tempPoint.setCoordinate(0, NDimensionalObject.interpolateLine(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index],NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index],sliceVars));
					float[] tempColor = new float[3];
					lineColor.getColorComponents(tempColor);
					tempPoint.setColor(0, tempColor);
					contents.addChild(new Shape3D(tempPoint));
					}
					catch(Exception g)
					{
						
					}
				}
				
				
				}
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No lines");
			   }
			   catch(NullPointerException f)
			   {
				   System.out.println("No slicing variables- 3d or less");
			   }
			   finally
			   {
				   
			   }
				
		   }
		  

		   if (pointView)
		   {
			   
			  
			   try
			   {
				
				Boolean[] canDraw = new Boolean[NDimensionalObject.myPoints.length];
				int canDrawLength = NDimensionalObject.myPoints.length;
				for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
					{
					canDraw[jjj]=true;//assume can draw initially
					for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
						{
						//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
						if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
							{
								if(NDimensionalObject.myPoints[jjj].location[iii] != sliceVars[iii])
								{
								canDraw[jjj]=false;
								canDrawLength -= 1;
								}
							}
						
						
						}
					
					}
			   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
			   PointArray mypoints = new PointArray(canDrawLength, PointArray.COORDINATES|PointArray.COLOR_3);
				int index = 0;
			   for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
				{
					
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					for(int mmm = 0; mmm < 3; mmm++ )//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
					{
						if (iii==activeAxes[mmm])
						{
							drawPoints[jjj][mmm]= NDimensionalObject.myPoints[jjj].location[iii];
						}
					}
				}
				if (canDraw[jjj])
				{
				mypoints.setCoordinate(index, drawPoints[jjj]);
				
				float[] tempColor = new float[3];
				pointColor.getColorComponents(tempColor);
				mypoints.setColor(index, new Color3f(tempColor));
				index++;
				}
				}
				
				contents.addChild(new Shape3D(mypoints));
				
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No points");
			   }
			   catch(NullPointerException g)
			   {
				   System.out.println("No points");
			   }
			   finally
			   {
				   
			   }
				
		   }
	   
	   }
	   
	   topGroup.addChild(contents);
	   
}

/**
 * Set active axes.
 * @param i
 * @param j
 * @param k
 */
public static void setActive(int i, int j, int k) {
	activeAxes[0] = i;
	activeAxes[1] = j;
	activeAxes[2] = k;
	
}

/**
 * Return testing canvas with colorcube object.
 * @return
 */
public static Canvas3D test() {
	  	   GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		   Canvas3D canvas = new Canvas3D(config);
		   
		   BranchGroup contents = new BranchGroup();
		   contents.addChild(new ColorCube(0.3));
		   SimpleUniverse universe = new SimpleUniverse(canvas);
		   universe.getViewingPlatform().setNominalViewingTransform();
		   Transform3D rotatecam = new Transform3D();
		   rotatecam.rotX(0.0d); // add camera controls
		   rotatecam.set(new Vector3d(0,0,5));
		   universe.getViewingPlatform().getViewPlatformTransform().setTransform(rotatecam);
		   System.out.println("Test function success");
		   universe.addBranchGraph(contents);
		   return canvas;
}


/**
 * Draw rotating axes method.
 * @param universe2
 */
private static void animated(SimpleUniverse universe2) {
	
	BranchGroup objRoot = new BranchGroup();
	objRoot.setCapability(BranchGroup.ALLOW_DETACH);
	TransformGroup objSpin = new TransformGroup();
	objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objRoot.addChild(objSpin);
	
	
	rotationAlpha = new Alpha(-1, Alpha.DECREASING_ENABLE|Alpha.INCREASING_ENABLE, 0,0,1000,250,2000,1000,250,2000);//set rotation to be 0 to pi/2
	RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objSpin, new Transform3D(), 0, (float) (Math.PI/2));
	
	
	BoundingSphere bounds = new BoundingSphere();
	rotator.setSchedulingBounds(bounds);
	
	objSpin.addChild(rotator);
	
	
	if(animView == "ortho")
	{
	universe.getViewer().getView().setProjectionPolicy(View.PARALLEL_PROJECTION);
	
	
		universe.getViewer().getView().setScreenScalePolicy(View.SCALE_EXPLICIT);
	   universe.getViewer().getView().setScreenScale(0.1);
	}
	else
	{
		universe.getViewer().getView().setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
	
		  universe.getViewingPlatform().getViewPlatformTransform().setTransform(rotatecam);
	}
	//set up transform groups/ objects
	
	Switch mainSwitch = new Switch(Switch.CHILD_MASK);
	mainSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
	mainSwitch.setCapability(Switch.ALLOW_CHILDREN_EXTEND);
	mainSwitch.setCapability(Switch.ALLOW_CHILDREN_READ);
	for( int qqq=0; qqq < NDimensionalObject.NumberOfDimensions-2; qqq++)
	{
		setActive(0,1,qqq+2);
	OrderedGroup contents = new OrderedGroup();
	if (axisView)
	   {
		TransformGroup textSpinGroupX = new TransformGroup(); 
		Transform3D oppositeTransformX = new Transform3D();
		TransformGroup textSpinGroupY = new TransformGroup(); 
		Transform3D oppositeTransformY = new Transform3D();
		TransformGroup textSpinGroupZ = new TransformGroup(); 
		Transform3D oppositeTransformZ = new Transform3D();
		oppositeTransformX.set(new Vector3d(1,0,0));
		oppositeTransformY.set(new Vector3d(0,1,0));
		oppositeTransformZ.set(new Vector3d(0,0,1));
		textSpinGroupX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		textSpinGroupY.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		textSpinGroupZ.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		RotationInterpolator oppositeRotatorX = new RotationInterpolator(rotationAlpha, textSpinGroupX,oppositeTransformX, 0, (float) (- Math.PI/2));
		RotationInterpolator oppositeRotatorY = new RotationInterpolator(rotationAlpha, textSpinGroupY,oppositeTransformY, 0, (float) (- Math.PI/2));
		RotationInterpolator oppositeRotatorZ = new RotationInterpolator(rotationAlpha, textSpinGroupZ,oppositeTransformZ, 0, (float) (- Math.PI/2));
		oppositeRotatorX.setSchedulingBounds(bounds);
		oppositeRotatorY.setSchedulingBounds(bounds);
		oppositeRotatorZ.setSchedulingBounds(bounds);
		textSpinGroupX.addChild(oppositeRotatorX);
		textSpinGroupY.addChild(oppositeRotatorY);
		textSpinGroupZ.addChild(oppositeRotatorZ);
		
		   float[] tempColor = new float[3];
			axesColor.getColorComponents(tempColor);
		  LineArray xAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		
		  xAxis.setCoordinate(0, new Point3d(-1,0,0));
		  xAxis.setCoordinate(1, new Point3d(1,0,0));
		  xAxis.setColor(0, new Color3f(tempColor));
		  xAxis.setColor(1, new Color3f(tempColor));
		  Font3D font3d = new Font3D(new Font("Helvetica", Font.PLAIN, 1), new FontExtrusion());
		  //set up the "x" axis label
		  Text3D textGeom = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[0]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText1 = new Shape3D(textGeom);
		  TransformGroup temp1 = new TransformGroup();
		  Matrix3d rotationMatrix1 = new Matrix3d();
		  Vector3d placementVector1 = new Vector3d();
		  rotator.getTransformAxis().get(rotationMatrix1, placementVector1);
		  Transform3D move1 = new Transform3D();
		  
		  move1.set(new Vector3d(1,0,0));
		  move1.setScale(0.1);
		  
		  temp1.setTransform(move1);
		 
		  temp1.addChild(tempText1);
		  textSpinGroupX.addChild(temp1);
		  
		  LineArray yAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  yAxis.setCoordinate(0, new Point3d(0,-1,0));
		  yAxis.setCoordinate(1, new Point3d(0,1,0));
		  yAxis.setColor(0, new Color3f(tempColor));
		  yAxis.setColor(1, new Color3f(tempColor));
		  Text3D textGeom2 = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[1]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText2 = new Shape3D(textGeom2);
		  TransformGroup temp2 = new TransformGroup();
		  Transform3D move2 = new Transform3D();
		  move2.set(new Vector3d(0,1,0));
		  move2.setScale(0.1);
		  
		  temp2.setTransform(move2);
		  temp2.addChild(tempText2);
		  textSpinGroupY.addChild(temp2);
		  LineArray zAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  zAxis.setCoordinate(0, new Point3d(0,0,-1));
		  zAxis.setCoordinate(1, new Point3d(0,0,1));
		  zAxis.setColor(0, new Color3f(tempColor));
		  zAxis.setColor(1, new Color3f(tempColor));
		  TransformGroup temp3 = new TransformGroup();
		  Transform3D move3 = new Transform3D();
		  Text3D textGeom3 = new Text3D(font3d, new String(NDimensionalObject.DimensionLabels[qqq+2]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText3 = new Shape3D(textGeom3);
		  move3.set(new Vector3d(0,0,1));
		  move3.setScale(0.1);
		  
		  temp3.setTransform(move3);
		  temp3.addChild(tempText3);
		  textSpinGroupZ.addChild(temp3);
		  contents.addChild(new Shape3D(xAxis));
		  contents.addChild(new Shape3D(yAxis));
		  contents.addChild(new Shape3D(zAxis));
		  contents.addChild(textSpinGroupY);
		  contents.addChild(textSpinGroupZ);
		  contents.addChild(textSpinGroupX);
		  
	   }
	   if (planeView)
	   {

		   try
		   {
			   
		    
			for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
			{
			double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
			TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
				   
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					
					{
						drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii];
						drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii];
						drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii];
					}
				}
			}
			myplanes.setCoordinate(0, drawPoints[jjj][0]);
			myplanes.setCoordinate(1, drawPoints[jjj][1]);
			myplanes.setCoordinate(2, drawPoints[jjj][2]);
			float[] tempColor = new float[3];
			planeColor.getColorComponents(tempColor);
			myplanes.setColor(0, new Color3f(tempColor)); 
			myplanes.setColor(1, new Color3f(tempColor));
			myplanes.setColor(2, new Color3f(tempColor));
			PolygonAttributes pgonAttrs = new PolygonAttributes();
	        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
	        Appearance tempApp = new Appearance();
	        tempApp.setPolygonAttributes(pgonAttrs);
	        Shape3D temporary = new Shape3D(myplanes);
	        temporary.setAppearance(tempApp);
			contents.addChild(temporary);
			}
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No Planes");
		   }
		   finally
		   {
			   
		   }  
	   }
	   //draw lines
	   if (lineView)
	   {
		  
		   try
		   {
			   
		   
		    for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
			{
			double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
			LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					
					{
						drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii];
						drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii];
					}
				}
			}
			mylines.setCoordinate(0, drawPoints[jjj][0]);
			mylines.setCoordinate(1, drawPoints[jjj][1]);
			float[] tempColor = new float[3];
			lineColor.getColorComponents(tempColor);
			mylines.setColor(0, new Color3f(tempColor)); 
			mylines.setColor(1, new Color3f(tempColor));
			contents.addChild(new Shape3D(mylines));
			}
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No lines");
		   }
		   finally
		   {
			   
		   }
			
	   }
	   
	   if (pointView)
	   {
		   
		  
		   try
		   {
			   
		   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
		   PointArray mypoints = new PointArray(NDimensionalObject.myPoints.length, PointArray.COORDINATES|PointArray.COLOR_3);
		    
			for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
			{
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					{
						drawPoints[jjj][mmm]= NDimensionalObject.myPoints[jjj].location[iii];
					}
				}
			}
			
			mypoints.setCoordinate(jjj, drawPoints[jjj]);
			float[] tempColor = new float[3];
			pointColor.getColorComponents(tempColor);
			mypoints.setColor(jjj, new Color3f(tempColor));
			}
			
			contents.addChild(new Shape3D(mypoints));
			
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No points");
		   }
		   finally
		   {
			   
		   }
			
	   }
	   
	   mainSwitch.addChild(contents);
	   
	   
	   
	   
	}
	mainSwitch.setWhichChild(0);
	long alphaLength = (NDimensionalObject.NumberOfDimensions-2)*6000;//((object.NumberOfDimensions-2)*(rotationAlpha.getDecreasingAlphaDuration()+rotationAlpha.getAlphaAtOneDuration()+rotationAlpha.getAlphaAtZeroDuration()+rotationAlpha.getIncreasingAlphaDuration()));
	long offsetFactorat1 = 2000;
	long offsetFactorat0 = 0;

	
	switchAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0,0,alphaLength-offsetFactorat1-offsetFactorat0,2000,offsetFactorat1,0,0,offsetFactorat0);
	SwitchValueInterpolator mySwitch = new SwitchValueInterpolator(switchAlpha,mainSwitch);
	mySwitch.setSchedulingBounds(bounds);
	mySwitch.setEnable(true);
	objSpin.addChild(mySwitch);
	objSpin.addChild(mainSwitch);
	
	//add all to universe
	topGroup.addChild(objRoot);


}

/**
 * Spiral recursive representation- not implemented in this version.
 * @return
 */
private static Canvas3D spiral() {
	// TODO Auto-generated method stub
	return null;
}

/**
 * Draw felder representation.
 * @param universe2
 */
private static void felder(SimpleUniverse universe2) {
	setActive(0,1,2);
	   //for each axis set:
	   try
	   {
	   for(int iii=0; iii< felderVectors.length;iii++)
	   {
	   //create transformgroup temp
		   TransformGroup temp = new TransformGroup();
		   temp.setCapability(TransformGroup.ALLOW_PARENT_READ);
		   BranchGroup tempBranch = new BranchGroup();
		   tempBranch.setCapability(BranchGroup.ALLOW_DETACH);
		   Transform3D move = new Transform3D();
		   move.set(felderVectors[iii]);
		   temp.setTransform(move);
	//draw axis
		   if (axisView)
		   {
			   float[] tempColor = new float[3];
				axesColor.getColorComponents(tempColor);
			  LineArray xAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
			  xAxis.setCoordinate(0, new Point3d(-1,0,0));
			  xAxis.setCoordinate(1, new Point3d(1,0,0));
			  xAxis.setColor(0, new Color3f(tempColor));
			  xAxis.setColor(1, new Color3f(tempColor));
			  LineArray yAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
			  yAxis.setCoordinate(0, new Point3d(0,-1,0));
			  yAxis.setCoordinate(1, new Point3d(0,1,0));
			  yAxis.setColor(0, new Color3f(tempColor));
			  yAxis.setColor(1, new Color3f(tempColor));
			  LineArray zAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
			  zAxis.setCoordinate(0, new Point3d(0,0,-1));
			  zAxis.setCoordinate(1, new Point3d(0,0,1));
			  zAxis.setColor(0, new Color3f(tempColor));
			  zAxis.setColor(1, new Color3f(tempColor));
			  temp.addChild(new Shape3D(xAxis));
			  temp.addChild(new Shape3D(yAxis));
			  temp.addChild(new Shape3D(zAxis));
		   }
	//draw text to mark each
		   Text2D tempText = new Text2D(felderStrings[iii],new Color3f(0.9f, 1.0f, 1.0f),"Helvetica", 18, Font.ITALIC);
		   temp.addChild(tempText);
		   if (planeView)
		   {
			   	//4 cases:
			   //plane doesn't intersect slice
			   //plane intersects slice at point
			   //plane intersects slice at line
			   //plane within slice
			   try //not selecting right drawing method
			   {
				String[] drawMethod = new String[NDimensionalObject.myPlanes.length];
				double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
				for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
				{
					
					   TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
					    
				for(int bbb=0;bbb<NDimensionalObject.NumberOfDimensions; bbb++)
				{
					if ((bbb!=activeAxes[0])&&(bbb!=activeAxes[1])&&(bbb!=activeAxes[2]))
					{
					if (((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> felderSliceVars[iii][bbb])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> felderSliceVars[iii][bbb])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> felderSliceVars[iii][bbb]))
							||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< felderSliceVars[iii][bbb])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< felderSliceVars[iii][bbb])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< felderSliceVars[iii][bbb])))
						drawMethod[jjj]= "none";
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== felderSliceVars[iii][bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]!= felderSliceVars[iii][bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]!= felderSliceVars[iii][bbb])))
						drawMethod[jjj]= "point1";//if all in same plane, setting as just point
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]!= felderSliceVars[iii][bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== felderSliceVars[iii][bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]!= felderSliceVars[iii][bbb])))
						drawMethod[jjj]= "point2";
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]!= felderSliceVars[iii][bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]!= felderSliceVars[iii][bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== felderSliceVars[iii][bbb])))
						drawMethod[jjj]= "point3";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="line")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== felderSliceVars[iii][bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== felderSliceVars[iii][bbb])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== felderSliceVars[iii][bbb])))
						drawMethod[jjj]= "triangle";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< felderSliceVars[iii][bbb])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< felderSliceVars[iii][bbb])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> felderSliceVars[iii][bbb]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> felderSliceVars[iii][bbb])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> felderSliceVars[iii][bbb])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< felderSliceVars[iii][bbb])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== felderSliceVars[iii][bbb])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== felderSliceVars[iii][bbb])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]!= felderSliceVars[iii][bbb])))
							drawMethod[jjj]= "line12";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< felderSliceVars[iii][bbb])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> felderSliceVars[iii][bbb])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< felderSliceVars[iii][bbb]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> felderSliceVars[iii][bbb])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< felderSliceVars[iii][bbb])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> felderSliceVars[iii][bbb])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]== felderSliceVars[iii][bbb])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]!= felderSliceVars[iii][bbb])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== felderSliceVars[iii][bbb])))
							drawMethod[jjj]= "line13";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]< felderSliceVars[iii][bbb])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]> felderSliceVars[iii][bbb])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]> felderSliceVars[iii][bbb]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]> felderSliceVars[iii][bbb])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]< felderSliceVars[iii][bbb])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]< felderSliceVars[iii][bbb])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb]!= felderSliceVars[iii][bbb])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb]== felderSliceVars[iii][bbb])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb]== felderSliceVars[iii][bbb])))
							drawMethod[jjj]= "line23";
					}
						for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (bbb==activeAxes[mmm])
						
						{
							drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[bbb];
							drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[bbb];
							drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[bbb];
						}
					}
				}
				if (drawMethod[jjj]=="triangle")
				{
				myplanes.setCoordinate(0, drawPoints[jjj][0]);
				myplanes.setCoordinate(1, drawPoints[jjj][1]);
				myplanes.setCoordinate(2, drawPoints[jjj][2]);
				float[] tempColor = new float[3];
				planeColor.getColorComponents(tempColor);
				myplanes.setColor(0, new Color3f(tempColor)); 
				myplanes.setColor(1, new Color3f(tempColor));
				myplanes.setColor(2, new Color3f(tempColor));
				PolygonAttributes pgonAttrs = new PolygonAttributes();
		        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
		        Appearance tempApp = new Appearance();
		        tempApp.setPolygonAttributes(pgonAttrs);
		        Shape3D temporary = new Shape3D(myplanes);
		        temporary.setAppearance(tempApp);
				temp.addChild(temporary);
				}
				else if (drawMethod[jjj]== "point1")
				{
					PointArray temp2 = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp2.setCoordinate(0, drawPoints[jjj][0]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp2.setColor(0, tempColor);
					temp.addChild(new Shape3D(temp2));
				}
				else if (drawMethod[jjj]== "point2")
				{
					PointArray temp2 = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp2.setCoordinate(0, drawPoints[jjj][1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp2.setColor(0, tempColor);
					temp.addChild(new Shape3D(temp2));
				}
				else if (drawMethod[jjj]== "point3")
				{
					PointArray temp2 = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp2.setCoordinate(0, drawPoints[jjj][2]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp2.setColor(0, tempColor);
					temp.addChild(new Shape3D(temp2));
				}
				else if (drawMethod[jjj]=="line12")
				{
					LineArray temp2 = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					try
					{
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,1,2,felderSliceVars[iii]);
					temp2.setCoordinate(0, lineCoords[0]);
					temp2.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp2.setColor(0, tempColor);
					temp2.setColor(1, tempColor);
					temp.addChild(new Shape3D(temp2));
					}
					catch(Exception g)
					{
						
					}
				}
				else if (drawMethod[jjj]=="line13")
				{
					try
					{
					LineArray temp2 = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,2,1,felderSliceVars[iii]);
					temp2.setCoordinate(0, lineCoords[0]);
					temp2.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp2.setColor(0, tempColor);
					temp2.setColor(1, tempColor);
					temp.addChild(new Shape3D(temp2));
					}
					catch(Exception g)
					{
						
					}
				}
				else if (drawMethod[jjj]=="line23")
				{
					try
					{
					LineArray temp2 = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,1,2,0,felderSliceVars[iii]);
					temp2.setCoordinate(0, lineCoords[0]);
					temp2.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp2.setColor(0, tempColor);
					temp2.setColor(1, tempColor);
					temp.addChild(new Shape3D(temp2));
					}
					catch(Exception g)
					{
						
					}
				}
				}
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No Planes");
			   }
			   finally
			   {
				   
			   }  
		   }
		   if (lineView)//need to draw points (could be lines if in plane)
		   {
			   //3 cases to deal with
			 //line doesn't intersect plane
				
				//lines are entirely within viewing plane
			//line intersects plane at 1 point
			  
			   try
			   {
				   String[] drawMethod = new String[NDimensionalObject.myLines.length];
			   double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
			   
			    
				for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
				{
					LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
				for(int aaa=0;aaa<NDimensionalObject.NumberOfDimensions; aaa++)
				{
					
					if ((aaa!=activeAxes[0])&&(aaa!=activeAxes[1])&&(aaa!=activeAxes[2]))
					{
					if (((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]> felderSliceVars[iii][aaa])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]> felderSliceVars[iii][aaa]))||((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]< felderSliceVars[iii][aaa])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]< felderSliceVars[iii][aaa])))
						drawMethod[jjj]= "none";
					if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="point")&&(((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]== felderSliceVars[iii][aaa])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]== felderSliceVars[iii][aaa]))))
						drawMethod[jjj]= "inplane";
					if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="inplane"))
						drawMethod[jjj]= "point";
					}
						for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (aaa==activeAxes[mmm])
						{   
							drawPoints[jjj][0][mmm]= (NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[aaa]);
							drawPoints[jjj][1][mmm]= (NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[aaa]);
						}
					}
				}
				
				if (drawMethod[jjj]=="inplane")
				{
				mylines.setCoordinate(0, drawPoints[jjj][0]);
				mylines.setCoordinate(1, drawPoints[jjj][1]);
				float[] tempColor = new float[3];
				lineColor.getColorComponents(tempColor);
				mylines.setColor(0, new Color3f(tempColor)); 
				mylines.setColor(1, new Color3f(tempColor));
				temp.addChild(new Shape3D(mylines));
				}
				else if (drawMethod[jjj]=="point")
				{
					try
					{
					PointArray tempPoint = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					tempPoint.setCoordinate(0, NDimensionalObject.interpolateLine(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index],NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index],felderSliceVars[iii]));
					float[] tempColor = new float[3];
					lineColor.getColorComponents(tempColor);
					tempPoint.setColor(0, tempColor);
					temp.addChild(new Shape3D(tempPoint));
					}
					catch(Exception g)
					{}
				}
				
				
				}
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No lines");
			   }
			   catch(NullPointerException f)
			   {
				   System.out.println("No slicing variables- 3d or less");
			   }
			   finally
			   {
				   
			   }
				
		   }
		   if (pointView)
		   {
			   try
			   {
				
				Boolean[] canDraw = new Boolean[NDimensionalObject.myPoints.length];
				int canDrawLength = NDimensionalObject.myPoints.length;
				for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
					{
					canDraw[jjj]=true;//assume can draw initially
					for(int ccc=0;ccc<NDimensionalObject.NumberOfDimensions; ccc++)
						{
						//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
						if ((ccc!=activeAxes[0])&&(ccc!=activeAxes[1])&&(ccc!=activeAxes[2]))
							{
								if(NDimensionalObject.myPoints[jjj].location[ccc] != felderSliceVars[iii][ccc])
								{
								canDraw[jjj]=false;
								canDrawLength -= 1;
								}
							}
						
						
						}
					
					}
			   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
			   PointArray mypoints = new PointArray(canDrawLength, PointArray.COORDINATES|PointArray.COLOR_3);
				int index = 0;
			   for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
				{
					
				for(int bbb=0;bbb<NDimensionalObject.NumberOfDimensions; bbb++)
				{
					for(int mmm = 0; mmm < 3; mmm++ )//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
					{
						if (bbb==activeAxes[mmm])
						{
							drawPoints[jjj][mmm]= (NDimensionalObject.myPoints[jjj].location[bbb]);
						}
					}
				}
				if (canDraw[jjj])
				{
				mypoints.setCoordinate(index, drawPoints[jjj]);
				
				float[] tempColor = new float[3];
				pointColor.getColorComponents(tempColor);
				mypoints.setColor(index, new Color3f(tempColor));
				index++;
				}
				}
				
				temp.addChild(new Shape3D(mypoints));
				
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No points (illegal)");
			   }
			   catch(NullPointerException g)
			   {
				   System.out.println("No points (null)");
			   }
			   finally
			   {
				   
			   }
				
		   }
		   

	   //add to transformgroup
		   
		   tempBranch.addChild(temp);
		   topGroup.addChild(tempBranch);
	   }
	   }
	   catch(NullPointerException F)
	   {
		   
	   }
	   }

/**
 * Draw multiple two-dimensional axes method.
 * @param universe2
 */
private static void mult2d(SimpleUniverse universe2) {
	//create universe/groups
	
	int n=NDimensionalObject.NumberOfDimensions;
	int k = 2;
	int numberAxes = 1;
	for (float iii=1; iii<=k; iii++)
	{
		numberAxes= (int) (numberAxes*((n-(k-iii))/iii));
	}
	//get dimension labeling
	String[][] myDimensions = new String[numberAxes][2];
	int[][] dimIndex = new int[numberAxes][2];
	int index1 = 0;
	for(int iii = 1;iii<NDimensionalObject.NumberOfDimensions;iii++)
	{
		for( int jjj=0; jjj< NDimensionalObject.NumberOfDimensions-iii; jjj++)
		{
			myDimensions[index1][0]= NDimensionalObject.DimensionLabels[iii-1];
			myDimensions[index1][1]= NDimensionalObject.DimensionLabels[iii+jjj];
			dimIndex[index1][0]= iii-1;
			dimIndex[index1][1]= iii+jjj;
			index1++;
		}
		
	}
	//draw axes sets
	   for(int xxx = 0; xxx < numberAxes; xxx++)
	   {
		   setActive(dimIndex[xxx][0],dimIndex[xxx][1],-1);
		   TransformGroup contents = new TransformGroup();
		   contents.setCapability(TransformGroup.ALLOW_PARENT_READ);
		   BranchGroup tempBranch = new BranchGroup();
		   tempBranch.setCapability(BranchGroup.ALLOW_DETACH);
		   Transform3D move = new Transform3D();
		   move.set(new Vector3d(distanceBetweenAxes*xxx,0,0));
		   contents.setTransform(move);
		   tempBranch.addChild(contents);
	   if (axisView)
	   {
		   float[] tempColor = new float[3];
			axesColor.getColorComponents(tempColor);
		  LineArray xAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  xAxis.setCoordinate(0, new Point3d(-1,0,0));
		  xAxis.setCoordinate(1, new Point3d(1,0,0));
		  xAxis.setColor(0, new Color3f(tempColor));
		  xAxis.setColor(1, new Color3f(tempColor));
		  Font3D font3d = new Font3D(new Font("Helvetica", Font.PLAIN, 1), new FontExtrusion());
		  Text3D textGeom = new Text3D(font3d, new String(myDimensions[xxx][0]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText1 = new Shape3D(textGeom);
		  TransformGroup temp1 = new TransformGroup();
		  Transform3D move1 = new Transform3D();
		  move1.set(new Vector3d(1,0,0));
		  move1.setScale(0.1);
		  temp1.setTransform(move1);
		  temp1.addChild(tempText1);
		  LineArray yAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  yAxis.setCoordinate(0, new Point3d(0,-1,0));
		  yAxis.setCoordinate(1, new Point3d(0,1,0));
		  yAxis.setColor(0, new Color3f(tempColor));
		  yAxis.setColor(1, new Color3f(tempColor));
		  Text3D textGeom2 = new Text3D(font3d, new String(myDimensions[xxx][1]),new Point3f(0.0f, 0.0f, 0.0f));
		  Shape3D tempText2 = new Shape3D(textGeom2);
		  TransformGroup temp2 = new TransformGroup();
		  Transform3D move2 = new Transform3D();
		  move2.set(new Vector3d(0,1,0));
		  move2.setScale(0.1);
		  
		  temp2.setTransform(move2);
		  temp2.addChild(tempText2);
		  
		  contents.addChild(new Shape3D(xAxis));
		  contents.addChild(new Shape3D(yAxis));
		 
		  contents.addChild(temp1);
		  contents.addChild(temp2);
		 
	   }
	 //draw planes
	   if (slice == false)
	   {
	   if (planeView)
	   {

		   try
		   {
			   
			   double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
			for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
			{
			
			TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
				   
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					
					{
						drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii];
						drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii];
						drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii];
					}
				}
			}
			
			myplanes.setCoordinate(0, drawPoints[jjj][0]);
			myplanes.setCoordinate(1, drawPoints[jjj][1]);
			myplanes.setCoordinate(2, drawPoints[jjj][2]);
			
			float[] tempColor = new float[3];
			planeColor.getColorComponents(tempColor);
			myplanes.setColor(0, new Color3f(tempColor)); 
			myplanes.setColor(1, new Color3f(tempColor));
			myplanes.setColor(2, new Color3f(tempColor));
			
			PolygonAttributes pgonAttrs = new PolygonAttributes();
	        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
	        Appearance tempApp = new Appearance();
	        tempApp.setPolygonAttributes(pgonAttrs);
	        Shape3D temporary = new Shape3D(myplanes);
	        temporary.setAppearance(tempApp);
			contents.addChild(temporary);
			}
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No Planes");
		   }
		   finally
		   {
			   
		   }  
	   }
	   //draw lines
	   if (lineView)
	   {
		  
		   try
		   {
			   
		   
			for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
			{
			double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
			LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					
					{
						drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii];
						drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii];
					}
				}
			}
			mylines.setCoordinate(0, drawPoints[jjj][0]);
			mylines.setCoordinate(1, drawPoints[jjj][1]);
			float[] tempColor = new float[3];
			lineColor.getColorComponents(tempColor);
			mylines.setColor(0, new Color3f(tempColor)); 
			mylines.setColor(1, new Color3f(tempColor));
			contents.addChild(new Shape3D(mylines));
			}
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No lines");
		   }
		   finally
		   {
			   
		   }
			
	   }
	   
	   if (pointView)
	   {
		   
		  
		   try
		   {
			   
		   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
		   PointArray mypoints = new PointArray(NDimensionalObject.myPoints.length, PointArray.COORDINATES|PointArray.COLOR_3);
		   
			for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
			{
			for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
			{
				for(int mmm = 0; mmm < 3; mmm++ )
				{
					if (iii==activeAxes[mmm])
					{
						drawPoints[jjj][mmm]= NDimensionalObject.myPoints[jjj].location[iii];
					}
				}
			}
			
			mypoints.setCoordinate(jjj, drawPoints[jjj]);
			
			float[] tempColor = new float[3];
			pointColor.getColorComponents(tempColor);
			mypoints.setColor(jjj, new Color3f(tempColor));
			}
			
			contents.addChild(new Shape3D(mypoints));
			
			
		   }
		   catch(IllegalArgumentException f)
		   {
			   System.out.println("No points");
		   }
		   finally
		   {
			   
		   }
			
	   }
	   }
	   if (slice == true) 
	   {
		   if (planeView)
		   {
			   	//4 cases:
			   //plane doesn't intersect slice
			   //plane intersects slice at point
			   //plane intersects slice at line
			   //plane within slice
			   try 
			   {
				String[] drawMethod = new String[NDimensionalObject.myPlanes.length];
				double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
			   
				for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
				{
					
					   TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
					    
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
					{
					if (((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))
							||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))
						drawMethod[jjj]= "none";
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
						drawMethod[jjj]= "point1";//if all in same plane, setting as just point
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
						drawMethod[jjj]= "point2";
					if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
						drawMethod[jjj]= "point3";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="line")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
						drawMethod[jjj]= "triangle";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))
									||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
							drawMethod[jjj]= "line12";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
							drawMethod[jjj]= "line13";
					if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
							&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
									&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&
											(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])
											&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
							drawMethod[jjj]= "line23";
					}
						for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (iii==activeAxes[mmm])
						
						{
							drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii];
							drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii];
							drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii];
						}
					}
				}
				if (drawMethod[jjj]=="triangle")
				{
				myplanes.setCoordinate(0, drawPoints[jjj][0]);
				myplanes.setCoordinate(1, drawPoints[jjj][1]);
				myplanes.setCoordinate(2, drawPoints[jjj][2]);
				float[] tempColor = new float[3];
				planeColor.getColorComponents(tempColor);
				myplanes.setColor(0, new Color3f(tempColor)); 
				myplanes.setColor(1, new Color3f(tempColor));
				myplanes.setColor(2, new Color3f(tempColor));
				PolygonAttributes pgonAttrs = new PolygonAttributes();
		        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
		        Appearance tempApp = new Appearance();
		        tempApp.setPolygonAttributes(pgonAttrs);
		        Shape3D temporary = new Shape3D(myplanes);
		        temporary.setAppearance(tempApp);
				contents.addChild(temporary);
				}
				else if (drawMethod[jjj]== "point1")
				{
					PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp.setCoordinate(0, drawPoints[jjj][0]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					contents.addChild(new Shape3D(temp));
				}
				else if (drawMethod[jjj]== "point2")
				{
					PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp.setCoordinate(0, drawPoints[jjj][1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					contents.addChild(new Shape3D(temp));
				}
				else if (drawMethod[jjj]== "point3")
				{
					PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					temp.setCoordinate(0, drawPoints[jjj][2]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					contents.addChild(new Shape3D(temp));
				}
				else if (drawMethod[jjj]=="line12")
				{
					LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					try
					{
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,1,2,sliceVars);
					temp.setCoordinate(0, lineCoords[0]);
					temp.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					temp.setColor(1, tempColor);
					contents.addChild(new Shape3D(temp));
					}
					catch(Exception g)
					{
						
					}
				}
				else if (drawMethod[jjj]=="line13")
				{
					try
					{
					LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,2,1,sliceVars);
					temp.setCoordinate(0, lineCoords[0]);
					temp.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					temp.setColor(1, tempColor);
					contents.addChild(new Shape3D(temp));
					}
					catch(Exception g)
					{
						
					}
				}
				else if (drawMethod[jjj]=="line23")
				{
					try
					{
					LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
					float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,1,2,0,sliceVars);
					temp.setCoordinate(0, lineCoords[0]);
					temp.setCoordinate(1, lineCoords[1]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					temp.setColor(0, tempColor);
					temp.setColor(1, tempColor);
					contents.addChild(new Shape3D(temp));
					}
					catch(Exception g)
					{
						
					}
				}
				}
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No Planes");
			   }
			   finally
			   {
				   
			   }  
		   }
		   //draw lines
		   if (lineView)
		   {
			   //3 cases to deal with
			 //line doesn't intersect plane
				
				//lines are entirely within viewing plane
			//line intersects plane at 1 point
			  
			   try
			   {
				   String[] drawMethod = new String[NDimensionalObject.myLines.length];
			   double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
			   LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
	
				for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
				{
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
					{
					if (((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]> sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]< sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]< sliceVars[iii])))
						drawMethod[jjj]= "none";
					if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="point")&&(((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]== sliceVars[iii]))))
						drawMethod[jjj]= "inplane";
					if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="inplane"))
						drawMethod[jjj]= "point";
					}
						for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (iii==activeAxes[mmm])
						{   
							drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii];
							drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii];
						}
					}
				}
				
				if (drawMethod[jjj]=="inplane")
				{
				mylines.setCoordinate(0, drawPoints[jjj][0]);
				mylines.setCoordinate(1, drawPoints[jjj][1]);
				float[] tempColor = new float[3];
				lineColor.getColorComponents(tempColor);
				mylines.setColor(0, new Color3f(tempColor)); 
				mylines.setColor(1, new Color3f(tempColor));
				contents.addChild(new Shape3D(mylines));
				}
				else if (drawMethod[jjj]=="point")
				{
					try
					{
					PointArray tempPoint = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
					tempPoint.setCoordinate(0, NDimensionalObject.interpolateLine(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index],NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index],sliceVars));
					float[] tempColor = new float[3];
					lineColor.getColorComponents(tempColor);
					tempPoint.setColor(0, tempColor);
					contents.addChild(new Shape3D(tempPoint));
					}
					catch(Exception g)
					{
						
					}
				}
				
				
				}
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No lines");
			   }
			   catch(NullPointerException f)
			   {
				   System.out.println("No slicing variables- 3d or less");
			   }
			   finally
			   {
				   
			   }
				
		   }
		  

		   if (pointView)
		   {
			   
			  
			   try
			   {
				
				Boolean[] canDraw = new Boolean[NDimensionalObject.myPoints.length];
				int canDrawLength = NDimensionalObject.myPoints.length;
				for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
					{
					canDraw[jjj]=true;//assume can draw initially
					for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
						{
						//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
						if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
							{
								if(NDimensionalObject.myPoints[jjj].location[iii] != sliceVars[iii])
								{
								canDraw[jjj]=false;
								canDrawLength -= 1;
								}
							}
						
						
						}
					
					}
			   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
			   PointArray mypoints = new PointArray(canDrawLength, PointArray.COORDINATES|PointArray.COLOR_3);
				int index = 0;
			   for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
				{
					
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					for(int mmm = 0; mmm < 3; mmm++ )//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
					{
						if (iii==activeAxes[mmm])
						{
							drawPoints[jjj][mmm]= NDimensionalObject.myPoints[jjj].location[iii];
						}
					}
				}
				if (canDraw[jjj])
				{
				mypoints.setCoordinate(index, drawPoints[jjj]);
				
				float[] tempColor = new float[3];
				pointColor.getColorComponents(tempColor);
				mypoints.setColor(index, new Color3f(tempColor));
				index++;
				}
				}
				
				contents.addChild(new Shape3D(mypoints));
				
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No points");
			   }
			   catch(NullPointerException g)
			   {
				   System.out.println("No points");
			   }
			   finally
			   {
				   
			   }
				
		   }
	   
	   }
	   topGroup.addChild(tempBranch);
}
	  
	   
	   
	
}

/**
 * Draw multiple three-dimensional axes.
 */
private static void mult3d() {
	//determine #
	int n=NDimensionalObject.NumberOfDimensions;
	int k = 3;
	int numberAxes = 1;
	for (float iii=1; iii<=k; iii++)
	{
		numberAxes= (int) (numberAxes*((n-(k-iii))/iii));
	}
	//get dimension labeling
	String[][] myDimensions = new String[numberAxes][3];
	int[][] dimIndex = new int[numberAxes][3];
	int index2 = 0;
	for(int iii = 0;iii<NDimensionalObject.NumberOfDimensions -2;iii++)
	{
		for( int jjj=iii+1; jjj< NDimensionalObject.NumberOfDimensions-1; jjj++)
		{
			for( int nnn=jjj+1; nnn< NDimensionalObject.NumberOfDimensions; nnn++)
			{
			myDimensions[index2][0]= NDimensionalObject.DimensionLabels[iii];
			myDimensions[index2][1]= NDimensionalObject.DimensionLabels[jjj];
			myDimensions[index2][2]= NDimensionalObject.DimensionLabels[nnn];
			dimIndex[index2][0]= iii;
			dimIndex[index2][1]= jjj;
			dimIndex[index2][2]= nnn;
			index2++;
			}
		}
		
	}
	
	   
	   //for each axis set:
	   try
	   {
	   for(int qqq=0; qqq< numberAxes;qqq++)
	   {
		   setActive(dimIndex[qqq][0],dimIndex[qqq][1],dimIndex[qqq][2]);
	   //create transformgroup temp
		   TransformGroup contents = new TransformGroup();
		   contents.setCapability(TransformGroup.ALLOW_PARENT_READ);
		   BranchGroup tempBranch = new BranchGroup();
		   tempBranch.setCapability(BranchGroup.ALLOW_DETACH);
		   Transform3D move = new Transform3D();
		   move.set(new Vector3d(distanceBetweenAxes*qqq,0,0));
		   contents.setTransform(move);
	//draw axis
		   if (axisView)
		   {
			   float[] tempColor = new float[3];
				axesColor.getColorComponents(tempColor);
			  LineArray xAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
			  xAxis.setCoordinate(0, new Point3d(-1,0,0));
			  xAxis.setCoordinate(1, new Point3d(1,0,0));
			  xAxis.setColor(0, new Color3f(tempColor));
			  xAxis.setColor(1, new Color3f(tempColor));
			  Text2D tempText1 = new Text2D(myDimensions[qqq][0],new Color3f(0.9f, 1.0f, 1.0f),"Helvetica", 18, Font.ITALIC);
			  TransformGroup temp1 = new TransformGroup();
			  Transform3D move1 = new Transform3D();
			  move1.set(new Vector3d(1,0,0));
			  temp1.setTransform(move1);
			  temp1.addChild(tempText1);
			  LineArray yAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
			  yAxis.setCoordinate(0, new Point3d(0,-1,0));
			  yAxis.setCoordinate(1, new Point3d(0,1,0));
			  yAxis.setColor(0, new Color3f(tempColor));
			  yAxis.setColor(1, new Color3f(tempColor));
			  Text2D tempText2 = new Text2D(myDimensions[qqq][1],new Color3f(0.9f, 1.0f, 1.0f),"Helvetica", 18, Font.ITALIC);
			  TransformGroup temp2 = new TransformGroup();
			  Transform3D move2 = new Transform3D();
			  move2.set(new Vector3d(0,1,0));
			  temp2.setTransform(move2);
			  temp2.addChild(tempText2);
			  LineArray zAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
			  zAxis.setCoordinate(0, new Point3d(0,0,-1));
			  zAxis.setCoordinate(1, new Point3d(0,0,1));
			  zAxis.setColor(0, new Color3f(tempColor));
			  zAxis.setColor(1, new Color3f(tempColor));
			  Text2D tempText3 = new Text2D(myDimensions[qqq][2],new Color3f(0.9f, 1.0f, 1.0f),"Helvetica", 18, Font.ITALIC);
			  TransformGroup temp3 = new TransformGroup();
			  Transform3D move3 = new Transform3D();
			  move3.set(new Vector3d(0,0,1));
			  temp3.setTransform(move3);
			  temp3.addChild(tempText3);
			  contents.addChild(new Shape3D(xAxis));
			  contents.addChild(new Shape3D(yAxis));
			  contents.addChild(new Shape3D(zAxis));
			  contents.addChild(temp1);
			  contents.addChild(temp2);
			  contents.addChild(temp3);
		   }
		   if (slice == false)
		   {
		   if (planeView)
		   {

			   try
			   {
				   
				for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
				{
				double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
				TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
					   
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (iii==activeAxes[mmm])
						
						{
							drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii];
							drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii];
							drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii];
						}
					}
				}
				myplanes.setCoordinate(0, drawPoints[jjj][0]);
				myplanes.setCoordinate(1, drawPoints[jjj][1]);
				myplanes.setCoordinate(2, drawPoints[jjj][2]);
				float[] tempColor = new float[3];
				planeColor.getColorComponents(tempColor);
				myplanes.setColor(0, new Color3f(tempColor)); 
				myplanes.setColor(1, new Color3f(tempColor));
				myplanes.setColor(2, new Color3f(tempColor));
				PolygonAttributes pgonAttrs = new PolygonAttributes();
		        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
		        Appearance tempApp = new Appearance();
		        tempApp.setPolygonAttributes(pgonAttrs);
		        Shape3D temporary = new Shape3D(myplanes);
		        temporary.setAppearance(tempApp);
				contents.addChild(temporary);
				}
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No Planes");
			   }
			   finally
			   {
				   
			   }  
		   }
		   //draw lines
		   if (lineView)
		   {
			  
			   try
			   {
				   
			   
			    for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
				{
				double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
				LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (iii==activeAxes[mmm])
						
						{
							drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii];
							drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii];
						}
					}
				}
				mylines.setCoordinate(0, drawPoints[jjj][0]);
				mylines.setCoordinate(1, drawPoints[jjj][1]);
				float[] tempColor = new float[3];
				lineColor.getColorComponents(tempColor);
				mylines.setColor(0, new Color3f(tempColor)); 
				mylines.setColor(1, new Color3f(tempColor));
				contents.addChild(new Shape3D(mylines));
				}
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No lines");
			   }
			   finally
			   {
				   
			   }
				
		   }
		   
		   if (pointView)
		   {
			   
			  
			   try
			   {
				   
			   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
			   PointArray mypoints = new PointArray(NDimensionalObject.myPoints.length, PointArray.COORDINATES|PointArray.COLOR_3);
			   
				for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
				{
				for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
				{
					for(int mmm = 0; mmm < 3; mmm++ )
					{
						if (iii==activeAxes[mmm])
						{
							drawPoints[jjj][mmm]= NDimensionalObject.myPoints[jjj].location[iii];
						}
					}
				}
				
				mypoints.setCoordinate(jjj, drawPoints[jjj]);
				
				float[] tempColor = new float[3];
				pointColor.getColorComponents(tempColor);
				mypoints.setColor(jjj, new Color3f(tempColor));
				}
				
				
				contents.addChild(new Shape3D(mypoints));
				
				
			   }
			   catch(IllegalArgumentException f)
			   {
				   System.out.println("No points");
			   }
			   finally
			   {
				   
			   }
				
		   }
		   }
		   if (slice==true)
		   {
			   if (planeView)
			   {
				   	//4 cases:
				   //plane doesn't intersect slice
				   //plane intersects slice at point
				   //plane intersects slice at line
				   //plane within slice
				   try 
				   {
					String[] drawMethod = new String[NDimensionalObject.myPlanes.length];
				  
					for(int jjj=0; jjj < NDimensionalObject.myPlanes.length; jjj++)
					{
						double [][][] drawPoints = new double[NDimensionalObject.myPlanes.length][3][3];
						   TriangleArray myplanes = new TriangleArray(3, TriangleArray.COORDINATES|TriangleArray.COLOR_3);
						    
					for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
					{
						if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
						{
						if (((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))
								||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))
							drawMethod[jjj]= "none";
						if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
							drawMethod[jjj]= "point1";//if all in same plane, setting as just point
						if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
							drawMethod[jjj]= "point2";
						if ((drawMethod[jjj]!="none")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
							drawMethod[jjj]= "point3";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="line")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&& ((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
							drawMethod[jjj]= "triangle";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&
												(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])
												&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]!= sliceVars[iii])))
								drawMethod[jjj]= "line12";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]== sliceVars[iii])&&
												(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]!= sliceVars[iii])
												&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
								drawMethod[jjj]= "line13";
						if ((drawMethod[jjj]!="none")&& (drawMethod[jjj]!="point1")&& (drawMethod[jjj]!="point2")&& (drawMethod[jjj]!="point3")&&(drawMethod[jjj]!="triangle")&&(((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]< sliceVars[iii])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]> sliceVars[iii])
								&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]> sliceVars[iii])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]< sliceVars[iii])
										&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]< sliceVars[iii])))||((NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii]!= sliceVars[iii])&&
												(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii]== sliceVars[iii])
												&&(NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii]== sliceVars[iii])))
								drawMethod[jjj]= "line23";
						}
							for(int mmm = 0; mmm < 3; mmm++ )
						{
							if (iii==activeAxes[mmm])
							
							{
								drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[0]].location[iii];
								drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[1]].location[iii];
								drawPoints[jjj][2][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myPlanes[jjj].pointsIndex[2]].location[iii];
							}
						}
					}
					if (drawMethod[jjj]=="triangle")
					{
					myplanes.setCoordinate(0, drawPoints[jjj][0]);
					myplanes.setCoordinate(1, drawPoints[jjj][1]);
					myplanes.setCoordinate(2, drawPoints[jjj][2]);
					float[] tempColor = new float[3];
					planeColor.getColorComponents(tempColor);
					myplanes.setColor(0, new Color3f(tempColor)); 
					myplanes.setColor(1, new Color3f(tempColor));
					myplanes.setColor(2, new Color3f(tempColor));
					PolygonAttributes pgonAttrs = new PolygonAttributes();
			        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
			        Appearance tempApp = new Appearance();
			        tempApp.setPolygonAttributes(pgonAttrs);
			        Shape3D temporary = new Shape3D(myplanes);
			        temporary.setAppearance(tempApp);
					contents.addChild(temporary);
					}
					else if (drawMethod[jjj]== "point1")
					{
						PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						temp.setCoordinate(0, drawPoints[jjj][0]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp.setColor(0, tempColor);
						contents.addChild(new Shape3D(temp));
					}
					else if (drawMethod[jjj]== "point2")
					{
						PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						temp.setCoordinate(0, drawPoints[jjj][1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp.setColor(0, tempColor);
						contents.addChild(new Shape3D(temp));
					}
					else if (drawMethod[jjj]== "point3")
					{
						PointArray temp = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						temp.setCoordinate(0, drawPoints[jjj][2]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp.setColor(0, tempColor);
						contents.addChild(new Shape3D(temp));
					}
					else if (drawMethod[jjj]=="line12")
					{
						LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
						try
						{
						float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,1,2,sliceVars);
						temp.setCoordinate(0, lineCoords[0]);
						temp.setCoordinate(1, lineCoords[1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp.setColor(0, tempColor);
						temp.setColor(1, tempColor);
						contents.addChild(new Shape3D(temp));
						}
						catch(Exception g)
						{
							
						}
					}
					else if (drawMethod[jjj]=="line13")
					{
						try
						{
						LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
						float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,0,2,1,sliceVars);
						temp.setCoordinate(0, lineCoords[0]);
						temp.setCoordinate(1, lineCoords[1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp.setColor(0, tempColor);
						temp.setColor(1, tempColor);
						contents.addChild(new Shape3D(temp));
						}
						catch(Exception g)
						{
							
						}
					}
					else if (drawMethod[jjj]=="line23")
					{
						try
						{
						LineArray temp = new LineArray(2,LineArray.COORDINATES|PointArray.COLOR_3);
						float[][] lineCoords =  NDimensionalObject.interpolatePlane(jjj,1,2,0,sliceVars);
						temp.setCoordinate(0, lineCoords[0]);
						temp.setCoordinate(1, lineCoords[1]);
						float[] tempColor = new float[3];
						planeColor.getColorComponents(tempColor);
						temp.setColor(0, tempColor);
						temp.setColor(1, tempColor);
						contents.addChild(new Shape3D(temp));
						}
						catch(Exception g)
						{
							
						}
					}
					}
					
				   }
				   catch(IllegalArgumentException f)
				   {
					   System.out.println("No Planes");
				   }
				   finally
				   {
					   
				   }  
			   }
			   //draw lines
			   if (lineView)//need to draw points (could be lines if in plane)
			   {
				   //3 cases to deal with
				 //line doesn't intersect plane
					
					//lines are entirely within viewing plane
				//line intersects plane at 1 point
				  
				   try
				   {
					   String[] drawMethod = new String[NDimensionalObject.myLines.length];
				   double [][][] drawPoints = new double[NDimensionalObject.myLines.length][2][3];
				   LineArray mylines = new LineArray(2, LineArray.COORDINATES|LineArray.COLOR_3);
				   
					for(int jjj=0; jjj < NDimensionalObject.myLines.length; jjj++)
					{
					for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
					{
						if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
						{
						if (((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]> sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]> sliceVars[iii]))||((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]< sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]< sliceVars[iii])))
							drawMethod[jjj]= "none";
						if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="point")&&(((NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii]== sliceVars[iii])&&(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii]== sliceVars[iii]))))
							drawMethod[jjj]= "inplane";
						if ((drawMethod[jjj]!="none")&&(drawMethod[jjj]!="inplane"))
							drawMethod[jjj]= "point";
						}
							for(int mmm = 0; mmm < 3; mmm++ )
						{
							if (iii==activeAxes[mmm])
							{   
								drawPoints[jjj][0][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index].location[iii];
								drawPoints[jjj][1][mmm]= NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index].location[iii];
							}
						}
					}
					
					if (drawMethod[jjj]=="inplane")
					{
					mylines.setCoordinate(0, drawPoints[jjj][0]);
					mylines.setCoordinate(1, drawPoints[jjj][1]);
					float[] tempColor = new float[3];
					lineColor.getColorComponents(tempColor);
					mylines.setColor(0, new Color3f(tempColor)); 
					mylines.setColor(1, new Color3f(tempColor));
					contents.addChild(new Shape3D(mylines));
					}
					else if (drawMethod[jjj]=="point")
					{
						try
						{
						PointArray tempPoint = new PointArray(1,PointArray.COORDINATES|PointArray.COLOR_3);
						tempPoint.setCoordinate(0, NDimensionalObject.interpolateLine(NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint1index],NDimensionalObject.myPoints[NDimensionalObject.myLines[jjj].endpoint2index],sliceVars));
						float[] tempColor = new float[3];
						lineColor.getColorComponents(tempColor);
						tempPoint.setColor(0, tempColor);
						contents.addChild(new Shape3D(tempPoint));
						}
						catch(Exception g)
						{
							System.out.println("some error");
						}
					}
					
					
					}
					
				   }
				   catch(IllegalArgumentException f)
				   {
					   System.out.println("No lines");
				   }
				   catch(NullPointerException f)
				   {
					   System.out.println("No slicing variables- 3d or less");
				   }
				   finally
				   {
					   
				   }
					
			   }
			  

			   if (pointView)
			   {
				   
				  
				   try
				   {
					
					Boolean[] canDraw = new Boolean[NDimensionalObject.myPoints.length];
					int canDrawLength = NDimensionalObject.myPoints.length;
					for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
						{
						canDraw[jjj]=true;//assume can draw initially
						for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
							{
							//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
							if ((iii!=activeAxes[0])&&(iii!=activeAxes[1])&&(iii!=activeAxes[2]))
								{
									if(NDimensionalObject.myPoints[jjj].location[iii] != sliceVars[iii])
									{
									canDraw[jjj]=false;
									canDrawLength -= 1;
									}
								}
							
							
							}
						
						}
				   double [][] drawPoints = new double[NDimensionalObject.myPoints.length][3];
				   PointArray mypoints = new PointArray(canDrawLength, PointArray.COORDINATES|PointArray.COLOR_3);
					int index = 0;
				   for(int jjj=0; jjj < NDimensionalObject.myPoints.length; jjj++)
					{
						
					for(int iii=0;iii<NDimensionalObject.NumberOfDimensions; iii++)
					{
						for(int mmm = 0; mmm < 3; mmm++ )//in order to draw, the point must lie in the required planes- must be equal to slicing variable for all dimensions other than active ones
						{
							if (iii==activeAxes[mmm])
							{
								drawPoints[jjj][mmm]= NDimensionalObject.myPoints[jjj].location[iii];
							}
						}
					}
					if (canDraw[jjj])
					{
					mypoints.setCoordinate(index, drawPoints[jjj]);
					
					float[] tempColor = new float[3];
					pointColor.getColorComponents(tempColor);
					mypoints.setColor(index, new Color3f(tempColor));
					index++;
					}
					}
					
					contents.addChild(new Shape3D(mypoints));
					
					
				   }
				   catch(IllegalArgumentException f)
				   {
					   System.out.println("No points");
				   }
				   catch(NullPointerException g)
				   {
					   System.out.println("No points");
				   }
				   finally
				   {
					   
				   }
					
			   
		   
		   }
		   }
		   tempBranch.addChild(contents);
			   topGroup.addChild(tempBranch);
		   }
	   }
	   catch(NullPointerException F)
	   {
	   }
	
   
}

/**
 * Draw parallel axes method.
 */
private static void parallel() {
	
	   //set up axes
	   BranchGroup contents = new BranchGroup();
	   contents.setCapability(BranchGroup.ALLOW_DETACH);
	   if (axisView)
	   {
		   float[] tempColor = new float[3];
			axesColor.getColorComponents(tempColor);
		  LineArray xAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  xAxis.setCoordinate(0, new Point3d(-1,0,0));
		  xAxis.setCoordinate(1, new Point3d(NDimensionalObject.NumberOfDimensions,0,0));
		  xAxis.setColor(0, new Color3f(tempColor));
		  xAxis.setColor(1, new Color3f(tempColor));
		  contents.addChild(new Shape3D(xAxis));
		  for(int iii=0; iii< NDimensionalObject.NumberOfDimensions; iii++)
		  {
		  LineArray yAxis = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		  yAxis.setCoordinate(0, new Point3d(iii,-1,0));
		  yAxis.setCoordinate(1, new Point3d(iii,1,0));
		  yAxis.setColor(0, new Color3f(tempColor));
		  yAxis.setColor(1, new Color3f(tempColor));
		  Text2D tempText2 = new Text2D(NDimensionalObject.DimensionLabels[iii],new Color3f(0.9f, 1.0f, 1.0f),"Helvetica", 18, Font.ITALIC);
		  TransformGroup temp2 = new TransformGroup();
		  Transform3D move2 = new Transform3D();
		  move2.set(new Vector3d(iii,1,0));
		  temp2.setTransform(move2);
		  temp2.addChild(tempText2);
		  contents.addChild(new Shape3D(yAxis));
		  contents.addChild(temp2);
		  }
		  
	   }
	   if (planeView)
	   {
		   float[] tempColor = new float[3];
		   planeColor.getColorComponents(tempColor);
		   for(int nnn=0; nnn< NDimensionalObject.myPlanes.length; nnn++)
		   {
			   for(int mmm=0; mmm< NDimensionalObject.NumberOfDimensions-1; mmm++)
			   {
				   int endpoint1 = NDimensionalObject.findLargestPlane(mmm, NDimensionalObject.myPlanes[nnn]);
				   int endpoint2 = NDimensionalObject.findSmallestPlane(mmm, NDimensionalObject.myPlanes[nnn]);
				   int endpoint3 = NDimensionalObject.findLargestPlane(mmm+1, NDimensionalObject.myPlanes[nnn]);
				   int endpoint4 = NDimensionalObject.findSmallestPlane(mmm+1, NDimensionalObject.myPlanes[nnn]);
				   
				   TriangleArray tempLine1 = new TriangleArray(3,LineArray.COORDINATES|LineArray.COLOR_3);
					  tempLine1.setCoordinate(0, new Point3d(mmm,NDimensionalObject.myPoints[endpoint1].location[mmm],0));
					  tempLine1.setCoordinate(1, new Point3d(mmm,NDimensionalObject.myPoints[endpoint2].location[mmm],0));
					  tempLine1.setCoordinate(2, new Point3d(mmm+1,NDimensionalObject.myPoints[endpoint3].location[mmm+1],0));
					  tempLine1.setColor(0, new Color3f(tempColor));
					  tempLine1.setColor(1, new Color3f(tempColor));
					  tempLine1.setColor(2, new Color3f(tempColor));
					  TriangleArray tempLine2 = new TriangleArray(3,LineArray.COORDINATES|LineArray.COLOR_3);
					  tempLine2.setCoordinate(0, new Point3d(mmm,NDimensionalObject.myPoints[endpoint1].location[mmm],0));
					  tempLine2.setCoordinate(1, new Point3d(mmm+1,NDimensionalObject.myPoints[endpoint3].location[mmm+1],0));
					  tempLine2.setCoordinate(2, new Point3d(mmm+1,NDimensionalObject.myPoints[endpoint4].location[mmm+1],0));
					  tempLine2.setColor(0, new Color3f(tempColor));
					  tempLine2.setColor(1, new Color3f(tempColor));
					  tempLine2.setColor(2, new Color3f(tempColor));
					  TriangleArray tempLine3 = new TriangleArray(3,LineArray.COORDINATES|LineArray.COLOR_3);
					  tempLine3.setCoordinate(0, new Point3d(mmm,NDimensionalObject.myPoints[endpoint1].location[mmm],0));
					  tempLine3.setCoordinate(1, new Point3d(mmm,NDimensionalObject.myPoints[endpoint2].location[mmm],0));
					  tempLine3.setCoordinate(2, new Point3d(mmm+1,NDimensionalObject.myPoints[endpoint4].location[mmm+1],0));
					  tempLine3.setColor(0, new Color3f(tempColor));
					  tempLine3.setColor(1, new Color3f(tempColor));
					  tempLine3.setColor(2, new Color3f(tempColor));
					  PolygonAttributes pgonAttrs = new PolygonAttributes();
				        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
				        Appearance tempApp = new Appearance();
				        tempApp.setPolygonAttributes(pgonAttrs);
				        Shape3D temporary = new Shape3D(tempLine1);
				        Shape3D temporary2 = new Shape3D(tempLine2);
				        Shape3D temporary3 = new Shape3D(tempLine3);
				        temporary.setAppearance(tempApp);
				        temporary2.setAppearance(tempApp);
				        temporary3.setAppearance(tempApp);
					  contents.addChild(temporary);
					  contents.addChild(temporary2);
					  contents.addChild(temporary3);
				   
			   }
		   }
		  
	   }
	   if (lineView)
	   {
		   float[] tempColor = new float[3];
		   lineColor.getColorComponents(tempColor);
		   for(int nnn=0; nnn< NDimensionalObject.myLines.length; nnn++)
		   {
			   for(int mmm=0; mmm< NDimensionalObject.NumberOfDimensions-1; mmm++)
			   {
				   
				   {
				  
				   TriangleArray tempLine1 = new TriangleArray(3,LineArray.COORDINATES|LineArray.COLOR_3);
					  tempLine1.setCoordinate(0, new Point3d(mmm,NDimensionalObject.myPoints[NDimensionalObject.myLines[nnn].endpoint1index].location[mmm],0));
					  tempLine1.setCoordinate(1, new Point3d(mmm,NDimensionalObject.myPoints[NDimensionalObject.myLines[nnn].endpoint2index].location[mmm],0));
					  tempLine1.setCoordinate(2, new Point3d(mmm+1,NDimensionalObject.myPoints[NDimensionalObject.myLines[nnn].endpoint1index].location[mmm+1],0));
					  tempLine1.setColor(0, new Color3f(tempColor));
					  tempLine1.setColor(1, new Color3f(tempColor));
					  tempLine1.setColor(2, new Color3f(tempColor));
					  TriangleArray tempLine2 = new TriangleArray(3,LineArray.COORDINATES|LineArray.COLOR_3);
					  tempLine2.setCoordinate(0, new Point3d(mmm,NDimensionalObject.myPoints[NDimensionalObject.myLines[nnn].endpoint1index].location[mmm],0));
					  tempLine2.setCoordinate(1, new Point3d(mmm+1,NDimensionalObject.myPoints[NDimensionalObject.myLines[nnn].endpoint1index].location[mmm+1],0));
					  tempLine2.setCoordinate(2, new Point3d(mmm+1,NDimensionalObject.myPoints[NDimensionalObject.myLines[nnn].endpoint2index].location[mmm+1],0));
					  tempLine2.setColor(0, new Color3f(tempColor));
					  tempLine2.setColor(1, new Color3f(tempColor));
					  tempLine2.setColor(2, new Color3f(tempColor));
					  TriangleArray tempLine3 = new TriangleArray(3,LineArray.COORDINATES|LineArray.COLOR_3);
					  tempLine3.setCoordinate(0, new Point3d(mmm,NDimensionalObject.myPoints[NDimensionalObject.myLines[nnn].endpoint1index].location[mmm],0));
					  tempLine3.setCoordinate(1, new Point3d(mmm,NDimensionalObject.myPoints[NDimensionalObject.myLines[nnn].endpoint2index].location[mmm],0));
					  tempLine3.setCoordinate(2, new Point3d(mmm+1,NDimensionalObject.myPoints[NDimensionalObject.myLines[nnn].endpoint2index].location[mmm+1],0));
					  tempLine3.setColor(0, new Color3f(tempColor));
					  tempLine3.setColor(1, new Color3f(tempColor));
					  tempLine3.setColor(2, new Color3f(tempColor));
					  PolygonAttributes pgonAttrs = new PolygonAttributes();
				        pgonAttrs.setCullFace(pgonAttrs.CULL_NONE);
				        Appearance tempApp = new Appearance();
				        tempApp.setPolygonAttributes(pgonAttrs);
				        Shape3D temporary = new Shape3D(tempLine1);
				        Shape3D temporary2 = new Shape3D(tempLine2);
				        Shape3D temporary3 = new Shape3D(tempLine3);
				        temporary.setAppearance(tempApp);
				        temporary2.setAppearance(tempApp);
				        temporary3.setAppearance(tempApp);
					  contents.addChild(temporary);
					  contents.addChild(temporary2);
					  contents.addChild(temporary3);
				   }
			   }
		   }
		  
	   }
	 //add points
	   if (pointView)
	   {
		   float[] tempColor = new float[3];
		   pointColor.getColorComponents(tempColor);
		   for(int nnn=0; nnn< NDimensionalObject.myPoints.length; nnn++)
		   {
			   for(int mmm=0; mmm< NDimensionalObject.NumberOfDimensions-1; mmm++)
			   {
				   LineArray tempPoint = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
					  tempPoint.setCoordinate(0, new Point3d(mmm,NDimensionalObject.myPoints[nnn].location[mmm],0));
					  tempPoint.setCoordinate(1, new Point3d(mmm+1,NDimensionalObject.myPoints[nnn].location[mmm+1],0));
					  tempPoint.setColor(0, new Color3f(tempColor));
					  tempPoint.setColor(1, new Color3f(tempColor));
					  contents.addChild(new Shape3D(tempPoint));
			   }
		   }
		  
	   }
	   topGroup.addChild(contents);
}

/**
 * Set the camera position.
 * @param d
 * @param e
 * @param f
 */
public static void setCamera(double d, double e, double f) {
	cameraPosition = new Vector3d(d,e,f);
	
}

/**
 * Clear the screen.
 */
public static void clearScreen()
{
	
	while(topGroup.numChildren()>2)
		topGroup.removeChild(2);
	
}

/**
 * Set whether using slices or not.
 * @param b
 */
public static void setSlice(Boolean b)
{
	slice = b;
}
/**
 * Set the active slices.
 * @param d
 */
public static void setSliceVars(double[] d)//note- d is a complete list of all slicing variables-including active Axes
{
	sliceVars = d;
}

/**
 * Calculation to set up felder method slicing variables.
 * @param dimStart
 * @param dimEnd
 * @param dimInterval
 * @param axisStartsX
 * @param axisStartsY
 * @param numSlices
 */
public static void setupFelder(Double[] dimStart, Double[] dimEnd,
		Double[] dimInterval, float[] axisStartsX, float[] axisStartsY, int[] numSlices) {
	felderVectors = new Vector3d[axisStartsX.length*axisStartsY.length];
	felderStrings = new String[axisStartsX.length*axisStartsY.length];
	felderSliceVars = new double[axisStartsX.length*axisStartsY.length][NDimensionalObject.NumberOfDimensions];
	int index=0;
	for(int iii=0; iii< axisStartsX.length;iii++)
	{
		for(int jjj=0; jjj< axisStartsY.length; jjj++)
		{
		felderVectors[index] = new Vector3d(axisStartsX[iii],axisStartsY[jjj],0);
		
		felderStrings[index] = new String("(");
		for(int qqq=0; qqq< NDimensionalObject.NumberOfDimensions; qqq++)
		{
			//assign slicing variables to felderslices- for each axis set, for each dimension, get right
		
			if (qqq<3)
				felderSliceVars[index][qqq]= 0;
			else if (qqq%2 != 0)//even dims
				felderSliceVars[index][qqq]= dimStart[qqq] + (jjj%numSlices[qqq-3])*dimInterval[qqq];
			else if (qqq%2 == 0)//odd dims
				felderSliceVars[index][qqq]= dimStart[qqq] + (iii%numSlices[qqq-3])*dimInterval[qqq];
			
			if (qqq!=(NDimensionalObject.NumberOfDimensions-1))
				felderStrings[index]+= felderSliceVars[index][qqq] + ",";
			else
				felderStrings[index]+= felderSliceVars[index][qqq];
		}
		felderStrings[index]+= ")";
		
		index++;
		}
	}
 felderSlices= numSlices;
	paint3Dobject();
}

/**
 * Save animation to .gif
 * @param fileName
 */
public static void saveAnimation(String fileName){
	try {
		 BufferedImage onimage; 
		AnimatedGifEncoder e = new AnimatedGifEncoder();
		 e.start(fileName);
		     e.setDelay(100);   // 1 frame per sec
		     long currentTime = System.currentTimeMillis(); 
		     long userSet=100;
		     int userReps=0;
		     Boolean switchAlphaExists = true;
		     Boolean rotationAlphaExists = true;
		     try
		     {
		    	 rotationAlpha.getAlphaAtOneDuration();
		     }
		     catch(NullPointerException f)
		     {
		    	 rotationAlphaExists= false;
		     }
		     try
		     {
		      userReps= (int) ((switchAlpha.getDecreasingAlphaDuration()+switchAlpha.getAlphaAtOneDuration()+switchAlpha.getAlphaAtZeroDuration()+switchAlpha.getIncreasingAlphaDuration())/100);
		     }
		     catch(NullPointerException f)
		     {
		    	 userReps = (int)((rotationAlpha.getDecreasingAlphaDuration()+rotationAlpha.getAlphaAtOneDuration()+rotationAlpha.getAlphaAtZeroDuration()+rotationAlpha.getIncreasingAlphaDuration())/100);
		    	 switchAlphaExists = false;
		     }
		      for(int nnn=1; nnn<userReps; nnn++)
		     {
		    	 
		    	if(rotationAlphaExists)
		    	rotationAlpha.pause(currentTime + nnn*userSet);
		    	if(switchAlphaExists)
		    	switchAlpha.pause(currentTime + nnn*userSet);
		    onimage = new Robot().createScreenCapture( new Rectangle( canvas.getLocation().x+GraphicUI3d.newframe.getX()+7,canvas.getLocation().y+GraphicUI3d.newframe.getY()+52,canvas.getWidth(),canvas.getHeight()));
		    e.addFrame(onimage);
		    if(rotationAlphaExists)
		    rotationAlpha.resume(currentTime + nnn*userSet);
		    if(switchAlphaExists)
		    switchAlpha.resume(currentTime + nnn*userSet);
		     }
		     e.finish();
	} catch (AWTException e) {
		e.printStackTrace();
	}

	
}
}
