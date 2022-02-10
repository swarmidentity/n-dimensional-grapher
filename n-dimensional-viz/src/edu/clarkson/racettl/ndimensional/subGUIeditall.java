package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu for editing all points.
 * @author Louis Racette
 *
 */
public class subGUIeditall extends JPanel implements ActionListener {
	protected JFrame editFrame = new JFrame("Edit all points");
	protected JTable editTable;
	protected JButton closeButton = new JButton("close");
	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions+1][2];
	protected double[] newPoint = new double[NDimensionalObject.NumberOfDimensions];
	protected int pointIndex=0;
	JScrollPane scrollPane;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIeditall()
	{
		try
		{
		String[] allPoints = new String[NDimensionalObject.actualPoints.length];
		for(int jjj=0; jjj < allPoints.length; jjj++)
			allPoints[jjj]=Integer.toString(jjj);
		JComboBox componentBox = new JComboBox(allPoints);
		componentBox.setSelectedIndex(0);
		componentBox.addActionListener(this);
		String[] columnNames = {"Dimension labels","Location"};
		
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
		}
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][1]= NDimensionalObject.actualPoints[pointIndex].location[jjj];
		}
		//Note: this is a workaround. For some reason, java's tables don't change value until the user has deselected them.
		data[NDimensionalObject.NumberOfDimensions][1]= "Click Here When Finished";
		editTable= new JTable(data, columnNames);
		
		scrollPane = new JScrollPane(editTable);
		editTable.setFillsViewportHeight(true);
		editFrame.add(componentBox, BorderLayout.NORTH);
		editFrame.add(scrollPane,BorderLayout.CENTER);
		editFrame.add(closeButton, BorderLayout.SOUTH);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		editFrame.pack();
		editFrame.setVisible(true);
		
		}
		catch(ArrayIndexOutOfBoundsException f)
		{
			System.out.println("No points added");
		}
		catch(IllegalArgumentException g)
		{
			System.out.println("No Points added (illegal)");
		}
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
				newPoint[jjj]=Double.parseDouble((String) data[jjj][1]);
				
				}
				catch(NullPointerException f)
				{
				newPoint[jjj]= 0;	
				}
				catch(ClassCastException g)
				{
				newPoint[jjj]=(Double) data[jjj][1];
				}
				finally
				{
					NDimensionalObject.DimensionLabels[jjj]= (String) data[jjj][0];
				}
			}

			NDimensionalObject.actualPoints[pointIndex] = new point(newPoint);
			editFrame.dispose();
			
		}
		if (!("close".equals(e.getActionCommand())))
		{
		JComboBox cb = (JComboBox)e.getSource();
		
        pointIndex = Integer.parseInt((String)cb.getSelectedItem());
        for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][1]= NDimensionalObject.actualPoints[pointIndex].location[jjj];
		}
		}
		scrollPane.repaint();
	}
}

