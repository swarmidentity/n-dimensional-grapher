package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu to select a line.
 * @author Louis Racette
 *
 */
public class subGUIselectLine extends JPanel implements ActionListener {
	protected JFrame selectFrame = new JFrame("Select lines");
	protected JTable selectTable;
	protected JButton closeButton = new JButton("close");
	protected Object[][] data = new Object[NDimensionalObject.myLines.length][3];
	JScrollPane scrollPane;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIselectLine()
	{
		try
		{
		String[] columnNames = {"","First Endpoint Index","Second Endpoint Index"};
		
		for(int iii=0; iii<NDimensionalObject.myLines.length; iii++)
		{
			data[iii][0]= iii;
			data[iii][1]= NDimensionalObject.myLines[iii].endpoint1index;
			data[iii][2]= NDimensionalObject.myLines[iii].endpoint2index;
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
		
			select.selectLines(selectTable.getSelectedRows());
			selectFrame.dispose();
			
		}
	}
}

