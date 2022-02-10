package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu for drawing lines.
 * @author Louis Racette
 *
 */

public class subGUIdrawLine extends JPanel implements ActionListener {
	protected JFrame lineFrame = new JFrame("Draw a line by selecting two points");
	protected JTable line1Table;
	protected JTable line2Table;
	protected JButton closeButton = new JButton("Draw Line");
	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions][2];
	protected Object[][] data2 = new Object[NDimensionalObject.NumberOfDimensions][2];
	protected double[] newPoint = new double[NDimensionalObject.NumberOfDimensions];
	protected int pointIndex=0;
	protected int pointIndex2= 0;
	JComboBox componentBox1;
	JComboBox componentBox2;
	JScrollPane scrollPane1;
	JScrollPane scrollPane2;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIdrawLine()
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
		
		String[] columnNames = {"Dimension labels","Location"};
		
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
			data2[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
		}
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][1]= NDimensionalObject.actualPoints[pointIndex].location[jjj];
			data2[jjj][1]= NDimensionalObject.actualPoints[pointIndex2].location[jjj];
		}
		line1Table= new JTable(data, columnNames);
		line2Table = new JTable(data2,columnNames);
		scrollPane1 = new JScrollPane(line1Table);
		scrollPane2 = new JScrollPane(line2Table);
		line1Table.setFillsViewportHeight(true);
		line2Table.setFillsViewportHeight(true);
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		panel1.add(componentBox1, BorderLayout.NORTH);
		panel1.add(scrollPane1,BorderLayout.CENTER);
		panel2.add(componentBox2, BorderLayout.NORTH);
		panel2.add(scrollPane2,BorderLayout.CENTER);
		lineFrame.add(panel1,BorderLayout.WEST);
		lineFrame.add(panel2, BorderLayout.EAST);
		lineFrame.add(closeButton, BorderLayout.SOUTH);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		lineFrame.pack();
		lineFrame.setVisible(true);
		}
		catch(ArrayIndexOutOfBoundsException f)
		{
			System.out.println("No points added");
		}
		catch(IllegalArgumentException h)
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
		
			
			line tempLine = new line(pointIndex,pointIndex2);
			NDimensionalObject.addline(tempLine);
			lineFrame.dispose();
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
		}
	}
}

