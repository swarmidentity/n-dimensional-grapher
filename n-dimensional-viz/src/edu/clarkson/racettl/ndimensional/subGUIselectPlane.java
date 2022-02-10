package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;
/**
 * Submenu to select a plane.
 * @author Louis Racette
 *
 */
public class subGUIselectPlane extends JPanel implements ActionListener {
	protected JFrame selectFrame = new JFrame("Select lines");
	protected JTable selectTable;
	protected JButton closeButton = new JButton("close");
	protected Object[][] data = new Object[NDimensionalObject.myPlanes.length][4];
	JScrollPane scrollPane;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw submenu.
	 */
	public subGUIselectPlane()
	{
		try
		{
		String[] columnNames = {"","First Endpoint Index","Second Endpoint Index","Third Endpoint Index"};
		
		for(int iii=0; iii<NDimensionalObject.myPlanes.length; iii++)
		{
			data[iii][0]= iii;
			for (int jjj = 0; jjj<3; jjj++)
				data[iii][jjj+1]= NDimensionalObject.myPlanes[iii].pointsIndex[jjj];
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
		
			select.selectPlanes(selectTable.getSelectedRows());
			selectFrame.dispose();
			
		}
	}
}

