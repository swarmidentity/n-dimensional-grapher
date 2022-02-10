package edu.clarkson.racettl.ndimensional;

import javax.media.j3d.Canvas3D;
import javax.swing.*;

//import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
//import java.awt.Graphics.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Provides the main user interface component of the project. Maintains all window opening/menu operations.
 * @author Louis Racette
 *
 */
public class GraphicUI3d  extends JPanel implements ActionListener, ItemListener {
	static JFrame newframe;
	Canvas3D canvas;
	
	JMenuItem vrotate;
	static String drawMethod = "sing3d";
	public static NDimensionalObject myObject;
	public static JComponent dirtyComponent;
	JCheckBoxMenuItem viewpoint;
	JCheckBoxMenuItem viewedge;
	JCheckBoxMenuItem viewplane;
	JCheckBoxMenuItem viewAxes;
	JMenuItem cameraPosition;

	private static final long serialVersionUID = 5947514012941006718L;

	/**
	 * Draw the Graphics window.
	 */
GraphicUI3d()
{
	newframe = new JFrame("N-Dimensional Grapher");
	newframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	newframe.setVisible(true);
	newframe.setSize(800,600);
	newframe.setLocation(100,100);

	//Define menu layout/components
	JMenuBar topBar = new JMenuBar();
	
	//Define menu: File
	JMenu fileMenu = new JMenu("File");
	JMenuItem load = new JMenuItem("Load");
	JMenuItem importObj = new JMenuItem("Import from excel");
	JMenuItem save = new JMenuItem("Save");
	JMenuItem saveImage = new JMenuItem("Save Image");
	JMenuItem imageSeries = new JMenuItem("Save series of images");
	JMenuItem saveAnim = new JMenuItem("Save Animation");
	JMenuItem newFile = new JMenuItem("New");
	fileMenu.add(load);
	fileMenu.add(save);
	fileMenu.add(importObj);
	fileMenu.add(saveImage);
	
	fileMenu.add(newFile);
	fileMenu.add(saveAnim);
	topBar.add(fileMenu);
	load.setActionCommand("load");
	importObj.setActionCommand("importObj");
	save.setActionCommand("save");
	saveImage.setActionCommand("saveImage");
	imageSeries.setActionCommand("imageSeries");
	newFile.setActionCommand("new");
	saveAnim.setActionCommand("saveAnim");
	load.addActionListener(this);
	importObj.addActionListener(this);
	save.addActionListener(this);
	newFile.addActionListener(this);
	saveImage.addActionListener(this);
	imageSeries.addActionListener(this);
	saveAnim.addActionListener(this);
	
	//Define menu: Edit
	JMenu editMenu = new JMenu("Edit");
	JMenuItem copy = new JMenuItem("Duplicate Selected");
	JMenuItem delete = new JMenuItem("Delete Selected");
	JMenuItem allPoints = new JMenuItem("Manual Point Edit");
	editMenu.add(copy);
	editMenu.add(delete);
	editMenu.addSeparator();
	editMenu.add(allPoints);
	topBar.add(editMenu);
	copy.setActionCommand("copy");
	delete.setActionCommand("delete");
	allPoints.setActionCommand("allPoints");
	copy.addActionListener(this);
	delete.addActionListener(this);
	allPoints.addActionListener(this);
	
	//Define menu: Draw Method
	JMenu methodMenu = new JMenu("Drawing Method");
	JMenuItem parallel = new JMenuItem("Parallel Axis");
	JMenuItem mult2d = new JMenuItem("Multiple 2d Axes");
	JMenuItem mult3d = new JMenuItem("Multiple 3d Axes");//collect as more methods added
	JMenuItem sing3d = new JMenuItem("Single 3d Axis");
	JMenuItem felder = new JMenuItem("Felder Method");
	JMenuItem spiral = new JMenuItem("Spiral Method");
	JMenuItem animated = new JMenuItem("Rotating Axes");
	JMenuItem sing2d = new JMenuItem("Single 2d Axis");
	JMenuItem timeAxis = new JMenuItem("Single 4d Axis");
	JMenuItem mult4d = new JMenuItem("Multiple 4d Axes");

	methodMenu.add(parallel);
	methodMenu.add(mult2d);
	methodMenu.add(mult3d);
	methodMenu.add(sing3d);
	methodMenu.add(felder);

	methodMenu.add(animated);
	methodMenu.add(sing2d);
	methodMenu.add(timeAxis);
	methodMenu.add(mult4d);
	topBar.add(methodMenu);
	parallel.setActionCommand("parallel");
	mult2d.setActionCommand("mult2d");
	mult3d.setActionCommand("mult3d");
	sing3d.setActionCommand("sing3d");
	felder.setActionCommand("felder");
	spiral.setActionCommand("spiral");
	animated.setActionCommand("animated");
	sing2d.setActionCommand("sing2d");
	timeAxis.setActionCommand("timeAxis");
	mult4d.setActionCommand("mult4d");
	parallel.addActionListener(this);
	mult2d.addActionListener(this);
	mult3d.addActionListener(this);
	sing3d.addActionListener(this);
	felder.addActionListener(this);
	spiral.addActionListener(this);
	animated.addActionListener(this);
	sing2d.addActionListener(this);
	timeAxis.addActionListener(this);
	mult4d.addActionListener(this);
	
	
	//Define menu: Draw
	JMenu drawMenu = new JMenu("Draw");
	JMenuItem numDimensions = new JMenuItem("Number of Dimensions");
	JMenuItem point = new JMenuItem("Point");
	JMenuItem line = new JMenuItem("Line");
	JMenuItem plane = new JMenuItem("Plane");
	JMenuItem cube = new JMenuItem("Cube");
	JMenuItem refresh = new JMenuItem("Refresh");
	JMenuItem autoLine = new JMenuItem("Automatically Draw Lines");
	drawMenu.add(numDimensions);
	drawMenu.addSeparator();
	drawMenu.add(refresh);
	drawMenu.addSeparator();
	drawMenu.add(point);
	drawMenu.add(line);
	drawMenu.add(plane);
	drawMenu.add(cube);
	drawMenu.add(autoLine);
	topBar.add(drawMenu);
	numDimensions.setActionCommand("numDimensions");
	refresh.setActionCommand("refresh");
	point.setActionCommand("point");
	line.setActionCommand("line");
	plane.setActionCommand("plane");
	cube.setActionCommand("cube");
	autoLine.setActionCommand("autoLine");
	numDimensions.addActionListener(this);
	refresh.addActionListener(this);
	point.addActionListener(this);
	line.addActionListener(this);
	plane.addActionListener(this);
	cube.addActionListener(this);
	autoLine.addActionListener(this);
	
	//Define menu: Select
	JMenu selectMenu = new JMenu("Select");
	JMenuItem sPoint = new JMenuItem("Point");
	JMenuItem sLine = new JMenuItem("Line");
	JMenuItem sPlane = new JMenuItem("Plane");
	JMenuItem sObject = new JMenuItem("Object");
	JMenuItem sDeselect = new JMenuItem("Deselect");
	selectMenu.add(sPoint);
	selectMenu.add(sLine);
	selectMenu.add(sPlane);
	selectMenu.add(sObject);
	selectMenu.add(sDeselect);
	topBar.add(selectMenu);
	sPoint.setActionCommand("sPoint");
	sPlane.setActionCommand("sPlane");
	sLine.setActionCommand("sLine");
	sObject.setActionCommand("sObject");
	sDeselect.setActionCommand("sDeselect");
	sPoint.addActionListener(this);
	sPlane.addActionListener(this);
	sLine.addActionListener(this);
	sObject.addActionListener(this);
	sDeselect.addActionListener(this);
	
	//Define menu: Transform
	JMenu transformMenu = new JMenu("Transform");
	JMenuItem translate = new JMenuItem("Translate");
	JMenuItem rotate = new JMenuItem("Rotate");
	JMenuItem scale = new JMenuItem("Scale");
	transformMenu.add(translate);
	transformMenu.add(scale);
	topBar.add(transformMenu);
	translate.setActionCommand("translate");
	rotate.setActionCommand("rotate");
	scale.setActionCommand("scale");
	translate.addActionListener(this);
	rotate.addActionListener(this);
	scale.addActionListener(this);

	//Define Menu: visibility
	JMenu visMenu = new JMenu("Visibility");
	viewpoint = new JCheckBoxMenuItem("Points");
	viewedge = new JCheckBoxMenuItem("Lines");
	viewplane = new JCheckBoxMenuItem("Planes");
	viewAxes = new JCheckBoxMenuItem("Axes");
	visMenu.add(viewpoint);
	visMenu.add(viewedge);
	visMenu.add(viewplane);
	visMenu.add(viewAxes);
	topBar.add(visMenu);
	viewpoint.setSelected(true);
	viewedge.setSelected(true);
	viewplane.setSelected(true);
	viewAxes.setSelected(true);
	viewpoint.addItemListener(this);
	viewedge.addItemListener(this);
	viewplane.addItemListener(this);
	viewAxes.addItemListener(this);
	
	//Define menu: View
	JMenu viewMenu = new JMenu("View");
	JMenuItem zoom = new JMenuItem("Scaling");
	vrotate = new JMenuItem("Rotate");
	cameraPosition = new JMenuItem("Camera Position");
	JMenuItem pointSize = new JMenuItem("Point Size");
	JMenuItem color = new JMenuItem("Color Select");
	viewMenu.add(zoom);
	viewMenu.add(vrotate);
	viewMenu.add(cameraPosition);
	viewMenu.add(color);
	topBar.add(viewMenu);
	zoom.setActionCommand("zoom");
	vrotate.setActionCommand("vrotate");
	cameraPosition.setActionCommand("cameraPosition");
	pointSize.setActionCommand("pointSize");
	color.setActionCommand("color");
	zoom.addActionListener(this);
	vrotate.addActionListener(this);
	cameraPosition.addActionListener(this);
	color.addActionListener(this);
	pointSize.addActionListener(this);


	newframe.setJMenuBar(topBar);
	
	
	//add a canvas to draw n-dimensional representations
	newframe.add("Center", paintMethods3d.canvas);
	
	paintMethods3d.paint3Dobject();
	
	
}
/**
 * Main string to launch grapher.
 * @param args
 */
	public static void main(String[] args) {
		myObject = new NDimensionalObject();
		new paintMethods3d();
		new GraphicUI3d();
	
	}

	
	/**
	 * Monitor the actions performed on the GUI.
	 */
	public void actionPerformed(ActionEvent e)
	{
		
		//Load new object- include its points in the current drawing
		if ("load".equals(e.getActionCommand()))
		{
			drawMethod = "parallel";
			
			try {
				new subGUIload();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			paintMethods3d.paint3Dobject();
		}
		
		//import a new object
		if ("importObj".equals(e.getActionCommand()))
		{
			drawMethod = "parallel";
			try {
				new subGUIimport();
			} catch (FileNotFoundException e1) {
				System.out.println("No such file");
				e1.printStackTrace();
			}
			paintMethods3d.paint3Dobject();
		}
		//save the current object
		if ("save".equals(e.getActionCommand()))
		{
			try {
				subGUIsave test = new subGUIsave();
				test.saveText();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		//save an image
		if ("saveImage".equals(e.getActionCommand()))
		{
			try {
				subGUIsave test = new subGUIsave();
				test.saveImage();
			} catch (IOException e1) {
			
				e1.printStackTrace();
			}
			
		}
		
		//save series of images- not included in current version
		if ("imageSeries".equals(e.getActionCommand()))
		{
				subGUIimageSeries test = new subGUIimageSeries();
				
			
		}
		//save animation
		if ("saveAnim".equals(e.getActionCommand()))
		{
			try {
				subGUIsave test = new subGUIsave();
				test.saveAnimation();
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
		}
		//new object
		if ("new".equals(e.getActionCommand()))
		{
			 
			myObject = new NDimensionalObject();
			drawMethod = "parallel";//otherwise crashes
			paintMethods3d.paint3Dobject();
		}
		//copy command
		if ("copy".equals(e.getActionCommand()))
		{
			new subGUIcopy();
		}
		//delete command
		if ("delete".equals(e.getActionCommand()))
		{
			 
			for(int iii=select.selectedPoints.length-1; iii>=0;iii--)
			NDimensionalObject.deletePoint(select.selectedPoints[iii]);
			for(int iii=select.selectedPlanes.length-1; iii>=0;iii--)
				NDimensionalObject.deletePlane(select.selectedPlanes[iii]);
			for(int iii=select.selectedLines.length-1; iii>=0;iii--)
				NDimensionalObject.deleteLine(select.selectedLines[iii]);
			select.deselect();
			paintMethods3d.paint3Dobject();//doesn't respond immediately
		}
		//all points selected
		if ("allPoints".equals(e.getActionCommand()))
		{
			 
			new subGUIeditall();
		}
		//parallel axis method selected
		if ("parallel".equals(e.getActionCommand()))
		{
			
			drawMethod = "parallel";
			
			paintMethods3d.paint3Dobject();
		}
		//multiple 3d axes method selected
		if ("mult3d".equals(e.getActionCommand()))
		{
			
			vrotate.setEnabled(true);
			drawMethod = "mult3d";
			
			new subGUImult3d();
			
		}
		//single 3d axes selected
		if ("sing3d".equals(e.getActionCommand()))
		{
			
			vrotate.setEnabled(true);
			drawMethod = "sing3d";
			
			new subGUIsing3d();
			
			
		}
		//multiple 2d axes selected
		if ("mult2d".equals(e.getActionCommand()))
		{
			
			drawMethod = "mult2d";
			
			paintMethods3d.paint3Dobject();
		}
		//felder method selected
		if ("felder".equals(e.getActionCommand()))
		{
			
			
			drawMethod = "felder";
			
			new subGUIfelder();
		}
		//rotating axes selected
		if ("animated".equals(e.getActionCommand()))
		{
			
			
			drawMethod = "animated";
			
			new subGUIanimated();		
		}
		//single 2d axis selected
		if ("sing2d".equals(e.getActionCommand()))
		{
			
			
			drawMethod = "sing2d";
			
			new subGUIsing2d();		
		}
		//time axis representation selected- not currently available
		if ("timeAxis".equals(e.getActionCommand()))
		{
			
			
			drawMethod = "timeAxis";
			
			new subGUIsing4d();		
		}
		//multiple 4d axes selected
		if ("mult4d".equals(e.getActionCommand()))
		{
			
			
			drawMethod = "mult4d";
			
			new subGUImult4d();		
		}
		//number of dimensions change selected
		if ("numDimensions".equals(e.getActionCommand()))
		{
			
			new subGUIdimensions();
		}
		//refresh selected
		if ("refresh".equals(e.getActionCommand()))
		{
			 
			paintMethods3d.paint3Dobject();
		}
		//new point selected
		if ("point".equals(e.getActionCommand()))
		{
			new subGUInewpoint();
		}
		//new line selected
		if ("line".equals(e.getActionCommand()))
		{
			new subGUIdrawLine();
		}
		//new plane selected
		if ("plane".equals(e.getActionCommand()))
		{
			new subGUIdrawPlane();
		}
		//new cube selected
		if ("cube".equals(e.getActionCommand()))
		{
			new subGUIdrawCube();
		}
		//automatically fill in lines- not perfect
		if ("autoLine".equals(e.getActionCommand()))
		{
			new subGUIautoLine();
		}
		//select point
		if ("sPoint".equals(e.getActionCommand()))
		{
			new subGUIselectPoint();
		}
		//select line
		if ("sLine".equals(e.getActionCommand()))
		{
			new subGUIselectLine();
		}
		//select plane
		if ("sPlane".equals(e.getActionCommand()))
		{
			new subGUIselectPlane();
		}
		//select entire object
		if ("sObject".equals(e.getActionCommand()))
		{
			select.selectEverything();
		}
		//deselect
		if ("sDeselect".equals(e.getActionCommand()))
		{
			select.deselect();
		}
		//translate command
		if ("translate".equals(e.getActionCommand()))
		{
			 
			new subGUItranslate();
			paintMethods3d.paint3Dobject();
		}
		//rotate command
		if ("rotate".equals(e.getActionCommand()))
		{
			 
			new subGUIrotate();
		}
		//scale command
		if ("scale".equals(e.getActionCommand()))
		{
			 ;
			new subGUIscale();
			paintMethods3d.paint3Dobject();
		}
		//zoom command
		if ("zoom".equals(e.getActionCommand()))
		{
			
			new subGUIzoom();
		}
		//view rotate command
		if ("vrotate".equals(e.getActionCommand()))
		{
			new subGUIviewRotate();
		}
		//camera position command
		if ("cameraPosition".equals(e.getActionCommand()))
		{
			 
			new subGUIcameraPosition();
		}
		//point size command- removed from current project
		if ("pointSize".equals(e.getActionCommand()))
		{
			new subGUIpointSize();
		}
		
		//color change command
		if ("color".equals(e.getActionCommand()))
		{
			 
			new subGUIcolor();
		}
		
	}
	
	/**
	 * Refresh the screen.
	 */
	public void refresh()
	{
		RepaintManager.currentManager(dirtyComponent).markCompletelyDirty(dirtyComponent);
		repaint();
	}
	
	/**
	 * Monitor checklist for point, line and plane visibility.
	 */
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();
	    if ((source == viewpoint)&&((e.getStateChange() == ItemEvent.SELECTED)))
	    	paintMethods3d.pointView=true;
	    else if ((source == viewpoint)&&((e.getStateChange() == ItemEvent.DESELECTED)))
	    {
	    	paintMethods3d.pointView=false;
	    	 
	    }
	    else if ((source == viewedge)&&((e.getStateChange() == ItemEvent.SELECTED)))
		    paintMethods3d.lineView=true;
	    else if ((source == viewedge)&&((e.getStateChange() == ItemEvent.DESELECTED)))
	    {
	    	paintMethods3d.lineView=false;
	    	 
	    }
	    else if ((source == viewplane)&&((e.getStateChange() == ItemEvent.SELECTED)))
		    paintMethods3d.planeView= true;
	    else if ((source == viewplane)&&((e.getStateChange() == ItemEvent.DESELECTED)))
	    {
		    paintMethods3d.planeView= false;
		    
	    }
	    else if ((source == viewAxes)&&((e.getStateChange() == ItemEvent.SELECTED)))
		    paintMethods3d.axisView= true;
	    else if ((source == viewAxes)&&((e.getStateChange() == ItemEvent.DESELECTED)))
	    {
		    paintMethods3d.axisView= false;
		    
	    }
	    paintMethods3d.paint3Dobject();
	}


}
