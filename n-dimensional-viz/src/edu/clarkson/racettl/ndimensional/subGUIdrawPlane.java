package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu for drawing planes.
 * @author Louis Racette
 *
 */
public class subGUIdrawPlane extends JPanel implements ActionListener {
	protected JFrame planeFrame = new JFrame("Draw a plane by selecting three points");
	protected JTable line1Table;
	protected JTable line2Table;
	protected JTable line3Table;
	protected JButton closeButton = new JButton("Draw Plane");
	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions][2];
	protected Object[][] data2 = new Object[NDimensionalObject.NumberOfDimensions][2];
	protected Object[][] data3 = new Object[NDimensionalObject.NumberOfDimensions][2];
	protected int pointIndex=0;
	protected int pointIndex2= 0;
	protected int pointIndex3= 0;
	JComboBox componentBox1;
	JComboBox componentBox2;
	JComboBox componentBox3;
	JScrollPane scrollPane1;
	JScrollPane scrollPane2;
	JScrollPane scrollPane3;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIdrawPlane()
	{
		try
		{
		String[] allPoints = new String[NDimensionalObject.actualPoints.length];
		for(int jjj=0; jjj < allPoints.length; jjj++)
			allPoints[jjj]=Integer.toString(jjj);
		componentBox1 = new JComboBox(allPoints);
		componentBox1.setSelectedIndex(0);
		componentBox1.addActionListener(this);
		
		componentBox2 = new JComboBox(allPoints);
		componentBox2.setSelectedIndex(0);
		componentBox2.addActionListener(this);
		
		componentBox3 = new JComboBox(allPoints);
		componentBox3.setSelectedIndex(0);
		componentBox3.addActionListener(this);
		
		String[] columnNames = {"Dimension labels","Location"};
		
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
			data2[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
			data3[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
		}
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][1]= NDimensionalObject.actualPoints[pointIndex].location[jjj];
			data2[jjj][1]= NDimensionalObject.actualPoints[pointIndex2].location[jjj];
			data3[jjj][1]= NDimensionalObject.actualPoints[pointIndex3].location[jjj];
		}
		
		line1Table= new JTable(data, columnNames);
		line2Table = new JTable(data2,columnNames);
		line3Table = new JTable(data3,columnNames);
		scrollPane1 = new JScrollPane(line1Table);
		scrollPane2 = new JScrollPane(line2Table);
		scrollPane3 = new JScrollPane(line3Table);
		
	
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		
		panel1.add(componentBox1, BorderLayout.NORTH);
		panel1.add(scrollPane1,BorderLayout.CENTER);
		panel2.add(componentBox2, BorderLayout.NORTH);
		panel2.add(scrollPane2,BorderLayout.CENTER);
		panel3.add(componentBox3, BorderLayout.NORTH);
		panel3.add(scrollPane3,BorderLayout.CENTER);
		planeFrame.add(panel1,BorderLayout.NORTH);
		planeFrame.add(panel2, BorderLayout.WEST);
		planeFrame.add(panel3,BorderLayout.EAST);
		planeFrame.add(closeButton, BorderLayout.SOUTH);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		planeFrame.pack();
		planeFrame.setVisible(true);
		}
		catch(ArrayIndexOutOfBoundsException f)
		{
			System.out.println("No points added");
		}
		catch(IllegalArgumentException f)
		{
			System.out.println("No points added");
		}
	}
	
	/**
	 * Monitor the submenu for changes.
	 */
	public void actionPerformed(ActionEvent e)
	{

		if ("close".equals(e.getActionCommand()))
		{
		
			
			plane tempPlane = new plane(pointIndex,pointIndex2,pointIndex3);
			NDimensionalObject.addplane(tempPlane);
			planeFrame.dispose();
			paintMethods3d.paint3Dobject();
			
		}
		if (!("close".equals(e.getActionCommand())))
		{
			JComboBox cb = (JComboBox)e.getSource();
		if (cb == componentBox1)
		{
			
        pointIndex = Integer.parseInt((String)cb.getSelectedItem());
       
        for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][1]= NDimensionalObject.actualPoints[pointIndex].location[jjj];
		}
        scrollPane1.repaint();
		}
		
		else if (cb == componentBox2)
		{
			
        pointIndex2 = Integer.parseInt((String)cb.getSelectedItem());
        for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data2[jjj][1]= NDimensionalObject.actualPoints[pointIndex2].location[jjj];
		}
        scrollPane2.repaint();
		}
		
		else if (cb == componentBox3)
		{
			
        pointIndex3 = Integer.parseInt((String)cb.getSelectedItem());
        for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data3[jjj][1]= NDimensionalObject.actualPoints[pointIndex3].location[jjj];
		}
        scrollPane3.repaint();
		}
		
		}
		
	}
}

