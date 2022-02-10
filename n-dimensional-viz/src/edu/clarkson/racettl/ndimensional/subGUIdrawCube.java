package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu for drawing cubes.
 * @author Louis Racette
 *
 */
public class subGUIdrawCube extends JPanel implements ActionListener {
	protected JFrame pointFrame = new JFrame("Specify center and side length");
	protected JTable pointTable;
	protected JButton closeButton = new JButton("Draw Cube");
	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions+1][2];
	protected double[] centerPoint = new double[NDimensionalObject.NumberOfDimensions];
	JTextField distance = new JTextField("Side Length");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIdrawCube()
	{
		String[] columnNames = {"Dimension labels","Center Location"};
		
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
		}
		data[NDimensionalObject.NumberOfDimensions][1]= "Click Here When Finished";
		pointTable= new JTable(data, columnNames);
		
		JScrollPane scrollPane = new JScrollPane(pointTable);
		pointTable.setFillsViewportHeight(true);
		pointFrame.add(distance,BorderLayout.NORTH);
		pointFrame.add(scrollPane,BorderLayout.CENTER);
		pointFrame.add(closeButton, BorderLayout.SOUTH);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		pointFrame.pack();
		pointFrame.setVisible(true);
	}
	
	/**
	 * Monitor the submenu for changes.
	 */
	public void actionPerformed(ActionEvent e)
	{

		if ("close".equals(e.getActionCommand()))
		{
		
			
			
			for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
			{
				try
				{
				centerPoint[jjj]= Float.parseFloat((String) data[jjj][1]);
				
				}
				catch(NullPointerException f)
				{
						centerPoint[jjj]=0;
				}
				catch(NumberFormatException g)
				{
				centerPoint[jjj]=0;
				}
				finally
				{
					NDimensionalObject.DimensionLabels[jjj]= (String) data[jjj][0];
				}
			}
			double offset;
			try
			{
			offset = Double.parseDouble(distance.getText())/2;
			}
			catch(Exception h)
			{
				offset = 0.2;
			}
			int numPoints = (int) Math.pow(2, NDimensionalObject.NumberOfDimensions);
			double[][] tempDouble = new double[numPoints][NDimensionalObject.NumberOfDimensions];
			for(int jjj=0; jjj< NDimensionalObject.NumberOfDimensions; jjj++)
			{
				int counter = (int) Math.pow(2, jjj);
				int tempVar = 0;
				for(int iii=0; iii < numPoints; iii++)
				{
					
					if(tempVar <counter)
						tempDouble[iii][jjj]= centerPoint[jjj]+offset;
					else
						tempDouble[iii][jjj]= centerPoint[jjj]-offset;
					tempVar++;
					if (tempVar == counter*2)
						tempVar=0;
				}
				
				
				
			}
			int beforeAdd=NDimensionalObject.myPoints.length;
			for(int iii=0; iii < numPoints; iii++)
			{
				point tempPoint = new point(tempDouble[iii]);
				NDimensionalObject.addpoint(tempPoint);
			}
			for(int iii=beforeAdd; iii< numPoints+beforeAdd-1; iii++)
			{
				for(int jjj=beforeAdd+1; jjj< numPoints+beforeAdd; jjj++)
				{
					if((NDimensionalObject.getDistance(iii, jjj)==offset*2))
					{
					line newLine = new line(iii,jjj);
					NDimensionalObject.addline(newLine);
					}
				}
				
			}
			paintMethods3d.paint3Dobject();
			pointFrame.dispose();
			
		}
	}
}

