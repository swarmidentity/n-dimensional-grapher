package edu.clarkson.racettl.ndimensional;

		

import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu to draw a single two-dimensional axes set.
 * @author Louis Racette
 *
 */
public class subGUIsing2d extends JPanel implements ActionListener {
	protected JFrame selectFrame = new JFrame("Select two dimensions");
	protected JTable selectTable;
	protected JButton closeButton = new JButton("close");
	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions][2];
	JScrollPane scrollPane;
	public static Boolean rotating = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIsing2d()
	{
		try
		{
		String[] columnNames = new String[2];
		columnNames[0]= "Dimensions";
		columnNames[1]= "Slicing variable (if unselected)";
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
			data[jjj][1]= 0.0;
		}
		JRadioButton sliceNo = new JRadioButton("Projection view");
		JRadioButton sliceYes = new JRadioButton("Slice view");
		JRadioButton rotateNo = new JRadioButton("Static");
		JRadioButton rotateYes = new JRadioButton("Rotating");
		ButtonGroup group2 = new ButtonGroup();
		ButtonGroup group = new ButtonGroup();
		group.add(sliceNo);
		group.add(sliceYes);
		group2.add(rotateYes);
		group2.add(rotateNo);
		JPanel rotatePanel = new JPanel();
		rotatePanel.add(rotateNo);
		rotatePanel.add(rotateYes);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(sliceNo);
		buttonPanel.add(sliceYes);
		selectTable= new JTable(data,columnNames);
		JPanel comboPanel = new JPanel();
		comboPanel.add(buttonPanel,BorderLayout.SOUTH);
		scrollPane = new JScrollPane(selectTable);
		selectTable.setFillsViewportHeight(true);
		selectFrame.add(comboPanel, BorderLayout.NORTH);
		selectFrame.add(scrollPane,BorderLayout.CENTER);
		selectFrame.add(closeButton, BorderLayout.SOUTH);
		sliceNo.addActionListener(this);
		sliceYes.addActionListener(this);
		rotateNo.addActionListener(this);
		rotateYes.addActionListener(this);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		sliceNo.setActionCommand("sliceNo");
		sliceYes.setActionCommand("sliceYes");
		rotateNo.setActionCommand("rotateNo");
		rotateYes.setActionCommand("rotateYes");
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
		
			int[] selectedDims = new int[3];
			if(selectTable.getSelectedRows().length == 2)
			{
			selectedDims = selectTable.getSelectedRows();
			paintMethods3d.setActive(selectedDims[0], selectedDims[1], -1);
			if((paintMethods3d.slice == true)&&(NDimensionalObject.NumberOfDimensions > 2))
			{
				double[] temp = new double[NDimensionalObject.NumberOfDimensions];
				for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
				{
					try
					{
					temp[jjj]=Double.parseDouble((String) data[jjj][1]);
					}
					
					catch(NullPointerException f)
					{
					temp[jjj]= 0;	
					}
					catch(ClassCastException g)
					{
					temp[jjj]=(Double) data[jjj][1];
					}
					finally
					{
					
					}
					
					}
					
				paintMethods3d.setSliceVars(temp);
			}
			}
			else
				System.out.println("Invalid number selected.");
			
			paintMethods3d.paint3Dobject();
			selectFrame.dispose();
		
		}
		if ("sliceNo".equals(e.getActionCommand()))
		{
			paintMethods3d.setSlice(false);
		}
		if ("sliceYes".equals(e.getActionCommand()))
		{
			paintMethods3d.setSlice(true);
		}
		if ("rotateNo".equals(e.getActionCommand()))
		{
			rotating = false;
		}
		if ("rotateYes".equals(e.getActionCommand()))
		{
			rotating = true;
		}
		
	}
}

