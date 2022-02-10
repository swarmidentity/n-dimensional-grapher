package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu to select points.
 * @author Louis Racette
 *
 */
public class subGUIselectPoint extends JPanel implements ActionListener {
	protected JFrame selectFrame = new JFrame("Select points");
	protected JTable selectTable;
	protected JButton closeButton = new JButton("close");
	protected Object[][] data = new Object[NDimensionalObject.actualPoints.length][NDimensionalObject.NumberOfDimensions+1];
	JScrollPane scrollPane;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIselectPoint()
	{
		try
		{
		String[] columnNames = new String[NDimensionalObject.DimensionLabels.length+1];
		columnNames[0]= "";
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			columnNames[jjj+1]= NDimensionalObject.DimensionLabels[jjj];
		}
		for(int iii=0; iii<NDimensionalObject.actualPoints.length; iii++)
		{
			data[iii][0]= iii;
		for(int jjj=1; jjj <= NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[iii][jjj]= NDimensionalObject.actualPoints[iii].location[jjj-1];
		}
		}
		selectTable= new JTable(data, columnNames);
		scrollPane = new JScrollPane(selectTable);
		selectTable.setFillsViewportHeight(true);
		selectFrame.add(scrollPane,BorderLayout.NORTH);
		selectFrame.add(closeButton, BorderLayout.SOUTH);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		selectFrame.pack();
		selectFrame.setVisible(true);
		
		}
		catch(ArrayIndexOutOfBoundsException f)
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
		
			select.selectPoints(selectTable.getSelectedRows());
			selectFrame.dispose();
			
		}
	}
}

